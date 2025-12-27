package com.example.scrapuncle.auth.di

import com.example.scrapuncle.auth.repo.ProfileRepository
import com.example.scrapuncle.auth.repo.ScheduleRepository
import com.example.scrapuncle.auth.repositoryImpl.ProfileRepositoryImpl
import com.example.scrapuncle.auth.repositoryImpl.ScheduleRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton




@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindScheduleRepo(
        impl: ScheduleRepositoryImpl
    ): ScheduleRepository


    @Binds
    @Singleton
    abstract fun bindProfileRepository(
        impl: ProfileRepositoryImpl
    ): ProfileRepository
}