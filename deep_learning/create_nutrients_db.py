#!/usr/bin/env python3
import json
import pandas as pd
from difflib import SequenceMatcher

from util import *

# TODO: replace _ with space for each food class
def get_classes(path):
  classes = None
  with open(path, 'r') as f:
    classes = sorted(json.load(f))
    f.close()
  return classes

def similar(a, b):
  return SequenceMatcher(None, a, b).ratio()

if __name__ == '__main__':
  food_classes = get_classes(classes_path)
  print("[+] Loaded %d food categories from %s"%(len(food_classes), classes_path))
  print(food_classes)

  print(similar("Dutch Apple Pie", "apple pie"))

