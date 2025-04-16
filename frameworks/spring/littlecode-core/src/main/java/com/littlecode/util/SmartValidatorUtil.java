package com.littlecode.util;

import com.littlecode.parsers.PrimitiveUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.SmartValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
public class SmartValidatorUtil {
    private final SmartValidator validator;
    private final List<Object> listObjects = new ArrayList<>();
    public List<Map<String,String>> check(Object o) {
        return internalCheck("root",o);
    }

    private List<Map<String,String>> internalCheck(String rootName, Object o) {
        if(o==null)
            throw new NullPointerException("Invalid object");
        listObjects.add(o);
        var errors = new BeanPropertyBindingResult(o, rootName);
        validator.validate(o, errors);
        List<Map<String,String>> errors__return = new ArrayList<>();
        errors
                .getFieldErrors()
                .forEach(error ->{
                    errors__return.add(Map.of("object",rootName,"attribute",error.getField(), "message", Objects.requireNonNull(error.getDefaultMessage())));
                });
        for(var field: o.getClass().getDeclaredFields()){
            field.setAccessible(true);
            try {
                var fieldValue=field.get(o);
                if(fieldValue!=null){
                    if(!PrimitiveUtil.isPrimitiveValue(fieldValue)){
                        if(!listObjects.contains(fieldValue)){
                            var errors__field=this.internalCheck("%s.%s".formatted(rootName,field.getName()),fieldValue);
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
