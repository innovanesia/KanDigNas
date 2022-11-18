package id.innovanesia.kandignas.backend.api

import id.innovanesia.kandignas.backend.response.*
import retrofit2.Call
import retrofit2.http.*

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
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("phone") phone: String,
        @Field("nis") nis: String,
        @Field("nisn") nisn: String,
        @Field("nik") nik: String,
    ): Call<LoginRegisterResponse>

    @GET("schools")
    fun getSchoolList() : Call<SchoolResponse>

    @GET("account")
    fun getAccount(
        @Header("Authorization") token: String
    ) : Call<AccountResponse>

    @GET("account/transactions")
    fun getTransaction(
        @Header("Authorization") token: String,
    ) : Call<TransactionResponse>

    @FormUrlEncoded
    @POST("account/topup")
    fun topupTransaction(
        @Header("Authorization") token: String,
        @Field("user_to") id: Int,
        @Field("amount") amount: Int
    ) : Call<TransactionFlowResponse>

    @FormUrlEncoded
    @POST("account/payment")
    fun paymentTransaction(
        @Header("Authorization") token: String,
        @Field("user_to") id: Int,
        @Field("amount") amount: Int
    ) : Call<TransactionFlowResponse>

    @FormUrlEncoded
    @POST("account/transfer")
    fun transferTransaction(
        @Header("Authorization") token: String,
        @Field("user_to") id: Int,
        @Field("amount") amount: Int
    ) : Call<TransactionFlowResponse>

    @FormUrlEncoded
    @POST("account/withdraw")
    fun withdrawTransaction(
        @Header("Authorization") token: String,
        @Field("user_to") id: Int,
        @Field("amount") amount: Int
    ) : Call<TransactionFlowResponse>
}