package com.markdevries.notes.api;

import com.markdevries.notes.models.Note;
import com.markdevries.notes.models.NoteList;
import com.markdevries.notes.models.auth.Login;
import com.markdevries.notes.models.auth.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by mark on 2/15/16.
 */
public interface NotesApiInterface {


    @POST("/api/login")
    Call<Login> signin(@Body User user);

    @FormUrlEncoded
    @POST("/oauth/access_token")
    Call<Login> refreshToken(@Field("grant_type") String grantType, @Field("refresh_token") String refreshToken);


    @FormUrlEncoded
    @POST("/api/notes/list")
    Call<NoteList> list(@Header("Authorization") String authorization, @Field("offset") int startIndex, @Field("max") int totalPerPage);


    @POST("/api/notes/create")
    Call<Note> create(@Header("Authorization") String authorization, @Body Note note);

}
