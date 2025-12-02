package com.peterj.motorwaysticker.core.di

import com.peterj.motorwaysticker.data.repository.HighwayRepositoryImplementation
import com.peterj.motorwaysticker.domain.repository.HighwayRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class HighwayModule {

    @Binds
    @Singleton
    abstract fun bindHighwayRepository(
        implementation: HighwayRepositoryImplementation
    ): HighwayRepository
}