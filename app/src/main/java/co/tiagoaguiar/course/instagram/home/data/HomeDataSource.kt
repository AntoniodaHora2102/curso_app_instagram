package co.tiagoaguiar.course.instagram.home.data

import co.tiagoaguiar.course.instagram.common.base.RequestCallback
import co.tiagoaguiar.course.instagram.common.model.Post
import co.tiagoaguiar.course.instagram.common.model.UserAuth
import java.lang.UnsupportedOperationException

interface HomeDataSource {

    //busca da lista de post
    fun fetchFeed(userUUID: String,callback: RequestCallback<List<Post>>)

    //busca sessão do user
    fun fetchSession() : String {throw UnsupportedOperationException()}

    //listagem de posts
    fun putFeed(response: List<Post>?) {throw UnsupportedOperationException()}
}