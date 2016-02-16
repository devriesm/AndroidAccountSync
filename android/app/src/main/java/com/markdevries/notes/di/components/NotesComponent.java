package com.markdevries.notes.di.components;

import com.markdevries.notes.NotesApplication;
import com.markdevries.notes.account.AccountAuthenticator;
import com.markdevries.notes.activity.MainActivity;
import com.markdevries.notes.di.modules.AccountModule;
import com.markdevries.notes.activity.LoginActivity;
import com.markdevries.notes.di.modules.ApiClientModule;
import com.markdevries.notes.fragment.NoteListFragment;
import com.markdevries.notes.sync.NotesSyncAdapter;
import com.markdevries.notes.widget.NavigationAccountView;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by mark on 2/15/16.
 */
@Singleton
@Component(modules = {ApiClientModule.class, AccountModule.class})
public interface NotesComponent {

    void inject(NotesApplication app);
    void inject(LoginActivity activity);
    void inject(MainActivity activity);
    void inject(NotesSyncAdapter syncAdapter);
    void inject(AccountAuthenticator accountAuthenticator);
    void inject(NoteListFragment fragment);
    void inject(NavigationAccountView view);
}
