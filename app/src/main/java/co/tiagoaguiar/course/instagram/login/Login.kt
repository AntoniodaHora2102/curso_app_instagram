package co.tiagoaguiar.course.instagram.login

import androidx.annotation.StringRes
import co.tiagoaguiar.course.instagram.common.base.BasePresenter
import co.tiagoaguiar.course.instagram.common.base.BaseView

interface Login {

    //camada presenter
    interface Presenter : BasePresenter {
        fun login(email: String, password: String)
    }

    //camada view
    //instanciando a interface BASEVIEW
    interface View : BaseView<Presenter> {
        //funcao do ProgressBar
        fun showProgress(enabled: Boolean)

        //mensagem de erro
        fun displayEmailFailure(@StringRes emailError: Int?)
        fun displayPasswordFailure(@StringRes passwordError: Int?)

        //autenticacao do login
        fun onUserAuthenticated()

        //falha na autenticacao ou não existe
        fun onUserUnauthorized(message: String)
    }
}