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

import android.util.Log
import android.os.Handler
import android.os.Looper

class RegistroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro)

        val root = findViewById<View>(R.id.registerLayout)
        ViewCompat.setOnApplyWindowInsetsListener(root) { v, insets ->
            val sysBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(0, sysBars.top, 0, sysBars.bottom)
            insets
        }

        // Views
        val nameInput = findViewById<EditText>(R.id.nameInput)
        val emailInput = findViewById<EditText>(R.id.emailInput)
        val passwordInput = findViewById<EditText>(R.id.passwordInput)
        val confirmPasswordInput = findViewById<EditText>(R.id.confirmPasswordInput)
        val registerButton = findViewById<Button>(R.id.registerButton)
        val goToLogin = findViewById<TextView>(R.id.goToLogin)

        fun hideKeyboard() {
            currentFocus?.let { view ->
                val imm = getSystemService<InputMethodManager>()
                imm?.hideSoftInputFromWindow(view.windowToken, 0)
                view.clearFocus()
            }
        }

        fun validate(): Boolean {
            var ok = true
            val name = nameInput.text.toString().trim()
            val email = emailInput.text.toString().trim()
            val pass = passwordInput.text.toString()
            val confirmPass = confirmPasswordInput.text.toString()

            if (!name.matches(Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$"))) {
                nameInput.error = "Solo letras y espacios"
                ok = false
            } else nameInput.error = null

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailInput.error = "Email inválido"
                ok = false
            } else emailInput.error = null

            // Contraseña
            val passRegex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{6,}\$")
            if (!pass.matches(passRegex)) {
                passwordInput.error =
                    "Debe tener al menos 6 caracteres, una mayúscula, una minúscula y un símbolo"
                ok = false
            } else passwordInput.error = null

            // Confirmar contraseña
            if (confirmPass != pass) {
                confirmPasswordInput.error = "Las contraseñas no coinciden"
                ok = false
            } else confirmPasswordInput.error = null
            return ok
        }

        registerButton.setOnClickListener {
            hideKeyboard()
            if (validate()) {
                val name = nameInput.text.toString().trim()
                Toast.makeText(this, "Registro exitoso: $name", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Revisa los campos", Toast.LENGTH_SHORT).show()
            }
        }

        goToLogin.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    // <-- onResume()  - Enfoca nombre completo y muestra teclado -->
    override fun onResume() {
        super.onResume()
        val nameInput = findViewById<EditText>(R.id.nameInput)
        nameInput.requestFocus()
        Handler(Looper.getMainLooper()).postDelayed({
            val imm = getSystemService<InputMethodManager>()
            imm?.showSoftInput(nameInput, InputMethodManager.SHOW_FORCED)  // <-- Cambiado a SHOW_FORCED para obligar
            Log.d("Lifecycle", "Teclado forzado mostrado para nameInput")  // Log extra para debug
        }, 200)  // 200ms de delay (ajusta si es necesario: 100-300ms)
    }
}
