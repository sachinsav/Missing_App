package com.dream.te;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiInterface {
    @FormUrlEncoded
    @POST("api/")
    Call<ImageClass> uploadImage(
            @Field("title") String title,
            @Field("u_image") String image
    );
    @FormUrlEncoded
    @PUT("check/{id}/")
    Call<Object> putPost( @Path("id") String id,
                          @Field("u2_image") String image);

}
