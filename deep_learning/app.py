#!/usr/bin/env python3
import os
import sys
import cv2
import json
import numpy as np

import torch
import torch.nn as nn
import torch.nn.functional as F

from model import *
from train import *
from util import *


if __name__ == '__main__':
  img_path = sys.argv[1]

  device = torch.device("cuda:0" if torch.cuda.is_available() else "cpu")
  print(device)

  # load and downscale image
  img_origin = cv2.imread(img_path)
  img = cv2.resize(img_origin, (IMG_SIZE, IMG_SIZE))
  print("[+] Loaded image", img_path)
  
  # load classes
  classes = []
  with open(classes_path, 'r') as f:
    classes = json.load(f)
    f.close()
  print("[+] %d classes loaded"%len(classes))

  # load model
  #model = FoodClassifier(len(classes)).to(device)
  model = init_resnet(len(classes), IMG_SIZE, device)
  model = load_model(model, model_path)
  model.eval()

  # make tensor
  img_in = np.moveaxis(img, -1, 0)
  X = torch.tensor(np.array([img_in])).float().to(device)
  print("Input tensor")
  print(X)
  print(X.shape)

  # forward to model
  pred = model(X)
  cat = torch.argmax(pred, dim=1).item()
  food_name = classes[cat]
  # get top 3 guesses
  cats = torch.topk(pred[0], 3).indices
  best_guesses = [classes[i.item()] for i in cats]
  print("Model prediction")
  print(pred)
  print(pred.shape)
  print(cat)
  print(food_name)
  print("Top 3 predictions:")
  print(best_guesses)

  cv2.imshow(food_name, cv2.resize(img_origin, (DISP_RES, DISP_RES)))
  cv2.waitKey(0)

