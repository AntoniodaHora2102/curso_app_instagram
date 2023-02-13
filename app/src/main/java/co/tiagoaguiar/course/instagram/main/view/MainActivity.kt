package co.tiagoaguiar.course.instagram.main.view

import android.content.Intent
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.WindowInsetsController
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import co.tiagoaguiar.course.instagram.R
import co.tiagoaguiar.course.instagram.post.view.AddFragment
import co.tiagoaguiar.course.instagram.common.extension.replaceFragment
import co.tiagoaguiar.course.instagram.databinding.ActivityMainBinding
import co.tiagoaguiar.course.instagram.home.view.HomeFragment
import co.tiagoaguiar.course.instagram.main.LogoutListener
import co.tiagoaguiar.course.instagram.profile.view.ProfileFragment
import co.tiagoaguiar.course.instagram.search.view.SearchFragment
import co.tiagoaguiar.course.instagram.splash.view.SplashActivity
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener,
AddFragment.AddListener,
SearchFragment.SearchListener,
ProfileFragment.FollowListener,
LogoutListener {

    private lateinit var binding: ActivityMainBinding

    private lateinit var homeFragment: HomeFragment
    private lateinit var searchFragment: Fragment
    private lateinit var addFragment: Fragment
    private lateinit var profileFragment: ProfileFragment

    private var currentFragment: Fragment? = null

  //  private lateinit var fragmentSavedState: HashMap<String, Fragment.SavedState?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        //trocar tema do icone do status bar
        //VERSÕES MAIS NOVAS DO SDK
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

                when(resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {

                    Configuration.UI_MODE_NIGHT_YES -> {
                        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
                    }
                    Configuration.UI_MODE_NIGHT_NO -> {

                        window.insetsController?.setSystemBarsAppearance(
                            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                        )

                        //trocar a cor do status bar
                        window.statusBarColor = ContextCompat.getColor(this, R.color.gray)
                    }
                }
            }

        // configurar a cor do TEMA ESCURO / TEM CLARO
        if (resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK) ==
            Configuration.UI_MODE_NIGHT_YES) {
            binding.mainImgLogo.imageTintList = ColorStateList.valueOf(Color.WHITE)
        } else {
            binding.mainImgLogo.imageTintList = ColorStateList.valueOf(Color.BLACK)
        }

        //val toolbar = findViewById<Toolbar>(R.id.main_toolbar)
        //ira escutar o evento de acao dos icones do TOOLBAR
        setSupportActionBar(binding.mainToolbar)  //-- não usar no modo escuro
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_insta_camera)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // se tornará clicavel
        supportActionBar?.title = ""


        //instancia do fragments
        homeFragment = HomeFragment()
        searchFragment = SearchFragment()
        addFragment = AddFragment()
        profileFragment = ProfileFragment()

        //inflar o layout do menu home
        binding.mainBottomNav.setOnNavigationItemSelectedListener(this)
        binding.mainBottomNav.selectedItemId = R.id.menu_bottom_home

    }

    override fun goToProfile(uuid: String) {
        val fragment = ProfileFragment().apply {
            arguments = Bundle().apply {
                putString(ProfileFragment.KEY_USER_ID, uuid)
            }
        }
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.main_fragment, fragment, fragment.javaClass.simpleName + "detail")
            addToBackStack(null)
            commit()
        }
    }

    override fun followUpdated() {
        homeFragment.presenter.clear()

        if (supportFragmentManager.findFragmentByTag(
                profileFragment.javaClass.simpleName) != null) {
            profileFragment.presenter.clear()
        }
    }

    //funcao logout
    override fun logout() {

        if (supportFragmentManager.findFragmentByTag(
                profileFragment.javaClass.simpleName) != null) {
            profileFragment.presenter.clear()
        }

        homeFragment.presenter.clear()
        homeFragment.presenter.logout()


        val intent = Intent(baseContext, SplashActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out) // para não pular tanto activity no momento transição

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        var scrollToolbarEnabled = false

         //v1
        //trocar o fragment baseado no id
        when(item.itemId) {
            R.id.menu_bottom_home -> {
                if (currentFragment == homeFragment) return false // para não chamar o mesmo activity varias vezes
                currentFragment = homeFragment
            }
            R.id.menu_bottom_search -> {
                if (currentFragment == searchFragment) return false
                currentFragment = searchFragment
                scrollToolbarEnabled = false // o scroll não será ativa apenas neste fragment
            }
            R.id.menu_bottom_add -> {
                if (currentFragment == addFragment) return false
                currentFragment = addFragment
                scrollToolbarEnabled = false // o scroll não será ativa apenas neste fragment
            }
            R.id.menu_bottom_profile -> {
                if (currentFragment == profileFragment) return false
                currentFragment = profileFragment
               scrollToolbarEnabled = true // o scroll será ativa apenas neste fragment
            }
        }

        setScrollToolbarEnabled(scrollToolbarEnabled)

        currentFragment?.let {
            //tirar o fragmento em branco quando clica em voltar
            replaceFragment(R.id.main_fragment, it)
        }
        return true
    }


    private fun setScrollToolbarEnabled(enabled: Boolean) {
        val params = binding.mainToolbar.layoutParams as AppBarLayout.LayoutParams
        var coordinatorParams = binding.mainAppbar.layoutParams as CoordinatorLayout.LayoutParams

        //ira ativar a rolagem
        if (enabled) {
            params.scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
            coordinatorParams.behavior = AppBarLayout.Behavior() // irá o comportamento do appbar
        } else {
            params.scrollFlags = 0
            coordinatorParams.behavior = null
        }
        binding.mainAppbar.layoutParams = coordinatorParams
    }


    override fun onPostCreated() {
        //irá limpar o cache
       homeFragment.presenter.clear()

        if (supportFragmentManager.findFragmentByTag(
            profileFragment.javaClass.simpleName) != null) {
            profileFragment.presenter.clear()
        }

        //ira chamar a tela principal
       binding.mainBottomNav.selectedItemId = R.id.menu_bottom_home
    }

}