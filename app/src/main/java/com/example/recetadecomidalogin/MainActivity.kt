package com.example.recetadecomidalogin

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
import android.content.Intent


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Tu layout se llama "main.xml"
        setContentView(R.layout.activity_main)

        // Insets (barra de estado / navegación)
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

        loginButton.setOnClickListener {
            hideKeyboard()
            if (validate()) {
                val email = emailInput.text.toString().trim()
                Toast.makeText(this, "Bienvenido $email", Toast.LENGTH_SHORT).show()
                // TODO: Navega a tu HomeActivity aquí si quieres
                // startActivity(Intent(this, HomeActivity::class.java))
            } else {
                Toast.makeText(this, "Revisa los campos", Toast.LENGTH_SHORT).show()
            }
        }

        signUpButton.setOnClickListener {

            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }

        forgotPassword.setOnClickListener {

            val intent = Intent(this, RecuperarpassActivity::class.java)  // <-- Actualizado al nuevo nombre
            startActivity(intent)
        }
    }
}
