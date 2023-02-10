package co.tiagoaguiar.course.instagram.profile.data

import co.tiagoaguiar.course.instagram.common.base.RequestCallback
import co.tiagoaguiar.course.instagram.common.model.Post
import co.tiagoaguiar.course.instagram.common.model.User
import co.tiagoaguiar.course.instagram.common.model.UserAuth
import java.lang.UnsupportedOperationException

interface ProfileDataSource {

    //busca do user
    fun fetchUserProfile(userUUID: String, callback: RequestCallback<Pair<User, Boolean?>>)

    //busca da lista de post
    fun fetchUserPosts(userUUID: String,callback: RequestCallback<List<Post>>)

    //busca sessão do user
    fun fetchSession() : String {throw UnsupportedOperationException()}

    //irá por no cache o user buscado
    fun putUser(response: Pair<User, Boolean?>?) {throw UnsupportedOperationException()}

    fun putPosts(response: List<Post>?) {throw UnsupportedOperationException()}

    //funcao de seguir e deixar de seguir
    fun followUser(userUUID: String, isFollow: Boolean, callback: RequestCallback<Boolean>)
    { throw UnsupportedOperationException() }
}