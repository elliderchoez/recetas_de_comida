package com.example.recetadecomidalogin

import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        // Ajustar insets (barra estado/nav)
        val root = findViewById<android.view.View>(R.id.registerCard)
        ViewCompat.setOnApplyWindowInsetsListener(root) { v, insets ->
            val sysBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(0, sysBars.top, 0, sysBars.bottom)
            insets
        }

        // Views
        val etName = findViewById<EditText>(R.id.etName)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val etConfirm = findViewById<EditText>(R.id.etConfirm)
        val btnRegister = findViewById<Button>(R.id.btnRegister)

        btnRegister.setOnClickListener {
            val name = etName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val pass = etPassword.text.toString()
            val confirm = etConfirm.text.toString()

            when {
                name.isEmpty() || email.isEmpty() || pass.isEmpty() || confirm.isEmpty() ->
                    toast("Completa todos los campos")
                !Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                    toast("Email no válido")
                pass.length < 6 ->
                    toast("La contraseña debe tener al menos 6 caracteres")
                pass != confirm ->
                    toast("Las contraseñas no coinciden")
                else -> {
                    toast("¡Registro completado!")
                    finish() // vuelve al login
                }
            }
        }
    }

    private fun toast(msg: String) =
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}
