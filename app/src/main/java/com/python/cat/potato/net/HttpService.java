package com.python.cat.potato.net;

import com.python.cat.potato.domain.LoginResult;
import com.python.cat.potato.domain.TODO;

import io.reactivex.Flowable;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * interface for http --> retrofit
 */
interface HttpService {

    String BASE_URL = "http://www.wanandroid.com/";

    @FormUrlEncoded
    @POST("user/login")
    Flowable<LoginResult> userLogin(@Field("username") String username,
                                    @Field("password") String password);

    /**
     * @param currentPage start at 1
     * @return domain
     */
    @GET("lg/todo/v2/list/{currentPage}/json")
    Flowable<TODO> queryTodo(@Header("Cookie") String header,
                             @Path("currentPage") int currentPage);


}

