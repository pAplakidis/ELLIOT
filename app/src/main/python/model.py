#!/usr/bin/env python3
import cv2
import json
import numpy as np

import torch
import torch.nn as nn
import torch.nn.functional as F
import torchvision.models as models

IMG_SIZE = 224
K = 3


def init_resnet(num_classes, input_size, device):
  model = models.resnet18(pretrained=False)
  num_feats = model.fc.in_features
  model.fc = nn.Linear(num_feats, num_classes)

  return model.to(device)


def load_model(model, path):
  model.load_state_dict(torch.load(path, map_location=torch.device('cpu')))
  return model


def classify(img_path, model_path, classes_path):
  device = torch.device('cpu')

  # load classes
  classes = []
  with open(classes_path, 'r') as f:
    classes = sorted(json.load(f))

  # load and  preprocess image
  img_original = cv2.imread(img_path)
  img = cv2.resize(img_original, (IMG_SIZE, IMG_SIZE))

  img_in = np.moveaxis(img, -1, 0)
  X = torch.tensor(np.array([img_in])).float().to(device)

  # load model
  model = init_resnet(len(classes), IMG_SIZE, device)
  model = load_model(model, model_path)
  model.eval()

  # forward to model
  pred = model(X)
  cat = torch.argmax(pred, dim=1).item()
  food_name = classes[cat]

  # get top k categories
  cats = torch.topk(pred[0], K).indices
  best_guesses = [classes[i.item()] for i in cats]

  # return top k food_names
  return best_guesses
