package com.everfrost.remak.Di

import com.everfrost.remak.dataSource.RemoteDataSource
import com.everfrost.remak.dataSource.RemoteDataSourceImpl
import com.everfrost.remak.service.ApiService
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