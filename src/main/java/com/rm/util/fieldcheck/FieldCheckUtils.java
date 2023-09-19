package com.rm.util.fieldcheck;

import com.rm.common.core.exception.ErrorType;
import com.rm.common.core.exception.RmCommonException;
import com.rm.common.core.exception.ServiceStatusCode;
import org.apache.commons.lang3.StringUtils;
import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Pattern;
import static com.rm.common.core.exception.ErrorType.ERROR_COMMON;
import static com.rm.common.core.exception.ServiceStatusCode.*;

public class FieldCheckUtils {

    // 유효성을 체크하고자 하는 VO와 유효성 체크를 하지 않을 VO의 idx를 param으로 받는다.
    public static void paramValidation(Object cls, String fieldIdx) {
        Class c = cls.getClass();
        try {
            for (Field f : c.getDeclaredFields()) {
                f.setAccessible(true);

                if (f.getType() == String.class) {
                    if (null == f.get(cls) || StringUtils.isBlank(f.get(cls).toString()) || f.get(cls).equals("")) {
                        throw new RmCommonException(ErrorType.ERROR_SYSTEM, ERROR_NO_PARAM, f.getName() + " must not be NULL");
                    }
                } else if (f.getType() == Integer.class || f.getType() == int.class) {
                    if (null != fieldIdx && fieldIdx.equals(f.getName())) {
                        continue;
                    }
                    if (f.get(cls) == null || (int) f.get(cls) == 0) {
                        throw new RmCommonException(ErrorType.ERROR_SYSTEM, ERROR_NO_PARAM, f.getName() + " must not be NULL");
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RmCommonException(ErrorType.ERROR_SYSTEM, ERROR_NO_PARAM, "Occurred IllegalAccessException Error during paramValidation");
        }
    }

    public static void specificParamValidation(Object cls, ErrorType errorType, String... properties) {
        Class<?> c = cls.getClass();
        //병렬로 처리하여 속도개선.
        Arrays.stream(properties).parallel().forEach(property -> {
            try {
                Field f = c.getDeclaredField(property);
                f.setAccessible(true);

                Object value = f.get(cls);
                if (value == null || (value instanceof String && ((String) value).isEmpty()) || (value instanceof Collection && ((Collection) value).isEmpty())) {
                    throw new RmCommonException(errorType, ERROR_NO_PARAM, property + " must not be NULL !!");
                }
            } catch (IllegalAccessException | NoSuchFieldException e) {
                throw new RmCommonException(ErrorType.ERROR_SYSTEM, ERROR_NO_PARAM, e.getMessage());
            }
        });
    }

    public static void specificParamValidation(Object cls, ErrorType errorType, List<ServiceStatusCode> serviceStatusCodeList, String... properties) {
        Class<?> c = cls.getClass();
        //병렬로 처리하여 속도개선.
        for (int i=0; i < properties.length; i++) {
            try {
                Field f = c.getDeclaredField(properties[i]);
                f.setAccessible(true);

                Object value = f.get(cls);
                if (value == null || (value instanceof String && ((String) value).isEmpty()) || (value instanceof Collection && ((Collection) value).isEmpty())) {
                    throw new RmCommonException(errorType, serviceStatusCodeList.get(i), properties[i] + " must not be NULL !!");
                }
            } catch (IllegalAccessException | NoSuchFieldException e) {
                throw new RmCommonException(ErrorType.ERROR_SYSTEM, ERROR_NO_PARAM, e.getMessage());
            }
        }
    }

    // 객체 안의 String 타입 trim() 처리
    public static void trimStringFields(Object object) {
        Field[] fields = object.getClass().getDeclaredFields();

        for (Field field : fields) {
            if (field.getType().equals(String.class)) {
                try {
                    field.setAccessible(true);
                    String value = (String) field.get(object);
                    if (value != null && value.trim().length() != value.length()) {
                        field.set(object, value.trim());
                    }
                } catch (IllegalAccessException e) {
                    throw new RmCommonException(ErrorType.ERROR_SYSTEM, ERROR_TRIM, "Error during trim");
                }
            }
        }
    }

    // 객체 안의 String 타입 trim() 처리
    public static String cleanSearchUtil(String keyword) {
        if (null != keyword) {
            keyword = keyword.trim();
            keyword = keyword.replaceAll("_", "#_");
            keyword = keyword.replaceAll("%", "#%");
        }
        return keyword;
    }

    // 한글, 영문, 숫자, 특수문자만 허용하는 정규식
    public static void patternRegex(String keyword) {

        //  영문 연속 자음 또는 모음 정규식 체크
        if ((Pattern.matches("^[aeiouAEIOU]+$", keyword) || Pattern.matches("^[b-df-hj-np-tv-zB-DF-HJ-NP-TV-Z]+$", keyword))) {
            throw new RmCommonException(ERROR_COMMON, ERROR_VALIDATE_PATTERN_REGEX, "Error Emojis or Consecutive Characters !!");
        }

        // 이모지 및 한글 자음 또는 모음 연속 제외, 한글, 영문 소문자, 대문자, 숫자, 특수 문자만 허용
        if (!(Pattern.matches("^(?!.*[ㄱ-ㅎㅏ-ㅣ]).*[\\p{IsHangul}\\p{IsLatin}\\p{Digit}\\p{Punct}]+$", keyword))) {
            throw new RmCommonException(ERROR_COMMON, ERROR_VALIDATE_PATTERN_REGEX, "Error Emojis or Consecutive Characters !!");
        }
    }
}
