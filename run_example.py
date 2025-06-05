import argparse
import json

from ocr_tflite import OCRModel
from ocr_utils import load_char_map, decode_greedy


def main():
    parser = argparse.ArgumentParser(description="Run OCR using a tflite model")
    parser.add_argument("--config", required=True, help="Path to config JSON file")
    parser.add_argument("--image_path", required=True, help="Path to input image")
    args = parser.parse_args()

    with open(args.config, "r", encoding="utf-8") as f:
        config = json.load(f)

    model_path = config["model_path"]
    char_map_path = config.get("char_map")
    blank_index = config.get("blank_index", 0)

    char_map = load_char_map(char_map_path) if char_map_path else None

    ocr_model = OCRModel(model_path)
    output = ocr_model.predict(args.image_path)
    if char_map:
        text = decode_greedy(output, char_map, blank_index)
        print(text)
    else:
        print(output)


if __name__ == "__main__":
    main()
