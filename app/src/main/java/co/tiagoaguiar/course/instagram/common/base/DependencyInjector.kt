package co.tiagoaguiar.course.instagram.common.base

import android.content.Context
import co.tiagoaguiar.course.instagram.add.data.AddFakeDataRemoteSource
import co.tiagoaguiar.course.instagram.add.data.AddLocalDataSource
import co.tiagoaguiar.course.instagram.add.data.AddRepository
import co.tiagoaguiar.course.instagram.add.data.FireAddDataSource
import co.tiagoaguiar.course.instagram.common.model.Post
import co.tiagoaguiar.course.instagram.home.data.FeedMemoryCache
import co.tiagoaguiar.course.instagram.home.data.HomeDataSourceFactory
import co.tiagoaguiar.course.instagram.home.data.HomeRepository
import co.tiagoaguiar.course.instagram.login.data.FakeDataSource
import co.tiagoaguiar.course.instagram.login.data.FireLoginDataSource
import co.tiagoaguiar.course.instagram.login.data.LoginRepository
import co.tiagoaguiar.course.instagram.post.data.PostDataSource
import co.tiagoaguiar.course.instagram.post.data.PostLocalDataSource
import co.tiagoaguiar.course.instagram.post.data.PostRepository
import co.tiagoaguiar.course.instagram.profile.data.*
import co.tiagoaguiar.course.instagram.register.data.FakeRegisterDataSource
import co.tiagoaguiar.course.instagram.register.data.FireRegisterDataSource
import co.tiagoaguiar.course.instagram.register.data.RegisterRepository
import co.tiagoaguiar.course.instagram.search.data.FireSearchDataSource
import co.tiagoaguiar.course.instagram.search.data.SearchDataSource
import co.tiagoaguiar.course.instagram.search.data.SearchFakeRemoteDataSource
import co.tiagoaguiar.course.instagram.search.data.SearchRepository
import co.tiagoaguiar.course.instagram.splash.Splash
import co.tiagoaguiar.course.instagram.splash.dados.FakeLocalDataSource
import co.tiagoaguiar.course.instagram.splash.dados.FireSplashDataSource
import co.tiagoaguiar.course.instagram.splash.dados.SplashRepository

//injector manual
object DependencyInjector {

    //tela splash
    fun splashRepository(): SplashRepository {
        return SplashRepository(FireSplashDataSource())
    }

    //iremos injetar a dependencia do LOGIN REPOSITORY
    fun loginRepository() : LoginRepository {
        return  LoginRepository(FireLoginDataSource())
    }

    //cadastro de email
    fun registerEmailRepository() : RegisterRepository {
        return RegisterRepository(FireRegisterDataSource())
    }

    // pesquisa de user
    fun searchRepository() : SearchRepository {
        return SearchRepository(FireSearchDataSource())
    }

    //profile
    fun profileRepository() : ProfileRepository {
        return  ProfileRepository(ProfileDataSourceFactory(ProfileMemoryCache, PostListMemoryCache))
    }

    //home - feed
    fun homeRepository() : HomeRepository {
        return  HomeRepository(HomeDataSourceFactory(FeedMemoryCache))
    }

    //posts - followers
    fun addRepository() : AddRepository {
        return AddRepository(FireAddDataSource(), AddLocalDataSource())
    }

    fun postRepository(context: Context): PostRepository {
        return  PostRepository(PostLocalDataSource(context))
    }
}