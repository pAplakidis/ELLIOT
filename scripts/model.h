#include <iostream>
#include <memory>
#include <stdlib.h>

#include <torch/torch.h>
#include <torch/script.h>
#include <opencv2/opencv.hpp>

std::string model_path = "../models/traced_simple_classifier.pt";

torch::jit::script::Module load_model();
cv::Mat load_image(std::string img_path);
std::string classify();

