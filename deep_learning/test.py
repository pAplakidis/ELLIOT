from model import *
from data_proc import *
from helpers import *

device = torch.device("cuda:0" if torch.cuda.is_available() else "cpu")
print(device)

images, labels, classes = get_training_data(base_dir)

model = FoodClassifier(len(classes)).to(device)
model = load_model(model, model_path)
model.eval()

samp_img, samp_label = images[0], labels[0]
samp_img = torch.Tensor(np.array(samp_img)).float().to(device)
samp_label = torch.Tensor(np.array(samp_label)).float().to(device)
traced_script_module = torch.jit.trace(model, samp_img)
traced_script_module.save(cpp_model_path)

