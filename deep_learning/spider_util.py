import os
from html.parser import HTMLParser
from urllib import parse

def create_database_dir(directory):
  if not os.path_exists(directory):
    print("Creating Directory " + directory)
    os.makedirs(directory)

# TODO: each line of a recipe will be like this:
# food_name: ingredient1, ingredient2, ...
def write_file(path, data):
  pass

# TODO: maybe use Beautiful Soup instead of HTMLParser
class ResultsFinder(HTMLParser):
  def __init__(self, page_url):
    super().__init__()
    self.page_url = page_url
    self.result_url = None

  def find_first_result(self):
    pass

class IngredientFinder(HTMLParser):
  def __init__(self, page_url):
    super().__init__()
    self.page_url = page_url
    self.ingredients = set()

  # TODO: handle tags (class maybe) of ingredients
  def gather_ingredients(self):
    print("[+] Gathering Ingredients from web-page ...")

