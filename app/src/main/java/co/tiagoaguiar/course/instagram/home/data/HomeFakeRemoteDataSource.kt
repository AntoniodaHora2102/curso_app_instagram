package co.tiagoaguiar.course.instagram.home.data

import android.os.Handler
import android.os.Looper
import co.tiagoaguiar.course.instagram.common.base.RequestCallback
import co.tiagoaguiar.course.instagram.common.model.Database
import co.tiagoaguiar.course.instagram.common.model.Post
import co.tiagoaguiar.course.instagram.common.model.UserAuth

class HomeFakeRemoteDataSource : HomeDataSource {

    override fun fetchFeed(userUUID: String, callback: RequestCallback<List<Post>>) {
         Handler(Looper.getMainLooper()).postDelayed({
            //ira buscar user na base de dados
            val feed = Database.feeds[userUUID]

           //ira retonar sempre sucesso
            callback.onSucess(feed?.toList() ?: emptyList())

            callback.onComplete()
        }, 2000)
    }




}