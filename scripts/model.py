#!/usr/bin/env python3
import torch
import torch.nn as nn
import torch.nn.functional as F

# image resolution (CHANGE ACCORDING TO NETWORK'S EFFICIENCY AND RAM USAGE)
img_size = 32

class FoodClassifier(nn.Module):
  def __init__(self):
    super(FoodClassifier, self).__init__()
    # TODO: define layers

  def forward(self, x):
    return x

  def num_flat_features(self, x):
    pass

