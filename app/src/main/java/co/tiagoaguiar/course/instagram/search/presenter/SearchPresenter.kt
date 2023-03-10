package co.tiagoaguiar.course.instagram.search.presenter

import co.tiagoaguiar.course.instagram.common.base.RequestCallback
import co.tiagoaguiar.course.instagram.common.model.Database
import co.tiagoaguiar.course.instagram.common.model.Post
import co.tiagoaguiar.course.instagram.common.model.User
import co.tiagoaguiar.course.instagram.common.model.UserAuth
import co.tiagoaguiar.course.instagram.profile.Profile
import co.tiagoaguiar.course.instagram.profile.data.ProfileRepository
import co.tiagoaguiar.course.instagram.search.Search
import co.tiagoaguiar.course.instagram.search.data.SearchRepository
import java.lang.RuntimeException

class SearchPresenter(
    private var view: Search.View?,
    private val repository: SearchRepository
) : Search.Presenter {


    override fun fecthUsers(name: String) {

        view?.showProgress(true)

        repository.fetchUsers(name, object : RequestCallback<List<User>> {
            override fun onSucess(data: List<User>) {
                if (data.isEmpty()) {
                    view?.displayEmptyUsers()
                } else {
                    view?.displayFullUsers(data)
                }
            }

            override fun onFailure(message: String) {
               view?.displayEmptyUsers()
            }

            override fun onComplete() {
               view?.showProgress(false)
            }
        })
    }


    override fun onDestroy() {
       view = null
    }

}