package co.tiagoaguiar.course.instagram.common.model

import android.net.Uri

data class UserAuth(
   //propriedades do usuer
    val uuid: String, // identificado único do user
    val name: String,
    val email: String,
    val password: String,
    val photoUri: Uri?, // passaremos null , pois não será obrigatório
    val postCount: Int = 0,
    val followingCount: Int = 0,
    val followersCount: Int = 0
)
