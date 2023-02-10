package co.tiagoaguiar.course.instagram.register.presentation

import android.net.Uri
import co.tiagoaguiar.course.instagram.R
import co.tiagoaguiar.course.instagram.register.RegisterNamePassword
import co.tiagoaguiar.course.instagram.register.RegisterPhoto
import co.tiagoaguiar.course.instagram.register.data.RegisterCallBack
import co.tiagoaguiar.course.instagram.register.data.RegisterRepository

class RegisterPhotoPresenter(
    private var view: RegisterPhoto.View?,
    private val repository: RegisterRepository
) : RegisterPhoto.Presenter {


    override fun updateUser(photoUri: Uri) {
            //exibe o progressBar
            view?.showProgress(true)

            repository.updateUser(photoUri , object : RegisterCallBack {

                override fun onSucess() {
                    view?.onUpdateSuccess()
                }

                override fun onFailure(message: String) {
                    view?.onUpdateFailure(message)
                }

                override fun onComplete() {
                    //oculta o progressBar
                    view?.showProgress(false)
                }

            })
        }


    override fun onDestroy() {
       view = null
    }

}