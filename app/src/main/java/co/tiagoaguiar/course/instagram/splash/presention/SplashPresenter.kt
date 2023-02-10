package co.tiagoaguiar.course.instagram.splash.presention

import co.tiagoaguiar.course.instagram.splash.Splash
import co.tiagoaguiar.course.instagram.splash.dados.SplashCallBack
import co.tiagoaguiar.course.instagram.splash.dados.SplashRepository

class SplashPresenter(
    private var view: Splash.View?,
    private val repository: SplashRepository
) : Splash.Presenter {

    override fun authenticated() {
       repository.session(object : SplashCallBack {
           override fun onSuccess() {
               view?.goToMainScreen()
           }

           override fun onFailure() {
               view?.goToLoginScreen()
           }
       })
    }

    override fun onDestroy() {
        view = null
    }
}