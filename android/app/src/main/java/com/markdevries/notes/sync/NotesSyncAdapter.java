package com.markdevries.notes.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.text.TextUtils;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.markdevries.notes.NotesApplication;
import com.markdevries.notes.account.AccountManager;
import com.markdevries.notes.api.NotesApiInterface;
import com.markdevries.notes.models.Note;
import com.markdevries.notes.models.NoteList;
import com.markdevries.notes.models.auth.User;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by mark on 2/15/16.
 */
public class NotesSyncAdapter extends AbstractThreadedSyncAdapter {

    private static final String TAG = NotesSyncAdapter.class.getSimpleName();

    private ContentResolver mContentResolver;
    @Inject
    AccountManager mAccountManager;
    @Inject
    NotesApiInterface mApiClient;


    public NotesSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContentResolver = context.getContentResolver();
        ((NotesApplication) context.getApplicationContext()).getNotesComponent().inject(this);
    }

    public NotesSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        mContentResolver = context.getContentResolver();
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        User user = new Select().from(User.class).where("Username=?", account.name).executeSingle();
        String authToken = mAccountManager.getAuthTokenForUser(user);
        if (user != null && !TextUtils.isEmpty(authToken)) {
            String authorizationHeader = "Bearer " + authToken;
            int startIndex = 0;
            int totalPerPage = 20;

            getNotes(user, authorizationHeader, startIndex, totalPerPage);
        }
    }


    private void getNotes(User user, String authorizationHeader, int startIndex, int totalPerPage) {
        Call<NoteList> call = mApiClient.list(authorizationHeader, startIndex, totalPerPage);
        try {
            Response<NoteList> response = call.execute();
            if (response.isSuccess()) {
                NoteList list = response.body();
                saveNotes(user, list.notes);
                if (list.total > list.end) {
                    getNotes(user, authorizationHeader, list.end, totalPerPage);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveNotes(User user, List<Note> notes) {
        ActiveAndroid.beginTransaction();
        try {
            for (Note note : notes) {
                note.isSynced = true;
                note.user = user;
                note.save();
            }
            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }
    }

}
