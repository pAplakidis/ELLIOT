#!/usr/bin/env python3
import matplotlib.pyplot as plt
from torch.utils.mobile_optimizer import optimize_for_mobile

from model import *
from data_proc import *
from util import *

def train(model, images, labels, classes):
  model.train()
  loss_function = nn.CrossEntropyLoss()
  #lr = 1e-4  # full dataset
  #lr = 1e-3  # food-101
  lr = 1e-4   # food-241
  optim = torch.optim.Adam(model.parameters(), lr=lr, weight_decay=1e-5)

  losses, accuracies = [], []
  BS = 256
  #epochs = 250 # full dataset
  #epochs = 100 # food-101
  epochs = 15   # food-251

  try:
    for epoch in range(epochs):
      print("[+] Epoch %d/%d"%(epoch+1,epochs))
      epoch_losses = []
      epoch_acc = []
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
        t.set_description("loss %.2f accuracy %.2f"%(loss, accuracy))

      print("Epoch average loss: %.2f"%(np.array(epoch_losses).mean()))
      print("Epoch average accuracy: %.2f"%(np.array(epoch_acc).mean()))
      losses.append(np.array(epoch_losses).mean())
      accuracies.append(np.array(epoch_acc).mean())
  except KeyboardInterrupt:
    print("[-] Training was interrupted")

  # plot stats
  print("Training Done")
  plt.figure(0)
  plt.plot(losses, label="loss")
  plt.plot(accuracies, label="train accuracy")
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
  BS = 64

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
    t.set_description("accuracy %.2f"%accuracy)

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


if __name__ == '__main__':
  device = torch.device("cuda:0" if torch.cuda.is_available() else "cpu")
  print(device)

  # preprocess data
  images, labels, classes = get_training_data(base_dir)

  # print images on tensorboard
  #img_grid = torchvision.utils.make_grid(images)
  #writer.add_image('food_images', img_grid)

  # train
  #model = FoodClassifier(len(classes)).to(device)
  model = init_resnet(len(classes), False, IMG_SIZE, device)
  model = train(model, images, labels, classes)
  save_model(model, model_path)

  # evaluate
  test_imgs, test_lbls = get_eval_data(base_dir, classes)
  evaluate(model, device, test_imgs, test_lbls, classes)

  # save the model for the C++ API
  # load a sample image
  samp_img = images[0:2]
  samp_img = torch.Tensor(np.array(samp_img)).float().to(device)

  # run the tracing and save the lite model
  model.eval()
  traced_script_module = torch.jit.trace(model, samp_img)
  traced_script_module_optimized = optimize_for_mobile(traced_script_module)
  traced_script_module_optimized._save_for_lite_interpreter(cpp_model_path)

