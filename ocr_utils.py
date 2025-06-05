import numpy as np


def load_char_map(path: str):
    """Load character mapping from a file.

    Each line in the file should contain a single character. Blank lines are
    ignored."""
    with open(path, "r", encoding="utf-8") as f:
        chars = [line.strip() for line in f if line.strip()]
    return chars


def decode_greedy(logits: np.ndarray, char_map, blank_index: int = 0) -> str:
    """Decode model output using a simple greedy algorithm."""
    if logits.ndim != 3:
        raise ValueError("Expected logits with shape [1, seq_len, num_classes]")
    indices = np.argmax(logits, axis=-1)[0]
    text = []
    prev = blank_index
    for idx in indices:
        if idx != blank_index and idx != prev:
            text.append(char_map[idx])
        prev = idx
    return "".join(text)
