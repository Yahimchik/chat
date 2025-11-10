package com.simple.taxi.user.utils.converter;

import io.r2dbc.postgresql.codec.Json;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

@WritingConverter
public class StringToJsonbConverter implements Converter<String, Json> {
    @Override
    public Json convert(@NotNull String source) {
        return Json.of(source);
    }
}
