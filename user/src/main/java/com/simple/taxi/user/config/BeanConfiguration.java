package com.simple.taxi.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.web.reactive.function.client.WebClient;

import com.simple.taxi.user.utils.converter.JsonbToStringConverter;
import com.simple.taxi.user.utils.converter.StringToJsonbConverter;
import org.springframework.data.convert.CustomConversions;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;
import org.springframework.data.r2dbc.dialect.PostgresDialect;

import java.util.ArrayList;
import java.util.List;
@Configuration
public class BeanConfiguration {
    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        return builder.build();
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate(ProducerFactory<String, String> pf) {
        return new KafkaTemplate<>(pf);
    }

//    @Bean
//    public R2dbcCustomConversions r2dbcCustomConversions() {
//        List<Object> converters = new ArrayList<>();
//        converters.add(new StringToJsonbConverter());
//        converters.add(new JsonbToStringConverter());
//
//        CustomConversions.StoreConversions storeConversions =
//                CustomConversions.StoreConversions.of(PostgresDialect.INSTANCE.getSimpleTypeHolder(), converters);
//
//        return new R2dbcCustomConversions(storeConversions, converters);
//    }
}
