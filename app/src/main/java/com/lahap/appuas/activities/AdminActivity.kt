package com.lahap.appuas.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.lahap.appuas.R

class AdminActivity : AppCompatActivity() {
    private lateinit var foodNameEditText: EditText
    private lateinit var foodPriceEditText: EditText
    private lateinit var foodDescriptionEditText: EditText
    private lateinit var foodImageUrlEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var backButton: Button
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        // Initialize views
        foodNameEditText = findViewById(R.id.foodNameEditText)
        foodPriceEditText = findViewById(R.id.foodPriceEditText)
        foodDescriptionEditText = findViewById(R.id.foodDescriptionEditText)
        foodImageUrlEditText = findViewById(R.id.foodImageUrlEditText)
        saveButton = findViewById(R.id.saveButton)
        backButton = findViewById(R.id.backButton) // Inisialisasi tombol back

        // Save menu to Firestore
        saveButton.setOnClickListener {
            saveMenuToFirestore()
        }

        // Back button logic
        backButton.setOnClickListener {
            finish() // Kembali ke halaman sebelumnya (LoginActivity)
        }
    }

    private fun saveMenuToFirestore() {
        val foodName = foodNameEditText.text.toString()
        val foodPrice = foodPriceEditText.text.toString()
        val foodDescription = foodDescriptionEditText.text.toString()
        val foodImageUrl = foodImageUrlEditText.text.toString()

        if (foodName.isEmpty() || foodPrice.isEmpty() || foodDescription.isEmpty() || foodImageUrl.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Save menu data to Firestore
        val menuItem = hashMapOf(
            "foodName" to foodName,
            "foodPrice" to foodPrice,
            "foodDescription" to foodDescription,
            "foodImage" to foodImageUrl
        )

        firestore.collection("menu")
            .add(menuItem)
            .addOnSuccessListener {
                Toast.makeText(this, "Menu saved successfully!", Toast.LENGTH_SHORT).show()
                clearFields()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to save menu: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun clearFields() {
        foodNameEditText.text.clear()
        foodPriceEditText.text.clear()
        foodDescriptionEditText.text.clear()
        foodImageUrlEditText.text.clear()
    }
}

