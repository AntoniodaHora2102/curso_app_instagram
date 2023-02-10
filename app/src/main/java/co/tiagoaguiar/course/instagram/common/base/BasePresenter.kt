package co.tiagoaguiar.course.instagram.common.base

//interface PAI para o Presenter
// não será necessário declarar os metodos em todas as class
// que utilizaram os metodos criados para o presenter
//basta instancia a interface na class ou interface que desejar
interface BasePresenter {

    //funcao onDestroy
    fun onDestroy()
}