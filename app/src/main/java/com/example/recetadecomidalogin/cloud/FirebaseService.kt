package com.example.recetadecomidalogin.cloud

import android.util.Log
import com.example.recetadecomidalogin.model.UsuarioEntity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
object FirebaseService {
    private val db = FirebaseFirestore.getInstance()
    private const val COLLECTION_NAME = "usuario"


    suspend fun guardarUsuario(usuario: UsuarioEntity) {
        try {
            val data = hashMapOf(
                "nombre" to usuario.nombre,
                "email" to usuario.email
            )

            db.collection(COLLECTION_NAME).add(data).await()
            Log.d("FirebaseService", "Usuario guardado correctamente en Firebase")
        } catch (e: Exception) {
            Log.e("FirebaseService", "Error al guardar en Firebase", e)
        }
    }


    suspend fun obtenerUsuarios(): List<UsuarioEntity> {
        return try {
            val querySnapshot = db.collection(COLLECTION_NAME).get().await()
            querySnapshot.toObjects(UsuarioEntity::class.java)
        } catch (e: Exception) {
            Log.e("FirebaseService", "Error al obtener usuarios de Firebase", e)
            emptyList()
        }
    }
}
