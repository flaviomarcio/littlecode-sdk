package com.littlecode.business.util;

import com.littlecode.parsers.ObjectUtil;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Builder
public class ModelUtil {
    private Object target;

    public Object target(){
        return this.target;
    }

    public static ModelUtil target(Object target){
        return ModelUtil
                .builder()
                .target(target)
                .build();
    }

    public boolean isValid(){
        return isValid(this.target);
    }


    private static boolean isValidField(Object target,Field field){



        Object fieldValue=null;
        try {
            field.setAccessible(true);
            fieldValue=field.get(target);
        } catch (IllegalAccessException ignored) {}

        boolean checkNull=false;
        boolean checkLen=false;
        int length=0;

        if(field.isAnnotationPresent(Column.class)){
            var annotation=field.getAnnotation(Column.class);
            if(annotation!=null){
                checkNull=!annotation.nullable();
                checkLen=annotation.length()>0;
                if(checkNull && fieldValue==null){
                    log.error("object:[{}], field: [{}], annotation:[{}], is null ", target.getClass().getName(), field.getName(), annotation.getClass().getName());
                    return false;
                }
                if(fieldValue!=null && annotation.length()>0 && field.getType().equals(String.class) ){
                    length=annotation.length();
                }
            }
        }

        if(field.isAnnotationPresent(JoinColumn.class))
            checkNull=checkNull || !field.getAnnotation(JoinColumn.class).nullable();

        if(field.isAnnotationPresent(Id.class)){
            var annotation=field.getAnnotation(Id.class);
            if(annotation!=null && !field.isAnnotationPresent(Column.class) && !field.isAnnotationPresent(JoinColumn.class))
                checkNull=true;
        }

        if(checkNull && fieldValue==null){
            log.error("object:[{}], field: [{}], message: \"field value is null\" ", target.getClass().getName(), field.getName());
            return false;
        }

        if(checkLen && field.getType().equals(String.class)){
            var strValue=((String)fieldValue);
            if(strValue!=null && strValue.length()>length){
                log.error("object:[{}], field: [{}], maximum:[{}], current:[{}], message:\"exceeded length\" ",target.getClass().getName(), field.getName(), length,((String) fieldValue).length());
                return false;
            }
        }

        return true;
    }

    public static boolean isValid(Object target){
        if(target==null)
            return false;

        if(!target.getClass().isAnnotationPresent(Table.class) && !target.getClass().isAnnotationPresent(Entity.class))
            return false;

        var listID=ObjectUtil.toFieldsByAnnotation(target.getClass(), Id.class);
        var listColumn=ObjectUtil.toFieldsByAnnotation(target.getClass(), Column.class);
        var listJoinColumn=ObjectUtil.toFieldsByAnnotation(target.getClass(), JoinColumn.class);

        if(listID.isEmpty() && listColumn.isEmpty() && listJoinColumn.isEmpty())
            return false;

        Map<String, Field> fields=new HashMap<>();

        List
                .of(listID,listColumn,listJoinColumn)
                .forEach(list->{
                    list.forEach(field ->
                        fields.put(field.getName(),field)
                    );
                });

        for(var field: fields.values())
            if(!isValidField(target, field))
                return false;
        return true;
    }
}
