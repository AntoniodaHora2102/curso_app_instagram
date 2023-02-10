package co.tiagoaguiar.course.instagram.search.view

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.tiagoaguiar.course.instagram.R
import co.tiagoaguiar.course.instagram.common.base.BaseFragment
import co.tiagoaguiar.course.instagram.common.base.DependencyInjector
import co.tiagoaguiar.course.instagram.common.model.User
import co.tiagoaguiar.course.instagram.common.model.UserAuth
import co.tiagoaguiar.course.instagram.databinding.FragmenteSearchBinding
import co.tiagoaguiar.course.instagram.search.Search
import co.tiagoaguiar.course.instagram.search.presenter.SearchPresenter
import java.util.*

class SearchFragment : BaseFragment<FragmenteSearchBinding, Search.Presenter>(
    R.layout.fragmente_search,
    FragmenteSearchBinding::bind
), Search.View {

    override lateinit var presenter: Search.Presenter

    //iria iniciar o adapter de forma atrasada
    private val adapter by lazy {  SearchAdapter(onItemClicked) }

    private var searchListener: SearchListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is SearchListener) {
            searchListener = context
        }
    }

    override fun setupViews() {
        //irá inflar o layout GRID com 3 colunas
        binding?.searchRv?.layoutManager = LinearLayoutManager(requireContext()) // layout de post
        binding?.searchRv?.adapter = adapter

    }

    override fun setupPresenter() {
          val repository = DependencyInjector.searchRepository()
          presenter = SearchPresenter(this, repository)
    }

    //será disparado a partir do momento que o itemClick (clicado) SearchAdapter
    private val onItemClicked: (String) -> Unit = { uuid ->
        searchListener?.goToProfile(uuid)
    }

    override fun getMenu() = R.menu.menu_search

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        //pesquisavel
        val searcherManager = requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager

        val searcherView = (menu.findItem(R.id.menu_search).actionView as SearchView)

        searcherView.apply {
         setSearchableInfo(searcherManager.getSearchableInfo(requireActivity().componentName))
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                  return false
                }

                // será responsável por digitar e buscar o resultado
                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText?.isNotEmpty() == true) {
                        presenter.fecthUsers(newText)
                        return true
                    }
                    return false
                }
            })
        }
    }

    override fun showProgress(enabled: Boolean) {
        binding?.searchProgress?.visibility = if (enabled) View.VISIBLE else View.GONE
    }

    override fun displayFullUsers(users: List<User>) {
        binding?.searchTxtEmpty?.visibility = View.GONE
        binding?.searchRv?.visibility = View.VISIBLE
        adapter.items = users
        adapter.notifyDataSetChanged()
    }

    override fun displayEmptyUsers() {
        binding?.searchTxtEmpty?.visibility = View.VISIBLE
        binding?.searchRv?.visibility = View.GONE
    }


    interface SearchListener {
        fun goToProfile(uuid: String)
    }

    //    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        return inflater.inflate(R.layout.fragmente_search, container, false)
//    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        val rv = view.findViewById<RecyclerView>(R.id.search_rv)
//
//        //irá inflar o layout GRID com 3 colunas
//        rv.layoutManager = LinearLayoutManager(requireContext()) // layout de post
//        rv.adapter = PostAdapter()
//
//    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        //este fragmento ficara responsável pelo opçao do menu
//        setHasOptionsMenu(true)
//    }

    //iremos inflar a opção Menu do Fragmento
//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater.inflate(R.menu.menu_profile, menu)
//        super.onCreateOptionsMenu(menu, inflater)
//    }


    //ADAPTER do layout GRID -
    //copiado para o SearchAdapter
//    private class PostAdapter : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {
//
//        //layout que será criado
//        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
//         return PostViewHolder(
//             LayoutInflater.from(parent.context).inflate(R.layout.item_user_list, parent, false)
//         )
//        }
//
//        // irá devolver a posição toda vez que realizamos a rolagem na tela - lista dinâmica
//        override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
//            holder.bind(R.drawable.ic_insta_add)
//        }
//
//        //quantidade de itens na tela
//        override fun getItemCount(): Int {
//           return 30
//        }
//
//        private  class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//            //metodo BIND
//            fun bind(image: Int) {
//                itemView.findViewById<ImageView>(R.id.search_img_user).setImageResource(image)
//            }
//        }
//
//    }

}