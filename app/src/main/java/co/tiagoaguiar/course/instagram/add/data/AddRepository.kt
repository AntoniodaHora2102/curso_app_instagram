package co.tiagoaguiar.course.instagram.add.data

import android.net.Uri
import co.tiagoaguiar.course.instagram.common.base.RequestCallback

class AddRepository(
    private val remoteSource: FireAddDataSource,
    private val localDataSource: AddLocalDataSource
) {
    fun createPost(uri: Uri, caption: String, callback: RequestCallback<Boolean>) {
        val uuid = localDataSource.fetchSession()

        remoteSource.createPost(uuid, uri, caption, object : RequestCallback<Boolean> {
            override fun onSucess(data: Boolean) {

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