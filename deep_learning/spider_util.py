import os
from html.parser import HTMLParser
from urllib import parse
from bs4 import BeautifulSoup

DATA_PATH = "ingredients.txt"

def create_database_dir(directory):
  if not os.path_exists(directory):
    print("Creating Directory " + directory)
    os.makedirs(directory)

def prep_search(string):
  search = string.split("_")
  search = "+".join(search)
  return search

def restore_search(string):
  search = string.split("+")
  search = "_".join(search)
  return search

def write_ingredients(path, foods, ingredients):
  file_str = ""
  with open(path, "w") as f:
    for i in range(len(foods)):
      file_str += restore_search(foods[i]) + "\n"
      for ingredient in ingredients[i]:
        file_str += ingredient + "\n"
      file_str += "\n"
    f.write(file_str)
    print("[+] Ingredients stored at ", path)
    f.close()

# NOTE: these are specific for the curent domain urls
def find_result(html):
  soup = BeautifulSoup(html, 'html.parser')
  result_url = soup.find_all("a", {"id": "card_1-0", "class": "comp card"}, href=True)[0].get('href')
  # TODO: have a backup result page
  backup_result_url = ''
  #print(result_url)
  #print(backup_result_url)
  return result_url

def find_ingredients(html):
  ingredients = []
  soup = BeautifulSoup(html, 'html.parser')

  unordered_lists = soup.find_all("div", {"id": "structured-ingredients_1-0", "class": "comp structured-ingredients"})[0].find_all("ul")
  if unordered_lists == '':
    return None
  for ul in unordered_lists:
    tmp_ing = ul.find_all("li")
    for ing in tmp_ing:
      paragraph = ing.find_all("p")[0]
      ingredients.append(paragraph.text)

  return ingredients

