package com.markdevries.notes.account;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.markdevries.notes.NotesApplication;
import com.markdevries.notes.activity.LoginActivity;
import com.markdevries.notes.api.NotesApiInterface;
import com.markdevries.notes.models.auth.Login;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.OkHttpClient;
import retrofit2.Call;

/**
 * Created by mark on 2/15/16.
 */
public class AccountAuthenticator extends AbstractAccountAuthenticator {

    public static final String KEY_REFRESH_TOKEN = "refreshToken";
    private static final int ERROR_CODE_ONE_ACCOUNT_ALLOWED = 10000;
    private Context mContext;
    @Inject
    OkHttpClient mOkHttpClient;
    @Inject
    NotesApiInterface mApiInterface;


    public AccountAuthenticator(Context context) {
        super(context);
        mContext = context.getApplicationContext();
        ((NotesApplication) mContext).getNotesComponent().inject(this);
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
        return null;
    }

    @Override
    public Bundle addAccount(AccountAuthenticatorResponse response, String accountType, String authTokenType, String[] requiredFeatures, Bundle options) throws NetworkErrorException {
        // For adding account, we needs to prompt credentials
        final Intent intent = new Intent(mContext, LoginActivity.class);
        intent.putExtra(LoginActivity.ACCOUNT_TYPE, accountType);
        intent.putExtra(LoginActivity.AUTH_TYPE, authTokenType);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);

        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);

        return bundle;
    }

    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account, Bundle options) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        // Extract the username and password from the Account Manager, and ask
        // the server for an appropriate AuthToken.
        final AccountManager accountManager = AccountManager.get(mContext);

        String authToken = accountManager.peekAuthToken(account, authTokenType);
        String refreshToken = accountManager.peekAuthToken(account, KEY_REFRESH_TOKEN);

        // Lets give another try to authenticate the user
        if (TextUtils.isEmpty(authToken)) {
            if (!TextUtils.isEmpty(refreshToken)) {
                Call<Login> call = mApiInterface.refreshToken("refresh_token", refreshToken);
                try {
                    Login login = call.execute().body();
                    authToken = login.authToken;
                    refreshToken = login.refreshToken;

                    accountManager.setAuthToken(account, AccountManager.KEY_AUTHTOKEN, authToken);
                    accountManager.setAuthToken(account, AccountAuthenticator.KEY_REFRESH_TOKEN, refreshToken);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if (!TextUtils.isEmpty(authToken) && !TextUtils.isEmpty(refreshToken)) {
            final Bundle result = new Bundle();
            result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
            result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
            result.putString(AccountManager.KEY_AUTHTOKEN, authToken);
            result.putString(AccountAuthenticator.KEY_REFRESH_TOKEN, refreshToken);
            return result;
        }

        // If we get here, then we couldn't access the user's password - so we
        // need to re-prompt them for their credentials. We do that by creating
        // an intent to display our AuthenticatorActivity.
        final Intent intent = new Intent(mContext, LoginActivity.class);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
        intent.putExtra(LoginActivity.ACCOUNT_TYPE, account.type);
        intent.putExtra(LoginActivity.AUTH_TYPE, authTokenType);
        intent.putExtra(LoginActivity.ACCOUNT_NAME, account.name);
        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);

        return bundle;
    }

    @Override
    public String getAuthTokenLabel(String authTokenType) {
        return authTokenType;
    }

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account, String[] features) throws NetworkErrorException {
        return null;
    }
}
