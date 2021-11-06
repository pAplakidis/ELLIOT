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
}

int main(int argc, char** argv){
  if(argc != 2){
    std::cerr << "Usage: " << argv[0] << " <image_path>\n";
    return -1;
  }

  std::string img_path = argv[1];
  int n_classes;

  //load_model();
  std::string* classes = load_classes(&n_classes);
  std::cout << n_classes << " food classes loaded\n";
  torch::Tensor img = load_image(img_path);
}

