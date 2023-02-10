package co.tiagoaguiar.course.instagram.register

import androidx.annotation.StringRes
import co.tiagoaguiar.course.instagram.common.base.BasePresenter
import co.tiagoaguiar.course.instagram.common.base.BaseView

interface RegisterNamePassword {

    interface Presenter : BasePresenter {
        fun create(email: String, name: String, password: String, confirm: String)
    }

    interface View: BaseView<Presenter> {
          fun showProgress(enabled: Boolean)

        //mensagem de erro
        fun displayNameFailure(@StringRes nameError: Int?)
        fun displayPasswordFailure(@StringRes passError: Int?)

        //ira exibir o nome do user caso esteja tudo ok
        fun onCreateSuccess(name: String)

        //ira exibir de erro caso tenha algum error
        fun onCreateFailure(message: String)
    }

}