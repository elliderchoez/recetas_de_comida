package com.example.recetadecomidalogin

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class RecuperarpassActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recuperarpass)

        // Insets para barras de estado/navegación
        val root = findViewById<View>(R.id.recuperarpassLayout)
        ViewCompat.setOnApplyWindowInsetsListener(root) { v, insets ->
            val sysBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(0, sysBars.top, 0, sysBars.bottom)
            insets
        }

        // Views
        val recoverEmailInput = findViewById<android.widget.EditText>(R.id.recoverEmailInput)
        val sendRecoveryButton = findViewById<Button>(R.id.sendRecoveryButton)
        val goToLogin = findViewById<TextView>(R.id.goToLogin)

        fun hideKeyboard() {
            currentFocus?.let { view ->
                val imm = getSystemService<InputMethodManager>()
                imm?.hideSoftInputFromWindow(view.windowToken, 0)
                view.clearFocus()
            }
        }

        fun validateEmail(): Boolean {
            val email = recoverEmailInput.text.toString().trim()
            return if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                true
            } else {
                recoverEmailInput.error = "Email inválido"
                false
            }
        }

        sendRecoveryButton.setOnClickListener {
            hideKeyboard()
            if (validateEmail()) {
                val email = recoverEmailInput.text.toString().trim()
                Toast.makeText(this, "Código de recuperación enviado", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Ingresa un email válido", Toast.LENGTH_SHORT).show()
            }
        }

        goToLogin.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}