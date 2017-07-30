package com.programacionymas.igadgets.io;

import com.programacionymas.igadgets.io.response.TopMatrixHour;
import com.programacionymas.igadgets.io.response.TopProductsResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MyApiService {

    @GET("top/productos/data")
    Call<TopProductsResponse> getTopProductsData(
            @Query("start_date") String startDate, @Query("end_date") String endDate,
            @Query("top") String top
    );

    @GET("top/horas/matriz")
    Call<ArrayList<ArrayList<TopMatrixHour>>> getTopProductsData(
            @Query("start_date") String startDate, @Query("end_date") String endDate
    );

    /*
    @FormUrlEncoded
    @POST("upload/photo")
    Call<SimpleResponse> postPhoto(
            @Field("image") String base64,
            @Field("extension") String extension,
            @Field("user_id") String user_id
    );

    @GET("/login")
    Call<LoginResponse> getLogin(
            @Query("username") String username,
            @Query("password") String password
    );

    @FormUrlEncoded
    @POST("product")
    Call<SimpleResponse> postNewProduct(
            @Field("code") String code,
            @Field("name") String name,
            @Field("description") String description
    );
*/
}