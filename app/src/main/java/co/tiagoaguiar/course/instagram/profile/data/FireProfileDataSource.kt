package co.tiagoaguiar.course.instagram.profile.data

import co.tiagoaguiar.course.instagram.common.base.RequestCallback
import co.tiagoaguiar.course.instagram.common.model.Post
import co.tiagoaguiar.course.instagram.common.model.User
import co.tiagoaguiar.course.instagram.common.model.UserAuth
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import java.lang.RuntimeException

class FireProfileDataSource : ProfileDataSource {

    //buscar perfil
    override fun fetchUserProfile(
        userUUID: String, callback: RequestCallback<Pair<User, Boolean?>>) {

        FirebaseFirestore.getInstance()
            .collection("/users")
            .document(userUUID)
            .get()
            .addOnSuccessListener { res ->
                val user = res.toObject(User::class.java)

                when(user) {

                    //erro na chamada
                    null -> {
                        callback.onFailure("Falha ao converter usuário")
                    }
                    // verifica se o user existe
                    else -> {

                        //user logado
                        if (user.uuid == FirebaseAuth.getInstance().uid) {
                            callback.onSucess(Pair(user, null))
                        }

                        //user não logado
                        else {
                            FirebaseFirestore.getInstance()
                                .collection("/followers") // lista de seguidores
                                .document(userUUID) // busca de seguidores
                                .get() // ira trazer uma lista
                                .addOnSuccessListener { response ->
                                    if (!response.exists()) { // não tem nenhum seguidor
                                        callback.onSucess(Pair(user, false))
                                    }
                                    else {
                                        val list = response.get("followers") as List<String> // estou dentro da lista de seguidores
                                        callback.onSucess(Pair(user, list.contains(FirebaseAuth.getInstance().uid))) // só retorna vazio se nao for seguidor
                                     }
                                }
                                .addOnFailureListener { exception ->
                                    callback.onFailure(exception.message ?: "Falha ao localizar seguidores")
                                }
                                .addOnCompleteListener {
                                    callback.onComplete()
                                }
                        }
                    }
                }
            }
            .addOnFailureListener { exception ->
                callback.onFailure(exception.message ?: "Falha ao buscar o usuário")
            }
    }

    //buscar os posts
    override fun fetchUserPosts(userUUID: String, callback: RequestCallback<List<Post>>) {
        FirebaseFirestore.getInstance()
            .collection("posts")
            .document(userUUID)
            .collection("posts")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { res ->
                val documents = res.documents
                val posts = mutableListOf<Post>()
                for (document in documents) {
                    val post = document.toObject(Post::class.java)
                    post?.let {
                        posts.add(it)
                    }
                }
                callback.onSucess(posts)
            }
            .addOnFailureListener { exception ->
                callback.onFailure(exception.message ?: "Falha ao buscar os posts")
            }
            .addOnCompleteListener {
                callback.onComplete()
            }
    }

    override fun followUser(
        userUUID: String, isFollow: Boolean,
        callback: RequestCallback<Boolean>) {

        val uid = FirebaseAuth.getInstance().uid ?: throw  RuntimeException("Usuário não logado!!")

        FirebaseFirestore.getInstance()
            .collection("/followers")
            .document(userUUID)
            .update(
                "followers" ,
                if (isFollow) FieldValue.arrayUnion(uid) // seguindo o user
                else FieldValue.arrayRemove(uid) // removendo o user
            )
            .addOnSuccessListener { res ->

                followingCounter(uid, isFollow) // contador de seguidores
                followersCounter(userUUID, callback) // contadores de seguindo

                updateFeed(userUUID, isFollow) // atualizar e remover os feeds do user
            }
            .addOnFailureListener { exception ->

               val err = exception as? FirebaseFirestoreException

                // se o erro de que ele ainda não existe vai criar o array de seguidores baseado no user
                if (err?.code == FirebaseFirestoreException.Code.NOT_FOUND) {

                    //ira criar uma sessão/colecao de followers
                    FirebaseFirestore.getInstance()
                        .collection("/followers")
                        .document(userUUID)
                        .set(
                            hashMapOf(
                                "followers" to listOf(uid)
                            )
                        )
                        .addOnSuccessListener { res ->

                            followingCounter(uid, isFollow) // contador de seguidores
                            followersCounter(userUUID, callback) // contadores de seguindo

                            updateFeed(userUUID, isFollow) // atualizar e remover os feeds do user
                        }
                        .addOnFailureListener { exception ->
                            callback.onFailure(exception.message ?: "Falha ao criar seguidor")
                        }
                }

                callback.onFailure(exception.message ?: "Falha ao atualizar seguidor")
            }
            .addOnCompleteListener {
                callback.onComplete()
            }
    }

    //contador de seguidores
    private fun followingCounter(uid: String, isFollow: Boolean) {

        // referencia do user logado
        val meRef = FirebaseFirestore.getInstance()
            .collection("/users")
            .document(uid)

        //começou a seguir aumentará um
        if (isFollow) meRef.update("following", FieldValue.increment(1))

        //deixou de seguir
        else meRef.update("following", FieldValue.increment(-1))
    }

    //contador de seguindo
    private fun followersCounter(uid: String, callback: RequestCallback<Boolean>) {

        // referencia do user logado
        val meRef = FirebaseFirestore.getInstance()
            .collection("/users")
            .document(uid)

        // contador de seguidores
        FirebaseFirestore.getInstance()
            .collection("/followers")
            .document(uid)
            .get()
            .addOnSuccessListener { response ->
                if (response.exists()) {
                    val list = response.get("followers") as List<String>
                    meRef.update("followers", list.size)
                }
                callback.onSucess(true)
            }
    }

    //funcao de adicionar e remover o feed do user
    //quando o user começa a seguir ou deixar
    private fun updateFeed(uid: String, isFollow: Boolean) {

        if (!isFollow) {
            //remover do feed
            FirebaseFirestore.getInstance()
                .collection("/feeds")
                .document(FirebaseAuth.getInstance().uid!!) // id logado
                .collection("posts") // meu feed / posts
                .whereEqualTo("publisher.uuid", uid) // feed do seguidor
                .get()
                .addOnSuccessListener { res ->
                    val documents = res.documents
                    for(document in documents) {
                        document.reference.delete() // exclui o post
                    }
                }
        } else {
            //adicionar do feed
            FirebaseFirestore.getInstance()
                .collection("/posts")
                .document(uid)
                .collection("posts")
                .get()
                .addOnSuccessListener { res ->
                    val posts = res.toObjects(Post::class.java)

                    posts.lastOrNull()?.let {
                        FirebaseFirestore.getInstance()
                            .collection("/feeds")
                            .document(FirebaseAuth.getInstance().uid!!)
                            .collection("posts")
                            .document(it.uuid!!)
                            .set(it)
                    }
                }

        }
    }
}