package com.example.recetadecomidalogin

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Ajuste de insets para barras del sistema
        val root = findViewById<View>(R.id.main)
        ViewCompat.setOnApplyWindowInsetsListener(root) { v, insets ->
            val sysBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(0, sysBars.top, 0, sysBars.bottom)
            insets
        }

        // Views
        val emailInput = findViewById<EditText>(R.id.emailInput)
        val passwordInput = findViewById<EditText>(R.id.passwordInput)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val signUpButton = findViewById<Button>(R.id.signUpButton)
        val forgotPassword = findViewById<TextView>(R.id.forgotPassword)

        fun hideKeyboard() {
            currentFocus?.let { view ->
                val imm = getSystemService<InputMethodManager>()
                imm?.hideSoftInputFromWindow(view.windowToken, 0)
                view.clearFocus()
            }
        }

        fun validate(): Boolean {
            val email = emailInput.text.toString().trim()
            val pass = passwordInput.text.toString()
            var ok = true

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailInput.error = "Email inválido"
                ok = false
            } else emailInput.error = null

            if (pass.length < 6) {
                passwordInput.error = "Mínimo 6 caracteres"
                ok = false
            } else passwordInput.error = null

            return ok
        }

        // Login
        loginButton.setOnClickListener {
            hideKeyboard()
            if (validate()) {
                val email = emailInput.text.toString().trim()
                Toast.makeText(this, "Bienvenido $email", Toast.LENGTH_SHORT).show()
                // TODO: startActivity(Intent(this, HomeActivity::class.java))
            } else {
                Toast.makeText(this, "Revisa los campos", Toast.LENGTH_SHORT).show()
            }
        }

        // Ir a Registro
        signUpButton.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        // Recuperar contraseña (placeholder)
        forgotPassword.setOnClickListener {
            Toast.makeText(this, "Recuperar contraseña…", Toast.LENGTH_SHORT).show()
            // TODO: abrir pantalla/diálogo de recuperación
        }
    }
}
