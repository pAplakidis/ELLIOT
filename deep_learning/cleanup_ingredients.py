#!/usr/bin/env python3

keys_file = "ing_keys.txt"
ing_file = "ingredients.txt"
out_file = "new_ingredients.txt"

def get_keywords():
  k = None
  with open(keys_file, "r") as f:
    k = f.read().split("\n")[:-1]
    f.close()
  return k

def get_ingredients():
  data = None
  with open(ing_file, "r") as f:
    data = f.read().split("\n\n")[:-1]
    f.close()

  foods = []
  for i in range(len(data)):
    data[i] = data[i].split("\n")
    foods.append(data[i][0])
    data[i] = data[i][1:]
  return foods, data

def cleanup(keywords, foods, data):
  new_data = data.copy()
  # TODO: maybe add progressbar
  for key in keywords:
    for i, food in enumerate(data):
      for j, ingredient in enumerate(food):
        if key in ingredient or key.lower() in ingredient or key.upper() in ingredient:
          new_data[i][j] = key

  for i in range(len(new_data)):
    new_data[i] = [foods[i]] + new_data[i]
    print(new_data[i])

  file_str = ""
  with open(out_file, "w") as f:
    for d in new_data:
      for ingredient in d:
        file_str += ingredient + "\n"
      file_str += "\n"
    f.write(file_str)
    print("[+] Ingredients stored at ", out_file)
    f.close()


if __name__ == '__main__':
  keywords = get_keywords()
  foods, ingredients = get_ingredients()
  cleanup(keywords, foods, ingredients)

