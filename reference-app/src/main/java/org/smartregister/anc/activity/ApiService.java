package org.smartregister.anc.activity;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("/data")
    Call<ResponseBody> getData();
}
