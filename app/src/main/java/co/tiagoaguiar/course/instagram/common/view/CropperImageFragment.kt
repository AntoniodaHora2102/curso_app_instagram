package co.tiagoaguiar.course.instagram.common.view

import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import co.tiagoaguiar.course.instagram.R
import co.tiagoaguiar.course.instagram.databinding.FragmentImageCropperBinding
import java.io.File

class CropperImageFragment : Fragment(R.layout.fragment_image_cropper) {

    private var binding: FragmentImageCropperBinding? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentImageCropperBinding.bind(view)

        val uri = arguments?.getParcelable<Uri>(KEY_URI)

        binding?.let {
            with(it) {

                cropperContainer.setAspectRatio(1,1)
                cropperContainer.setFixedAspectRatio(true)

                cropperContainer.setImageUriAsync(uri)

                //BUTTON DE CANCELAR
                cropperBtnCancel.setOnClickListener {
                    parentFragmentManager.popBackStack()
                }


                //IRA JOGAR A IMAGEM PARA O FRAGMENT_REGISTER_PHOTO
                cropperContainer.setOnCropImageCompleteListener { view, result ->
                    Log.i("Teste", "nova imagem ${result.uri}")
                    setFragmentResult("cropKey", bundleOf(KEY_URI to result.uri))
                    parentFragmentManager.popBackStack()
                }

                //BUTTON DE SALVAR A IMAGEM
                cropperBtnSave.setOnClickListener {
                    val dir = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                    if (dir != null) {
                      val uriToSaved =  Uri.fromFile(File(dir.path, System.currentTimeMillis()
                          .toString() + ".jpeg"))
                        cropperContainer.saveCroppedImageAsync(uriToSaved)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    companion object {
        const val KEY_URI = "key_uri"
    }


}