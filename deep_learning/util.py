import cv2
import random

# Constants
# Image Resolution (CHANGE ACCORDING TO NETWORK'S EFFICIENCY AND RAM USAGE)
IMG_SIZE = 224
DISP_RES = 1080//2

K = 3 # dictates top k predictions printed out and evaluated

# Model Paths
#model_path = "../models/cres_classifier_251.pth"
model_path = "../models/big_resnet18_classifier_251_aug.pth"
#model_path = "../models/big_mobilenetv2_classifier_251.pth"
#model_path = "../models/inceptionv3_251.pth"
#onnx_path = "../models/cres_classifier_251.onnx"
onnx_path = "../models/big_resnet18_classifier_251_aug.onnx"
#onnx_path = "../models/big_mobilenetv2_classifier_251.onnx"
#onnx_path = "../models/inceptionv3_251.onnx"
#cpp_model_path = "../models/cres_classifier_251.pt"
cpp_model_path = "../models/big_resnet18_classifier_251_aug.ptl"
#cpp_model_path = "../models/big_mobilenetv2_classifier_251.ptl"
#cpp_model_path = "../models/inceptionv3_251.ptl"
#light_model_path = "../models/cres_classifier_251.ptl"
light_model_path = "../models/big_resnet18_classifier_251_aug.ptl"
#light_model_path = "../models/big_mobilenetv2_classifier_251.ptl"
#light_model_path = "../models/inceptionv3_251.ptl"

#classes_path = "../models/classes.json"
classes_path = "../models/classes_251.json"

# plot paths
#train_plot = "../plots/cres_training_stats_251.png"
#eval_plot = "../plots/cres_evaluation_stats_251.png"
train_plot = "../plots/training_stats_big_res251_aug.png"
eval_plot = "../plots/evaluation_stats_big_res251_aug.png"
cf_plot = "../plots/big_res_confusion_matrix_251_aug.png"
cf_csv = "../plots/big_res_confusion_matrix_251_aug.csv"
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

# data augmentation functions
def fill(img, h, w):
  img = cv2.resize(img, (h, w), cv2.INTER_CUBIC)
  return img

def horizontal_shift(img, ratio=0.0):
  if ratio > 1 or ratio < 0:
    print("[-] We need: 0 < ratio < 1")
    exit(1)

  ratio = random.uniform(-ratio, ratio)
  h, w = IMG_SIZE, IMG_SIZE
  to_shift = w * ratio
  if ratio > 0:
    img = img[:, ing(w-to_shift), :]
  elif ratio < 0:
    img = img[:, int(-1*to_shift):, :]
  img = fill(img, h, w)
  return img

def vertical_shift(img, ratio=0.0):
  if ratio > 1 or ratio < 0:
    print('Value should be less than 1 and greater than 0')
    exit(1)

  ratio = random.uniform(-ratio, ratio)
  h, w = IMG_SIZE, IMG_SIZE
  to_shift = h*ratio
  if ratio > 0:
    img = img[:int(h-to_shift), :, :]
  if ratio < 0:
    img = img[int(-1*to_shift):, :, :]
  img = fill(img, h, w)
  return img

def horizontal_flip(img):
  return cv2.flip(img, 1)

def vertical_flip(img):
  return cv2.flip(img, 0)

def zoom(img, value=0.3):
  if value > 1 or value < 0:
    print('Value for zoom should be less than 1 and greater than 0')
    exit(1)

  value = random.uniform(value, 1)
  h, w = IMG_SIZE, IMG_SIZE
  h_taken = int(value*h)
  w_taken = int(value*w)
  h_start = random.randint(0, h-h_taken)
  w_start = random.randint(0, w-w_taken)
  img = img[h_start:h_start+h_taken, w_start:w_start+w_taken, :]
  img = fill(img, h, w)
  return img

def rotation(img, angle=30):
  angle = int(random.uniform(-angle, angle))
  h, w = IMG_SIZE, IMG_SIZE
  M = cv2.getRotationMatrix2D((int(w/2), int(h/2)), angle, 1)
  img = cv2.warpAffine(img, M, (w, h))
  return img

