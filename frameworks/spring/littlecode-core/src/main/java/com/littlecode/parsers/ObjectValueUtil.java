package com.littlecode.parsers;

import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.net.URI;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Slf4j
@Getter
public class ObjectValueUtil {
    private Object target;
    private final List<String> fieldNames=new ArrayList<>();
    private final List<Field> fieldList=new ArrayList<>();
    private final Map<String,Field> fieldMap=new HashMap<>();

    public ObjectValueUtil(Object target){
        if(target == null)
            throw new NullPointerException("Target is null");
        this.target=target;
        this.refresh();
    }

    public void setTarget(Object target){
        if(target == null)
            throw new NullPointerException("Target is null");
        this.target=target;
        this.refresh();
    }

    public void refresh(){
        this.fieldList.clear();
        this.fieldMap.clear();
        this.fieldNames.clear();
        for(Field field : ObjectUtil.toFieldsList(target.getClass())){
            this.fieldMap.put(field.getName().toLowerCase(), field);
            this.fieldList.add(field);
            this.fieldNames.add(field.getName());
        }
    }

    public static ObjectValueUtil of(Object target){
        return new ObjectValueUtil(target);
    }

    public Map<String,Object> asMap(){
        Map<String,Object> __return=new HashMap<>();
        for(Field field : this.fieldList){
            field.setAccessible(true);
            try {
                var value=field.get(this.target);
                if(value!=null)
                    __return.put(field.getName(), value);
            } catch (Exception ignored) {
            }
        }
        return __return;
    }

    public Map<String,String> asMapString(){
        Map<String,String> __return=new HashMap<>();
        for(Field field : this.fieldList){
            field.setAccessible(true);
            try {
                __return.put(field.getName(), PrimitiveUtil.toString(field.get(this.target)));
            } catch (Exception ignored) {
            }
        }
        return __return;
    }

    public boolean contains(String name){
        return this.fieldMap.containsKey(name.toLowerCase());
    }

    public boolean contains(Field field){
        return this.fieldMap.containsValue(field);
    }

    public Field getField(String name){
        if(name==null)
            throw new NullPointerException("invalid fieldName");
        return this.fieldMap.get(name.trim().toLowerCase());
    }

    public Object getFieldValue(Field field){
        if(field==null)
            throw new NullPointerException("invalid field");
        try {
            field.setAccessible(true);
            return field.get(this.target);
        } catch (Exception ignored) {
        }
        return null;
    }

    public Object getFieldValue(String fieldName){
        return this.getFieldValue(this.getField(fieldName));
    }

    public String toString(){
        return ObjectUtil.toString(this.target);
    }

    public Object asValue(Field field){
        return this.getFieldValue(field);
    }

    public Object asValue(String fieldName){
        return this.getFieldValue(fieldName);
    }

    public String asString(Field field){
        return ConverterUtil.of(this.getFieldValue(field)).toString();
    }

    public String asString(String fieldName){
        return ConverterUtil.of(this.getFieldValue(fieldName)).toString();
    }

    public Integer asInt(Field field){
        return ConverterUtil.of(this.getFieldValue(field)).toInt();
    }

    public Integer asInt(String fieldName){
        return ConverterUtil.of(this.getFieldValue(fieldName)).toInt();
    }

    public Long asLong(Field field){
        return ConverterUtil.of(this.getFieldValue(field)).toLong();
    }

    public Long asLong(String fieldName){
        return ConverterUtil.of(this.getFieldValue(fieldName)).toLong();
    }

    public Double asDouble(Field field){
        return ConverterUtil.of(this.getFieldValue(field)).toDouble();
    }

    public Double asDouble(String fieldName){
        return ConverterUtil.of(this.getFieldValue(fieldName)).toDouble();
    }

    public LocalDate asLocalDate(Field field){
        return ConverterUtil.of(this.getFieldValue(field)).toLocalDate();
    }

    public LocalDate asLocalDate(String fieldName){
        return ConverterUtil.of(this.getFieldValue(fieldName)).toLocalDate();
    }

    public LocalTime asLocalTime(Field field){
        return ConverterUtil.of(this.getFieldValue(field)).toLocalTime();
    }

    public LocalTime asLocalTime(String fieldName){
        return ConverterUtil.of(this.getFieldValue(fieldName)).toLocalTime();
    }

    public LocalDateTime asLocalDateTime(Field field){
        return ConverterUtil.of(this.getFieldValue(field)).toLocalDateTime();
    }

    public LocalDateTime asLocalDateTime(String fieldName){
        return ConverterUtil.of(this.getFieldValue(fieldName)).toLocalDateTime();
    }

    public UUID asUUID(Field field){
        return ConverterUtil.of(this.getFieldValue(field)).toUUID();
    }

    public UUID asUUID(String fieldName){
        return ConverterUtil.of(this.getFieldValue(fieldName)).toUUID();
    }

    public URI asURI(Field field){
        return ConverterUtil.of(this.getFieldValue(field)).toURI();
    }

    public URI asURI(String fieldName){
        return ConverterUtil.of(this.getFieldValue(fieldName)).toURI();
    }

    public Path asPath(Field field){
        return ConverterUtil.of(this.getFieldValue(field)).toPath();
    }

    public Path asPath(String fieldName){
        return ConverterUtil.of(this.getFieldValue(fieldName)).toPath();
    }

}
