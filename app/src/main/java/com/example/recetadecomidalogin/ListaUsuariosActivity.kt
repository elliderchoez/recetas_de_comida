package com.example.recetadecomidalogin

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recetadecomidalogin.cloud.FirebaseService
import com.example.recetadecomidalogin.database.AppDatabase
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
class ListaUsuariosActivity : AppCompatActivity() {
    private lateinit var rv: RecyclerView
    private lateinit var adapter: UsuarioAdapter
    private lateinit var btnFirebase: Button
    private lateinit var btnRoom: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_usuarios)
        rv = findViewById(R.id.rvUsuarios)
        adapter = UsuarioAdapter()
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter
        val dao = AppDatabase.getInstance(this).usuarioDao()
        btnFirebase = findViewById(R.id.btnFirebase)
        btnRoom = findViewById(R.id.btnRoom)

        btnRoom.setOnClickListener {
            lifecycleScope.launch {
                repeatOnLifecycle (Lifecycle.State.STARTED){
                    dao.obtenerTodos().collectLatest { lista ->
                        adapter.submitList(lista)
                    }
                }
            }
        }

        btnFirebase.setOnClickListener {
            lifecycleScope.launch {
                Log.d("ListaUsuariosActivity", "Botón Firebase presionado. Obteniendo usuarios...")

                val listaDeUsuarios = FirebaseService.obtenerUsuarios()

                if (listaDeUsuarios.isNotEmpty()) {
                    Log.d("ListaUsuariosActivity", "Se encontraron ${listaDeUsuarios.size} usuarios. Actualizando UI.")
                    // El adapter es tu UsuarioAdapter
                    adapter.submitList(listaDeUsuarios)
                } else {
                    Log.d("ListaUsuariosActivity", "La lista de Firebase está vacía.")
                    Toast.makeText(this@ListaUsuariosActivity, "No se encontraron usuarios en Firebase", Toast.LENGTH_SHORT).show()
                    adapter.submitList(emptyList()) // Limpia la lista si estaba llena
                }
            }
        }
    }
}