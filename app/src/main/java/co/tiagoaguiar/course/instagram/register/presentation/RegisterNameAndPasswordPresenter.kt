package co.tiagoaguiar.course.instagram.register.presentation

import co.tiagoaguiar.course.instagram.R
import co.tiagoaguiar.course.instagram.register.RegisterNamePassword
import co.tiagoaguiar.course.instagram.register.data.RegisterCallBack
import co.tiagoaguiar.course.instagram.register.data.RegisterRepository

class RegisterNameAndPasswordPresenter(
    private var view: RegisterNamePassword.View?,
    private val repository: RegisterRepository
) : RegisterNamePassword.Presenter {

    //validando o formulario de email
    override fun create(email: String, name: String, password: String, confirm: String) {

        //verificacao do nome são válido
        val isNameValid = name.length > 3
        val isPasswordValid = password.length >= 8
        val isConfirmValid = password == confirm


        //verifacao do nome
        if (!isNameValid) {
            view?.displayNameFailure(R.string.invalid_name)
        }else {
            view?.displayNameFailure(null)
        }

        //verificacao de caracteres da password
        if (!isConfirmValid ) {
            view?.displayPasswordFailure(R.string.password_not_equal)
        } else {

            //verificacao de caracteres da password
            if (!isPasswordValid ) {
                view?.displayPasswordFailure(R.string.invalid_password)

            } else {
                view?.displayPasswordFailure(null)
            }

        }


        //camada de modelo (model) será chamada aqui
        if (isNameValid && isPasswordValid && isConfirmValid ) {

            //exibe o progressBar
            view?.showProgress(true)

            repository.create(email, name, password, object : RegisterCallBack {

                override fun onSucess() {
                    view?.onCreateSuccess(name)
                }

                override fun onFailure(message: String) {
                    view?.onCreateFailure(message)
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