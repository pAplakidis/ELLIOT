#!/usr/bin/env python3
import matplotlib.pyplot as plt

from model import *
from data_proc import *
from util import *

def train(model, images, labels, classes):
  model.train()
  loss_function = nn.CrossEntropyLoss()
  optim = torch.optim.Adam(model.parameters(), lr=0.001)

  losses, accuracies = [], []
  BS = 128
  epochs = 50

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
    losses.append(np.array(epoch_losses).mean())
    accuracies.append(np.array(epoch_acc).mean())

  # plot stats
  print("Training Done")
  plt.plot(losses, label="loss")
  plt.plot(accuracies, label="train accuracy")
  plt.savefig("../plots/training_stats.png")
  plt.show()

  return model

def evaluate(model, device, images, labels, classes):
  model.eval()
  accuracies = []
  BS = 128

  for i in (t := trange(0, len(images), BS)):
    # prep tensor/batch
    X = torch.tensor(np.array(images[i:i+BS])).float().to(device)
    Y = torch.tensor(np.array(labels[i:i+BS])).long().to(device)

    # feed to net and stats
    out = model(X)
    cat = torch.argmax(out, dim=1)
    accuracy = (cat == Y).float().mean()
    accuracy = accuracy.item()
    accuracies.append(accuracy)
    t.set_description("accuracy %.2f"%accuracy)

  # plot stats
  print("Evaluation Done")
  print("[+] Average Validation Accuracy: %.2f"%(sum(accuracies)/len(accuracies)))
  plt.plot(accuracies, label="validation accuracy")
  plt.savefig("../plots/evaluation_stats.png")
  plt.show()


if __name__ == '__main__':
  device = torch.device("cuda:0" if torch.cuda.is_available() else "cpu")
  print(device)

  # preprocess data
  images, labels, classes = get_training_data(base_dir)

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
  samp_img, samp_label = images[0:32], labels[0:32] # TODO: 32 is the Batch Size
  samp_img = torch.Tensor(np.array(samp_img)).float().to(device)
  samp_label = torch.Tensor(np.array(samp_label)).float().to(device)

  # run the tracing
  model.eval()
  traced_script_module = torch.jit.trace(model, samp_img)

  # save the converted model
  traced_script_module.save(cpp_model_path)

