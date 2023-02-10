package co.tiagoaguiar.course.instagram.splash.dados

import co.tiagoaguiar.course.instagram.common.model.Database

class FakeLocalDataSource : SplashDataSource {
    override fun session(callback: SplashCallBack) {
        if (Database.sessionAuth != null) {
            callback.onSuccess()
        } else{
            callback.onFailure()
        }

    }
}