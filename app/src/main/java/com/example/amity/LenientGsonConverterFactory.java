package com.example.amity;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

public class LenientGsonConverterFactory extends Converter.Factory {

    private final Gson gson;

    public LenientGsonConverterFactory(Gson gson) {
        this.gson = gson;
    }

    public static LenientGsonConverterFactory create() {
        return create(new Gson());
    }

    public static LenientGsonConverterFactory create(Gson gson) {
        return new LenientGsonConverterFactory(gson);
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        final TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new Converter<ResponseBody, Object>() {
            @Override
            public Object convert(ResponseBody value) throws IOException {
                JsonReader jsonReader = gson.newJsonReader(value.charStream());
                jsonReader.setLenient(true);  // Set lenient to true
                try {
                    return adapter.read(jsonReader);
                } finally {
                    value.close();
                }
            }
        };
    }
}
