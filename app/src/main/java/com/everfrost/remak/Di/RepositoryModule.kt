package com.everfrost.remak.Di

import com.everfrost.remak.dataSource.RemoteDataSource
import com.everfrost.remak.repository.AccountRepository
import com.everfrost.remak.repository.AccountRepositoryImpl
import com.everfrost.remak.repository.CollectionRepository
import com.everfrost.remak.repository.CollectionRepositoryImpl
import com.everfrost.remak.repository.DocumentRepository
import com.everfrost.remak.repository.DocumentRepositoryImpl
import com.everfrost.remak.repository.MainRepository
import com.everfrost.remak.repository.MainRepositoryImpl
import com.everfrost.remak.repository.SearchRepository
import com.everfrost.remak.repository.SearchRepositoryImpl
import com.everfrost.remak.repository.TagRepository
import com.everfrost.remak.repository.TagRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideAccountRepository(remoteDataSource: RemoteDataSource): AccountRepository =
        AccountRepositoryImpl(remoteDataSource)

    @Provides
    @Singleton
    fun provideMainRepository(remoteDataSource: RemoteDataSource): MainRepository =
        MainRepositoryImpl(remoteDataSource)

    @Provides
    @Singleton
    fun documentRepository(remoteDataSource: RemoteDataSource): DocumentRepository =
        DocumentRepositoryImpl(remoteDataSource)

    @Provides
    @Singleton
    fun tagRepository(remoteDataSource: RemoteDataSource): TagRepository =
        TagRepositoryImpl(remoteDataSource)

    @Provides
    @Singleton
    fun collectionRepository(remoteDataSource: RemoteDataSource): CollectionRepository =
        CollectionRepositoryImpl(remoteDataSource)

    @Provides
    @Singleton
    fun searchRepository(remoteDataSource: RemoteDataSource): SearchRepository =
        SearchRepositoryImpl(remoteDataSource)

}