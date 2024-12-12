package com.lahap.appuas.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.lahap.appuas.R
import com.lahap.appuas.databinding.ActivityLoginBinding
import com.lahap.appuas.fragments.HomeFragment
import com.lahap.appuas.utils.FirebaseAuthHelper

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Gunakan Data Binding untuk mengatur layout
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginbutton.setOnClickListener {
            val email = binding.emailLogin.text.toString().trim()
            val password = binding.passwordLogin.text.toString().trim()

            // Validasi input kosong
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email atau Password tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Login menggunakan FirebaseAuthHelper
            FirebaseAuthHelper.login(email, password, { user ->
                // Berhasil login
                Toast.makeText(this, "Login berhasil!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java) // Ubah ke HomeActivity
                startActivity(intent)
                finish()
            }, { error ->
                // Gagal login
                Toast.makeText(this, "Login gagal: $error", Toast.LENGTH_SHORT).show()
            })
        }

        // Navigasi ke SignUpActivity
        binding.donthavebutton.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        // Navigasi ke AdminActivity
        binding.adminButton.setOnClickListener {
            startActivity(Intent(this, AdminActivity::class.java))
        }

    }
}
