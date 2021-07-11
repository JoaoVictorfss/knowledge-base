package br.com.knowledgeBase.api.knowledgebaseapi.utils;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;

public class BindResultUtils {
    public BindResultUtils() {};

    public static void bindErrorMessage(BindingResult result, String name, String defaultMessage) {
        result.addError(new ObjectError(name, defaultMessage));
    }

    public static List<String> getAllErrorMessages(BindingResult result) {
        List<String> errors = new ArrayList<>();
        result.getAllErrors().forEach(error -> errors.add(error.getDefaultMessage()));
        return  errors;
    }
}
