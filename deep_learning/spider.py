#!/usr/bin/env python3
import threading
import json
import threading
from bs4 import BeautifulSoup
from urllib.request import Request, urlopen
from urllib.parse import urlparse, parse_qs

from util import *
from spider_util import *

class Spider:
  def __init__(self):
    self.current_food = None
    self.classes = None
    self.queue = set()
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

        # TODO: return the url of the first result (maybe add more than 1 result)
        res_finder = ResultsFinder(url)
        res_finder.feed(html_string)
        return res_finder.result_url
    except Exception as e:
      print(str(e))

    return None

  def gather_ingredients(self, url):
    pass

  def thread_work(self, f_category):
    result_url = self.search(f_category)
    ingredients = self.gather_ingredients(result_url)

  # TODO: this is the main of spider (add threads)
  def create_database(self):
    # prep search queue
    self.read_categories()
    for c in self.classes:
      self.queue.add(self.prep_search(c))
    print("[+] Created search queue")
    #print(self.queue)

    #self.ingredient_finder.gather_ingredients()
    
    for q in sorted(self.queue):
      print("[~] Searching for", q)
      self.thread_work(q)
      break


if __name__ == "__main__":

  spider = Spider()
  spider.create_database()

