package com.markdevries.notes.account;

import android.accounts.Account;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.markdevries.notes.models.auth.User;

/**
 * Created by mark on 2/15/16.
 */
public class AccountManager {

    private User mCurrentUser;
    private android.accounts.AccountManager mAccountManager;

    public AccountManager(Context context) {
        mAccountManager = android.accounts.AccountManager.get(context);
    }

    public void setCurrentUser(User user) {
        mCurrentUser = user;
    }

    public User getCurrentUser() {
        return mCurrentUser;
    }

    public Account[] getAccounts() {
        return mAccountManager.getAccounts();
    }

    public Account getCurrentAccount() {
        return getAccountForUser(mCurrentUser);
    }

    public Account getAccountForUser(User user) {
        if (user == null || TextUtils.isEmpty(user.username))
            return null;

        for (Account acct : getAccounts()) {
            if (user.username.equalsIgnoreCase(acct.name))
                return acct;
        }

        return null;
    }


    public String getAuthTokenForCurrentUser() {
        return getAuthTokenForUser(mCurrentUser);
    }


    public String getRefreshTokenForCurrentUser() {
        return getRefreshTokenForUser(mCurrentUser);
    }


    public String getAuthTokenForUser(User user) {
        Account account = getAccountForUser(user);
        if (account == null)
            return null;

        return mAccountManager.peekAuthToken(account, android.accounts.AccountManager.KEY_AUTHTOKEN);
    }

    public String getRefreshTokenForUser(User user) {
        Account account = getAccountForUser(user);
        if (account == null)
            return null;

        return mAccountManager.peekAuthToken(account, AccountAuthenticator.KEY_REFRESH_TOKEN);
    }


    public Intent createAccount(User user, String authToken, String refreshToken) {
        Bundle bundle = new Bundle();
        Intent intent = new Intent();

        bundle.putString(android.accounts.AccountManager.KEY_ACCOUNT_TYPE, user.accountType.toString());
        bundle.putString(android.accounts.AccountManager.KEY_AUTHTOKEN, authToken);
        bundle.putString(AccountAuthenticator.KEY_REFRESH_TOKEN, refreshToken);

        intent.putExtras(bundle);

        final Account account = new Account(user.username, user.accountType.toString());

        mAccountManager.addAccountExplicitly(account, null, null);
        mAccountManager.setAuthToken(account, android.accounts.AccountManager.KEY_AUTHTOKEN, authToken);
        mAccountManager.setAuthToken(account, AccountAuthenticator.KEY_REFRESH_TOKEN, refreshToken);

        return intent;
    }

}
