package co.tiagoaguiar.course.instagram.register.view

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import co.tiagoaguiar.course.instagram.R
import co.tiagoaguiar.course.instagram.common.base.DependencyInjector
import co.tiagoaguiar.course.instagram.common.view.CropperImageFragment
import co.tiagoaguiar.course.instagram.common.view.CustomDialog
import co.tiagoaguiar.course.instagram.databinding.FragmentRegisterPhotoBinding
import co.tiagoaguiar.course.instagram.register.RegisterPhoto
import co.tiagoaguiar.course.instagram.register.presentation.RegisterPhotoPresenter

// será chamado aqui e não mais no onCreateView
class RegisterPhotoFragment : Fragment(R.layout.fragment_register_photo), RegisterPhoto.View {

    private var binding: FragmentRegisterPhotoBinding? = null // irá iniciar null

    private var fragmentAttachListener: FragmentAttachListener? = null

    override lateinit var presenter: RegisterPhoto.Presenter

    // com o BINDING o onCreateView não será mais necessário pois a classe já irá identificar o fragmento
//    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        return inflater.inflate(R.layout.fragment_resgiter_photo, container, false)
//    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFragmentResultListener("cropKey") { requestKey, bundle ->
            val uri = bundle.getParcelable<Uri>(CropperImageFragment.KEY_URI)

            onCropImageResult(uri)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // usaremos a view (não inflate), pois, trata-se de um fragment e não de activity
        binding = FragmentRegisterPhotoBinding.bind(view)

        val repository = DependencyInjector.registerEmailRepository()
        presenter = RegisterPhotoPresenter(this, repository)

        binding?.let {
            with(it) {

                when(resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {

                    Configuration.UI_MODE_NIGHT_YES -> {
                        registerImgProfile.imageTintList = ColorStateList.valueOf(Color.WHITE)
                    }
                    Configuration.UI_MODE_NIGHT_NO -> {
                    }
                }

                registerBtnJump.setOnClickListener {
                    fragmentAttachListener?.goToMainScreen()
                }

                registerBtnNext.isEnabled = true
                registerBtnNext.setOnClickListener {
                    openDialog()
                }
            }
        }


    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is FragmentAttachListener) {
            fragmentAttachListener = context
        }
    }

    override fun showProgress(enabled: Boolean) {
        binding?.registerBtnNext?.showProgress(enabled)
    }

    override fun onUpdateSuccess() {
        fragmentAttachListener?.goToMainScreen() // irá direcionar para a tela principal do app
    }

    override fun onUpdateFailure(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    //funcao para abrir o Dialog do fragment
    private fun openDialog() {

        val customDialog = CustomDialog(requireContext()) // temos que passar o contexto da DIALOG

        //adicionando a funcao do BUTTON mais simplificada
        customDialog.addButton(R.string.photo, R.string.gallery) {

            //iremos verificar o evento de click
            when (it.id) {

                //irá abrir a camera
                R.string.photo -> {
                    fragmentAttachListener?.goToCameraScreen()
                    Log.i("TESTE", "foto")
                }

                //irá abrir a galeria
                R.string.gallery -> {
                    fragmentAttachListener?.goToGalleryScreen()
                    Log.i("TESTE", "galeria")

                }
            }
        }

        //adicionando a funcao do BUTTON
//        customDialog.addButton({
//            Log.i("TESTE", (it as TextView).text.toString())
//        }, R.string.photo, R.string.gallery)

        //ira exibir fragment DIALOG
        customDialog.show()
    }

    private fun onCropImageResult(uri: Uri?) {
        if (uri != null) {

            //ira funcionar em api >= que a versão do SDK 28
            val bitmap = if (Build.VERSION.SDK_INT >= 28 ) {
                val source = ImageDecoder.createSource(requireContext().contentResolver, uri)
                ImageDecoder.decodeBitmap(source)
            } else {
                //ira funcionar para a versao mais antiga
                MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)
            }
            binding?.registerImgProfile?.setImageBitmap(bitmap)

            presenter.updateUser(uri)
        }
    }


    // ao fim do clico de vida será destruído
    override fun onDestroy() {
        binding = null
        presenter.onDestroy()
        super.onDestroy()
    }
}