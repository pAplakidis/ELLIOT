#!/usr/bin/env python3
import cv2
import json
import numpy as np

import torch
import torch.nn as nn
import torch.nn.functional as F
import torchvision.models as models

IMG_SIZE = 128

def init_resnet(num_classes, input_size, device):
  model = models.resnet18(pretrained=False)
  num_feats = model.fc.in_features
  model.fc = nn.Linear(num_feats, num_classes)
  #print(model)
  return model.to(device)

def load_model(model, path):
  model.load_state_dict(torch.load(path, map_location=torch.device("cpu")))
  model.eval()
  return model

def classify(img_path, model_path, classes_path):
  device = torch.device("cpu")

  # load classes
  classes = []
  print("[+] Classes Path:", classes_path)
  with open(classes_path, 'r') as f:
    classes = json.load(f)
    f.close()
  #print("[+] %d classes loaded"%len(classes))

  # load and  preprocess image
  img_original = cv2.imread(img_path)
  img = cv2.resize(img_original, (IMG_SIZE, IMG_SIZE))
  #print("[+] Loaded image", img_path)

  img_in = np.moveaxis(img, -1, 0)
  X = torch.tensor(np.array([img_in])).float().to(device)
  #print(img_in.shape)

  # load model
  model = init_resnet(len(classes), IMG_SIZE, device)
  model = load_model(model, model_path)

  # forward to model
  pred = model(X)
  cat = torch.argmax(pred, dim=1).item()
  food_name = classes[cat]
  return food_name

