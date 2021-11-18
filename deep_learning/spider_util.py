import os

def create_database_dir(directory):
  if not os.path_exists(directory):
    print("Creating Directory " + directory)
    os.makedirs(directory)

# TODO: each line of a recipe will be like this:
# food_name: ingredient1, ingredient2, ...
def write_file(path, data):
  pass

