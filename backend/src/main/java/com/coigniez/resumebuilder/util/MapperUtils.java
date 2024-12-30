package com.coigniez.resumebuilder.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

public class MapperUtils {

    /**
     * Set default values for fields in a request object.
     *
     * @param request       the request object
     * @param defaultValues a map of field names and their default values
     * @param <T>           the type of the request object
     */
    public static <T> void setDefaultValues(T request, Map<String, Object> defaultValues) {
        defaultValues.forEach((key, value) -> {
            try {
                String capitalizedKey = key.substring(0, 1).toUpperCase() + key.substring(1);
                Method getter = request.getClass().getMethod("get" + capitalizedKey);
                Class<?> parameterType = value instanceof Set ? Set.class : value.getClass();
                Method setter = request.getClass().getMethod("set" + capitalizedKey, parameterType);
                if (getter.invoke(request) == null) {
                    setter.invoke(request, value);
                }
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException("Error setting default values", e);
            }
        });
    }
}
