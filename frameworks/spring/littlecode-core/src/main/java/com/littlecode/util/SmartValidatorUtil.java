package com.littlecode.util;

import com.littlecode.parsers.ObjectUtil;
import com.littlecode.parsers.PrimitiveUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.SmartValidator;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class SmartValidatorUtil {
    private final SmartValidator validator;
    private final List<Object> listObjects = new ArrayList<>();
    public List<String> check(Object o) {
        if(o==null)
            throw new NullPointerException("Invalid object");
        listObjects.add(o);
        var errors = new BeanPropertyBindingResult(o, "root");
        validator.validate(o, errors);
        List<String> errors__return = new ArrayList<>();
        errors
                .getFieldErrors()
                .forEach(error ->
                    errors__return.add("{\"property\":\"%s\",\"message\":\"%s\"}".formatted(error.getField(), error.getDefaultMessage()))
                );
        for(var field: o.getClass().getDeclaredFields()){
            field.setAccessible(true);
            try {
                var fieldValue=field.get(o);
                if(fieldValue!=null){
                    if(!PrimitiveUtil.isPrimitiveValue(fieldValue)){
                        if(!listObjects.contains(fieldValue)){
                            var errors__field=this.check(fieldValue);
                            errors__return.addAll(errors__field);
                        }
                    }
                }
            } catch (Exception ignored) {
            }
        }
        return errors__return;
    }
}
