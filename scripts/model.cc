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

cv::Mat load_image(std::string img_path){
  cv::Mat img = cv::imread(img_path, 0);
  cv::imshow("grayscale image", img);
  cv::waitKey(0);
  cv::destroyAllWindows();

  return img;
}

std::string classify(){

}

int main(int argc, char** argv){
  if(argc != 2){
    std::cerr << "Usage: " << argv[0] << " <image_path>\n";
    return -1;
  }
  std::string img_path = argv[1];

  //load_model();
  cv::Mat img = load_image(img_path);
}

