package com.m.socialnetworklogin;


import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ServiceInterface {


    @Multipart
    @POST("paytm/register.php")
    Call<Registration> register(
            @Part("securecode") RequestBody securecode,
            @Part("user_id") RequestBody user_id,
            @Part("email") RequestBody email,
            @Part("username") RequestBody username

    );

}
