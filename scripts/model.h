#include <iostream>
#include <fstream>
#include <memory>
#include <stdlib.h>

#include <jsoncpp/json/json.h>
#include <torch/torch.h>
#include <torch/script.h>
#include <opencv2/core.hpp>
#include <opencv2/opencv.hpp>

#define IMG_SIZE 128

std::string model_path = "../../models/traced_simple_classifier.pt";
std::string classes_path = "../../models/classes.json";

torch::jit::script::Module load_model();
torch::Tensor load_image(std::string img_path);
std::string* load_classes();
std::string classify(torch::Tensor img, torch::jit::script::Module module);

