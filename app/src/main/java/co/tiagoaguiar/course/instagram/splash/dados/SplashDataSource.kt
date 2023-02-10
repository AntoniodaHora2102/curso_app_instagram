package co.tiagoaguiar.course.instagram.splash.dados

interface SplashDataSource {
    fun session(callback: SplashCallBack)
}