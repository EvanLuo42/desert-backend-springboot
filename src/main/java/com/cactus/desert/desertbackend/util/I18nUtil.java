package com.cactus.desert.desertbackend.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.Locale;

/**
 * @author EvanLuo42
 * @date 4/28/22 5:29 PM
 */
public class I18nUtil {
    private static final Logger logger = LoggerFactory.getLogger(I18nUtil.class);

    public static String getMessage(String code) {
        return getMessage(code, null);
    }

    public static String getMessage(String code, Object[] args) {
        return getMessage(code, args, "");
    }

    public static String getMessage(String code, Object[] args, String defaultMessage) {

        Locale locale = LocaleContextHolder.getLocale();
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("i18n/messages");
        String content;

        try {
            content = messageSource.getMessage(code, args, locale);
        } catch (Exception e) {
            logger.error(String.valueOf(e));
            content = defaultMessage;
        }

        return content;
    }
}
