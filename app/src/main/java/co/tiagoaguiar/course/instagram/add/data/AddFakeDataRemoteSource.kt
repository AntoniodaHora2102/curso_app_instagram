package co.tiagoaguiar.course.instagram.add.data

import android.net.Uri
import android.os.Handler
import android.os.Looper
import co.tiagoaguiar.course.instagram.common.base.RequestCallback
import co.tiagoaguiar.course.instagram.common.model.Database
import co.tiagoaguiar.course.instagram.common.model.Post
import java.util.*

class AddFakeDataRemoteSource : AddDataSource {

    override fun createPost(userUUID: String, uri: Uri, caption: String, callback: RequestCallback<Boolean>) {
       Handler(Looper.getMainLooper()).postDelayed({

           //lista de posts do user
           var posts = Database.posts[userUUID]

           if (posts == null) {
              posts = mutableSetOf()
               Database.posts[userUUID] = posts
           }

           //cria o
           //TODO: remover essa classe
           var post = Post(UUID.randomUUID().toString(),null, caption, System.currentTimeMillis(), null)

           posts.add(post)

           //lista de seguidores
           var followers = Database.followers[userUUID]
            if (followers == null) {
                followers = mutableSetOf()
                Database.followers[userUUID] = followers

            } else {

                //feed dos seguidores
                for (follower in followers) {
                    Database.feeds[follower]?.add(post)
                }

                //feed do user
                Database.feeds[userUUID]?.add(post)
            }
           callback.onSucess(true)

       }, 1000)
    }
}