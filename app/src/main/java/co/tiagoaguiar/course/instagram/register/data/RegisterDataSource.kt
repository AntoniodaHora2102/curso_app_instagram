package co.tiagoaguiar.course.instagram.register.data

import android.net.Uri

interface RegisterDataSource {

    fun create(email: String, callback: RegisterCallBack)
    fun create(email: String, name: String, password: String, callback: RegisterCallBack)
    fun updateUser(photoUri: Uri, callback: RegisterCallBack)
}