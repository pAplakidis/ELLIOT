#!/usr/bin/env python3
import threading
import json
import threading
from bs4 import BeautifulSoup
from urllib.request import Request, urlopen
from urllib.parse import urlparse, parse_qs

from util import *

class Spider:
  def __init__(self):
    self.current_food = None
    self.classes = None
    self.queue = set()

  def read_categories(self):
    with open(classes_path, 'r') as f:
      self.classes = json.load(f)
      f.close()
    print("[+] Loaded food categories from", classes_path)
    #print(self.classes)

  def prep_search(self, string):
    search = string.split("_")
    search = "+".join(search)
    return search

  def search(self, page_url, search_string):
    html_string = ""
    # TODO: change this depending on the website
    url = page_url + "?search=" + search_string
    try:
      response = urlopen(url)
      if "text/html" in response.getheader("Content-Type"):
        html_bytes = response.read()
        html_string = html_bytes.decode("utf-8")
    except Exception as e:
      print(str(e))

    return html_string

  # TODO: this is the main of spider (add threads)
  def create_database(self):
    # prep search queue
    self.read_categories()
    for c in self.classes:
      self.queue.add(self.prep_search(c))
    print("[+] Created search queue")
    print(self.queue)


if __name__ == "__main__":
  spider = Spider()
  spider.create_database()

