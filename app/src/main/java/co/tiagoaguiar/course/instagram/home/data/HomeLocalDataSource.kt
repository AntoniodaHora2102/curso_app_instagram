package co.tiagoaguiar.course.instagram.home.data

import co.tiagoaguiar.course.instagram.common.base.Cache
import co.tiagoaguiar.course.instagram.common.base.RequestCallback
import co.tiagoaguiar.course.instagram.common.model.Database
import co.tiagoaguiar.course.instagram.common.model.Post
import co.tiagoaguiar.course.instagram.common.model.UserAuth
import com.google.firebase.auth.FirebaseAuth
import java.lang.RuntimeException

class HomeLocalDataSource(private val feedCache: Cache<List<Post>>) : HomeDataSource {

    override fun fetchFeed(userUUID: String, callback: RequestCallback<List<Post>>) {
         val posts = feedCache.get(userUUID)
        if (posts != null) {
            callback.onSucess(posts)
        } else {
            callback.onFailure("Postes não encontrados!")
        }
        callback.onComplete()
    }

    override fun fetchSession(): String {
       return FirebaseAuth.getInstance().uid ?: throw RuntimeException("Usuário não logado!")
    }

    override fun putFeed(response: List<Post>?) {
         feedCache.put(response)
    }
}