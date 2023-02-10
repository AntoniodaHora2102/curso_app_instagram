package co.tiagoaguiar.course.instagram.home.presenter

import co.tiagoaguiar.course.instagram.common.base.RequestCallback
import co.tiagoaguiar.course.instagram.common.model.Database
import co.tiagoaguiar.course.instagram.common.model.Post
import co.tiagoaguiar.course.instagram.common.model.UserAuth
import co.tiagoaguiar.course.instagram.home.Home
import co.tiagoaguiar.course.instagram.home.data.HomeRepository
import co.tiagoaguiar.course.instagram.profile.Profile
import co.tiagoaguiar.course.instagram.profile.data.ProfileRepository
import java.lang.RuntimeException

class HomePresenter(
    private var view: Home.View?,
    private val repository: HomeRepository
) : Home.Presenter {

    //ira limpar o cache
    override fun clear() {
        repository.clearCache()
    }

    override fun fetchFeed() {
            view?.showProgress(true)
           repository.fetchFeed( object : RequestCallback<List<Post>> {
            override fun onSucess(data: List<Post>) {
                if (data.isEmpty()) {
                    view?.displayEmptyPosts()
                } else {
                    view?.displayFullPosts(data)
                }
            }

            override fun onFailure(message: String) {
               view?.displayRequestFailure(message)
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