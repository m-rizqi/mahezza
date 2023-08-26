package com.mahezza.mahezza.di

import android.content.Context
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.mahezza.mahezza.R
import com.mahezza.mahezza.data.source.firebase.auth.FirebaseAuthentication
import com.mahezza.mahezza.data.source.firebase.auth.MainFirebaseAuthentication
import com.mahezza.mahezza.data.source.firebase.firestore.ChildrenFirebaseFirestore
import com.mahezza.mahezza.data.source.firebase.firestore.MainChildrenFirebaseFirestore
import com.mahezza.mahezza.data.source.firebase.firestore.MainPuzzleFirebaseFirestore
import com.mahezza.mahezza.data.source.firebase.firestore.MainUserFirebaseFirestore
import com.mahezza.mahezza.data.source.firebase.firestore.PuzzleFirebaseFirestore
import com.mahezza.mahezza.data.source.firebase.firestore.UserFirebaseFirestore
import com.mahezza.mahezza.data.source.firebase.storage.FirebaseStorage
import com.mahezza.mahezza.data.source.firebase.storage.MainFirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideRemoteConfig() : FirebaseRemoteConfig {
        val remoteConfig : FirebaseRemoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        return remoteConfig
    }

    @Singleton
    @Provides
    @FirebaseWebClientId
    fun provideFirebaseWebClientId(
        @ApplicationContext context: Context
    ) : String = context.getString(R.string.default_web_client_id)

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class FirebaseWebClientId

    @Singleton
    @Provides
    fun provideFirebaseAuthentication(
        @ApplicationContext context: Context,
        @FirebaseWebClientId firebaseWebClientId : String,
        @IODispatcher distpacher : CoroutineDispatcher
    ) : FirebaseAuthentication {
        return MainFirebaseAuthentication(firebaseWebClientId, context, distpacher)
    }

    @Singleton
    @Provides
    fun provideUserFirebaseFirestore(
        @IODispatcher dispatcher: CoroutineDispatcher
    ) : UserFirebaseFirestore = MainUserFirebaseFirestore(dispatcher)

    @Singleton
    @Provides
    fun provideChildrenFirebaseFirestore(
        @IODispatcher dispatcher: CoroutineDispatcher
    ) : ChildrenFirebaseFirestore = MainChildrenFirebaseFirestore(dispatcher)

    @Singleton
    @Provides
    fun providePuzzleFirebaseFirestore(
        @IODispatcher dispatcher: CoroutineDispatcher
    ) : PuzzleFirebaseFirestore = MainPuzzleFirebaseFirestore(dispatcher)

    @Singleton
    @Provides
    fun provideFirebaseStorage(
        @IODispatcher dispatcher: CoroutineDispatcher
    ) : FirebaseStorage = MainFirebaseStorage(dispatcher)
}