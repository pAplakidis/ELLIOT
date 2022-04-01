#!/usr/bin/env python3
import torch
import torch.nn as nn
import torch.nn.functional as F
import torchvision.models as models

# NOTE: best models are custom with 1 Linear and resnet18 (it also takes a bigger BS)

class FoodClassifier(nn.Module):
  def __init__(self, n_classes):
    super(FoodClassifier, self).__init__()
    self.n_classes = n_classes

    # TODO: reduce feature maps and play with kernel sizes
    # TODO: conv1, conv2 + input, conv3, conv4 + input (implement custom residual blocks)

    # TODO: optimize this part of the net (maybe more conv layers? or same with no BN?)
    # Convolutional Layers (Feature Detector)
    #self.pool = nn.MaxPool2d(2, 2)
    self.pool = nn.AvgPool2d(2, 2)
    self.conv1 = nn.Conv2d(3, 32, 5)
    self.conv2_bn1 = nn.BatchNorm2d(32)
    self.conv2 = nn.Conv2d(32, 64, 5)
    self.conv2_bn2 = nn.BatchNorm2d(64)
    self.conv3 = nn.Conv2d(64, 128, 5)
    self.conv2_bn3 = nn.BatchNorm2d(128)
    self.conv4 = nn.Conv2d(128, 256, 5)
    self.conv2_bn4 = nn.BatchNorm2d(256)

    # Fully Connected Layers (Classifier)
    self.fc = nn.Linear(256*4*4, self.n_classes)

  def forward(self, x):
    x = self.pool(F.relu(self.conv2_bn1(self.conv1(x))))
    x = self.pool(F.relu(self.conv2_bn2(self.conv2(x))))
    x = self.pool(F.relu(self.conv2_bn3(self.conv3(x))))
    x = self.pool(F.relu(self.conv2_bn4(self.conv4(x))))
    #print(x.shape)
    x = x.view(-1, self.num_flat_features(x))
    x = self.fc(x)
    return x

  def num_flat_features(self, x):
    size = x.size()[1:] # all dimensions except the batch dimension
    num_features = 1
    for s in size:
      num_features *= s
    return num_features


def init_resnet(num_classes, input_size, device):
  model = models.resnet18(pretrained=False)
  num_feats = model.fc.in_features
  model.fc = nn.Linear(num_feats, num_classes)
  #print(model)
  return model.to(device)

def init_inception(num_classes, input_size, device):
  model = models.inception_v3(pretrained=False)
  num_feats = model.fc.in_features
  model.fc = nn.Linear(num_feats, num_classes)
  #print(model)
  return model.to(device)

def save_model(model, path):
  torch.save(model.state_dict(), path)
  print("Model saved at", path)

def save_onnx(model, x, path):
  torch.onnx.export(model, x, path, export_params=True, opset_version=10,
                    do_constant_folding=True, input_names=['image'], output_names=['class'],
                    dynamic_axes={'input': {0: 'batch_size'}, 'output': {0: 'batch_size'}})
  print("[+] Saved onnx model at:", path)

def load_model(model, path):
  model.load_state_dict(torch.load(path))
  print("Loaded model from", path)
  return model

