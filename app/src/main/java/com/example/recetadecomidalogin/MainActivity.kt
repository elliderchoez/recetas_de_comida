package com.example.recetadecomidalogin

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Patterns
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.recetadecomidalogin.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private var isPasswordVisible = false  // Estado de visibilidad

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val root = findViewById<View>(R.id.main)
        ViewCompat.setOnApplyWindowInsetsListener(root) { v, insets ->
            val sysBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(0, sysBars.top, 0, sysBars.bottom)
            insets
        }

        val emailInput = findViewById<EditText>(R.id.emailInput)
        val passwordInput = findViewById<EditText>(R.id.passwordInput)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val signUpButton = findViewById<Button>(R.id.signUpButton)
        val forgotPassword = findViewById<TextView>(R.id.forgotPassword)
        val btnEnlistar = findViewById<Button>(R.id.btnEnlistar)

        // 👁️ Toggle de visibilidad de contraseña
        val visibilityOn = ContextCompat.getDrawable(this, R.drawable.ic_visibility)
        val visibilityOff = ContextCompat.getDrawable(this, R.drawable.ic_visibility_off)

        passwordInput.setOnTouchListener { v, event ->
            val DRAWABLE_END = 2
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (passwordInput.right - passwordInput.compoundDrawables[DRAWABLE_END].bounds.width())) {
                    isPasswordVisible = !isPasswordVisible
                    if (isPasswordVisible) {
                        passwordInput.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                        passwordInput.setCompoundDrawablesWithIntrinsicBounds(null, null, visibilityOn, null)
                    } else {
                        passwordInput.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                        passwordInput.setCompoundDrawablesWithIntrinsicBounds(null, null, visibilityOff, null)
                    }
                    passwordInput.setSelection(passwordInput.text.length)
                    return@setOnTouchListener true
                }
            }
            false
        }

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
                val password = passwordInput.text.toString().trim()

                lifecycleScope.launch(Dispatchers.IO) {
                    val db = AppDatabase.getInstance(this@MainActivity)
                    val usuario = db.usuarioDao().obtenerUsuarioPorCredenciales(email, password)

                    withContext(Dispatchers.Main) {
                        if (usuario != null) {
                            Toast.makeText(this@MainActivity, "Bienvenido ${usuario.nombre}", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@MainActivity, Home::class.java))
                            finish()
                        } else {
                            Toast.makeText(this@MainActivity, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Revisa los campos", Toast.LENGTH_SHORT).show()
            }
        }


        signUpButton.setOnClickListener {
            startActivity(Intent(this, RegistroActivity::class.java))
        }

        forgotPassword.setOnClickListener {
            startActivity(Intent(this, RecuperarpassActivity::class.java))
        }

        btnEnlistar.setOnClickListener {
            startActivity(Intent(this, ListaUsuariosActivity::class.java))
        }
    }
}
