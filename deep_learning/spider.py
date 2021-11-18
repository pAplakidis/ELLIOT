#!/usr/bin/env python3
import threading
import json
from bs4 import BeautifulSoup
from urllib.request import Request, urlopen
from urllib.parse import urlparse, parse_qs

class Spider:
  def __init__(self):
    self.current_food = None

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


if __name__ == "__main__":
  spider = Spider()

