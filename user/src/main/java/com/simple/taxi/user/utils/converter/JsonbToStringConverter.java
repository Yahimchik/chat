package com.simple.taxi.user.utils.converter;

import io.r2dbc.postgresql.codec.Json;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
public class JsonbToStringConverter implements Converter<Json, String> {
    @Override
    public String convert(Json source) {
        return source.asString();
    }
}