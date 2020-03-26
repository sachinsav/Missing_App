package com.dream.te;

import com.google.gson.annotations.SerializedName;

public class ImageClass {
    @SerializedName("title")
    private String title;
    @SerializedName("u_image")
    private String image;

    @SerializedName("response")
    private String Response;

    public String getResponse() {
        return Response;
    }
}
