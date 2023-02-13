package co.tiagoaguiar.course.instagram.login.view

import android.content.Intent
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowInsetsController
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import co.tiagoaguiar.course.instagram.R
import co.tiagoaguiar.course.instagram.common.base.DependencyInjector
import co.tiagoaguiar.course.instagram.common.model.Database
import co.tiagoaguiar.course.instagram.common.util.TxtWatcher
import co.tiagoaguiar.course.instagram.databinding.ActivityLoginBinding
import co.tiagoaguiar.course.instagram.login.Login
import co.tiagoaguiar.course.instagram.login.data.FakeDataSource
import co.tiagoaguiar.course.instagram.login.data.LoginRepository
import co.tiagoaguiar.course.instagram.login.presentation.LoginPresenter
import co.tiagoaguiar.course.instagram.main.view.MainActivity
import co.tiagoaguiar.course.instagram.register.view.RegisterActivity

class LoginActivity : AppCompatActivity(), Login.View {

  private lateinit var binding: ActivityLoginBinding
  override lateinit var presenter: Login.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    //inflar o layout binding
    binding = ActivityLoginBinding.inflate(layoutInflater)

    //view principal
    setContentView(binding.root)

      //instancia do LoginRepository
      //al repository = LoginRepository(FakeDataSource()) - Vamos utilizar o DependencyInjector

      // instancia da class LOGINPRESENTER
      //val repository = LoginRepository(FakeDataSource())
      presenter = LoginPresenter(this, DependencyInjector.loginRepository())

      //para deixa o codigo para enxuto vamos utilizar o WITH
      //não será mais necessário repetir o binding, quando houver mais de um input ou button
      with(binding) {
              when(resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {

                  Configuration.UI_MODE_NIGHT_YES -> {
                     loginImgLogo.imageTintList = ColorStateList.valueOf(Color.WHITE)
                  }
                  Configuration.UI_MODE_NIGHT_NO -> {
                  }
              }


        //com o viewBinding não se faz mais necessário essa chamada
  //    val editTextEmail = findViewById<TextInputEditText>(R.id.login_edit_email)
  //    val editTextPassword = findViewById<TextInputEditText>(R.id.login_edit_password)

           loginEditEmail.addTextChangedListener(watcher)

          //invalida o formulario toda vez que digitar no input
          //ira limpar a informçao de erro do editText
          loginEditEmail.addTextChangedListener(TxtWatcher {
              displayEmailFailure(null)
          })

           loginEditPassword.addTextChangedListener(watcher)
            loginEditPassword.addTextChangedListener(TxtWatcher {
                displayPasswordFailure(null)
            })

        //BUTTON DE LOGIN
        //   val buttonEnter = findViewById<LoadingButton>(R.id.login_btn_enter)
            loginBtnEnter.setOnClickListener {
//
//            loginBtnEnter.showProgress(true)
//
//          // EditText de email
//          //findViewById<TextInputLayout>(R.id.login_edit_email_input)
//            loginEditEmailInput.error = "Esse email é inválido!"
//
//          // EditText de password
//          //findViewById<TextInputLayout>(R.id.login_edit_password_input)
//                loginEditPasswordInput.error = "Senha Incorreta!"
//
//          Handler(Looper.getMainLooper()).postDelayed({
//            loginBtnEnter.showProgress(false)
//          }, 4000) // tempo de atraso da chamada da funcao

            //iremos chamar o presenter não mais a função acima
           // a logica do código é por parte do presenter / regra de negocio
           presenter.login(loginEditEmail.text.toString(), loginEditPassword.text.toString())
        }

          // TextView de acesso a Cadastro Activity
            loginTxtRegister.setOnClickListener {
                goToRegisterScreen()
            }
      }
  }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    private fun goToRegisterScreen() {
        startActivity(Intent(this, RegisterActivity::class.java))
    }

    //ira ouvir o evento de campo de texto sendo alterado 
    //TextWatcher(class) customizado -- PACOTE cammon/util
    private val watcher = TxtWatcher {

        //toda vez que o usuario digitar algo no campo a funcao ira mudar o estado do BUTTON ENABLED / DISABLED
        //findViewById<LoadingButton>(R.id.login_btn_enter).isEnabled = s.toString().isNotEmpty()
        binding.loginBtnEnter.isEnabled = binding.loginEditEmail.text.toString().isNotEmpty()
                && binding.loginEditPassword.text.toString().isNotEmpty()

    }

    //funcoes da interface de LOGIN.KT
    override fun showProgress(enabled: Boolean) {
        binding.loginBtnEnter.showProgress(enabled)
    }

    override fun displayEmailFailure(emailError: Int?) {
        binding.loginEditEmailInput.error = emailError?.let { getString(it) }
    }

    override fun displayPasswordFailure(passwordError: Int?) {
        binding.loginEditPasswordInput.error = passwordError?.let { getString(it) }
    }

    // quando houver sucesso login
    override fun onUserAuthenticated() {
     val intent = Intent(this, MainActivity::class.java)
        //ira tirar activity da frente no caso a tela de login
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    //quando houver erro ou falha na autencicacao do login
    override fun onUserUnauthorized(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}