package co.tiagoaguiar.course.instagram.register.view

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import co.tiagoaguiar.course.instagram.R
import co.tiagoaguiar.course.instagram.common.extension.hideKeyboard
import co.tiagoaguiar.course.instagram.common.extension.replaceFragment
import co.tiagoaguiar.course.instagram.common.view.CropperImageFragment
import co.tiagoaguiar.course.instagram.databinding.ActivityRegisterBinding
import co.tiagoaguiar.course.instagram.common.view.CropperImageFragment.Companion.KEY_URI
import co.tiagoaguiar.course.instagram.main.view.MainActivity
import co.tiagoaguiar.course.instagram.register.view.RegisterNamePasswordFragment.Companion.KEY_EMAIL
import co.tiagoaguiar.course.instagram.register.view.RegisterWelcomeFragment.Companion.KEY_NAME
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class RegisterActivity : AppCompatActivity(), FragmentAttachListener {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var currentPhoto: Uri


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)

        setContentView(binding.root)

        //gerenciador de fragments
        val fragment = RegisterEmailFragment()
        replaceFragment(fragment)

//        supportFragmentManager.beginTransaction().apply {
//            add(R.id.register_fragment, fragment)
//            commit()

    }


    override fun goToNameAndPasswordScreen(email: String) {

        //passar a informção do email para o proximo fragment
        val fragment = RegisterNamePasswordFragment().apply {
            arguments = Bundle().apply {
                putString(KEY_EMAIL, email)
            }
        }

        //irá chamar o próximo fragment
        replaceFragment(fragment)
            Log.i("Teste", email)
    }

    override fun goToWelcomeScreen(name: String) {
        //passar a informção do email para o proximo fragment
        val fragment = RegisterWelcomeFragment().apply {
            arguments = Bundle().apply {
                putString(KEY_NAME, name)
            }
        }

        //irá chamar o próximo fragment
        replaceFragment(fragment)
        //Log.i("Teste", email)
    }


    //fragmento de foto
    override fun goToPhotoScreen() {

        //irá chamar o fragmento de de Adicionar a Photo
        val fragment = RegisterPhotoFragment()
        replaceFragment(fragment)
    }


    //irá adicionar para tela principal do app instagram
    override fun goToMainScreen() {
        val intent = Intent(this, MainActivity::class.java)

        //irá limpar as atividades da frente
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    //funcao que ira disparada toda vez que formos escolher a foto
    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) {
        uri: Uri? ->
        uri?.let {
            openImageCropper(it)
        }
    }

    //fragmento de galeria
    override fun goToGalleryScreen() {
        getContent.launch("image/*")
    }

    private val getCamera = registerForActivityResult(ActivityResultContracts.TakePicture()) { saved ->
        if (saved) {
            openImageCropper(currentPhoto)
        }
    }


    //fragmento de camera
    override fun goToCameraScreen() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE) // captura da foto
        if (intent.resolveActivity(packageManager) != null) { //abrir o app de camera

            val photoFile: File? = try {
                createImageFile()
            } catch (e: IOException) {
                Log.e("RegisterActivity", e.message, e)
                null
            }

            //ira pegar a foto e lancar na uri
            photoFile?.also {
                val photoUri = FileProvider.getUriForFile(this, "co.tiagoaguiar.course.instagram.fileprovider", it)
                currentPhoto = photoUri

                getCamera.launch(photoUri)
            }
        }
    }

    @Throws(IOException::class) // será lancada uma seção quando não conseguir registrar
    private fun createImageFile(): File {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date()) //nome do arquivo da foto
        val dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES) //apenas este app podera usar a foto tirada a partir dele
        return File.createTempFile("JPEG_${timestamp}_",".jpg", dir) // local que ira gravar
    }

    private fun replaceFragment(fragment: Fragment) {

         replaceFragment(R.id.register_fragment, fragment)
         hideKeyboard() // irá ocultar o teclado nas SKDs mais antigas
   }

    private fun openImageCropper(uri: Uri) {
        val fragment = CropperImageFragment().apply {
            arguments = Bundle().apply {
                putParcelable(KEY_URI, uri)
            }
        }
        replaceFragment(fragment)
        Log.i("Teste", uri.toString())
    }

}

