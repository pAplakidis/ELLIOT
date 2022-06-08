#!/usr/bin/env python3
import json
import pandas as pd
from tqdm import trange
from difflib import SequenceMatcher

from util import *

# NOTE: all nutrients are for 100g servings
nutrients_path = "nutrients_xl/MyFoodData-Nutrition-Facts-SpreadSheet-Release-1-4.xlsx"
out_path = "nutrients.txt"

def get_classes(path):
  classes = None
  with open(path, 'r') as f:
    classes = sorted(json.load(f))
    f.close()

  #for i in range(len(classes)):
  #  classes[i] = classes[i].replace("_", " ")

  return classes

def get_nutrients(path):
  data = pd.read_excel(path)
  nutrients = ["name", "Protein (g)", "Fat (g)", "Carbohydrate (g)", "Fiber (g)", "Sodium (mg)"]
  return data[nutrients]

def similar(a, b):
  return SequenceMatcher(None, a.lower(), b.lower()).ratio()

def check(a, b):
  if b != None and a in b.lower().replace(" ", "_"):
    return True
  return False

if __name__ == '__main__':
  food_classes = get_classes(classes_path)
  print("[+] Loaded %d food categories from %s"%(len(food_classes), classes_path))
  #print(food_classes)

  nutrients = get_nutrients(nutrients_path)
  print(nutrients)

  out_data = []
  matches = 0
  for i in (t := trange(len(food_classes))):
    f_class = food_classes[i]
    t.set_description("Processing: %s"%f_class)
    data = [f_class]
    for idx, n in nutrients.iterrows():
      # TODO: maybe add "or similar" with .lower().replace(" ", "_")
      if check(f_class, n["name"]):
        #print(f_class, "matched with", n["name"])
        # NOTE: out_data = [[f_name, protein (g), fat (g), carbohydrate (g), fiber (g), sodium (g)], [...], ...]
        matches += 1
        data.append(n["Protein (g)"])
        data.append(n["Fat (g)"])
        data.append(n["Carbohydrate (g)"])
        data.append(n["Fiber (g)"])
        data.append(n["Sodium (mg)"])
        break
    out_data.append(data)
  print(out_data)
  print("%d/%d Matches"%(matches, len(food_classes)))

  write_str = ""
  with open(out_path, 'w') as f:
    for food in out_data:
      for item in food:
        write_str += str(item) + "\n"
      write_str += "\n"
    f.write(write_str)
    print("Nutrients stored at ", out_path)
    f.close()

