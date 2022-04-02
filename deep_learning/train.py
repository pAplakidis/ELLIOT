#!/usr/bin/env python3
import pandas as pd
import seaborn as sn
import matplotlib.pyplot as plt
from torch.utils.mobile_optimizer import optimize_for_mobile
from sklearn.metrics import confusion_matrix

from model import *
from data_proc import *
from util import *

def train(model, images, labels, timages, tlabels, classes, estop=False):
  model.train()
  loss_function = nn.CrossEntropyLoss()
  #lr = 1e-4  # full dataset
  #lr = 1e-3  # food-101
  lr = 1e-3   # food-251
  wd = 1e-4   # TODO: try 1e-3 with resnet18
  optim = torch.optim.Adam(model.parameters(), lr=lr, weight_decay=wd)

  losses, accuracies = [], []
  vlosses, vaccuracies = [], []
  best_vloss = float('inf')
  BS = 256
  EBS = 16
  #epochs = 250 # full dataset
  #epochs = 100 # food-101
  epochs = 100 # food-251

  try:
    for epoch in range(epochs):
      print("[+] Epoch %d/%d"%(epoch+1,epochs))
      epoch_losses = []
      epoch_acc = []
      epoch_vlosses = []
      epoch_vacc = []
      # train
      for i in (t := trange(0, len(images), BS)):
        X = torch.tensor(np.array(images[i:i+BS])).float().to(device)
        Y = torch.tensor(np.array(labels[i:i+BS])).long().to(device)

        # feed forward and backpropagate
        optim.zero_grad()
        out = model(X)
        cat = torch.argmax(out, dim=1)
        accuracy = (cat == Y).float().mean()
        loss = loss_function(out, Y).mean()
        loss.backward()
        optim.step()

        # stats
        loss, accuracy = loss.item(), accuracy.item()
        #losses.append(loss)
        #accuracies.append(accuracy)
        epoch_losses.append(loss)
        epoch_acc.append(accuracy)
        t.set_description("t_loss %.2f t_accuracy %.2f"%(loss, accuracy))
      
      # eval
      for i in (t := trange(0, len(timages), BS)):
        # prep tensor/batch
        X = torch.tensor(np.array(timages[i:i+EBS])).float().to(device)
        Y = torch.tensor(np.array(tlabels[i:i+EBS])).long().to(device)

        # feed to net and stats
        out = model(X)
        cat = torch.argmax(out, dim=1)
        accuracy = (cat == Y).float().mean()
        accuracy = accuracy.item()
        loss = loss_function(out, Y).mean()
        loss = loss.item()
        epoch_vlosses.append(loss)
        epoch_vacc.append(accuracy)
        t.set_description("v_loss %.2f v_accuracy %.2f"%(loss, accuracy))

      avg_epoch_loss = np.array(epoch_vlosses).mean()
      print("Epoch average training loss: %.4f"%(np.array(epoch_losses).mean()))
      print("Epoch average training accuracy: %.4f"%(np.array(epoch_acc).mean()))
      losses.append(np.array(epoch_losses).mean())
      accuracies.append(np.array(epoch_acc).mean())
      print("Epoch average val loss: %.4f"%(avg_epoch_loss))
      print("Epoch average val accuracy: %.4f"%(np.array(epoch_vacc).mean()))
      vlosses.append(np.array(epoch_losses).mean())
      vaccuracies.append(np.array(epoch_acc).mean())

      # automated early stopping
      # TODO: need to save model from previous/best epoch
      if avg_epoch_loss < best_vloss:
        best_vloss = avg_epoch_loss
      elif avg_epoch_loss > best_vloss and estop:
        print("[+] Stopped Early at epoch", epoch)
        break
  except KeyboardInterrupt:
    print("[-] Training was interrupted")

  # plot stats
  print("Training Done")
  plt.figure(0)
  plt.plot(losses, label="train loss")
  plt.plot(accuracies, label="train accuracy")
  plt.plot(vlosses, label="val loss")
  plt.plot(vaccuracies, label="val accuracy")
  plt.xlabel("Epochs")
  plt.legend(loc="upper left")
  plt.savefig(train_plot)
  plt.show()

  # save onnx
  inpt = torch.tensor(np.array(images[0:2])).float().to(device)
  save_onnx(model, inpt, onnx_path)
  return model

