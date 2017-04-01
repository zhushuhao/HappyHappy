package com.d.dao.a.api;

import com.d.dao.zlibrary.baseutils.StringConverterFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.socks.library.KLog;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by dao on 2017/2/18.
 */

public class API {
    private static API sInstance;
    private ApiService mApiService;//默认通用的配置
    private ApiService mApiService2;//默认通用的配置
    private ApiService mApiService3;//默认通用的配置

    private OkHttpClient mOkHttpClient;//默认通用的配置
    private Gson gson;

    private ApiService welcomeApiService;
    private OkHttpClient welcomeOkHttpClient;

    private Retrofit retrofit;
    private Retrofit retrofit2;
    private Retrofit retrofit3;

    private API() {
        gson = new GsonBuilder().setDateFormat(Urls.DATA_FORMAT).create();
        OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();
        mOkHttpClient = httpBuilder.readTimeout(8, TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();

                        Request request = original.newBuilder()
                                .method(original.method(), original.body())
                                .build();
                        Response response = chain.proceed(request);
                        return response;
                    }
                })
                .addInterceptor(new LoggingInterceptor())
                .followRedirects(true)
                .connectTimeout(8, TimeUnit.SECONDS)
                .writeTimeout(8, TimeUnit.SECONDS) //设置超时
                .build();

        retrofit = new Retrofit.Builder().baseUrl(Urls.KAIYAN_HOST)
                .addCallAdapterFactory(
                        RxJavaCallAdapterFactory.create())
                .addConverterFactory(StringConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(mOkHttpClient)
                .build();

        retrofit2 = new Retrofit.Builder().baseUrl(Urls.WANGYI_HOST)
                .addCallAdapterFactory(
                        RxJavaCallAdapterFactory.create())
                .addConverterFactory(StringConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(mOkHttpClient)
                .build();
        retrofit3 = new Retrofit.Builder().baseUrl(Urls.GANK_IO)
                .addCallAdapterFactory(
                        RxJavaCallAdapterFactory.create())
                .addConverterFactory(StringConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(mOkHttpClient)
                .build();

        this.mApiService = retrofit.create(ApiService.class);
        this.mApiService2 = retrofit2.create(ApiService.class);
        this.mApiService3 = retrofit3.create(ApiService.class);

    }


    /**
     * 日志拦截
     */
    public class LoggingInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {

            Request request = chain.request();
            KLog.d(String.format("Sending request %s on %s%n%s", request.url(), chain.connection(), request.headers()));

            long t1 = System.nanoTime();
            Response response = chain.proceed(chain.request());
            long t2 = System.nanoTime();
            KLog.d(String.format(Locale.getDefault(), "Received response for %s in %.1fms%n%s",
                    response.request().url(), (t2 - t1) / 1e6d, response.headers()));

            okhttp3.MediaType mediaType = response.body().contentType();
            String content = response.body().string();
            KLog.json(content);
            return response.newBuilder()
                    .body(okhttp3.ResponseBody.create(mediaType, content))
                    .build();
        }
    }

    public synchronized static API getInstance() {
        if (sInstance == null) {
            sInstance = new API();
        }
        return sInstance;
    }

    /**
     * 通用
     *
     * @return
     */
    public synchronized ApiService getApiService() {
        return mApiService;
    }

    public synchronized ApiService getApiService2() {
        return mApiService2;
    }

    public synchronized ApiService getApiService3() {
        return mApiService3;
    }

    /**
     * 欢迎页的超时设置为3秒
     *
     * @param timeout 秒
     * @return
     */
    public synchronized ApiService getWelcomeApiService(int timeout) {
        if (welcomeApiService == null) {
            OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();
            welcomeOkHttpClient = httpBuilder.readTimeout(timeout, TimeUnit.MILLISECONDS)
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request original = chain.request();

                            Request request = original.newBuilder()
//                                    .header("token", .getToken())
                                    .method(original.method(), original.body())
                                    .build();
                            Response response = chain.proceed(request);
                            return response;
                        }
                    })
                    .addInterceptor(new LoggingInterceptor())
                    .followRedirects(true)
                    .connectTimeout(timeout, TimeUnit.MILLISECONDS)
                    .writeTimeout(timeout, TimeUnit.MILLISECONDS) //设置超时
                    .build();

            Retrofit retrofit = new Retrofit.Builder().baseUrl(Urls.KAIYAN_HOST)
                    .addCallAdapterFactory(
                            RxJavaCallAdapterFactory.create())
                    .addConverterFactory(StringConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(welcomeOkHttpClient)
                    .build();
            this.welcomeApiService = retrofit.create(ApiService.class);
        }

        return welcomeApiService;

    }


}
