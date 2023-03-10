package co.tiagoaguiar.course.instagram.search.data

import co.tiagoaguiar.course.instagram.common.base.RequestCallback
import co.tiagoaguiar.course.instagram.common.model.Post
import co.tiagoaguiar.course.instagram.common.model.User
import co.tiagoaguiar.course.instagram.common.model.UserAuth

class SearchRepository (private val dataSource: SearchDataSource) {


    fun fetchUsers(name: String, callback: RequestCallback<List<User>>) {

        dataSource.fetchUsers(name, object : RequestCallback<List<User>> {
            override fun onSucess(data: List<User>) {
               callback.onSucess(data)
            }

            override fun onFailure(message: String) {
               callback.onFailure(message)
            }

            override fun onComplete() {
                callback.onComplete()
            }
        })
    }
}