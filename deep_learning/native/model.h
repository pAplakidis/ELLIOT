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

//std::string model_path = "../../models/traced_simple_classifier.pt";
std::string model_path = "../../../models/classifier_251.pt";
std::string onnx_path = "../../../models/classifier_251.onnx";
std::string classes_path = "../../../models/classes_251.json";

// using torch
std::string* load_classes(int* n_classes);
torch::jit::script::Module load_model();
torch::Tensor load_image(std::string img_path);
std::string classify(torch::Tensor img, torch::jit::script::Module module);

// using onnx
cv::dnn::Net load_onnx_model();
cv::Mat load_image_onnx(std::string path);
std::string onnx_classify(cv::Mat img, cv::dnn::Net model);


