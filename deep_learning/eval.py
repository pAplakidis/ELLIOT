#!/usr/bin/env python3
from train import *

if __name__ == '__main__':
  # TODO: load model+val_data and call evaluate() from here instead of training from the beginning
  device = torch.device("cuda:0" if torch.cuda.is_available() else "cpu")
  print(device)

  # load classes
  classes = []
  with open(classes_path, 'r') as f:
    classes = json.load(f)
    f.close()
  print("[+] %d classes loaded"%len(classes))

  # load model
  #model = FoodClassifier(len(classes)).to(device)
  model = init_resnet(len(classes), IMG_SIZE, device)
  model = load_model(model, model_path)

  test_imgs, test_lbls = get_eval_data(base_dir, classes)
  evaluate(model, device, test_imgs, test_lbls, classes)

