package co.tiagoaguiar.course.instagram.profile

import co.tiagoaguiar.course.instagram.common.base.BasePresenter
import co.tiagoaguiar.course.instagram.common.base.BaseView
import co.tiagoaguiar.course.instagram.common.model.Post
import co.tiagoaguiar.course.instagram.common.model.User
import co.tiagoaguiar.course.instagram.common.model.UserAuth
import java.util.*

interface Profile {

        interface Presenter: BasePresenter {
        fun fetchUserProfile(uuid: String?) // buscar perfil do user
        fun fetchUserPosts(uuid: String?) // buscar posts do perfil do user
        fun clear() // irá limpar o cache criado

        //seguir e parar de seguir
        fun followUser(uuid: String?, follow: Boolean)
  }

    //v3
//    interface StatefulPresenter<S: State>: BasePresenter {
//        fun subscribe(state: S?)
//        fun getState(): S
//    }

//    interface Presenter : StatefulPresenter<State> {
//    }
//
//    interface State {
//        fun fetchUserProfile() : UserAuth?
//        fun fetchUserPosts() : List<Post>?
//
//    }

    interface View : BaseView<Presenter> {
        fun showProgress(enabled: Boolean)

        fun displayUserProfile(user: Pair<User, Boolean?>)
        fun displayRequestFailure(message: String)
        fun displayEmptyPosts()
        fun displayFullPosts(posts: List<Post>)
    }
}