package ua.antonk.googlecustomsearch.interfaces;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;
import ua.antonk.googlecustomsearch.database.entities.SearchResult;

/**
 * Created by Anton on 01.08.2015.
 */
public interface ApiMethods {

    @GET("/")
    SearchResult getSearchResults(@Query("key") String key,
                                  @Query("cx") String cx,
                                  @Query("q") String request,
                                  @Query("start") int startIndex,
                                  @Query("fields") String fields);
}
