package co.tiagoaguiar.course.instagram.search.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import co.tiagoaguiar.course.instagram.R
import co.tiagoaguiar.course.instagram.common.model.User
import co.tiagoaguiar.course.instagram.common.model.UserAuth
import com.bumptech.glide.Glide

class SearchAdapter(
    private val itemClick: (String) -> Unit // funcao de setOnClickListener
) : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {



    var items: List<User> = mutableListOf()

    //layout que será criado
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_user_list, parent, false)
        )
    }

    // irá devolver a posição toda vez que realizamos a rolagem na tela - lista dinâmica
    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(items[position])
    }

    //quantidade de itens na list
    override fun getItemCount(): Int {
        return items.size
    }

     inner class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //metodo BIND
        fun bind(user: User) {

            Glide.with(itemView.context).load(user.photoUrl).into(itemView.findViewById(R.id.search_img_user))

            itemView.findViewById<TextView>(R.id.search_txt_username).text = user.name

            //evento de click para escolher o user
            itemView.setOnClickListener {
                if (user.uuid != null)
                itemClick.invoke(user.uuid) //invocara o user ao ser clicado
            }
        }
    }

}