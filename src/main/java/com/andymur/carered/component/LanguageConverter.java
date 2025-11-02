package com.andymur.carered.component;

import com.andymur.carered.model.Language;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class LanguageConverter implements Converter<String, Language> {

    @Override
    public Language convert(String source) {
        for (Language lang : Language.values()) {
            if (lang.name().equalsIgnoreCase(source)) {
                return lang;
            }
        }
        throw new IllegalArgumentException(
                "Invalid language '" + source + "'. Allowed: Java, Go, Python"
        );
    }
}