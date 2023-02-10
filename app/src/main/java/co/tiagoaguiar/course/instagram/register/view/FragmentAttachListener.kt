package co.tiagoaguiar.course.instagram.register.view

//Anexa o fragmento dentro da atividade
interface FragmentAttachListener {
    fun goToNameAndPasswordScreen(email: String) //framento de cadastro NOME/SENHA/CONFIRMAÇÃO SENHA
    fun goToWelcomeScreen(name: String) // fragmento de bem vindo
    fun goToPhotoScreen() // fragmento de Galeria de foto
    fun goToMainScreen() //tela principal do App
    fun goToGalleryScreen() // fragment de Galeria
    fun goToCameraScreen() // fragment de camera

}