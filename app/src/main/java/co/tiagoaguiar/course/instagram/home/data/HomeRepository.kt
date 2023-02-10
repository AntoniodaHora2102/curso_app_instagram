package co.tiagoaguiar.course.instagram.home.data

import co.tiagoaguiar.course.instagram.common.base.RequestCallback
import co.tiagoaguiar.course.instagram.common.model.Post
import co.tiagoaguiar.course.instagram.common.model.UserAuth

class HomeRepository (private val dataSourceFactory: HomeDataSourceFactory) {

      // irá limpar o cache criados
      fun clearCache() {
        val localDataSource = dataSourceFactory.createLocalDataSource()
          localDataSource.putFeed(null)
      }

      fun fetchFeed(callback: RequestCallback<List<Post>>) {
        val localDataSource = dataSourceFactory.createLocalDataSource()

        val userId = localDataSource.fetchSession()
        val dataSource = dataSourceFactory.createFromFeed()

        dataSource.fetchFeed(userId, object : RequestCallback<List<Post>> {
            override fun onSucess(data: List<Post>) {
                localDataSource.putFeed(data)
                callback.onSucess(data)
            }

            override fun onFailure(message: String) {
               callback.onFailure(message)
            }

            override fun onComplete() {
                callback.onComplete()
            }
        })
    }
}