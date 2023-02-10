package co.tiagoaguiar.course.instagram.register.data

import android.net.Uri

class RegisterRepository(private val dataSource: RegisterDataSource) {

    fun create(email: String, callBack: RegisterCallBack) {
        //ira decidir o que fazer com os dados
        dataSource.create(email, callBack)
    }

    // confirmacao de nome /senha
    fun create(email: String, name: String, password: String, callBack: RegisterCallBack) {
        //ira decidir o que fazer com os dados
        dataSource.create(email, name, password, callBack)
    }

    fun updateUser(photoUri: Uri, callBack: RegisterCallBack) {
        dataSource.updateUser(photoUri, callBack)
    }
}
