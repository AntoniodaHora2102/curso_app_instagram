package co.tiagoaguiar.course.instagram.post.data

import android.net.Uri

interface PostDataSource {

    //ira funciona de forma paralela (corotinas)
    suspend fun fetchPictures() : List<Uri>
}