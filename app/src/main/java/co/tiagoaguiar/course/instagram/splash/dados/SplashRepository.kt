package co.tiagoaguiar.course.instagram.splash.dados

class SplashRepository(
    private val dataSource: SplashDataSource
) {
    fun session(callBack: SplashCallBack) {
        dataSource.session(callBack)
    }
}