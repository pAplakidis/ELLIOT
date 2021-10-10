#!/usr/bin/env python3
import sys
import cv2
import numpy as np
from os import listdir

from model import *

# PATHS
FOOD101_path = "Food-101/images/"

# TODO: maybe this could be more generic
def get_data(base_dir):
  classes = []  # list of all possible classes  (same size as NN's output tensor)
  images, labels = [], [] # images and their corresponding class

  # handle Food-101 dataset
  print("[+] Loading data from FOOD-101 ...")
  for f in listdir(base_dir+FOOD101_path):
    classes.append(f)
    for image in listdir(base_dir+FOOD101_path+f):
      img = cv2.imread(base_dir+FOOD101_path+f+'/'+image)
      img = cv2.resize(img, (img_size, img_size))
      images.append(img)
      labels.append(f)
      print(img.shape, f)

  # TODO: handle other datasets as well (start with Food-251)
  return images, labels, classes


if __name__ == '__main__':
  #base_dir = sys.argv[1]
  #dest_dir = sys.argv[2]
  base_dir = "/media/paul/HDD/ELLIOT/datasets/"
  dest_dir = "/home/paul/Dev/ELLIOT/data/"

  images, labels, classes = get_data(base_dir)

