package com.everfrost.remak_compose.Di

import com.everfrost.remak_compose.dataSource.RemoteDataSource
import com.everfrost.remak_compose.repository.AccountRepository
import com.everfrost.remak_compose.repository.AccountRepositoryImpl
import com.everfrost.remak_compose.repository.DocumentRepository
import com.everfrost.remak_compose.repository.DocumentRepositoryImpl
import com.everfrost.remak_compose.repository.MainRepository
import com.everfrost.remak_compose.repository.MainRepositoryImpl
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

}