package co.tiagoaguiar.course.instagram.splash.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import co.tiagoaguiar.course.instagram.R
import co.tiagoaguiar.course.instagram.common.base.DependencyInjector
import co.tiagoaguiar.course.instagram.common.extension.animationEnd
import co.tiagoaguiar.course.instagram.databinding.ActivitySplashBinding
import co.tiagoaguiar.course.instagram.login.view.LoginActivity
import co.tiagoaguiar.course.instagram.main.view.MainActivity
import co.tiagoaguiar.course.instagram.splash.Splash
import co.tiagoaguiar.course.instagram.splash.presention.SplashPresenter

class SplashActivity : AppCompatActivity(), Splash.View {
    private lateinit var binding: ActivitySplashBinding

    override lateinit var presenter: Splash.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository = DependencyInjector.splashRepository()
        presenter = SplashPresenter(this, repository)

        binding.splashImg.animate().apply {

            setListener(animationEnd {
                presenter.authenticated()
            })
            duration = 1000
            alpha(1.0f)
            start()
        }

    }

    override fun goToMainScreen() {

        //fade in - deixa a transição da splash para o activity principal
        binding.splashImg.animate().apply {
            setListener(animationEnd {
                val intent = Intent(baseContext, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out) // para não pular tanto activity no momento transição

            })

            duration = 1000 // duração da splash
            startDelay = 1000 // ira esperar um tempo inicia a activity foi chamada
            alpha(0.0f) // transparencia da img irá a 0
            start()
        }
    }

    override fun goToLoginScreen() {

        //fade in - deixa a transição da splash para o activity login
        binding.splashImg.animate().apply {

            setListener(animationEnd {
                val intent = Intent(baseContext, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out) // para não pular tanto activity no momento transição

            })
            duration = 1000
            startDelay = 1000 // ira esperar um tempo inicia a activity foi chamada
            alpha(0.0f) // transparencia da img irá a 0
            start()
        }
    }


}