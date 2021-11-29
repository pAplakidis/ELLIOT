#!/usr/bin/env python3
import threading
import json
import threading
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
    self.domain = "https://www.simplyrecipes.com/"  # CHANGE THIS

  def read_categories(self):
    with open(classes_path, 'r') as f:
      self.classes = json.load(f)
      f.close()
    print("[+] Loaded food categories from", classes_path)

  @staticmethod
  def prep_search(string):
    search = string.split("_")
    search = "+".join(search)
    return search

  def search(self, search_string):
    html_string = ""
    # TODO: change this depending on the website
    url = self.domain + "search?q=" + search_string
    try:
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
      self.not_found.add(result_url)
    print(result_url)
    ingredients = self.gather_ingredients(result_url)

  def create_database(self):
    # prep search queue
    self.read_categories()
    for c in self.classes:
      self.queue.add(self.prep_search(c))
    print("[+] Created search queue")
    #print(self.queue)

    #self.ingredient_finder.gather_ingredients()
    
    # TODO: this needs to be threaded
    for q in sorted(self.queue):
      print("[~] Searching for", q)
      self.thread_work(q)


if __name__ == "__main__":

  spider = Spider()
  spider.create_database()

