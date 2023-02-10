package co.tiagoaguiar.course.instagram.login.presentation

import android.util.Patterns
import co.tiagoaguiar.course.instagram.R
import co.tiagoaguiar.course.instagram.common.model.UserAuth
import co.tiagoaguiar.course.instagram.login.Login
import co.tiagoaguiar.course.instagram.login.Login.Presenter
import co.tiagoaguiar.course.instagram.login.data.LoginCallBack
import co.tiagoaguiar.course.instagram.login.data.LoginRepository

class LoginPresenter(
    private var view: Login.View?,
    private val repository: LoginRepository
) : Presenter {

    //validando o formulario de login
    override fun login(email: String, password: String) {

        //verificacao se o email e senha são validos
        val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
        val isPasswordValid = password.length >= 8

        //verifacao de email
        if (!isEmailValid) {
            view?.displayEmailFailure(R.string.invalid_email)

        } else {
            view?.displayEmailFailure(null)
        }

        //verificacao de caracteres da password
        if (!isPasswordValid ) {
            view?.displayPasswordFailure(R.string.invalid_password)

        } else {
            view?.displayPasswordFailure(null)
        }

        //camada de modelo (model) será chamada aqui
        if (isEmailValid && isPasswordValid) {

            //exibe o progressBar
            view?.showProgress(true)

            repository.login(email, password, object : LoginCallBack {

                override fun onSucess() {
                    view?.onUserAuthenticated()
                }

                override fun onFailure(message: String) {
                    view?.onUserUnauthorized(message)
                }

                override fun onComplete() {
                    //oculta o progressBar
                    view?.showProgress(false)
                }

            })
        }
    }

    override fun onDestroy() {
       view = null
    }

}