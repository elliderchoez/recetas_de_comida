package com.example.recetadecomidalogin

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.recetadecomidalogin.cloud.FirebaseService
import com.example.recetadecomidalogin.database.AppDatabase
import com.example.recetadecomidalogin.model.UsuarioEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

        val scrollView = findViewById<ScrollView>(R.id.scrollView)

        // Función para que el ScrollView haga scroll hasta el EditText enfocado
        fun scrollToView(view: View) {
            scrollView.post {
                scrollView.smoothScrollTo(0, view.top)
            }
        }



        // Views
        val nameInput = findViewById<EditText>(R.id.nameInput)
        val emailInput = findViewById<EditText>(R.id.emailInput)
        val passwordInput = findViewById<EditText>(R.id.passwordInput)
        val confirmPasswordInput = findViewById<EditText>(R.id.confirmPasswordInput)
        val registerButton = findViewById<Button>(R.id.registerButton)
        val goToLogin = findViewById<TextView>(R.id.goToLogin)
        val togglePass = findViewById<ImageView>(R.id.togglePassword)
        val toggleConfirm = findViewById<ImageView>(R.id.toggleConfirmPassword)

        confirmPasswordInput.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) scrollToView(v)
        }
        registerButton.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) scrollToView(v)
        }

        // Ocultar teclado
        fun hideKeyboard() {
            currentFocus?.let { view ->
                val imm = getSystemService<InputMethodManager>()
                imm?.hideSoftInputFromWindow(view.windowToken, 0)
                view.clearFocus()
            }
        }

        // Mostrar/Ocultar contraseñas
        fun toggleVisibility(editText: EditText, toggleIcon: ImageView) {
            if (editText.inputType == (android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                editText.inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                toggleIcon.setImageResource(R.drawable.ic_visibility_off)
            } else {
                editText.inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
                toggleIcon.setImageResource(R.drawable.ic_visibility)
            }
            editText.setSelection(editText.text.length)
        }

        togglePass.setOnClickListener {
            toggleVisibility(passwordInput, togglePass)
        }

        toggleConfirm.setOnClickListener {
            toggleVisibility(confirmPasswordInput, toggleConfirm)
        }

        // Validación
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
            val passRegex = Regex("^(?=.*[A-Z]).{6,}$")
            if (!pass.matches(passRegex)) {
                passwordInput.error =
                    "Debe tener al menos 6 caracteres, una mayúscula"
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
                val nombre = nameInput.text.toString().trim()
                val email = emailInput.text.toString().trim()
                val password = passwordInput.text.toString().trim()

                val usuario = UsuarioEntity(nombre = nombre, email = email, password = password)
                val dao = AppDatabase.getInstance(this).usuarioDao()

                lifecycleScope.launch(Dispatchers.IO) {
                    try {

                        dao.insertar(usuario)


                        FirebaseService.guardarUsuario(usuario)

                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@RegistroActivity, "Registro exitoso: $nombre", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@RegistroActivity, MainActivity::class.java))
                            finish()
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@RegistroActivity, "Error al registrar: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Revisa los campos", Toast.LENGTH_SHORT).show()
            }
        }


        goToLogin.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        val nameInput = findViewById<EditText>(R.id.nameInput)
        nameInput.requestFocus()
        Handler(Looper.getMainLooper()).postDelayed({
            val imm = getSystemService<InputMethodManager>()
            imm?.showSoftInput(nameInput, InputMethodManager.SHOW_FORCED)
        }, 200)
    }
}
