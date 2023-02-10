package co.tiagoaguiar.course.instagram.login.data

import com.google.firebase.auth.FirebaseAuth

class FireLoginDataSource : LoginDataSource {

    override fun login(email: String, password: String, callback: LoginCallBack) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { res ->
                if (res.user != null) {
                    callback.onSucess()
                } else {
                    callback.onFailure("Usuário não encontrado")
                }

            }
            .addOnFailureListener { exception ->
                callback.onFailure(exception.message ?: "Erro ao fazer Login")
            }
            .addOnCompleteListener {
                callback.onComplete()
            }
    }
}

