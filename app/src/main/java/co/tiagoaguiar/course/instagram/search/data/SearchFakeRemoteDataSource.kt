package co.tiagoaguiar.course.instagram.search.data

import android.os.Handler
import android.os.Looper
import co.tiagoaguiar.course.instagram.common.base.RequestCallback
import co.tiagoaguiar.course.instagram.common.model.Database
import co.tiagoaguiar.course.instagram.common.model.Post
import co.tiagoaguiar.course.instagram.common.model.User
import co.tiagoaguiar.course.instagram.common.model.UserAuth

class SearchFakeRemoteDataSource : SearchDataSource {


    override fun fetchUsers(name: String, callback: RequestCallback<List<User>>) {

           Handler(Looper.getMainLooper()).postDelayed({

           //fazer o filtro
           val users = Database.usersAuth.filter {
               it.name.lowercase().startsWith(name.lowercase()) && it.uuid != Database.sessionAuth!!.uuid
           }

         //      callback.onSucess(users.toList())
               callback.onComplete()
        }, 2000)
    }




}