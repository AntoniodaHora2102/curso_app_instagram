package co.tiagoaguiar.course.instagram.login.data

class LoginRepository(private val dataSource: LoginDataSource) {

    fun login(email:String, password: String, callBack: LoginCallBack) {
        //ira decidir o que fazer com os dados
        dataSource.login(email, password, callBack)
    }
}
