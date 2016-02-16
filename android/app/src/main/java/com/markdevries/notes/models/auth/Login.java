package com.markdevries.notes.models.auth;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mark on 2/15/16.
 */
public class Login {

    public String username;

    @SerializedName("token_type")
    public String tokenType;

    @SerializedName("access_token")
    public String authToken;

    @SerializedName("refresh_token")
    public String refreshToken;

    @SerializedName("expires_in")
    public int expiresIn;
}
