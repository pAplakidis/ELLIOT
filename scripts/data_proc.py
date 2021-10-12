#!/usr/bin/env python3
import sys
import cv2
import numpy as np
from os import listdir
from tqdm import trange

from model import *
from helpers import *

# TODO: maybe this could be more generic
def get_data(base_dir):
  classes = []  # list of all possible classes  (same size as NN's output tensor)
  images, labels = [], [] # images and their corresponding class

  # handle Food-101 dataset
  print("[+] Loading data from FOOD-101 ...")
  files = listdir(base_dir+FOOD101_path)
  #for f in listdir(base_dir+FOOD101_path):
  for i in (t := trange(len(files))):
    f = files[i]
    classes.append(f)
    for image in listdir(base_dir+FOOD101_path+f):
      img = cv2.imread(base_dir+FOOD101_path+f+'/'+image)
      img = cv2.resize(img, (IMG_SIZE, IMG_SIZE))
      img = np.moveaxis(img, -1, 0) # [batch_size, channels, height, width] to be used in NN
      images.append(img)
      labels.append(f)
      t.set_description("processing file: %s"%(f+'/'+image))

  # TODO: handle other datasets as well (start with Food-251)

  # convert labels to indicies from classes
  new_labels = []
  for l in labels:
    idx = classes.index(l)
    new_labels.append(idx)

  return images, new_labels, classes


if __name__ == '__main__':
  #base_dir = sys.argv[1]
  # TODO: move usable training data to SSD to save time
  base_dir = "/media/paul/HDD/ELLIOT/datasets/"

  images, labels, classes = get_data(base_dir)
  print(labels)
  print(len(labels))
  print(classes)
  print(len(classes))

