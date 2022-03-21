#!/usr/bin/env python3
import os
import sys
import cv2
import json
import onnx
import onnxruntime
import numpy as np

import torch
import torch.nn as nn
import torch.nn.functional as F
import torchvision.transforms as transforms

from model import *
from train import *
from util import *

def to_numpy(tensor):
  return tensor.detach.cpu().numpy() if tensor.requires_grad else tensor.cpu().numpy()


if __name__ == '__main__':
  img_path = sys.argv[1]
  ort_session = onnxruntime.InferenceSession(onnx_path)
  
  # load classes
  classes = []
  with open(classes_path, 'r') as f:
    classes = json.load(f)
    f.close()
  print("[+] %d classes loaded"%len(classes))

  # load and  preprocess image
  img_original = cv2.imread(img_path)
  img = cv2.resize(img_original, (IMG_SIZE, IMG_SIZE))
  print("[+] Loaded image", img_path)

  img_in = np.moveaxis(img, -1, 0).astype('float32')
  img_in = np.array([img_in, img_in])
  print(img_in.shape)

  # forward to model
  ort_inputs = {ort_session.get_inputs()[0].name: img_in}
  ort_outs = ort_session.run(None, ort_inputs)
  pred = ort_outs[0][0]
  cat = np.argmax(pred)
  food_name= classes[cat]
  print("Model prediction")
  print(pred)
  print(pred.shape)
  print(food_name)

  cv2.imshow(food_name, cv2.resize(img_original, (DISP_RES, DISP_RES)))
  cv2.waitKey(0)

