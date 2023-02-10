package co.tiagoaguiar.course.instagram.post

import android.net.Uri
import co.tiagoaguiar.course.instagram.common.base.BasePresenter
import co.tiagoaguiar.course.instagram.common.base.BaseView

interface Post {

    interface Presenter : BasePresenter {
        fun selectUri(uri: Uri)
        fun getSelectedUri(): Uri?
        fun fetchPicture() //buscar as fotos
    }

    interface View : BaseView<Presenter> {
        fun showProgress(enabled: Boolean)

        fun displayEmptyPictures()
        fun displayFullPictures(posts: List<Uri>)
        fun displayRequestFailure(message: String)
    }
}