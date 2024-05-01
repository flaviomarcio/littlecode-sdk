package com.littlecode.parsers;

import com.littlecode.config.CorePublicConsts;
import lombok.Getter;

import java.io.File;
import java.net.URI;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.UUID;

public class ConverterUtil {
    @Getter
    private Object target;

    public ConverterUtil(Object target){
//        if(target == null)
//            throw new NullPointerException("target is null");
        this.target = target;
    }

    public ConverterUtil(String target){
//        if(target == null)
//            throw new NullPointerException("target is null");
        this.target = target;
    }

    public ConverterUtil(Integer target){
//        if(target == null)
//            throw new NullPointerException("target is null");
        this.target = target;
    }

    public ConverterUtil(Long target){
//        if(target == null)
//            throw new NullPointerException("target is null");
        this.target = target;
    }

    public ConverterUtil(Double target){
//        if(target == null)
//            throw new NullPointerException("target is null");
        this.target = target;
    }

    public ConverterUtil(LocalDate target){
//        if(target == null)
//            throw new NullPointerException("target is null");
        this.target = target;
    }

    public ConverterUtil(LocalTime target){
//        if(target == null)
//            throw new NullPointerException("target is null");
        this.target = target;
    }

    public ConverterUtil(LocalDateTime target){
//        if(target == null)
//            throw new NullPointerException("target is null");
        this.target = target;
    }

    public ConverterUtil(UUID target){
//        if(target == null)
//            throw new NullPointerException("target is null");
        this.target = target;
    }

    public ConverterUtil(URI target){
//        if(target == null)
//            throw new NullPointerException("target is null");
        this.target = target;
    }

    public ConverterUtil(Path target){
//        if(target == null)
//            throw new NullPointerException("target is null");
        this.target = target;
    }

    public ConverterUtil(Boolean target){
//        if(target == null)
//            throw new NullPointerException("target is null");
        this.target = target;
    }

    public ConverterUtil(File target){
//        if(target == null)
//            throw new NullPointerException("target is null");
        this.target = target;
    }

    public static ConverterUtil of(Object target) {
        return new ConverterUtil(target);
    }

    public static ConverterUtil of(String target) {
        return new ConverterUtil(target);
    }

    public static ConverterUtil of(Integer target) {
        return new ConverterUtil(target);
    }

    public static ConverterUtil of(Long target) {
        return new ConverterUtil(target);
    }

    public static ConverterUtil of(Double target) {
        return new ConverterUtil(target);
    }

    public static ConverterUtil of(LocalDate target) {
        return new ConverterUtil(target);
    }

    public static ConverterUtil of(LocalTime target) {
        return new ConverterUtil(target);
    }

    public static ConverterUtil of(LocalDateTime target) {
        return new ConverterUtil(target);
    }

    public static ConverterUtil of(UUID target) {
        return new ConverterUtil(target);
    }

    public static ConverterUtil of(URI target) {
        return new ConverterUtil(target);
    }

    public static ConverterUtil of(Path target) {
        return new ConverterUtil(target);
    }

    public static ConverterUtil of(File target) {
        return new ConverterUtil(target);
    }

    public String toString(){
        if(target instanceof String string)
            return string;
        else if(target instanceof Integer integer)
            return integer.toString();
        else if(target instanceof Double aDouble)
            return aDouble.toString();
        else if(target instanceof Long aLong)
            return aLong.toString();
        else if(target instanceof LocalDate localDate)
            return localDate.toString();
        else if(target instanceof LocalTime localTime)
            return localTime.toString();
        else if(target instanceof LocalDateTime localDateTime)
            return localDateTime.toString();
        else if(target instanceof UUID uuid)
            return uuid.toString();
        else if(target instanceof URI uri)
            return uri.toString();
        else if(target instanceof File file)
            return file.getAbsolutePath();
        else if(target instanceof Path path)
            return path.toString();
        else if(this.target!=null)
            return target.toString();
        else
            return "";
    }

    public int toInt(){
        if(target instanceof Integer integer)
            return integer;
        else if(target instanceof Double aDouble)
            return aDouble.intValue();
        else if(target instanceof Long aLong)
            return aLong.intValue();
        else if(target instanceof String string)
            return PrimitiveUtil.toInt(string);
        else if(target instanceof LocalDate localDate)
            return (int)localDate.toEpochDay();
        else if(target instanceof LocalTime localTime)
            return (int)localTime.toEpochSecond(LocalDate.now(), ZoneOffset.UTC);
        else if(target instanceof LocalDateTime localDateTime)
            return (int)localDateTime.toEpochSecond(ZoneOffset.UTC);
        else
            return 0;
    }

