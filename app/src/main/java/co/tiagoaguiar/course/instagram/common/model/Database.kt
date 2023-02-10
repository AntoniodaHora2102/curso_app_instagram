package co.tiagoaguiar.course.instagram.common.model

import android.net.Uri
import java.io.File
import java.util.*

object Database {

    //hashSetOf permite adiciona apenas um único identificador
    //um usuario por vez
    val usersAuth = mutableListOf<UserAuth>()

    //data class Photo excluida
    // pois, não será mais usado desta forma e sim por URI
    //uma única foto
    // val photos = hashSetOf<Photo>() -

    //lista de posts
    val posts = hashMapOf<String, MutableSet<Post>>()

    //feed do user
    val feeds = hashMapOf<String, MutableSet<Post>>()

    //seguidores
    val followers = hashMapOf<String, MutableSet<String>>()

    //armazena a sessão do usuário
    var sessionAuth: UserAuth? = null



    init {

        //base de dados falsas
        val userA = UserAuth(UUID.randomUUID().toString(),"UserA",
                     "userA@gmail.com", "12345678", null)

//        val userB = UserAuth(UUID.randomUUID().toString(),"UserB","userB@gmail.com", "87654321",
//            Uri.fromFile(File("/storage/emulated/0/Android/media/co.tiagoaguiar.course.instagram/Instagram/2023-01-31-16-13-02-463.jpg")))
//
//        //tabela de users
          usersAuth.add(userA)
//        usersAuth.add(userB)
//
//        // tabela de posts
//        followers[userA.uuid] = hashSetOf()
//        posts[userA.uuid] = hashSetOf()
//        feeds[userA.uuid] = hashSetOf()
//
//        followers[userB.uuid] = hashSetOf()
//        posts[userB.uuid] = hashSetOf()
//        feeds[userB.uuid] = hashSetOf()

//        feeds[userA.uuid]?.addAll(
//            arrayListOf(
//                Post(UUID.randomUUID().toString(),
//                Uri.fromFile(File("/storage/emulated/0/Android/media/co.tiagoaguiar.course.instagram/Instagram/2023-02-02-14-15-18-737.jpg")),
//                "desc", System.currentTimeMillis(), userA ),
//
//                 Post(UUID.randomUUID().toString(),
//                 Uri.fromFile(File("/storage/emulated/0/Android/media/co.tiagoaguiar.course.instagram/Instagram/2023-02-02-14-15-18-737.jpg")),
//                "desc", System.currentTimeMillis(), userA ),
//
//                Post(UUID.randomUUID().toString(),
//                 Uri.fromFile(File("/storage/emulated/0/Android/media/co.tiagoaguiar.course.instagram/Instagram/2023-02-02-14-15-18-737.jpg")),
//                "desc", System.currentTimeMillis(), userA ),
//
//                Post(UUID.randomUUID().toString(),
//                Uri.fromFile(File("/storage/emulated/0/Android/media/co.tiagoaguiar.course.instagram/Instagram/2023-02-02-14-15-18-737.jpg")),
//                "desc", System.currentTimeMillis(), userA )
//            )
//        )
//
//
//        feeds[userA.uuid]?.toList()?.let {
//            feeds[userB.uuid]?.addAll(it)
//        }

        for (i in 0..30) {
            val user = UserAuth(UUID.randomUUID().toString(),
                "User$i", "user$i@gmail.com", "12345678", null)
            usersAuth.add(user)
        }

   //     sessionAuth = usersAuth.first() //app irá iniciar com o primeiro usuer já logado!

        //seguidor
//        followers[sessionAuth!!.uuid]?.add(usersAuth[2].uuid)
//        followers[sessionAuth!!.uuid]?.add(usersAuth[3].uuid)
    }
}