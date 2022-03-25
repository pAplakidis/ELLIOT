#include "model.h"

torch::jit::script::Module load_model(){
  torch::jit::script::Module module;
  try{
    // Deserialize the ScriptModule from a file
    module = torch::jit::load(model_path);
  }catch(const c10::Error& e){
    std::cerr << "Error loading model from " << model_path << std::endl;
    exit(-1);
  }

  std::cout << "[+] Loaded model from " << model_path << std::endl;
  return module;
}

std::string* load_classes(int* n_classes){
  Json::Value root;
  std::ifstream ifs;
  ifs.open(classes_path);

  Json::CharReaderBuilder builder;
  builder["collectComments"] = false;
  JSONCPP_STRING errs;
  if(!parseFromStream(builder, ifs, &root, &errs)){
    std::cerr << errs << std::endl;
    exit(-1);
  }

  //std::cout << root[0].asString() << std::endl;
  std::string* classes = new std::string[root.size()];
  for(int i=0; i<root.size(); i++){
    classes[i] = root[i].asString();
  }

  *n_classes = root.size();
  return classes;
}

torch::Tensor load_image(std::string img_path){
  cv::Mat img = cv::imread(img_path);
  cv::resize(img, img, cv::Size(IMG_SIZE, IMG_SIZE), 0, 0, 1);
  std::cout << img.size << std::endl;
  //cv::imshow("grayscale image", img);
  //cv::waitKey(0);
  //cv::destroyAllWindows();

  // this code might be wrong!!! (might be converting to RGB while we need BGR, default opencv image)
  auto tensor_img = torch::from_blob(img.data, { img.rows, img.cols, img.channels() }, at::kByte);
  tensor_img = tensor_img.permute({2, 0, 1});
  tensor_img.unsqueeze_(0);
  tensor_img = tensor_img.toType(c10::kFloat).sub(127.5).mul(0.0078125);
  tensor_img.to(c10::DeviceType::CPU);
  
  std::cout << tensor_img << std::endl;
  return tensor_img;
}

std::string classify(torch::Tensor img, torch::jit::script::Module module){
  std::vector<torch::jit::IValue> inputs;
  inputs.push_back(img);

  at::Tensor output = module.forward(inputs).toTensor();
  std::cout << output.slice(1, 0, 5) << std::endl;

  // TODO: convert output from tensor to string from classes
  return "No Food";
}

cv::dnn::Net load_onnx_model(){
  cv::dnn::Net net = cv::dnn::readNetFromONNX(onnx_path);
  std::cout << "Loaded model from: " << onnx_path << std::endl;
  return net;
}

cv::Mat load_image_onnx(std::string path){
  cv::Mat img = cv::imread(path);
  cv::imshow("food", img);
  cv::waitKey(0);
  cv::destroyAllWindows();

  cv::resize(img, img, cv::Size(IMG_SIZE, IMG_SIZE), 0, 0, 1);
  cv::Mat ret;
  cv::dnn::blobFromImage(img, ret); // like np.moveaxis
  std::cout << "Loaded image\n";

  /*
  std::vector<float> matrix;
  if(ret.isContinuous()){
    matrix.assign((float*)ret.data, (float*)ret.data + ret.total()*ret.channels());
  }
  else{
    for(int i=0; i<ret.rows; i++){
      matrix.insert(matrix.end(), ret.ptr<float>(i), ret.ptr<float>(i)+ret.cols*ret.channels());
    }
  }

  for(float i: matrix){
    std::cout << i << " ";
  }
  std::cout << std::endl;
  */

  return ret;
}

std::string onnx_classify(cv::Mat img, cv::dnn::Net model, std::string* classes){
  // forward to net
  model.setInput(img);
  cv::Mat out = model.forward();
  std::cout << "classification output:\n" << out << std::endl;

  // process output tensor
  double minVal, maxVal;
  int minIdx, maxIdx;
  cv::minMaxLoc((cv::SparseMat)out, &minVal, &maxVal, &minIdx, &maxIdx);
  int idx = maxVal;
  std::cout << "Max-Val idx: " << idx << std::endl;

  return classes[idx];
}

int main(int argc, char** argv){
  if(argc != 2){
    std::cerr << "Usage: " << argv[0] << " <image_path>\n";
    return -1;
  }

  std::string img_path = argv[1];
  int n_classes;

  std::string* classes = load_classes(&n_classes);
  std::cout << n_classes << " food classes loaded\n";
  //torch::jit::script::Module module = load_model();
  //torch::Tensor img = load_image(img_path);
  //std::string food_detected = classify(img, module);

  cv::dnn::Net model = load_onnx_model();
  cv::Mat img = load_image_onnx(img_path);
  std::string food = onnx_classify(img, model, classes);
  std::cout << "Food Detected: " << food << std::endl;
}

