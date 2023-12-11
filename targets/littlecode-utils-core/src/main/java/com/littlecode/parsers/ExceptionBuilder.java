package com.littlecode.parsers;

import com.littlecode.exceptions.ArithmeticException;
import com.littlecode.exceptions.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class ExceptionBuilder {

    private Type type;
    private Object target;
    private Object[] args;

    @SuppressWarnings("unused")
    public ExceptionBuilder() {
        this.type = Type.Default;
    }

    private static Object[] argsClean(final Object[] args) {
        if (args == null || args.length == 0) {
            return new Object[0];
        }
        List<Object> list = new ArrayList<>();
        List.of(args)
                .forEach(o -> {
                    if (o == null || o.toString().trim().isEmpty())
                        return;
                    list.add(o);
                });
        return list.isEmpty() ? new Object[0] : list.toArray();
    }

    private static String targetClean(Object target) {
        if (target == null)
            return "";
        return target.toString().trim();
    }

    public static String makeMessage(Type typeIn, Object targetIn, Object... argsIn) {
        var args = argsClean(argsIn);
        var target = targetClean(targetIn);
        var type = typeIn.name();

        if (target.isEmpty() && args.length == 0)
            return String.format("[%s]: fail", type);

        if (target.contains("%s") && args.length > 0)
            return String.format("[%s]: %s", type, String.format(target, args));

        if (!target.isEmpty() && args.length > 0)
            return String.format("[%s]: %s, %s", type, target, Arrays.toString(args));

        return String.format("[%s]: %s", type, target);
    }

    public static RuntimeException of(Exception e) {
        return new FrameworkException(e);
    }

    public static RuntimeException of(Object target) {
        return ExceptionBuilder.builder().type(Type.Default).target(target).build().create();
    }

    public static RuntimeException of(Type type, Object target) {
        return ExceptionBuilder.builder().type(type).target(target).build().create();
    }

    public static RuntimeException of(Type type, Object target, String message) {
        return ExceptionBuilder.builder().type(type).target(target).args(new String[]{message}).build().create();
    }

    public static RuntimeException of(Type type, String format, Object... args) {
        return ExceptionBuilder.builder().type(type).target(format).args(args).build().create();
    }

    public static RuntimeException ofDefault(Object target) {
        return of(Type.Default, target);
    }
    public static RuntimeException ofArithmetical(String message) {
        return of(Type.Arithmetical, message);
    }
    public static RuntimeException ofBadRequest(String message) {
        return of(Type.BadRequest, message);
    }
    public static RuntimeException ofConflict(String message) {
        return of(Type.Conflict, message);
    }
    public static RuntimeException ofConversion(String message) {
        return of(Type.Conversion, message);
    }
    public static RuntimeException ofObject(String message) {
        return of(Type.Object, message);
    }
    public static RuntimeException ofSetting(String message) {
        return of(Type.Setting, message);
    }
    public static RuntimeException ofNotFound(String message) {
        return of(Type.NotFound, message);
    }
    public static RuntimeException ofParser(String message) {
        return of(Type.Parser, message);
    }
    public static RuntimeException ofInvalid(String message) {
        return of(Type.Invalid, message);
    }
    public static RuntimeException ofUnknown(String message) {
        return of(Type.Unknown, message);
    }
    public static RuntimeException ofNoImplemented(String message) {
        return of(Type.NoImplemented, message);
    }
    public static RuntimeException ofFrameWork(String message) {
        return of(Type.FrameWork, message);
    }
    public static RuntimeException ofUnAuthorization(String message) {
        return of(Type.UnAuthorization, message);
    }
    public static RuntimeException ofNetwork(String message) {
        return of(Type.Network, message);
    }

    //Class<?>
    public static RuntimeException ofArithmetical(Class<?> eClass) {
        return of(Type.Arithmetical, eClass);
    }

    public static RuntimeException ofBadRequest(Class<?> eClass) {
        return of(Type.BadRequest, eClass);
    }

    public static RuntimeException ofConflict(Class<?> eClass) {
        return of(Type.Conflict, eClass);
    }

    public static RuntimeException ofConversion(Class<?> eClass) {
        return of(Type.Conversion, eClass);
    }

    public static RuntimeException ofObject(Class<?> eClass) {
        return of(Type.Object, eClass);
    }

    public static RuntimeException ofSetting(Class<?> eClass) {
        return of(Type.Setting, eClass);
    }

    public static RuntimeException ofNotFound(Class<?> eClass) {
        return of(Type.NotFound, eClass);
    }

    public static RuntimeException ofParser(Class<?> eClass) {
        return of(Type.Parser, eClass);
    }

    public static RuntimeException ofInvalid(Class<?> eClass) {
        return of(Type.Invalid, eClass);
    }
    public static RuntimeException ofUnknown(Class<?> eClass) {
        return of(Type.Unknown, eClass);
    }
    public static RuntimeException ofNoImplemented(Class<?> eClass) {
        return of(Type.NoImplemented, eClass);
    }
    public static RuntimeException ofFrameWork(Class<?> eClass) {
        return of(Type.FrameWork, eClass);
    }
    public static RuntimeException ofUnAuthorization(Class<?> eClass) {
        return of(Type.UnAuthorization, eClass);
    }
    public static RuntimeException ofNetwork(Class<?> eClass) {
        return of(Type.Network, eClass);
    }

    //Class<?>,message
    public static RuntimeException ofDefault(Class<?> eClass, String message) {
        return of(Type.Default, eClass, message);
    }

    public static RuntimeException ofArithmetical(Class<?> eClass, String message) {
        return of(Type.Arithmetical, eClass, message);
    }

    public static RuntimeException ofBadRequest(Class<?> eClass, String message) {
        return of(Type.BadRequest, eClass, message);
    }

    public static RuntimeException ofConflict(Class<?> eClass, String message) {
        return of(Type.Conflict, eClass, message);
    }

    public static RuntimeException ofConversion(Class<?> eClass, String message) {
        return of(Type.Conversion, eClass, message);
    }

    public static RuntimeException ofObject(Class<?> eClass, String message) {
        return of(Type.Object, eClass, message);
    }

    public static RuntimeException ofSetting(Class<?> eClass, String message) {
        return of(Type.Setting, eClass, message);
    }

    public static RuntimeException ofNotFound(Class<?> eClass, String message) {
        return of(Type.NotFound, eClass, message);
    }

    public static RuntimeException ofParser(Class<?> eClass, String message) {
        return of(Type.Parser, eClass, message);
    }

    public static RuntimeException ofInvalid(Class<?> eClass, String message) {
        return of(Type.Invalid, eClass, message);
    }
    public static RuntimeException ofUnknown(Class<?> eClass, String message) {
        return of(Type.Unknown, eClass, message);
    }
    public static RuntimeException ofNoImplemented(Class<?> eClass, String message) {
        return of(Type.NoImplemented, eClass, message);
    }
    public static RuntimeException ofFrameWork(Class<?> eClass, String message) {
        return of(Type.FrameWork, eClass, message);
    }
    public static RuntimeException ofUnAuthorization(Class<?> eClass, String message) {
        return of(Type.UnAuthorization, eClass, message);
    }
    public static RuntimeException ofNetwork(Class<?> eClass, String message) {
        return of(Type.Network, eClass, message);
    }

    //format
    public static RuntimeException ofDefault(String format, Object... args) {
        return of(Type.Default, format, args);
    }
    public static RuntimeException ofArithmetical(String format, Object... args) {
        return of(Type.Arithmetical, format, args);
    }
    public static RuntimeException ofBadRequest(String format, Object... args) {
        return of(Type.BadRequest, format, args);
    }
    public static RuntimeException ofConflict(String format, Object... args) {
        return of(Type.Conflict, format, args);
    }
    public static RuntimeException ofConversion(String format, Object... args) {
        return of(Type.Conversion, format, args);
    }
    public static RuntimeException ofObject(String format, Object... args) {
        return of(Type.Object, format, args);
    }

    public static RuntimeException ofSetting(String format, Object... args) {
        return of(Type.Setting, format, args);
    }

    public static RuntimeException ofNotFound(String format, Object... args) {
        return of(Type.NotFound, format, args);
    }

    public static RuntimeException ofParser(String format, Object... args) {
        return of(Type.Parser, format, args);
    }

    public static RuntimeException ofInvalid(String format, Object... args) {
        return of(Type.Invalid, format, args);
    }
    public static RuntimeException ofUnknown(String format, Object... args) {
        return of(Type.Unknown, format, args);
    }
    public static RuntimeException ofNoImplemented(String format, Object... args) {
        return of(Type.NoImplemented, format, args);
    }
    public static RuntimeException ofFrameWork(String format, Object... args) {
        return of(Type.FrameWork, format, args);
    }
    public static RuntimeException ofUnAuthorization(String format, Object... args) {
        return of(Type.UnAuthorization, format, args);
    }
    public static RuntimeException ofNetwork(String format, Object... args) {
        return of(Type.Network, format, args);
    }

    void setArgs(Object[] args) {
        this.args = args;
    }

    @SuppressWarnings("unused")
    void setArgs(List<Object> args) {
        setArgs(args.toArray());
    }

    private String makeMessage() {
        return makeMessage(this.type, this.target, this.args);
    }

    public RuntimeException create() {
        var message = makeMessage();
        return switch (this.type){
            case Invalid -> new InvalidException(message);
            case Unknown -> new UnknownException(message);
            case FrameWork -> new FrameworkException(message);
            case Arithmetical ->  new ArithmeticException(message);
            case BadRequest -> new BadRequestException(message);
            case Conflict -> new ConflictException(message);
            case Conversion -> new ConversionException(message);
            case NoImplemented -> new NoImplementedException(message);
            case NotFound -> new NotFoundException(message);
            case Object -> new InvalidObjectException(message);
            case Parser -> new ParserException(message);
            case Setting -> new InvalidSettingException(message);
            case UnAuthorization -> new UnAuthorizationException(message);
            case Network -> new NetworkException(message);
            default -> new RuntimeException(message);
        };
    }

    public enum Type {
        Default,
        Invalid,
        Unknown,
        Arithmetical,
        BadRequest,
        Conflict,
        Conversion,
        FrameWork,
        NoImplemented,
        NotFound,
        Object,
        Parser,
        Setting,
        UnAuthorization,
        Network
    }

}
