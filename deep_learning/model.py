#!/usr/bin/env python3
import torch
import torch.nn as nn
import torch.nn.functional as F

class FoodClassifier(nn.Module):
  def __init__(self, n_classes):
    super(FoodClassifier, self).__init__()

    # number of classes/foods
    self.n_classes = n_classes

    # Convolutional Layers (Feature Detector)
    self.conv1 = nn.Conv2d(3, 64, 5)
    self.conv2_bn1 = nn.BatchNorm2d(64)
    self.pool = nn.MaxPool2d(2, 2)
    self.conv2 = nn.Conv2d(64, 128, 5)
    self.conv2_bn2 = nn.BatchNorm2d(128)
    self.conv3 = nn.Conv2d(128, 256, 5)
    self.conv2_bn3 = nn.BatchNorm2d(256)
    self.conv4 = nn.Conv2d(256, 512, 5)
    self.conv2_bn4 = nn.BatchNorm2d(512)

    # Fully Connected Layers (Classifier)
    self.fc1 = nn.Linear(512 * 4 * 4, 4096)
    self.bn1 = nn.BatchNorm1d(num_features=4096)
    self.fc2 = nn.Linear(4096, 4096)
    self.bn2 = nn.BatchNorm1d(num_features=4096)
    self.fc3 = nn.Linear(4096, self.n_classes)

  def forward(self, x):
    x = self.pool(F.relu(self.conv2_bn1(self.conv1(x))))
    x = self.pool(F.relu(self.conv2_bn2(self.conv2(x))))
    x = self.pool(F.relu(self.conv2_bn3(self.conv3(x))))
    x = self.pool(F.relu(self.conv2_bn4(self.conv4(x))))
    #print(x.shape)
    x = x.view(-1, self.num_flat_features(x))
    x = F.relu(self.bn1(self.fc1(x)))
    x = F.relu(self.bn2(self.fc2(x)))
    #x = F.softmax(self.fc3(x))
    x = self.fc3(x) # NOTE: don't forget to use softmax in deployment (in training CrossEntropy already applies softmax)
    return x

  def num_flat_features(self, x):
    size = x.size()[1:] # all dimensions except the batch dimension
    num_features = 1
    for s in size:
      num_features *= s
    return num_features

def save_model(model, path):
  torch.save(model.state_dict(), path)
  print("Model saved at", path)

def load_model(model, path):
  model.load_state_dict(torch.load(path))
  print("Loaded model from", path)
  return model

