import os
from html.parser import HTMLParser
from urllib import parse
from bs4 import BeautifulSoup

def create_database_dir(directory):
  if not os.path_exists(directory):
    print("Creating Directory " + directory)
    os.makedirs(directory)

# TODO: each line of a recipe will be like this:
# food_name: ingredient1, ingredient2, ...
def write_file(path, data):
  pass

def find_result(html):
  soup = BeautifulSoup(html, 'html.parser')
  #print(soup.find(id="card-list__item_1-0").find_all('a'))
  result_url = soup.find_all("a", {"id": "card_1-0", "class": "comp card"}, href=True)[0].get('href')
  return result_url

def find_ingredients(html):
  ingredients = []
  soup = BeautifulSoup(html, 'html.parser')

  unordered_lists = soup.find_all("div", {"id": "structured-ingredients_1-0", "class": "comp structured-ingredients"})[0].find_all("ul")
  for ul in unordered_lists:
    tmp_ing = ul.find_all("li")
    for ing in tmp_ing:
      paragraph = ing.find_all("p")[0]
      ingredients.append(paragraph.text)

  return ingredients

