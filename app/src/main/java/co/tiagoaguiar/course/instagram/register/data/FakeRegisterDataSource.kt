package co.tiagoaguiar.course.instagram.register.data
import android.net.Uri
import android.os.Handler
import android.os.Looper
import co.tiagoaguiar.course.instagram.common.model.Database
import co.tiagoaguiar.course.instagram.common.model.UserAuth
import java.util.*

//dados falsos para realizarmos uma simulação no app
class FakeRegisterDataSource : RegisterDataSource {

    override fun create(email: String, callback: RegisterCallBack) {
        Handler(Looper.getMainLooper()).postDelayed({

            //irá retornar um ou nenhum usuário
            val userAuth = Database.usersAuth.firstOrNull { email == it.email }

            //vamos verifcar se email existente
            if (userAuth == null) {
                callback.onSucess()
            } else {
                callback.onFailure("Usuário já cadastrado!")
            }

            callback.onComplete()
        }, 2000)
    }

    override fun create(email: String, name: String, password: String, callback: RegisterCallBack) {

        Handler(Looper.getMainLooper()).postDelayed({

            //irá retornar um ou nenhum usuário
            val userAuth = Database.usersAuth.firstOrNull { email == it.email }

            //vamos verifcar se email existente
            if (userAuth != null) {
                callback.onFailure("Usuário já cadastrado!")
            } else {

             //criar novo user
             val newUser =  UserAuth(UUID.randomUUID().toString(),
                                      name, email, password, null)

                //adicionando o novo user
                val created = Database.usersAuth.add(newUser)

                if (created) {
                    //vamos guardar o novo user na seção
                    Database.sessionAuth = newUser

                    Database.followers[newUser.uuid] = hashSetOf()
                    Database.posts[newUser.uuid] = hashSetOf()
                    Database.feeds[newUser.uuid] = hashSetOf()

                    callback.onSucess()
                }
                else {
                    callback.onFailure("Erro interno no servidor.")
                }
            }

            callback.onComplete()
        }, 2000)
    }

    override fun updateUser(photoUri: Uri, callback: RegisterCallBack) {
        Handler(Looper.getMainLooper()).postDelayed({

            //irá retornar um ou nenhum usuário
            val userAuth = Database.sessionAuth

            //vamos verifcar se email existente
            if (userAuth == null) {
                callback.onFailure("Usuário não encontrado")
            } else {
                 //não será mais usado desta forma e sim por URI
                //val newPhoto = Photo(userAuth.uuid, photoUri)
                //val created = Database.photos.add(newPhoto)

                //foto de perfil do user
                val index = Database.usersAuth.indexOf(Database.sessionAuth)
                Database.usersAuth[index] = Database.sessionAuth!!.copy(photoUri = photoUri)
                Database.sessionAuth = Database.usersAuth[index]

                    callback.onSucess()
                }
            callback.onComplete()

        }, 2000)
    }
}

//            if (email == "a@a.com" && password == "12345678") {
//                callback.onSucess()
//            } else {
//                callback.onFailure("Usuário não encontrado")
//            }