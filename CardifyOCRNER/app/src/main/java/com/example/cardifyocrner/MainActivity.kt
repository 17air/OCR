package com.example.cardifyocrner

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import org.tensorflow.lite.Interpreter
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.io.FileInputStream

class MainActivity : AppCompatActivity() {

    private lateinit var interpreter: Interpreter
    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        interpreter = Interpreter(loadModelFile("ner_model_fp16.tflite"), Interpreter.Options())
    }

    private fun processImage(bitmap: Bitmap) {
        val image = InputImage.fromBitmap(bitmap, 0)
        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                val rawText = visionText.text
                val entities = runNerModel(rawText)
                findViewById<TextView>(R.id.textView).text = entities.joinToString("\n")
            }
    }

    private fun runNerModel(text: String): List<String> {
        val inputIds = encodeText(text)
        val output = Array(1) { IntArray(inputIds.size) }
        interpreter.run(arrayOf(inputIds), output)
        return decodeTags(output[0])
    }

    private fun encodeText(text: String): IntArray {
        return IntArray(text.length) { 0 }
    }

    private fun decodeTags(predictions: IntArray): List<String> {
        return predictions.map { "TAG$it" }
    }

    private fun loadModelFile(fileName: String): MappedByteBuffer {
        val fileDescriptor = assets.openFd(fileName)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }
}
