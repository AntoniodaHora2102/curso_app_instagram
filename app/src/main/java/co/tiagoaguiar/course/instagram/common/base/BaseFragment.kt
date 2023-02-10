package co.tiagoaguiar.course.instagram.common.base

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.annotation.LayoutRes
import androidx.annotation.MenuRes
import androidx.fragment.app.Fragment

abstract class BaseFragment<T, P: BasePresenter>(
    @LayoutRes layoutId: Int,
    val bind: (View) -> T // funcao para chamar o BINDING
) : Fragment(layoutId) {

    //binding generico
    protected var binding: T? = null

    //presenter generico
    abstract var presenter: P

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//      val rv = view.findViewById<RecyclerView>(R.id.profile_rv)
        binding = bind(view)

        if (savedInstanceState == null) {
            setupViews()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getMenu()?.let {
           //este fragmento ficara responsável pelo opçao do menu
           setHasOptionsMenu(true)
        }

        setupPresenter()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        getMenu()?.let {
         menu.clear() // irá limpar o menu antes de inflar
         inflater.inflate(it, menu)
        }
       super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onDestroy() {
        binding = null
        presenter.onDestroy()
        super.onDestroy()
    }

    //aqui será declaro as coisas dinamicas
    abstract fun setupViews()
    abstract fun setupPresenter()

    @MenuRes
    open fun getMenu(): Int? {
        return null
    }
}