package co.tiagoaguiar.course.instagram.register.data

import android.net.Uri
import android.util.Log
import co.tiagoaguiar.course.instagram.common.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

//informação serão geradas através do FIREBASE
class FireRegisterDataSource : RegisterDataSource {

    //verificação de emails
    override fun create(email: String, callback: RegisterCallBack) {
        FirebaseFirestore.getInstance()
            .collection("/users") // irá verifica se há email cadastrado
            .whereEqualTo("email", email) // verifica se o email é igual o que foi passado
            .get()
            .addOnSuccessListener { documents ->

                //documents == vazio não há user cadastrado
                if (documents.isEmpty) {
                    callback.onSucess()

                } else {
                    callback.onFailure("Usuário já cadastrado")
                }
            }
            .addOnFailureListener { exception ->
                callback.onFailure(exception.message ?: "Erro interno no Servidor")
            }
            .addOnCompleteListener {
                callback.onComplete()
            }
         }

    //criar o user
    override fun create(email: String, name: String, password: String,
                        callback: RegisterCallBack) {

        //irá salvar o user no FIREBASE
        //primeiro devo criar o user no AUTHENTACION
        FirebaseAuth.getInstance()
            .createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->

                val uid = result.user?.uid

                if (uid == null) {
                    callback.onFailure("Erro interno no servidor")
                }
                else {
                  FirebaseFirestore.getInstance()
                      .collection("/users")
                      .document(uid)
                      .set(
                          hashMapOf(
                              "name" to name,
                              "email" to email,
                              "followers" to 0,
                              "following" to 0,
                              "uuid" to uid,
                              "postCount" to 0,
                              "photoUrl" to null,

                          )
                      )
                      .addOnSuccessListener {
                          callback.onSucess()
                      }
                      .addOnFailureListener { exception ->
                          callback.onFailure(exception.message ?: "Erro interno no Servidor")
                      }
                      .addOnCompleteListener {
                          callback.onComplete()
                      }
                }

            }
            .addOnFailureListener { exception ->
                callback.onFailure(exception.message ?: "Erro interno no Servidor")
            }
        }

    //atualizar as propriedades do user logado + url da foto
    override fun updateUser(photoUri: Uri, callback: RegisterCallBack) {

        //ira aguardar o id do user logado na sessão
        val uid = FirebaseAuth.getInstance().uid

        // se user e uri == null não foi add nada
        if (uid == null || photoUri.lastPathSegment == null) {
        callback.onFailure("Usuário não encontrado")
            return
        }

        //iremos pegar a referencia do storage
        val storageRef = FirebaseStorage.getInstance().reference

        val imgRef = storageRef.child("images/")
            .child(uid)
            .child(photoUri.lastPathSegment!!)

        //irá subir a foto para storage
        imgRef.putFile(photoUri)
            .addOnSuccessListener { result ->

                //caminho para o download publico
                imgRef.downloadUrl
                    .addOnSuccessListener { res ->
                        val usersRef = FirebaseFirestore.getInstance()
                            .collection("/users")
                            .document(uid)

                        usersRef.get()
                            .addOnSuccessListener { document ->
                              val user = document.toObject(User::class.java)
                               val newUser = user?.copy(photoUrl = res.toString())

                                if(newUser != null) {
                                   usersRef.set(newUser)
                                       .addOnSuccessListener {
                                           callback.onSucess()
                                       }
                                       .addOnFailureListener { exception ->
                                           callback.onFailure(exception.message ?: "Falha ao atualizar a foto")
                                       }
                                       .addOnCompleteListener {
                                           callback.onComplete()
                                       }
                                }
                            }
                    }
            }
            .addOnFailureListener { exception ->
                callback.onFailure(exception.message ?: "Falha ao subir a foto")
            }
    }
}