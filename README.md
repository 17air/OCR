# OCR

This repository provides a minimal example of using a TensorFlow Lite (tflite) model for Optical Character Recognition (OCR).

Binary model files are **not** included in this repository. Instead, you should provide your own tflite model and update the configuration file with the correct path.

## Files

- `ocr_tflite.py` – Python module that loads a tflite model and runs inference on input images.
- `config_example.json` – Example configuration file with a placeholder for the tflite model path.
- `run_example.py` – Example script showing how to use the module with a configuration file.

## Usage

1. Place your tflite OCR model at the desired location. Update `config_example.json` so that the `model_path` field points to your model file.
2. Install the required dependencies:

```bash
pip install tensorflow pillow
```

3. Run the example script on an input image:

```bash
python run_example.py --config config_example.json --image_path path/to/image.png
```

The script will print the raw output from the model. Post-processing steps depend on the specific model you are using.

