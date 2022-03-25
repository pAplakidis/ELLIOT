# Constants

# Image Resolution (CHANGE ACCORDING TO NETWORK'S EFFICIENCY AND RAM USAGE)
#IMG_SIZE = 128
IMG_SIZE = 299    # for inceptionv3
DISP_RES = 1080//2

# Model Paths
#model_path = "../models/classifier_251.pth"
#model_path = "../models/resnet18_classifier_251.pth"
model_path = "../models/inceptionv3_251.pth"
#onnx_path = "../models/classifier_251.onnx"
#onnx_path = "../models/resnet18_classifier_251.onnx"
onnx_path = "../models/inceptionv3_251.onnx"
#cpp_model_path = "../models/classifier_251.pt"
#cpp_model_path = "../models/resnet18_classifier_251.ptl"
cpp_model_path = "../models/inceptionv3_251.ptl"
#classes_path = "../models/classes.json"
#light_model_path = "../models/classifier_251.ptl"
#light_model_path = "../models/resnet18_classifier_251.ptl"
light_model_path = "../models/inceptionv3_251.ptl"

classes_path = "../models/classes_251.json"

# plot paths
#train_plot = "../plots/training_stats.png"
#eval_plot = "../plots/evaluation_stats.png"
train_plot = "../plots/training_stats_inc251.png"
eval_plot = "../plots/evaluation_stats_inc251.png"

# Data Paths
base_dir = "../data/"
FOOD101_path = "Food-101/images/"
FOOD101_meta_path = "Food-101/meta/meta/"
FOOD251_annot_path = "Food-251/annot/"
FOOD251_train_path = "Food-251/train/train_set/"
FOOD251_val_path = "Food-251/val/val_set/"
FOOD251_test_path = "Food-251/test/test_set/"

