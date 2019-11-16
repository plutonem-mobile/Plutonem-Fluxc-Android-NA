package com.plutonem.android.fluxc.network.rest;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.plutonem.android.fluxc.network.BaseRequest;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public abstract class GsonRequest<T> extends BaseRequest<T> {
    private final Gson mGson;
    private final Class<T> mClass;
    private final Type mType;
    private final Listener<T> mListener;
    private final Map<String, String> mParams;
    private final Map<String, Object> mBody;

    protected GsonRequest(int method, Map<String, String> params, Map<String, Object> body, String url, Class<T> clazz,
                          Type type, Response.Listener<T> listener, BaseErrorListener errorListener) {
        super(method, url, errorListener);
        // HTTP RFC requires a body (even empty) for all POST requests. Volley will default to using the params
        // for the body so only do this if params is null since this behavior is desirable for form-encoded
        // POST requests.
        if (method == Request.Method.POST && body == null && (params == null || params.size() == 0)) {
            body = new HashMap<>();
        }

        mClass = clazz;
        mType = type;
        mListener = listener;
        mGson = setupGsonBuilder().create();
        mParams = params;
        mBody = body;
    }

    @Override
    protected void deliverResponse(T response) {
        mListener.onResponse(response);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            T res;
            if (mClass == null) {
                res = mGson.fromJson(json, mType);
            } else {
                res = mGson.fromJson(json, mClass);
            }
            return Response.success(res, createCacheEntry(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }

    private GsonBuilder setupGsonBuilder() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setLenient();
        gsonBuilder.registerTypeHierarchyAdapter(JsonObjectOrFalse.class, new JsonObjectOrFalseDeserializer());
        gsonBuilder.registerTypeHierarchyAdapter(JsonObjectOrEmptyArray.class,
                new JsonObjectOrEmptyArrayDeserializer());
        return gsonBuilder;
    }
}