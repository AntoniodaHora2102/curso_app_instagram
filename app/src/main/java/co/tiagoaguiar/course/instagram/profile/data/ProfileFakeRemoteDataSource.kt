package co.tiagoaguiar.course.instagram.profile.data

import android.os.Handler
import android.os.Looper
import co.tiagoaguiar.course.instagram.common.base.RequestCallback
import co.tiagoaguiar.course.instagram.common.model.Database
import co.tiagoaguiar.course.instagram.common.model.Post
import co.tiagoaguiar.course.instagram.common.model.User
import co.tiagoaguiar.course.instagram.common.model.UserAuth

class ProfileFakeRemoteDataSource : ProfileDataSource {

    override fun fetchUserProfile(
        userUUID: String, callback: RequestCallback<Pair<User, Boolean?>>) {

        Handler(Looper.getMainLooper()).postDelayed({
            //ira buscar user na base de dados
            val userAuth = Database.usersAuth.firstOrNull { userUUID == it.uuid }

            if (userAuth != null) {
              //quando o user já pertence a sessão
               if (userAuth == Database.sessionAuth) {
                 //  TODO: remover essa classe - callback.onSucess(Pair(userAuth, null))
               }
                // exibir seguindo o seguidor
                //busca o user do Perfil
                else {
                  val followings = Database.followers[Database.sessionAuth!!.uuid]

                   //verifica se o id pertence ao meus seguidores
                   val destUser = followings?.firstOrNull { it == userUUID}

                       //se for diferente de null é porque estamos seguindo
                  //  TODO: remover essa classe - callback.onSucess(Pair(userAuth, destUser != null))
               }
            } else {
                callback.onFailure("Usuário não encontrado!")
            }
            callback.onComplete()
        }, 2000)
    }


    override fun fetchUserPosts(userUUID: String, callback: RequestCallback<List<Post>>) {

        Handler(Looper.getMainLooper()).postDelayed({
            //ira buscar user na base de dados
            val posts = Database.posts[userUUID]

           //ira retonar sempre sucesso
            callback.onSucess(posts?.toList() ?: emptyList())

            callback.onComplete()
        }, 2000)
    }

    //logica de seguir e deixar de seguir
    override fun followUser(userUUID: String, isFollow: Boolean, callback: RequestCallback<Boolean>) {

        Handler(Looper.getMainLooper()).postDelayed({
            var followers = Database.followers[Database.sessionAuth!!.uuid]

            //se não tiver nenhum seguidor
            if(followers == null) {
                followers = mutableSetOf() //irá retornar uma lista
                Database.followers[Database.sessionAuth!!.uuid] = followers
            }

            //lista de seguidores
            if (isFollow) {
                Database.followers[Database.sessionAuth!!.uuid]!!.add(userUUID)
            }
            //lista que deixou de seguir
            else {
                Database.followers[Database.sessionAuth!!.uuid]!!.remove(userUUID)
            }
        }, 500)
    }

}