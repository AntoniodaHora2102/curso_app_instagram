package co.tiagoaguiar.course.instagram.search.data

import co.tiagoaguiar.course.instagram.common.base.RequestCallback
import co.tiagoaguiar.course.instagram.common.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FireSearchDataSource : SearchDataSource {

    override fun fetchUsers(name: String, callback: RequestCallback<List<User>>) {

        FirebaseFirestore.getInstance()
            .collection("/users")
            .whereGreaterThanOrEqualTo("name" , name)
            .whereLessThanOrEqualTo("name", name.lowercase() + "\uf8ff")// \uf88ff - unicode
            .get()
            .addOnSuccessListener { res ->
                val documents = res.documents
                val users = mutableListOf<User>()
                for (document in documents) {
                    val user = document.toObject(User::class.java)

                    //irá verificar se o usuário pesquisado não seja do user logado
                    if (user != null && user.uuid != FirebaseAuth.getInstance().uid){
                        users.add(user)
                 }
                }
                callback.onSucess(users)
            }
            .addOnFailureListener { exception ->
                callback.onFailure(exception.message ?: "Usuário não encontrado")
            }
            .addOnCompleteListener {
                callback.onComplete()
            }
    }
}