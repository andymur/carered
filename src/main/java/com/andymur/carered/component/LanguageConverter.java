package com.andymur.carered.component;

import com.andymur.carered.error.UnsupportedLanguageException;
import com.andymur.carered.model.Language;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class LanguageConverter implements Converter<String, Language> {

    @Override
    public Language convert(String language) {
        for (Language lang : Language.values()) {
            if (lang.name().equalsIgnoreCase(language)) {
                return lang;
            }
        }
        throw new UnsupportedLanguageException(language);
    }
}