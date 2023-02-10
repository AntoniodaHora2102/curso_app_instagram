package co.tiagoaguiar.course.instagram.register.view

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import co.tiagoaguiar.course.instagram.R
import co.tiagoaguiar.course.instagram.common.base.DependencyInjector
import co.tiagoaguiar.course.instagram.common.util.TxtWatcher
import co.tiagoaguiar.course.instagram.databinding.FragmentRegisterEmailBinding
import co.tiagoaguiar.course.instagram.register.RegisterEmail
import co.tiagoaguiar.course.instagram.register.presentation.RegisterEmailPresenter

class RegisterEmailFragment : Fragment(R.layout.fragment_register_email), RegisterEmail.View {

    private var binding: FragmentRegisterEmailBinding? = null
    private var fragmentAttachListener: FragmentAttachListener? = null

    override  lateinit var presenter: RegisterEmail.Presenter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentRegisterEmailBinding.bind(view)

        val repository = DependencyInjector.registerEmailRepository()
        presenter = RegisterEmailPresenter(this, repository)


        binding?.let {
            with(it) {
                //ira finalizar activity
                registerTxtLogin.setOnClickListener {
                    activity?.finish()
                }

                //ira chamar o presenter do RegisterEmail
                registerBtnNext.setOnClickListener {
                    presenter.create(
                        registerEditEmail.text.toString()
                    )
                }

                //reativa o button da tela
                registerEditEmail.addTextChangedListener(watcher)
                registerEditEmail.addTextChangedListener(TxtWatcher {
                    displayEmailFailure(null)
                })
            }
        }
    }

    //toda vez que o fragmento é anexado ele é disparado
    override fun onAttach(context: Context) {
        super.onAttach(context)

        //guarda a referencia do Listener
        if (context is FragmentAttachListener){
            fragmentAttachListener = context
        }
    }

    private val watcher = TxtWatcher {
        binding?.registerBtnNext?.isEnabled = binding?.registerEditEmail?.text.toString().isNotEmpty()
    }

    override fun onDestroy() {
        binding = null
        fragmentAttachListener = null
        presenter.onDestroy()
        super.onDestroy()
    }

    override fun showProgress(enabled: Boolean) {
       binding?.registerBtnNext?.showProgress(enabled)
    }

    override fun displayEmailFailure(emailError: Int?) {
        binding?.registerEditEmailInput?.error = emailError?.let { getString(it) }
    }

    override fun onEmailFailure(message: String) {
        binding?.registerEditEmailInput?.error = message
    }

    override fun goToNameAndPasswordScreen(email: String) {
       fragmentAttachListener?.goToNameAndPasswordScreen(email)
    }



//    //administa o fragment_register_email.xml
//    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//
//        //irá inflar o layout do FRAGMENTO DE EMAIL
//        return inflater.inflate(R.layout.fragment_register_email, container, false )
//    }
}