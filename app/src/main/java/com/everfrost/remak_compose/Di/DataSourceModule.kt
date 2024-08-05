package com.everfrost.remak_compose.Di

import com.everfrost.remak_compose.dataSource.RemoteDataSource
import com.everfrost.remak_compose.dataSource.RemoteDataSourceImpl
import com.everfrost.remak_compose.service.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {
    @Provides
    @Singleton
    fun provideRemoteDataSource(apiService: ApiService): RemoteDataSource =
        RemoteDataSourceImpl(apiService)
}