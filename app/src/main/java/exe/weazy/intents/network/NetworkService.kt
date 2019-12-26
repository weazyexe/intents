package exe.weazy.intents.network

import io.reactivex.Observable
import retrofit2.http.*

interface NetworkService {

    @GET("requests/?format=json")
    fun getIntents(
        @Query("is_marked_up") isMarkedUp : String = "False",
        @Query("q") search: String = "",
        @Query("page") page: Int = 1
    ) : Observable<IntentsResponse>

    @GET("categories/?format=json")
    fun getCategories() : Observable<CategoriesResponse>

    @FormUrlEncoded
    @PUT("admin/requests/{id}")
    fun updateIntent(
        @Path("id") id: Int,
        @Field("content") content: String,
        @Field("category_ids") ids: List<Int>
    ) : Observable<IntentResponse>

    @FormUrlEncoded
    @GET("predict_category")
    fun getPredictedCategory(@Field("content") content : String) : Observable<CategoriesResponse>
}