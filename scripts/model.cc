#include <iostream>
#include <memory>
#include <stdlib.h>

#include <torch/torch.h>
#include <torch/script.h>
#include <opencv2/opencv.hpp>

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

void classify(){

}

int main(int argc, char** argv){
  if(argc != 2){
    std::cerr << "Usage: " << argv[0] << " <image_path>\n";
    return -1;
  }

  load_model();
}

