package co.tiagoaguiar.course.instagram.home.view

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.tiagoaguiar.course.instagram.R
import co.tiagoaguiar.course.instagram.common.base.BaseFragment
import co.tiagoaguiar.course.instagram.common.base.DependencyInjector
import co.tiagoaguiar.course.instagram.common.model.Post
import co.tiagoaguiar.course.instagram.databinding.FragmentHomeBinding
import co.tiagoaguiar.course.instagram.home.Home
import co.tiagoaguiar.course.instagram.home.presenter.HomePresenter
import co.tiagoaguiar.course.instagram.main.LogoutListener

class HomeFragment : BaseFragment<FragmentHomeBinding, Home.Presenter>(
    R.layout.fragment_home , FragmentHomeBinding::bind), Home.View {

    override lateinit var presenter: Home.Presenter
    private var adapter = FeedAdapter()

    private var logoutListener: LogoutListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is LogoutListener) {
            logoutListener = context
        }
    }

    override fun setupViews() {
        //irá inflar o layout GRID com 3 colunas
        binding?.homeRv?.layoutManager = LinearLayoutManager(requireContext()) // layout de post
        binding?.homeRv?.adapter = adapter

        presenter.fetchFeed()
    }

    override fun setupPresenter() {
        presenter = HomePresenter(this, DependencyInjector.homeRepository())
       }


    //iremos inflar a opção Menu do Fragmento
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_profile, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun getMenu() = R.menu.menu_profile

    override fun showProgress(enabled: Boolean) {
        binding?.homeProgress?.visibility = if (enabled) View.VISIBLE else View.GONE
    }

    override fun displayRequestFailure(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    override fun displayEmptyPosts() {
        binding?.homeTxtEmpty?.visibility = View.VISIBLE
        binding?.homeRv?.visibility = View.GONE
    }

    override fun displayFullPosts(posts: List<Post>) {
        binding?.homeTxtEmpty?.visibility = View.GONE
        binding?.homeRv?.visibility = View.VISIBLE

        adapter.items = posts
        adapter.notifyDataSetChanged()
    }

    //metodo de logout
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_logout -> {
                logoutListener?.logout()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}