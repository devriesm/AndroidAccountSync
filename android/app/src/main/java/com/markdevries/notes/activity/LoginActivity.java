package com.markdevries.notes.activity;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.markdevries.notes.NotesApplication;
import com.markdevries.notes.R;
import com.markdevries.notes.api.NotesApiInterface;
import com.markdevries.notes.models.auth.Login;
import com.markdevries.notes.models.auth.User;

import javax.inject.Inject;

import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AccountAuthenticatorActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    private static final int UPDATE_INTERVAL = 2000;

    // Intent arg keys
    public static final String ACCOUNT_TYPE = "ACCOUNT_TYPE";
    public static final String AUTH_TYPE = "AUTH_TYPE";
    public static final String ACCOUNT_NAME = "ACCOUNT_NAME";

    public static final String ACTION_REGISTER = "action_register";
    public static final String ACTION_LOG_IN = "action_log_in";

    @Inject com.markdevries.notes.account.AccountManager mAccountManager;
    @Inject NotesApiInterface mApiInterface;

    private EditText mEmailText;
    private EditText mPasswordText;
    private Button mSignInButton;
    private ProgressBar mProgressView;

    private String mAction;

    private User mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((NotesApplication) getApplication()).getNotesComponent().inject(this);

        Intent intent = getIntent();

        mAction = intent.getAction();
        if(TextUtils.isEmpty(mAction)) {
            mAction = ACTION_LOG_IN;
        }

        setContentView(R.layout.activity_login);

        mEmailText = (EditText) findViewById(R.id.email);
        mPasswordText = (EditText) findViewById(R.id.password);
        mSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mProgressView = (ProgressBar) findViewById(R.id.login_progress);

        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClicked();
            }
        });

        if(ACTION_REGISTER.equals(mAction)) {

            mSignInButton.setText(R.string.account_create);
        }else {
            mSignInButton.setText(R.string.account_signin);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void buttonClicked() {
        mSignInButton.setEnabled(false);

        mUser = new User();
        mUser.username = mEmailText.getText().toString();
        mUser.password = mPasswordText.getText().toString();

        boolean validated = isInputDataValidated();
        if(!validated)
            return;

        hideKeyboard();

        if(ACTION_LOG_IN.equals(mAction)) {
            attemptLogin();
        }else {
            attemptRegister();
        }
    }


    private void attemptRegister() {
        showProgress(true);
    }


    private void attemptLogin() {
        showProgress(true);
        Call<Login> call = mApiInterface.signin(mUser);
        call.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, retrofit2.Response<Login> response) {
                if (response.isSuccess()) {
                    Log.e(TAG, "RESPONSE : " + response.body().authToken);
                    loginSuccess(response.body());
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                Log.e(TAG, "Failure!!!!! : " + t.getMessage());
                loginFailure();
            }
        });
    }


    private boolean isInputDataValidated() {
        mEmailText.setError(null);
        mPasswordText.setError(null);

        if(TextUtils.isEmpty(mUser.username) || !isEmailValid(mUser.username)) {
            mEmailText.setError(getString(R.string.error_invalid_email));
            return false;
        }else {
            Account account = mAccountManager.getAccountForUser(mUser);
            if(account != null) {
                mEmailText.setError(getString(R.string.error_account_in_use));
                return false;
            }
        }

        if (TextUtils.isEmpty(mUser.password) || !isPasswordValid(mUser.password)) {
            mPasswordText.setError(getString(R.string.error_invalid_password));
            return false;
        }

        return true;
    }


    private void loginSuccess(Login login) {
        Intent intent = mAccountManager.createAccount(mUser, login.authToken, login.refreshToken);
        setAccountAuthenticatorResult(intent.getExtras());
        initializeSync();
        setResult(RESULT_OK, intent);
        finish();
    }


    private void loginFailure() {
        mSignInButton.setEnabled(false);
        showProgress(false);
    }


    private void initializeSync(){
        //need to save the user, as it will be used in the sync adapter
        mUser.save();

        Account account = mAccountManager.getAccountForUser(mUser);
        //setup sync
        ContentResolver resolver = getContentResolver();
        resolver.setSyncAutomatically(account, "com.markdevries.notes", true);

        Bundle settingsBundle = new Bundle();
        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);

        //requests an immediate sync
        ContentResolver.requestSync(account, "com.markdevries.notes", settingsBundle);
    }


    private boolean isPasswordValid(String password) {
        return (password.length() >= 8);
    }


    private boolean isEmailValid(String email) {
        try {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches();
        } catch (Exception e) {
        }

        return false;
    }


    private void showProgress(boolean showProgress) {
        if(showProgress) {
            mProgressView.setVisibility(View.VISIBLE);
        }else {
            mProgressView.setVisibility(View.GONE);
        }
    }


    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}

