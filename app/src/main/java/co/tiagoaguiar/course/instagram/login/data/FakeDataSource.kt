package co.tiagoaguiar.course.instagram.login.data
import android.os.Handler
import android.os.Looper
import co.tiagoaguiar.course.instagram.common.model.Database

//dados falsos para realizarmos uma simulação no app
class FakeDataSource : LoginDataSource {
    override fun login(email: String, password: String, callback: LoginCallBack) {
        Handler(Looper.getMainLooper()).postDelayed({

            //irá retornar um ou nenhum usuário
            val userAuth = Database.usersAuth.firstOrNull { email == it.email }

            //vamos verifcar se o user e password existem
            when {
                userAuth ==  null -> {
                    callback.onFailure("Usuário não encontrado!")
                }
                userAuth.password != password -> {
                    callback.onFailure("Email ou Senha incorreto!")
                }
                else -> {
                    //ira aguarda a sessão do usuário
                    Database.sessionAuth = userAuth
                    callback.onSucess()
                }
            }
            callback.onComplete()
          }, 2000)
    }
}

//            if (email == "a@a.com" && password == "12345678") {
//                callback.onSucess()
//            } else {
//                callback.onFailure("Usuário não encontrado")
//            }