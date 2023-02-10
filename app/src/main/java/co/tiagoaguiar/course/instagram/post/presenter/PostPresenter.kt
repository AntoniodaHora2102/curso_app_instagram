package co.tiagoaguiar.course.instagram.post.presenter

import android.net.Uri
import co.tiagoaguiar.course.instagram.post.Post
import co.tiagoaguiar.course.instagram.post.data.PostRepository
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class PostPresenter(
    private var view: Post.View?,
    private val repository: PostRepository
) : Post.Presenter, CoroutineScope {
    private var uri: Uri? = null

    // será executa de forma paralela a corotina
    private val job = Job()

    // não irá funciona na activity principal. Mas sim paralela a ela
    override val coroutineContext: CoroutineContext = job + Dispatchers.IO

    override fun fetchPicture() {
        // Aqui acontece a chamada na Thread MAIN (UI)
        view?.showProgress(true)

        launch {
            //Aqui acontece a chamada paralela(corotina IO)
            val pictures = repository.fetchPictures()

            //Executa dentro da MainThread
            withContext(Dispatchers.Main) {
                if (pictures.isEmpty()) {
                    view?.displayEmptyPictures()
                } else {
                    view?.displayFullPictures(pictures)
                }
                view?.showProgress(false)
            }
        }
    }

    override fun selectUri(uri: Uri) {
        this.uri = uri
    }

    override fun getSelectedUri(): Uri? {
      return uri
    }

    override fun onDestroy() {
        job.cancel() // destroi a tarefa paralela
       view = null
    }

}