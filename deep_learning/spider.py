import threading
import json
from bs4 import BeautifulSoup
from urllib.request import Request, urlopen
from urllib.parse import urlparse, parse_qs

class Spider:
  def __init__(self):
    self.current_food = None

