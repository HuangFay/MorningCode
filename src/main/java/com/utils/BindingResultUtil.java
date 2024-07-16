package com.utils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Iterator;

public class BindingResultUtil {

    public static void removeFieldErrors(BindingResult bindingResult, String fieldName) {
        Iterator<FieldError> iterator = bindingResult.getFieldErrors().iterator();
        while (iterator.hasNext()) {
            FieldError error = iterator.next();
            if (error.getField().equals(fieldName)) {
                iterator.remove();
            }
        }
    }
}
