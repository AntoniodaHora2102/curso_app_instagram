package co.tiagoaguiar.course.instagram.register.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import co.tiagoaguiar.course.instagram.R
import co.tiagoaguiar.course.instagram.databinding.FragmenteRegisterWelcomeBinding
import java.lang.IllegalArgumentException

class RegisterWelcomeFragment : Fragment(R.layout.fragmente_register_welcome) {

    private var binding: FragmenteRegisterWelcomeBinding? = null
    private var fragmentAttachListener: FragmentAttachListener? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmenteRegisterWelcomeBinding.bind(view)

        val name = arguments?.getString(KEY_NAME) ?: throw IllegalArgumentException("nome not found") // caso o email nao exista

        binding?.let {

            with(it) {
                registerTxtWelcome.text = getString(R.string.welcome_to_instagram, name)

                registerBtnNext.isEnabled = true // button ficará ativado
                registerBtnNext.setOnClickListener {
                    fragmentAttachListener?.goToPhotoScreen() // não necessita de nenhum parametro - Fragmento de Galerid de foto

                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        //guarda a referencia do Listener
        if (context is FragmentAttachListener){
            fragmentAttachListener = context
        }
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    //constante
    companion object {
        const val KEY_NAME = "key_name"
    }

//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        return inflater.inflate(R.layout.fragmente_register_welcome, container, false)
//    }
}