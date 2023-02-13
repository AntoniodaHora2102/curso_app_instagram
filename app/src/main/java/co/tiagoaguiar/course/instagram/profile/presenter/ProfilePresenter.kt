package co.tiagoaguiar.course.instagram.profile.presenter

import co.tiagoaguiar.course.instagram.common.base.RequestCallback
import co.tiagoaguiar.course.instagram.common.model.Database
import co.tiagoaguiar.course.instagram.common.model.Post
import co.tiagoaguiar.course.instagram.common.model.User
import co.tiagoaguiar.course.instagram.common.model.UserAuth
import co.tiagoaguiar.course.instagram.profile.Profile
import co.tiagoaguiar.course.instagram.profile.data.ProfileRepository
import java.lang.RuntimeException

class ProfilePresenter(
    private var view: Profile.View?,
    private val repository: ProfileRepository
) : Profile.Presenter {

    override fun clear() {
        repository.clearCache()
    }

    override fun fetchUserProfile(uuid: String?) {
        view?.showProgress(true)
        //val userUUID = Database.sessionAuth?.uuid ?: throw RuntimeException("user not found")
        repository.fetchUserProfile(uuid, object : RequestCallback<Pair<User, Boolean?>> {
            override fun onSucess(data: Pair<User, Boolean?>) {
                view?.displayUserProfile(data)
            }

            override fun onFailure(message: String) {
                view?.displayRequestFailure(message)
            }

            override fun onComplete() {
            }
        })

    }

    override fun followUser(uuid: String?, follow: Boolean) {
        repository.followUser(uuid, follow, object : RequestCallback<Boolean> {
            override fun onSucess(data: Boolean) {
                fetchUserProfile(uuid) // irá dar um reload na pagina,

                if (data) {
                    view?.followUpdated()
                }
            }

            override fun onFailure(message: String) {
            }

            override fun onComplete() {
            }
        })
    }

    override fun fetchUserPosts(uuid: String?) {
      //  val userUUID = Database.sessionAuth?.uuid ?: throw RuntimeException("user not found")
        repository.fetchUserPosts(uuid, object : RequestCallback<List<Post>> {
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