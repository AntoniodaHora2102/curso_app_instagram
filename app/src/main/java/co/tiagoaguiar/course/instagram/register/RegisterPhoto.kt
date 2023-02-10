package co.tiagoaguiar.course.instagram.register

import android.net.Uri
import androidx.annotation.StringRes
import co.tiagoaguiar.course.instagram.common.base.BasePresenter
import co.tiagoaguiar.course.instagram.common.base.BaseView

interface RegisterPhoto {

    interface Presenter : BasePresenter {
        fun updateUser (photoUri: Uri)
    }

    interface View: BaseView<Presenter> {
        fun showProgress(enabled: Boolean)

        fun onUpdateSuccess() // só irá navegar para a próxima tela por isso não há parametros

        //ira exibir de erro caso tenha algum error
        fun onUpdateFailure(message: String)
    }

}