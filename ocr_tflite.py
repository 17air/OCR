import numpy as np
from PIL import Image
import tensorflow as tf
from ocr_utils import decode_greedy


class OCRModel:
    """Wrapper around a tflite model for OCR."""

    def __init__(self, model_path: str):
        # The model_path should point to your .tflite file
        self.interpreter = tf.lite.Interpreter(model_path=model_path)
        self.interpreter.allocate_tensors()
        self.input_details = self.interpreter.get_input_details()
        self.output_details = self.interpreter.get_output_details()

    def _preprocess(self, image_path: str) -> np.ndarray:
        """Load image and preprocess according to model requirements."""
        img = Image.open(image_path).convert('L')
        input_shape = self.input_details[0]['shape']
        width, height = input_shape[2], input_shape[1]
        img = img.resize((width, height))
        img_array = np.array(img, dtype=np.float32)
        img_array = img_array.reshape((1, height, width, 1))
        return img_array

    def predict(self, image_path: str):
        input_data = self._preprocess(image_path)
        self.interpreter.set_tensor(self.input_details[0]['index'], input_data)
        self.interpreter.invoke()
        output_data = self.interpreter.get_tensor(self.output_details[0]['index'])
        return output_data

    def predict_text(self, image_path: str, char_map, blank_index: int = 0) -> str:
        """Return the decoded text for an image."""
        logits = self.predict(image_path)
        return decode_greedy(logits, char_map, blank_index)
