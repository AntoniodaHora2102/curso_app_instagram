package co.tiagoaguiar.course.instagram.register.presentation

import android.util.Patterns
import co.tiagoaguiar.course.instagram.R
import co.tiagoaguiar.course.instagram.register.RegisterEmail
import co.tiagoaguiar.course.instagram.register.data.RegisterCallBack
import co.tiagoaguiar.course.instagram.register.data.RegisterRepository

class RegisterEmailPresenter(
    private var view: RegisterEmail.View?,
    private val repository: RegisterRepository
) : RegisterEmail.Presenter {

    //validando o formulario de email
    override fun create(email: String) {

        //verificacao se o email e senha são validos
        val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()


        //verifacao de email
        if (!isEmailValid) {
            view?.displayEmailFailure(R.string.invalid_email)

        } else {
            view?.displayEmailFailure(null)
        }


        //camada de modelo (model) será chamada aqui
        if (isEmailValid ) {

            //exibe o progressBar
            view?.showProgress(true)

            repository.create(email, object : RegisterCallBack {

                override fun onSucess() {
                    view?.goToNameAndPasswordScreen(email)
                }

                override fun onFailure(message: String) {
                    view?.onEmailFailure(message)
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