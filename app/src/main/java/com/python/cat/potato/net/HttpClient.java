package com.python.cat.potato.net;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.apkfuns.logutils.LogUtils;
import com.python.cat.potato.base.BaseApplication;
import com.python.cat.potato.global.GlobalInfo;
import com.python.cat.potato.utils.SpUtils;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;
import okio.GzipSink;
import okio.Okio;

/**
 * https://github.com/square/okhttp/wiki/Interceptors
 */
class HttpClient {

    private HttpClient() {
        throw new RuntimeException("error");
    }

    static OkHttpClient client() {
//        LogUtils.e("client...");
        return new OkHttpClient.Builder()
//                .cookieJar(new JavaNetCookieJar(cacheCookie()))
//                .addInterceptor(new LoggingInterceptor())
                .addInterceptor(new CookieInterceptor())
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
//                .retryOnConnectionFailure(true)
                .build();
    }

    private static CookieManager cacheCookie() {
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        return cookieManager;
    }

}


class LoggingInterceptor implements Interceptor {

    @Override
    public Response intercept(@NonNull Interceptor.Chain chain) throws IOException {
        Request request = chain.request();
        long t1 = System.nanoTime();
        LogUtils.i(String.format(Locale.CHINA, "Sending request %s on %s%n%s",
                request.url(), chain.connection(), request.headers()));
        Response response = chain.proceed(request);
        long t2 = System.nanoTime();
        LogUtils.i(String.format(Locale.CHINA,
                "Received response for %s in %.1fms%n%s",
                response.request().url(), (t2 - t1) / 1e6d, response.headers()));

        LogUtils.e(response);

        return response;
    }
}

class CookieInterceptor implements Interceptor {

    private static final String SET_COOKIE = "Set-Cookie";

    @Override
    public Response intercept(@NonNull Interceptor.Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        Headers headers = response.headers();
        LogUtils.v(headers);
        Map<String, List<String>> listMap = headers.toMultimap();
        LogUtils.e("=================" + headers.size() + " xxx " + listMap.size());
        LogUtils.v(listMap);
        StringBuilder cookies = new StringBuilder();
        for (Map.Entry<String, List<String>> next : listMap.entrySet()) {
            String key = next.getKey();
            if (key.toUpperCase().equals(SET_COOKIE.toUpperCase())) {
                List<String> value = next.getValue();
                for (int i = 0; i < value.size(); i++) {
                    String coo = value.get(i);
                    String cookie = coo.substring(0, coo.indexOf(";"));
                    if (i < value.size() - 1) {
                        cookies.append(cookie)
                                .append(";");
                    } else {
                        cookies.append(cookie);
                    }
                }
            }
        }
        if (!TextUtils.isEmpty(cookies)) {
            SpUtils.put(BaseApplication.get(), GlobalInfo.SP_KEY_COOKIE, cookies.toString());
        }
//        LogUtils.v("COOKIE:" + cookies);
//        LogUtils.v("COOKIE:" + SpUtils.get(BaseApplication.get(), GlobalInfo.SP_KEY_COOKIE));
        return response;
    }
}

/**
 * This interceptor compresses the HTTP request body. Many webservers can't handle this!
 */
final class GzipRequestInterceptor implements Interceptor {
    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request originalRequest = chain.request();
        if (originalRequest.body() == null || originalRequest.header("Content-Encoding") != null) {
            return chain.proceed(originalRequest);
        }

        Request compressedRequest = originalRequest.newBuilder()
                .header("Content-Encoding", "gzip")
                .method(originalRequest.method(), gzip(originalRequest.body()))
                .build();
        return chain.proceed(compressedRequest);
    }

    private RequestBody gzip(final RequestBody body) {
        return new RequestBody() {
            @Override
            public MediaType contentType() {
                return body.contentType();
            }

            @Override
            public long contentLength() {
                return -1; // We don't know the compressed length in advance!
            }

            @Override
            public void writeTo(@NonNull BufferedSink sink) throws IOException {
                BufferedSink gzipSink = Okio.buffer(new GzipSink(sink));
                body.writeTo(gzipSink);
                gzipSink.close();
            }
        };
    }
}