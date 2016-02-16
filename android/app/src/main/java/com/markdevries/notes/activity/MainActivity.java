package com.markdevries.notes.activity;

import android.accounts.Account;
import android.app.FragmentTransaction;
import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.activeandroid.query.Select;
import com.markdevries.notes.NotesApplication;
import com.markdevries.notes.R;
import com.markdevries.notes.account.AccountManager;
import com.markdevries.notes.fragment.NoteListFragment;
import com.markdevries.notes.models.auth.User;
import com.markdevries.notes.utils.MenuTintUtils;
import com.markdevries.notes.widget.NavigationAccountView;

import javax.inject.Inject;

/**
 * Created by mark on 2/15/16.
 */
public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SIGN_IN = 901;

    private static final String TAG = MainActivity.class.getSimpleName();

    @Inject
    AccountManager mAccountManager;

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;

    private NoteListFragment mFragment = new NoteListFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((NotesApplication) getApplication()).getNotesComponent().inject(this);

        setContentView(R.layout.activity_main);

        init();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content, mFragment);
        transaction.commit();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (REQUEST_CODE_SIGN_IN == requestCode) {
            if (RESULT_OK == resultCode) {
                validate();
            } else {
                finish();
            }
        } else {
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onResume() {
        super.onResume();

        validate();
    }


    @Override
    protected boolean onPrepareOptionsPanel(View view, Menu menu) {
        MenuTintUtils.tintAllIcons(menu, getResources().getColor(R.color.colorAccent));
        return super.onPrepareOptionsPanel(view, menu);
    }


    private void init() {
        initToolBar();
        initNavDrawer();
    }


    protected void initToolBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
    }


    private void initNavDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.nav_drawer);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        initNavDrawerEvents();
    }


    private void initNavDrawerEvents() {
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                menuItem.setChecked(true);

                mDrawerLayout.closeDrawer(GravityCompat.START);

                return true;
            }
        });

        if (mToolbar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mToolbar.setNavigationIcon(R.drawable.ic_drawer);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
            });
        }
    }


    private void validate() {
        Account[] accounts = mAccountManager.getAccounts();
        if (accounts == null || accounts.length <= 0) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, REQUEST_CODE_SIGN_IN, null);
            return;
        } else {
            init(accounts[0].name);
        }
    }

    private void init(String accountName) {
        mFragment.initLoader();
        requestSync();
    }

    private void requestSync() {
        Bundle settingsBundle = new Bundle();
        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        ContentResolver.requestSync(mAccountManager.getCurrentAccount(), "com.markdevries.notes", settingsBundle);
    }
}