    public Long toLong(){
        if(target instanceof Long aLong)
            return aLong;
        else if(target instanceof Double aDouble)
            return aDouble.longValue();
        else if(target instanceof Integer integer)
            return integer.longValue();
        else if(target instanceof String string)
            return PrimitiveUtil.toLong(string);
        else if(target instanceof LocalDate localDate)
            return localDate.toEpochDay();
        else if(target instanceof LocalTime localTime)
            return localTime.toEpochSecond(LocalDate.now(), ZoneOffset.UTC);
        else if(target instanceof LocalDateTime localDateTime)
            return localDateTime.toEpochSecond(ZoneOffset.UTC);
        else
            return 0L;
    }

    public Double toDouble(){
        if(target instanceof Double aDouble)
            return aDouble;
        else if(target instanceof Integer integer)
            return integer.doubleValue();
        else if(target instanceof Long aLong)
            return aLong.doubleValue();
        else if(target instanceof String string)
            return PrimitiveUtil.toDouble(string);
        else if(target instanceof LocalDate localDate)
            return (double)localDate.toEpochDay();
        else if(target instanceof LocalTime localTime)
            return (double)localTime.toEpochSecond(LocalDate.now(), ZoneOffset.UTC);
        else if(target instanceof LocalDateTime localDateTime)
            return (double)localDateTime.toEpochSecond(ZoneOffset.UTC);
        else
            return 0D;
    }

    public LocalDate toLocalDate(){
        if(target instanceof LocalDate localDate)
            return localDate;
        else if(target instanceof LocalDateTime localDateTime)
            return localDateTime.toLocalDate();
        else if(target instanceof String string)
            return PrimitiveUtil.toDate(string);
        else if(target instanceof Integer integer)
            return LocalDate.ofEpochDay(integer);
        else if(target instanceof Long aLong)
            return LocalDate.ofEpochDay(aLong);
        else if(target instanceof Double aDouble)
            return LocalDate.ofEpochDay(aDouble.longValue());
        else
            return null;
    }

    public LocalTime toLocalTime(){
        if(target instanceof LocalTime localDate)
            return localDate;
        else if(target instanceof LocalDateTime localDateTime)
            return localDateTime.toLocalTime();
        else if(target instanceof String string)
            return PrimitiveUtil.toTime(string);
        else if(target instanceof Integer integer)
            return LocalTime.ofSecondOfDay(integer);
        else if(target instanceof Long aLong)
            return LocalTime.ofSecondOfDay(aLong.intValue());
        else if(target instanceof Double aDouble)
            return LocalTime.ofSecondOfDay(aDouble.intValue());
        else
            return null;
    }

    public LocalDateTime toLocalDateTime(){
        if(target instanceof LocalDateTime localDateTime)
            return localDateTime;
        else if(target instanceof LocalDate localDate)
            return LocalDateTime.of(localDate, CorePublicConsts.MIN_LOCALTIME);
        else if(target instanceof String string)
            return PrimitiveUtil.toDateTime(string);
        else if(target instanceof LocalTime localTime )
            return LocalDateTime.of(CorePublicConsts.MIN_LOCALDATE,localTime);
        else if(target instanceof Integer integer)
            return LocalDateTime.ofEpochSecond(integer.longValue(),0,ZoneOffset.UTC);
        else if(target instanceof Long aLong)
            return LocalDateTime.ofEpochSecond(aLong,0,ZoneOffset.UTC);
        else if(target instanceof Double aDouble)
            return LocalDateTime.ofEpochSecond(aDouble.longValue(),0,ZoneOffset.UTC);
        else
            return null;
    }

    public UUID toUUID(){
        if(target instanceof String string)
            return PrimitiveUtil.toUUID(string);
        else if(target instanceof UUID uuid)
            return uuid;
        else
            return null;
    }

    public URI toURI(){
        if(target instanceof URI uri)
            return uri;
        else if(target instanceof Path path)
            return path.toUri();
        else if(target instanceof File file)
            return file.toURI();
        else if(target instanceof String string)
            return URI.create(string);
        else
            return null;
    }

    public Path toPath(){
        if(target instanceof Path path)
            return path;
        else if(target instanceof URI uri)
            return Path.of(uri.toString());
        else if(target instanceof File file)
            return file.toPath();
        else if(target instanceof String string)
            return Path.of(string);
        else
            return null;
    }

    public boolean toBool(){
        if(target instanceof Boolean aBoolean)
            return aBoolean;
        else if(target instanceof String string)
            return PrimitiveUtil.toBool(string);
        else if(target instanceof Integer integer)
            return PrimitiveUtil.toBool(integer);
        else if(target instanceof Long longValue)
            return PrimitiveUtil.toBool(longValue);
        else if(target instanceof Double aDouble)
            return PrimitiveUtil.toBool(aDouble);
        else
            return false;
    }
}
