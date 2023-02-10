package co.tiagoaguiar.course.instagram.profile.view

import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import co.tiagoaguiar.course.instagram.R
import co.tiagoaguiar.course.instagram.common.base.BaseFragment
import co.tiagoaguiar.course.instagram.common.base.DependencyInjector
import co.tiagoaguiar.course.instagram.common.model.Post
import co.tiagoaguiar.course.instagram.common.model.User
import co.tiagoaguiar.course.instagram.common.model.UserAuth
import co.tiagoaguiar.course.instagram.databinding.FragmentProfileBinding
import co.tiagoaguiar.course.instagram.profile.Profile
import co.tiagoaguiar.course.instagram.profile.presenter.ProfilePresenter
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProfileFragment : BaseFragment<FragmentProfileBinding, Profile.Presenter>
    (R.layout.fragment_profile, FragmentProfileBinding::bind), Profile.View,
    BottomNavigationView.OnNavigationItemSelectedListener {

    override lateinit var presenter : Profile.Presenter

    private val adapter = PostAdapter()
    private var uuid: String? = null

    //v3
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        presenter.subscribe(
//            if (savedInstanceState != null) {
//                ProfileState(
//                    (savedInstanceState.getParcelableArray("posts") as Array<Post>).toList(),
//                    savedInstanceState.getParcelable("user")
//                )
//            } else {
//                null
//            }
//        )
//    }


    //ao usarmos a class Base Fragment não necessitamos maos do onCreateView
//    override fun onCreateView( inflater: LayoutInflater, container:
//    ViewGroup?, savedInstanceState: Bundle?): View? {
//        return inflater.inflate(R.layout.fragment_profile, container, false)
//    }

    /* Metodo transferido para a Class BASEFRAGNENT
    * Class Generica
    */
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
////        val rv = view.findViewById<RecyclerView>(R.id.profile_rv)
//        binding = FragmentProfileBinding.bind(view)
//
//        //irá inflar o layout GRID com 3 colunas
////        rv.layoutManager = GridLayoutManager(requireContext(), 3) // quantidade de grids que iremos trabalhar
////        rv.adapter = PostAdapter()
//        binding?.profileRv?.layoutManager = GridLayoutManager(requireContext(), 3) // quantidade de grids que iremos trabalhar
//        binding?.profileRv?.adapter = PostAdapter()
//
//    }

    override fun setupViews() {
        uuid = arguments?.getString(KEY_USER_ID)

        binding?.profileRv?.layoutManager = GridLayoutManager(requireContext(), 3) // quantidade de grids que iremos trabalhar
        binding?.profileRv?.adapter = adapter
        binding?.profileNavTabs?.setOnNavigationItemSelectedListener(this)

        //evento de btn para seguir / seguindo
        binding?.profileBtnEditProfile?.setOnClickListener {
          //comecar a seguir
            if (it.tag == true) {
                binding?.profileBtnEditProfile?.text = getString(R.string.follow)
                binding?.profileBtnEditProfile?.tag = false
                presenter.followUser(uuid, false)
            }

            //deixara de seguir
            else if (it.tag == false ) {
                binding?.profileBtnEditProfile?.text = getString(R.string.unfollow)
                binding?.profileBtnEditProfile?.tag = true
                presenter.followUser(uuid, true)
            }
        }

        presenter.fetchUserProfile(uuid)

    }


    override fun setupPresenter() {
        val repository = DependencyInjector.profileRepository()
        presenter = ProfilePresenter(this, repository)
    }

//    override fun onViewStateRestored(savedInstanceState: Bundle?) {
//        if (savedInstanceState != null) {
//            val state = savedInstanceState.getParcelable<UserAuth>("myState")
//            state?.let {
//                displayUserProfile(it)
//            }
//        }
//        super.onViewStateRestored(savedInstanceState)
//    }

//    override fun onSaveInstanceState(outState: Bundle) {
//        outState.putParcelable("user", presenter.getState().fetchUserProfile())
//        outState.putParcelableArray("posts", presenter.getState().fetchUserPosts()?.toTypedArray())
//        super.onSaveInstanceState(outState)
//    }

    override fun showProgress(enabled: Boolean) {
        binding?.profileProgress?.visibility = if (enabled) View.VISIBLE else View.GONE
    }

    override fun displayUserProfile(user: Pair<User, Boolean?>) {

        //depois de adicionar o PAIR
        //devemos destruir objeto
        val (userAuth, following) = user

      binding?.profileTxtPostsCount?.text = userAuth.postCount.toString()
      binding?.profileTxtFollowingCount?.text = userAuth.following.toString()
      binding?.profileTxtFollowersCount?.text = userAuth.followers.toString()
      binding?.profileTxtUsername?.text = userAuth.name
      binding?.profileTxtBio?.text = "TODO"

      binding?.let {
          Glide.with(requireContext()).load(userAuth.photoUrl).into(it.profileImgIcon)
      }
      //binding?.profileImgIcon?.setImageURI(userAuth.photoUrl)


        //aqui irá alterar o texto do BUTTON do PERFIL
      binding?.profileBtnEditProfile?.text = when(following) {
          null -> getString(R.string.edit_profile) // BTN EDITAR do PERFIL USER
          true -> getString(R.string.unfollow) // BTN de SEGUINDO
          false -> getString(R.string.follow) // BTN SEGUIR
      }

        //ira mudar as informações do BUTTON seguindo a lógica quando for clicado
      binding?.profileBtnEditProfile?.tag = following

      presenter.fetchUserPosts(uuid)
    }

    override fun displayRequestFailure(message: String) {
     Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    override fun displayEmptyPosts() {
       binding?.profileTxtEmpty?.visibility = View.VISIBLE
       binding?.profileRv?.visibility = View.GONE
    }

    override fun displayFullPosts(posts: List<Post>) {
        binding?.profileTxtEmpty?.visibility = View.GONE
        binding?.profileRv?.visibility = View.VISIBLE

        adapter.items = posts
        adapter.notifyDataSetChanged()
    }

    //    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        //este fragmento ficara responsável pelo opçao do menu
//        setHasOptionsMenu(true)
//    }

//    override fun onDestroy() {
//        binding = null
//        //presenter.onDestroy
//        super.onDestroy()
//    }


    //iremos inflar a opção Menu do Fragmento
//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater.inflate(R.menu.menu_profile, menu)
//        super.onCreateOptionsMenu(menu, inflater)
//    }


    //iremos inflar a opção Menu do Fragmento - generica BaseFragment
    override fun getMenu(): Int {
        return R.menu.menu_profile
    }

    //grid do perfil de user
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            //quando clicar no Icone de Grid Perfil
             R.id.menu_profile_grid -> {
               binding?.profileRv?.layoutManager = GridLayoutManager(requireContext(), 3)
             }

           //quando clicar no Icone de Lista no Perfil
             R.id.menu_profile_list -> {
               binding?.profileRv?.layoutManager = LinearLayoutManager(requireContext())
             }
          }
        return true
    }

    companion object {
        const val KEY_USER_ID = "key_user_id"
    }



//    //ADAPTER do layout GRID - class PosAdapter
//    private class PostAdapter : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {
//
//        //layout que será criado
//        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
//         return PostViewHolder(
//             LayoutInflater.from(parent.context).inflate(R.layout.item_profile_grid , parent, false)
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
//                itemView.findViewById<ImageView>(R.id.item_profile_img_grid).setImageResource(image)
//            }
//        }
//
//    }

}