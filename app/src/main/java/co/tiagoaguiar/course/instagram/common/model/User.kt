package co.tiagoaguiar.course.instagram.common.model

data class User(
   //propriedades do usuer
    val uuid: String? = null, // identificado único do user
    val name: String? = null,
    val email: String? = null,
    val photoUrl: String? = null, // passaremos null , pois não será obrigatório
    val postCount: Int = 0,
    val following: Int = 0,
    val followers: Int = 0
)
