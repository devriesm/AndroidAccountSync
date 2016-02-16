package com.markdevries.notes.widget;

import android.accounts.Account;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.markdevries.notes.NotesApplication;
import com.markdevries.notes.R;
import com.markdevries.notes.account.AccountManager;
import com.markdevries.notes.models.auth.User;

import javax.inject.Inject;

/**
 * Created by mark on 2/15/16.
 */
public class NavigationAccountView extends FrameLayout {

    private static final String TAG = NavigationAccountView.class.getSimpleName();
    @Inject
    AccountManager mAccountManager;

    private Spinner mAccountSelector;


    public NavigationAccountView(Context context) {
        this(context, null);
    }

    public NavigationAccountView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NavigationAccountView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        NotesApplication app = (NotesApplication) getContext().getApplicationContext();
        app.getNotesComponent().inject(this);

        LayoutInflater.from(getContext()).inflate(R.layout.view_navigation_account, this);

        Account[] accounts = mAccountManager.getAccounts();
        final String[] accountNames = new String[accounts.length];
        int count = 0;
        for(Account acct : accounts) {
            accountNames[count] = acct.name;
            count++;
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, accountNames);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = (Spinner) findViewById(R.id.account_selector);
        spinner.setAdapter(arrayAdapter);
    }
}
