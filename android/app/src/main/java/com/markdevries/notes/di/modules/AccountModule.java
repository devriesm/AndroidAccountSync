package com.markdevries.notes.di.modules;

import com.markdevries.notes.NotesApplication;
import com.markdevries.notes.account.AccountManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by mark on 2/15/16.
 */
@Module
public class AccountModule {

    private NotesApplication mApplication;

    public AccountModule(NotesApplication application) {
        mApplication = application;
    }


    @Provides
    @Singleton
    AccountManager provideAccountManager() {
        return new AccountManager(mApplication);
    }

}
