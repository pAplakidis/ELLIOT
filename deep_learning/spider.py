#!/usr/bin/env python3
import threading
import json
import threading
from tqdm import trange
from urllib.request import Request, urlopen
from urllib.parse import urlparse, parse_qs

from util import *
from spider_util import *

class Spider:
  def __init__(self):
    self.current_food = None
    self.classes = None
    self.queue = set()
    self.not_found = set()
    self.found = []
    self.ingredients = []
    self.domain = "https://www.simplyrecipes.com/"  # CHANGE THIS

  def read_categories(self):
    with open(classes_path, 'r') as f:
      self.classes = json.load(f)
      f.close()
    print("[+] Loaded %d food categories from %s"%(len(self.classes), classes_path))

  def search(self, search_string):
    html_string = ""
    # NOTE: change this depending on the website
    url = self.domain + "search?q=" + search_string
    try:
      #print("Visiting", url)
      response = urlopen(url)
      if "text/html" in response.getheader("Content-Type"):
        html_bytes = response.read()
        html_string = html_bytes.decode("utf-8")
        # TODO: maybe add more than 1 result
        return find_result(html_string)
    except Exception as e:
      print(str(e))
    return None

  def gather_ingredients(self, url):
    try:
      #print("Visiting", url)
      response = urlopen(url)
      if "text/html" in response.getheader("Content-Type"):
        html_string = response.read().decode("utf-8")
        return find_ingredients(html_string)
    except Exception as e:
      print(str(e))
    return None


  def thread_work(self, f_category):
    result_url = self.search(f_category)
    if result_url is None:
      self.not_found.add(f_category)  # TODO: handle not-found recipes
    else:
      #print("[~] Gathering ingredients from:", result_url)
      ingredients = self.gather_ingredients(result_url)
      if ingredients is None:
        self.not_found.add(f_category)  # TODO: handle not-found recipes
      else:
        #print("Ingredients:")
        #print(ingredients)
        # NOTE: if using threads, use mutex for accessing these and the not-found categories
        self.found.append(f_category)
        self.ingredients.append(ingredients)


  def create_database(self):
    # prep search queue
    self.read_categories()
    for c in self.classes:
      self.queue.add(prep_search(c))
    print("[+] Created search queue")
    #print(self.queue)

    #self.ingredient_finder.gather_ingredients()
    
    # TODO: this needs to be threaded
    self.queue = sorted(self.queue)
    for i in (t := trange(len(self.queue))):
      t.set_description("Processing %s"%restore_search(self.queue[i]))
      self.thread_work(self.queue[i])
    write_ingredients(DATA_PATH, self.found, self.ingredients)


if __name__ == "__main__":

  spider = Spider()
  spider.create_database()

  print("Found the ingredients of the following %d food categories:"%len(spider.found))
  for f in spider.found:
    print(f)
  print("Couldn't find the ingredients of the following %d food categories:"%len(spider.not_found))
  for f in spider.not_found:
    print(f)
    # TODO: write not-found recipes in a file

