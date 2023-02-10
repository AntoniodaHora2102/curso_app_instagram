package co.tiagoaguiar.course.instagram.profile.data

import co.tiagoaguiar.course.instagram.common.base.Cache
import co.tiagoaguiar.course.instagram.common.model.Post
import co.tiagoaguiar.course.instagram.common.model.User
import co.tiagoaguiar.course.instagram.common.model.UserAuth

//classe generica
class ProfileDataSourceFactory(
    private val profileCache: Cache<Pair<User, Boolean?>>,
    private val postCache: Cache<List<Post>>
) {

    //funcao localdatasource
    fun createLocalDataSource() : ProfileDataSource {
        return ProfileLocalDataSource(profileCache, postCache)
    }

    fun createRemoteDataSource() : ProfileDataSource {
        return FireProfileDataSource()
    }


    fun createFromUser(uuid: String?): ProfileDataSource {
        if (uuid != null) return createRemoteDataSource()

        if (profileCache.isCached()) {
            return ProfileLocalDataSource(profileCache, postCache)
        }
        return createRemoteDataSource()
    }

    fun createFromPosts(uuid: String?): ProfileDataSource {
        if (uuid != null) return createRemoteDataSource()

        if (postCache.isCached()) {
            return ProfileLocalDataSource(profileCache, postCache)
        }
        return createRemoteDataSource()
    }
}