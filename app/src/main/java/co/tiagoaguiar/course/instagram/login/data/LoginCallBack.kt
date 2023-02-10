package co.tiagoaguiar.course.instagram.login.data

import co.tiagoaguiar.course.instagram.common.model.UserAuth

interface LoginCallBack {

    //três estados de requisão realizado pelo usuário seja do servidor ou local
    fun onSucess()
    fun onFailure(message: String)
    fun onComplete()
}