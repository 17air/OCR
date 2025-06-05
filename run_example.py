import argparse
import json

from ocr_tflite import OCRModel


def main():
    parser = argparse.ArgumentParser(description="Run OCR using a tflite model")
    parser.add_argument("--config", required=True, help="Path to config JSON file")
    parser.add_argument("--image_path", required=True, help="Path to input image")
    args = parser.parse_args()

    with open(args.config, "r", encoding="utf-8") as f:
        config = json.load(f)

    model_path = config["model_path"]

    ocr_model = OCRModel(model_path)
    output = ocr_model.predict(args.image_path)
    print(output)


if __name__ == "__main__":
    main()
