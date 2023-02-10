package co.tiagoaguiar.course.instagram.common.model

data class Post(
    val uuid: String? = null,
    val photoUrl: String? = null,
    val caption: String? = null, // texto do post
    val timestamp: Long = 0, // hora dos post
    val publisher: User? = null // irá buscar o usuer que realizou o post na home
)
