package com.example.ownchatapp.di

import android.content.Context
import com.example.ownchatapp.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.logger.ChatLogLevel
import io.getstream.chat.android.models.UploadAttachmentsNetworkType
import io.getstream.chat.android.offline.plugin.factory.StreamOfflinePluginFactory
import io.getstream.chat.android.state.plugin.config.StatePluginConfig
import io.getstream.chat.android.state.plugin.factory.StreamStatePluginFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun providesStatePluginFactory(@ApplicationContext context: Context) = StreamStatePluginFactory(
        appContext = context,
        config = StatePluginConfig(
            backgroundSyncEnabled = true,
            userPresence = true
        )
    )

    @Provides
    fun provideOfflinePluginFactory(@ApplicationContext context: Context) =
        StreamOfflinePluginFactory(appContext = context)

    @Singleton
    @Provides
    fun provideChatClient(
        @ApplicationContext context: Context,
        offlinePluginFactory: StreamOfflinePluginFactory,
        statePluginFactory: StreamStatePluginFactory
    ) = ChatClient.Builder(
        context.getString(R.string.api_key), context
    ).uploadAttachmentsNetworkType(UploadAttachmentsNetworkType.NOT_ROAMING)
        .withPlugins(offlinePluginFactory, statePluginFactory).logLevel(ChatLogLevel.ALL).build()
}