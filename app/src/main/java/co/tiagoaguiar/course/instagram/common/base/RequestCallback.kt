package co.tiagoaguiar.course.instagram.common.base

//callback generico
interface RequestCallback<T> {
    //três estados de requisão realizado pelo usuário seja do servidor ou local
    fun onSucess(data: T)
    fun onFailure(message: String)
    fun onComplete()

}