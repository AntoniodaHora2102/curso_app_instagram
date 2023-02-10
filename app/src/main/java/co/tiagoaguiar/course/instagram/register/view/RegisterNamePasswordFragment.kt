package co.tiagoaguiar.course.instagram.register.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import co.tiagoaguiar.course.instagram.R
import co.tiagoaguiar.course.instagram.common.base.DependencyInjector
import co.tiagoaguiar.course.instagram.common.util.TxtWatcher
import co.tiagoaguiar.course.instagram.databinding.FragmentRegisterNamePasswordBinding
import co.tiagoaguiar.course.instagram.register.RegisterNamePassword
import co.tiagoaguiar.course.instagram.register.presentation.RegisterNameAndPasswordPresenter
import java.lang.IllegalArgumentException

class RegisterNamePasswordFragment : Fragment(R.layout.fragment_register_name_password),
    RegisterNamePassword.View{

    private var binding: FragmentRegisterNamePasswordBinding? = null
    private var fragmentAttachListener: FragmentAttachListener? = null

    override lateinit var presenter: RegisterNamePassword.Presenter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentRegisterNamePasswordBinding.bind(view)

        val repository = DependencyInjector.registerEmailRepository()
        presenter = RegisterNameAndPasswordPresenter(this, repository)

        val email = arguments?.getString(KEY_EMAIL) ?: throw IllegalArgumentException("email not found") // caso o email nao exista

        binding?.let {
            with(it) {

                registerTxtLogin.setOnClickListener {
                    activity?.finish()
                }

                //enviaremos as informações do fragmento para o próximo
                registerNameBtnNext.setOnClickListener {
                    presenter.create(
                        email,
                        registerEditName.text.toString(),
                        registerEditPassword.text.toString(),
                        registerEditConfirm.text.toString()
                    )
                }

                registerEditName.addTextChangedListener(watcher)
                registerEditPassword.addTextChangedListener(watcher)
                registerEditConfirm.addTextChangedListener(watcher)

                registerEditName.addTextChangedListener(TxtWatcher {
                    displayNameFailure(null)
                })

                registerEditPassword.addTextChangedListener(TxtWatcher {
                    displayNameFailure(null)
                })

                registerEditConfirm.addTextChangedListener(TxtWatcher {
                    displayNameFailure(null)
                })
            }
        }

    }

    //constante
    companion object {
        const val KEY_EMAIL = "key_email"
    }

    private val watcher = TxtWatcher {
        binding?.registerNameBtnNext?.isEnabled = binding?.registerEditName?.text.toString().isNotEmpty()
                && binding?.registerEditPassword?.text.toString().isNotEmpty()
                && binding?.registerEditConfirm?.text.toString().isNotEmpty()
    }

    override fun onDestroy() {
        binding = null
        fragmentAttachListener = null
        presenter.onDestroy()
        super.onDestroy()
    }

    override fun showProgress(enabled: Boolean) {
       binding?.registerNameBtnNext?.showProgress(enabled)
    }

    override fun displayNameFailure(nameError: Int?) {
        binding?.registerEditNameInput?.error = nameError?.let { getString(it) }
    }

    override fun displayPasswordFailure(passError: Int?) {
        binding?.registerEditPasswordInput?.error = passError?.let { getString(it) }
    }

    override fun onCreateSuccess(name: String) {
       fragmentAttachListener?.goToWelcomeScreen(name) // chama a tela de bem-vindo do insta
    }

    override fun onCreateFailure(message: String) {
       Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    //toda vez que o fragmento é anexado ele é disparado
    override fun onAttach(context: Context) {
        super.onAttach(context)

        //guarda a referencia do Listener
        if (context is FragmentAttachListener){
            fragmentAttachListener = context
        }
    }


}