#include <iostream>
#include <memory>
#include <stdlib.h>

#include <torch/torch.h>
#include <torch/script.h>

#include "model.h"

void load_model(){
  std::shared_ptr<torch::jit::script::Module> module;
  try{
    // Deserialize the ScriptModule from a file
    module = torch::jit::load(model_path);
  }catch(const c10::Error& e){
    std::cerr << "Error loading model from " << model_path << std::endl;
    exit(-1);
  }
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

