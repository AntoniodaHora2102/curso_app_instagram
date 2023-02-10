package co.tiagoaguiar.course.instagram.post.view

import android.net.Uri
import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import co.tiagoaguiar.course.instagram.R

class PictureAdapter(private val onClick: (Uri) -> Unit) : RecyclerView.Adapter<PictureAdapter.PostViewHolder>() {

    var items: List<Uri> = mutableListOf()

        //layout que será criado
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
            return PostViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_profile_grid , parent, false)
            )
        }

        // irá devolver a posição toda vez que realizamos a rolagem na tela - lista dinâmica
        override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
            holder.bind(items[position])
        }

        //quantidade de itens na tela
        override fun getItemCount(): Int {
            return items.size
        }

    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            //metodo BIND

      fun bind(image: Uri) {
       //deixar a foto menor
       val bitmap = itemView.context.contentResolver.loadThumbnail(image, Size(200,200), null)

         itemView.findViewById<ImageView>(R.id.item_profile_img_grid).setImageBitmap(bitmap)
           itemView.setOnClickListener {
                onClick.invoke(image) // será disparada toda vez que for clicada na celula
             }
          }
       }
}