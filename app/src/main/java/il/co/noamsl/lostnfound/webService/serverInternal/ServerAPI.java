package il.co.noamsl.lostnfound.webService.serverInternal;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

// * paths that start with '/' are absolute, paths that don't are
//   relative to the base url (we always want relative paths).
public interface ServerAPI {
    // User-related calls
    @POST("user")
    Call<Integer> user_create(@Body Users u);

    @PUT("user")
    Call<Integer> user_edit(@Body Users u);

    @GET("user")
    Call<Users> user_getSettings(@Query("id") Integer id, @Query("email") String email);

    @GET("user/lost")
    Call<LostTableList> user_getLostItems(@Query("id") Integer id);

    @GET("user/found")
    Call<FoundTableList> user_getFoundItems(@Query("id") Integer id);

    // restrictions-related calls
    @POST("restrict")
    Call<Integer> restriction_create(@Body Restrictions u);

    @PUT("restrict")
    Call<Integer> restriction_edit(@Body Restrictions u);

    @GET("restrict")
    Call<Users> restriction_getAllowed(@Query("recordid") Integer recordId);

    // lost entries-related calls
    @POST("lost")
    Call<Integer> lost_create(@Body LostTable l);

    @PUT("lost")
    Call<Integer> lost_edit(@Body LostTable l);

    @GET("lost")
    Call<LostTableList> lost_queryItems(@Query("name") String name,
                                        @Query("description") String description,
                                        @Query("location") String location);

    // found entries-related calls
    @POST("found")
    Call<Integer> found_create(@Body FoundTable l);

    @PUT("found")
    Call<Integer> found_edit(@Body FoundTable l);

    @GET("found")
    Call<FoundTableList> found_queryItems(@Query("name") String name,
                                          @Query("description") String description,
                                          @Query("location") String location);
}
