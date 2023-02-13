package co.tiagoaguiar.course.instagram.home.data

import co.tiagoaguiar.course.instagram.common.base.RequestCallback
import co.tiagoaguiar.course.instagram.common.model.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.lang.RuntimeException

class FireHomeDataSource : HomeDataSource {

    override fun fetchFeed(userUUID: String, callback: RequestCallback<List<Post>>) {
        val uid = FirebaseAuth.getInstance().uid ?: throw RuntimeException("Usuário não encontrado")

        FirebaseFirestore.getInstance()
            .collection("/feeds") // varios feeds
            .document(uid) // um usuário corrente
            .collection("/posts") // posts dos outros usuarios
            .orderBy("timestamp" , Query.Direction.DESCENDING) // ordem do recente para o antigo
            .get()
            .addOnSuccessListener { res ->
                //ira retornar uma lista de posts
                val feed = mutableListOf<Post>()
                val documents = res.documents
                for (document in documents) {
                    val post = document.toObject(Post::class.java)

                    //se post existir será adicionado
                    post?.let { feed.add(it) }
                }
                callback.onSucess(feed)
            }
            .addOnFailureListener { exception ->
                callback.onFailure(exception.message ?: "Erro ao carregar o feed")

            }
            .addOnCompleteListener {
                callback.onComplete()
            }
    }

    //funcao logout
    override fun logout() {
        FirebaseAuth.getInstance().signOut()
    }
}