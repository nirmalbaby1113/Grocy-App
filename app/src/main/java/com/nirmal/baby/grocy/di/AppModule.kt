package com.nirmal.baby.grocy.di

import com.nirmal.baby.grocy.data.repository.GroceryRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideGroceryRepository(): GroceryRepository = GroceryRepository()
}
