package com.example.recetadecomidalogin

import Post
import Story
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recetadecomidalogin.databinding.ActivityHomeBinding  // Reemplaza [TU_PAQUETE_REAL] con tu paquete real, e.g., com.example.recetadecomidalogin

class Home : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)  // Esto debería funcionar si View Binding está bien

        // Datos de ejemplo
        val stories = listOf(
            Story("Usuario1", "android.resource://" + packageName + "/" + R.drawable.almuerzo),
        )
        val posts = listOf(
            Post("android.resource://" + packageName + "/" + R.drawable.premium_photo, 10, "¡Qué bonito!"),
        )

        // Configurar RecyclerView de historias
        binding.storiesRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.storiesRecyclerView.adapter = StoryAdapter(stories)

        // Configurar RecyclerView de feed
        binding.feedRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.feedRecyclerView.adapter = PostAdapter(posts)
    }
}