def evaluate(model, device, images, labels, classes):
  model.eval()
  loss_function = nn.CrossEntropyLoss()
  losses = []
  accuracies = []
  BS = 32

  for i in (t := trange(0, len(images), BS)):
    # prep tensor/batch
    X = torch.tensor(np.array(images[i:i+BS])).float().to(device)
    Y = torch.tensor(np.array(labels[i:i+BS])).long().to(device)

    # feed to net and stats
    out = model(X)
    cat = torch.argmax(out, dim=1)
    accuracy = (cat == Y).float().mean()
    accuracy = accuracy.item()
    loss = loss_function(out, Y).mean()
    loss = loss.item()
    losses.append(loss)
    accuracies.append(accuracy)
    t.set_description("loss %.2f accuracy %.2f"%(loss, accuracy))

  # plot stats
  print("Evaluation Done")
  print("[+] Average Validation Accuracy: %.2f"%(sum(accuracies)/len(accuracies)))
  print("[+] Average Validation Loss: %.2f"%(sum(losses)/len(losses)))
  plt.figure(1)
  plt.ylim(0, 1)
  plt.plot(losses, label="validation loss")
  plt.plot(accuracies, label="validation accuracy")
  plt.xlabel("Batch")
  plt.legend(loc="upper left")
  plt.savefig(eval_plot)
  plt.show()

  # feed images one by one
  preds = []
  accs = 0
  for i in (t := trange(len(images))):
    X = torch.tensor(np.array([images[i], images[i]])).float().to(device)
    Y = torch.tensor(np.array([labels[i], labels[i]])).long().to(device)

    out = model(X)
    pred = torch.argmax(out, dim=1)
    if (pred == Y)[0].item():
      accs += 1
    preds.append(pred[0].item())
    t.set_description("Building confusion matrix")

  # build confusion matrix
  cf_matrix = confusion_matrix(labels, preds)
  df_cm = pd.DataFrame(cf_matrix/np.sum(cf_matrix) *10, index = [i for i in classes],
                       columns = [i for i in classes])
  print("[+] Built Confusion Matrix")
  print("[+] Overall Evaluation Accuracy: %.2f"%(accs/len(images)))
  print(df_cm)
  df_cm.to_csv(cf_csv)
  plt.figure(2, figsize=(100, 100))
  sn.heatmap(df_cm, annot=True)
  plt.savefig(cf_plot)
  print("[+] Saved Confusion Matrix Plot")


if __name__ == '__main__':
  device = torch.device("cuda:0" if torch.cuda.is_available() else "cpu")
  print(device)

  # get and preprocess data
  images, labels, classes = get_training_data(base_dir)
  test_imgs, test_lbls = get_eval_data(base_dir, classes)

  # print images on tensorboard
  #img_grid = torchvision.utils.make_grid(images)
  #writer.add_image('food_images', img_grid)

  # train
  #model = FoodClassifier(len(classes)).to(device)
  model = init_resnet(len(classes), IMG_SIZE, device)
  #model = init_inception(len(classes), IMG_SIZE, device)
  model = train(model, images, labels, test_imgs, test_lbls, classes, True)
  save_model(model, model_path)

  # evaluate
  evaluate(model, device, test_imgs, test_lbls, classes)

  # save the model for the C++ API
  # load a sample image
  samp_img = images[0:2]
  samp_img = torch.Tensor(np.array(samp_img)).float().to(device)

  # run the tracing and save the lite model
  model.eval()
  traced_script_module = torch.jit.trace(model, samp_img)
  traced_script_module.save(cpp_model_path)
  traced_script_module_optimized = optimize_for_mobile(traced_script_module)
  traced_script_module_optimized._save_for_lite_interpreter(light_model_path)

