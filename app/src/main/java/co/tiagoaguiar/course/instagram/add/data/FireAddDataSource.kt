package co.tiagoaguiar.course.instagram.add.data

import android.net.Uri
import co.tiagoaguiar.course.instagram.common.base.RequestCallback
import co.tiagoaguiar.course.instagram.common.model.Post
import co.tiagoaguiar.course.instagram.common.model.User
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

import com.google.firebase.storage.FirebaseStorage
import java.lang.IllegalArgumentException
import java.lang.RuntimeException

class FireAddDataSource : AddDataSource {

    override fun createPost(userUUID: String, uri: Uri, caption: String,
                            callback: RequestCallback<Boolean>) {
        //caminho da imagem
        val uriLastPath = uri.lastPathSegment ?: throw IllegalArgumentException("Invalid img")

        //referencia do storage
        val imgRef = FirebaseStorage.getInstance().reference
            .child("images/")
            .child(userUUID)
            .child(uriLastPath)

        imgRef.putFile(uri)
            .addOnSuccessListener { res ->

                imgRef.downloadUrl // baixar a imagem
                    .addOnSuccessListener { resDownload ->

                        val meRef = FirebaseFirestore.getInstance()
                            .collection("/users")
                            .document(userUUID)

                            meRef.get()
                            .addOnSuccessListener { resMe ->

                                val me = resMe.toObject(User::class.java)

                                //coleçao de posts
                                val postRef = FirebaseFirestore.getInstance()
                                    .collection("/posts")
                                    .document(userUUID)
                                    .collection("posts")
                                    .document()

                                //informações da class Post.kt
                                val post = Post(
                                    uuid = postRef.id,
                                    photoUrl = resDownload.toString(),
                                    caption = caption,
                                    timestamp = System.currentTimeMillis(),
                                    publisher = me
                                )

                                postRef.set(post)
                                    .addOnSuccessListener {  resPost ->

                                        //contador de post
                                        meRef.update("postCount", FieldValue.increment(1))


                                        //salvar o post no feed (meu)
                                        FirebaseFirestore.getInstance()
                                            .collection("/feeds")
                                            .document(userUUID)
                                            .collection("posts")
                                            .document(postRef.id)
                                            .set(post)
                                            .addOnSuccessListener { resMyFeed ->

                                                //feeds dos meus seguidores
                                                FirebaseFirestore.getInstance()
                                                    .collection("/followers")
                                                    .document(userUUID)
                                                    .get()
                                                    .addOnSuccessListener { resFollowers ->

                                                        if (resFollowers.exists()) {

                                                          val list = resFollowers.get("followers")
                                                                  as List<String>

                                                            for (followersUUID in list){

                                                                FirebaseFirestore.getInstance()
                                                                    .collection("/feeds")
                                                                    .document(followersUUID)
                                                                    .collection("posts")
                                                                    .document(postRef.id)
                                                                    .set(post)
                                                            }
                                                        }


                                                        callback.onSucess(true)

                                                    }
                                                    .addOnFailureListener { exception ->
                                                        callback.onFailure(exception.message ?: "Falha ao inserir feed dos seguidores")
                                                    }
                                                    .addOnCompleteListener {
                                                        callback.onComplete()
                                                    }
                                            }

                                    }.addOnFailureListener { exception ->
                                        callback.onFailure(exception.message ?: "Falha ao carregar o feed")
                                    }


                            }.addOnFailureListener { exception ->
                                callback.onFailure(exception.message ?: "Falha ao buscar usuário logado")
                            }


                    }.addOnFailureListener { exception ->
                        callback.onFailure(exception.message ?: "Falha ao baixar foto")
                    }
            }
            .addOnFailureListener { exception ->
                callback.onFailure(exception.message ?: "Falha ao subir imagem")
            }
    }
}