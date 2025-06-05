package com.example.cardifyocrner

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class MainActivity : AppCompatActivity() {

    private lateinit var selectImageLauncher: ActivityResultLauncher<String>
    private lateinit var captureImageLauncher: ActivityResultLauncher<Void?>

    private val recognizer by lazy {
        TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val imageView = findViewById<ImageView>(R.id.imageView)
        val textView = findViewById<TextView>(R.id.textView)
        val nameText = findViewById<TextView>(R.id.nameTextView)
        val companyText = findViewById<TextView>(R.id.companyTextView)
        val emailText = findViewById<TextView>(R.id.emailTextView)
        val phoneText = findViewById<TextView>(R.id.phoneTextView)

        selectImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, it)
                imageView.setImageBitmap(bitmap)
                runOCR(bitmap, textView, nameText, companyText, emailText, phoneText)
            }
        }

        captureImageLauncher = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap: Bitmap? ->
            bitmap?.let {
                imageView.setImageBitmap(it)
                runOCR(it, textView, nameText, companyText, emailText, phoneText)
            }
        }

        findViewById<Button>(R.id.selectButton).setOnClickListener {
            selectImageLauncher.launch("image/*")
        }

        findViewById<Button>(R.id.captureButton).setOnClickListener {
            captureImageLauncher.launch(null)
        }
    }

    private fun runOCR(
        bitmap: Bitmap,
        textView: TextView,
        nameText: TextView,
        companyText: TextView,
        emailText: TextView,
        phoneText: TextView
    ) {
        val image = InputImage.fromBitmap(bitmap, 0)
        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                val text = visionText.text
                textView.text = text
                val info = runNER(text)
                nameText.text = "Name: ${'$'}{info.name ?: ""}"
                companyText.text = "Company: ${'$'}{info.company ?: ""}"
                emailText.text = "Email: ${'$'}{info.email ?: ""}"
                phoneText.text = "Phone: ${'$'}{info.phone ?: ""}"
            }
            .addOnFailureListener {
                textView.text = "Error: ${'$'}{it.message}"
            }
    }

    private fun runNER(text: String): CardInfo {
        // TODO: Replace this simple parser with your TensorFlow Lite NER model
        val email = Regex("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}").find(text)?.value
        val phone = Regex("\\+?\\d[\\d\\s-]{7,}\\d").find(text)?.value
        val lines = text.lines().filter { it.isNotBlank() }
        val name = lines.firstOrNull()
        val company = lines.drop(1).firstOrNull()
        return CardInfo(name, company, email, phone)
    }
}

data class CardInfo(
    val name: String?,
    val company: String?,
    val email: String?,
    val phone: String?
)
