std::string model_path = "../models/traced_simple_classifier.pt";

torch::jit::script::Module load_model();
void classify();

