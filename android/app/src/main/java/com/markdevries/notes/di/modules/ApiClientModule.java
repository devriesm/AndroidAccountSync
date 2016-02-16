package com.markdevries.notes.di.modules;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.markdevries.notes.NotesApplication;
import com.markdevries.notes.api.NotesApiInterface;

import java.lang.reflect.Modifier;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mark on 2/15/16.
 */
@Module
public class ApiClientModule {

    private NotesApplication mApplication;
    private String mBaseUrl;

    // Constructor needs one parameter to instantiate.
    public ApiClientModule(NotesApplication app, String baseUrl) {
        this.mBaseUrl = baseUrl;
    }

    @Provides
    @Singleton
    Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC);
        return gsonBuilder.create();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {
        OkHttpClient client = new OkHttpClient();
        return client;
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(mBaseUrl)
                .client(okHttpClient)
                .build();
        return retrofit;
    }

    @Provides
    @Singleton
    NotesApiInterface provideApiInterface(Retrofit retrofit) {
        return retrofit.create(NotesApiInterface.class);
    }
}
