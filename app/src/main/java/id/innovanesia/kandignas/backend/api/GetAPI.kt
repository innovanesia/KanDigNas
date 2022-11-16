package id.innovanesia.kandignas.backend.api

import id.innovanesia.kandignas.backend.response.AccountResponse
import id.innovanesia.kandignas.backend.response.LoginRegisterResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface GetAPI
{
    @FormUrlEncoded
    @POST("auth/login")
    fun login(
        @Field("username") user: String,
        @Field("password") password: String
    ): Call<LoginRegisterResponse>

    @FormUrlEncoded
    @POST("auth/register")
    fun register(
        @Field("type") type: String,
        @Field("name") fullName: String,
        @Field("school_id") school_id: Int,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("nis") nis: String,
        @Field("nisn") nisn: String,
        @Field("nik") nik: String,
    ): Call<LoginRegisterResponse>

    @GET("account")
    fun getAccount(
        @Header("Authorization") token: String
    ) : Call<AccountResponse>
}