package co.tiagoaguiar.course.instagram.register.data

import co.tiagoaguiar.course.instagram.common.model.UserAuth

interface RegisterCallBack {

    //três estados de requisão realizado pelo usuário seja do servidor ou local
    fun onSucess()
    fun onFailure(message: String)
    fun onComplete()
}