# Constants

# Image Resolution (CHANGE ACCORDING TO NETWORK'S EFFICIENCY AND RAM USAGE)
IMG_SIZE = 224
DISP_RES = 1080//2

# Model Paths
#model_path = "../models/cres_classifier_251.pth"
model_path = "../models/big_resnet18_classifier_251.pth"
#model_path = "../models/big_mobilenetv2_classifier_251.pth"
#model_path = "../models/inceptionv3_251.pth"
#onnx_path = "../models/cres_classifier_251.onnx"
onnx_path = "../models/big_resnet18_classifier_251.onnx"
#onnx_path = "../models/big_mobilenetv2_classifier_251.onnx"
#onnx_path = "../models/inceptionv3_251.onnx"
#cpp_model_path = "../models/cres_classifier_251.pt"
cpp_model_path = "../models/big_resnet18_classifier_251.ptl"
#cpp_model_path = "../models/big_mobilenetv2_classifier_251.ptl"
#cpp_model_path = "../models/inceptionv3_251.ptl"
#light_model_path = "../models/cres_classifier_251.ptl"
light_model_path = "../models/big_resnet18_classifier_251.ptl"
#light_model_path = "../models/big_mobilenetv2_classifier_251.ptl"
#light_model_path = "../models/inceptionv3_251.ptl"

#classes_path = "../models/classes.json"
classes_path = "../models/classes_251.json"

# plot paths
#train_plot = "../plots/cres_training_stats_251.png"
#eval_plot = "../plots/cres_evaluation_stats_251.png"
train_plot = "../plots/training_stats_big_res251.png"
eval_plot = "../plots/evaluation_stats_big_res251.png"
cf_plot = "../plots/big_res_confusion_matrix_251.png"
cf_csv = "../plots/big_res_confusion_matrix_251.csv"
#train_plot = "../plots/training_stats_big_mob251.png"
#eval_plot = "../plots/evaluation_stats_big_mob251.png"
#cf_plot = "../plots/big_mob_confusion_matrix_251.png"
#cf_csv = "../plots/big_mob_confusion_matrix_251.csv"

# Data Paths
base_dir = "../data/"
FOOD101_path = "Food-101/images/"
FOOD101_meta_path = "Food-101/meta/meta/"
FOOD251_annot_path = "Food-251/annot/"
FOOD251_train_path = "Food-251/train/train_set/"
FOOD251_val_path = "Food-251/val/val_set/"
FOOD251_test_path = "Food-251/test/test_set/"

