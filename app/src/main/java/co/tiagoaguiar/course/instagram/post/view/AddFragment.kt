package co.tiagoaguiar.course.instagram.post.view

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import co.tiagoaguiar.course.instagram.R
import co.tiagoaguiar.course.instagram.add.view.AddActivity
import co.tiagoaguiar.course.instagram.databinding.FragmenteAddBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class AddFragment : Fragment(R.layout.fragmente_add) {

    private var binding: FragmenteAddBinding? = null
    private var addListerner: AddListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFragmentResultListener("takePhotoKey") { requestKey, bundle ->
            val uri = bundle.getParcelable<Uri>("uri")
            uri?.let {
                val intent = Intent(requireContext(), AddActivity::class.java)
                intent.putExtra("photoUri", uri)
                //startActivity(intent)

                addActivityResult.launch(intent)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmenteAddBinding.bind(view)

        if (savedInstanceState == null) {
            setupViews()
        }
    }

    //toda que o fragmento foi anexado na activity principal
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is AddListener) {
            addListerner = context
        }
    }


   //referencia das pages
   private fun setupViews() {
        val tabLayout = binding?.addTab
        val viewPager = binding?.addViewpager
        val adapter = AddViewPagerAdapter(requireActivity())
       viewPager?.adapter = adapter

       //sincronização entre as viewPages
       if (tabLayout != null && viewPager != null) {

           //ira escutar quando aba for selecionada
           tabLayout.addOnTabSelectedListener( object : TabLayout.OnTabSelectedListener {
                   override fun onTabSelected(tab: TabLayout.Tab?) {
                      if (tab?.text == getString(adapter.tabs[0])) {
                          startCamera()
                      }
                   }

                   override fun onTabUnselected(tab: TabLayout.Tab?) {
                   }

                   override fun onTabReselected(tab: TabLayout.Tab?) {
                   }
               })
               TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                   tab.text = getString(adapter.tabs[position])
               }.attach() // ira sincronizar e vincular
       }

       //solicitação de ativacao da camera
       if(allPermissionGranted()) {
           startCamera()
       } else {
            getPermission.launch(REQUIRED_PERMISSION)
       }
    }

    // metodo que irá ativa a camera
    private fun startCamera(){
        setFragmentResult("cameraKey", bundleOf("startCamera" to true))
    }

    //INTERFACE QUE IRÁ REALIZAR O REFRESH DO POST
    interface AddListener {
        fun onPostCreated()
    }

    private val addActivityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                addListerner?.onPostCreated() // irá informar que atividade foi finalizada
            }
    }

    //ira solicitar a permissão para o uso da camera
    private fun allPermissionGranted() =
        ContextCompat.checkSelfPermission(requireContext(),
            REQUIRED_PERMISSION[0]) == PackageManager.PERMISSION_GRANTED
        && ContextCompat.checkSelfPermission(requireContext(),
            REQUIRED_PERMISSION[1]) == PackageManager.PERMISSION_GRANTED

    companion object {
        private val REQUIRED_PERMISSION = arrayOf(Manifest.permission.CAMERA,
                                            Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    private val getPermission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions())
        { granted ->
        if (allPermissionGranted()) {
            startCamera()
        } else {
            Toast.makeText(requireContext(), R.string.permission_camera_denied, Toast.LENGTH_LONG).show()
        }
    }

}