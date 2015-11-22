package com.tamerbarsbay.depothouston.data.net;

import com.google.gson.Gson;
import com.squareup.okhttp.ResponseBody;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;

import retrofit.Converter;

public class CustomGsonConverter<T> implements Converter<ResponseBody, T> {

    private final Gson gson;
    private final Type type;

    CustomGsonConverter(Gson gson, Type type) {
        this.gson = gson;
        this.type = type;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String responseString = value.string();
        try {
            JSONObject json = new JSONObject(responseString);
            JSONArray results = json.getJSONObject("d").getJSONArray("results");
            return gson.fromJson(results.toString(), type);
        } catch (JSONException e) {
            return null;
        }
    }
}
