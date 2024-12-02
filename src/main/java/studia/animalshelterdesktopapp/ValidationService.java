package studia.animalshelterdesktopapp;

import java.lang.reflect.Field;

public class ValidationService {
    public static boolean validate(Object obj) throws IllegalAccessException {
        Class<?> clazz = obj.getClass();

        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(ValidateNotEmpty.class)) {
                field.setAccessible(true);
                Object value = field.get(obj);

                ValidateNotEmpty annotation = field.getAnnotation(ValidateNotEmpty.class);

                if (value == null || (value instanceof String && ((String) value).isEmpty())) {
                    return false;
                }

                if (value instanceof Integer && (Integer) value <= 0) {
                    return false;
                }
            }
        }
        return true;
    }
}