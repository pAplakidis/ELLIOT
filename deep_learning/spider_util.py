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
  # TODO: find results and put them to a list or set
  return ingredients

