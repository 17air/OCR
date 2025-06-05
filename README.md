# OCR

This repository provides a minimal example of using a TensorFlow Lite (tflite) model for Optical Character Recognition (OCR).

Binary model files are **not** included in this repository. Instead, provide your own `.tflite` model and update the configuration file with the correct path.

## Files

- `ocr_tflite.py` – Wrapper around the tflite interpreter.
- `ocr_utils.py` – Utilities for loading character maps and decoding model output.
- `config_example.json` – Example configuration file with placeholders for paths.
- `run_example.py` – Example script showing how to run inference on a single image.
- `char_map_example.txt` – Character mapping used by the example configuration.

## Usage

1. Place your tflite OCR model at the desired location. Update `config_example.json` so that `model_path` points to your model file. Optionally modify `char_map` to match your model's character set.
2. Install the required dependencies:

```bash
pip install tensorflow pillow
```

3. Run the example script on an input image:

```bash
python run_example.py --config config_example.json --image_path path/to/image.png
```

If `char_map` is provided in the configuration, the script prints the decoded text. Otherwise it prints the raw model output.

