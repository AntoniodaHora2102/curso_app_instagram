package co.tiagoaguiar.course.instagram.home.data

import co.tiagoaguiar.course.instagram.common.base.Cache
import co.tiagoaguiar.course.instagram.common.model.Post
import co.tiagoaguiar.course.instagram.common.model.UserAuth

//classe generica
class HomeDataSourceFactory(
    private val feedCache: Cache<List<Post>>
) {

    //funcao localdatasource
    fun createLocalDataSource() : HomeDataSource {
        return HomeLocalDataSource(feedCache)
    }


    fun createFromFeed(): HomeDataSource {
        if (feedCache.isCached()) {
            return HomeLocalDataSource(feedCache)
        }
        return FireHomeDataSource()
    }
}