package co.tiagoaguiar.course.instagram.post.data

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import java.net.URI

class PostLocalDataSource(private val context: Context) : PostDataSource {

    //está sendo chamado em background(rotina paralela - corotinas)
    override suspend fun fetchPictures(): List<Uri> {

        //verifica api do SDK-BUILD
        val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        // dados devolvidos da midia
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.WIDTH,
            MediaStore.Images.Media.HEIGHT,
        )

        // lista de foto
        val photos = mutableListOf<Uri>()

        context.contentResolver.query(
            collection, // coleção de dados
            projection, //projecao dos dados que será retornado
            null,
            null,
            "${MediaStore.Images.Media._ID} DESC" //organiza em ordem descrescente
        )?.use { cursor ->
            //vai buscar o id da imagem especifica
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)

             // irá fazer uma varredura para proxima interação
             while (cursor.moveToNext()) {
                    val id = cursor.getLong(idColumn)

                    //caminho da uri
                    val uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                    photos.add(uri)
                    if (photos.size == 99) //limita a quantidade de fotos que serão exibidas
                        break
                    }
                }

            return photos // retorna a lista de fotos

        }

}