package com.markdevries.notes;


import android.accounts.Account;

import com.activeandroid.app.Application;
import com.activeandroid.query.Select;
import com.markdevries.notes.account.AccountManager;
import com.markdevries.notes.di.components.DaggerNotesComponent;
import com.markdevries.notes.di.components.NotesComponent;
import com.markdevries.notes.di.modules.AccountModule;
import com.markdevries.notes.di.modules.ApiClientModule;
import com.markdevries.notes.models.auth.User;

import javax.inject.Inject;

/**
 * Created by mark on 2/15/16.
 */
public class NotesApplication extends Application {

    private NotesComponent mNotesComponent;

    @Inject
    AccountManager mAccountManager;

    @Override
    public void onCreate() {
        super.onCreate();

        mNotesComponent = DaggerNotesComponent.builder()
                .accountModule(new AccountModule(this))
                .apiClientModule(new ApiClientModule(this, "http://192.168.1.121:8080"))
                .build();

        mNotesComponent.inject(this);

        initAccounts();
    }


    private void initAccounts() {
        Account[] accounts = mAccountManager.getAccounts();
        if(accounts != null && accounts.length > 0) {
            Account acct = accounts[0];
            User user = new Select().from(User.class).where("Username=?", acct.name).executeSingle();
            mAccountManager.setCurrentUser(user);
        }
    }


    public NotesComponent getNotesComponent() {
        return mNotesComponent;
    }
}
