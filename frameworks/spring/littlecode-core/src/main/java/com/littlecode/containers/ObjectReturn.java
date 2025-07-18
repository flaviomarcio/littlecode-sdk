package com.littlecode.containers;

import com.littlecode.parsers.HashUtil;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.*;

@Slf4j
public class ObjectReturn {
    private static final String INVALID_OBJECT_OF_TYPE = "Invalid object of type: [%s]";
    private static final String CODE_NAME = "code";
    private static final String ERRORS_NAME = "erros";
    private static final String BODY_NAME = "body";
    private static final String UNKNOWN_ERROR = "Unknown error";
    private final List<Message> internalMessages = new ArrayList<>();
    private final MessageMaker message = new MessageMaker(this);
    @Getter
    private Object body;

    public ObjectReturn() {
    }

    public ObjectReturn(ObjectReturn objectReturn) {
        if (objectReturn == null)
            return;
        this.body = objectReturn.getBody();
        for (var m : objectReturn.internalMessages)
            this.message
                    .clear()
                    .type(m.type)
                    .code(m.code)
                    .message(m.message)
                    .end();
    }

    public static MessageMaker create() {
        return new ObjectReturn().m();
    }

    public static ObjectReturn Empty() {
        return new ObjectReturn();
    }

    public static ObjectReturn Success() {
        return Success((String) null);
    }

    public static ObjectReturn Success(Class<?> aClass) {
        return Success(INVALID_OBJECT_OF_TYPE, aClass);
    }

    public static ObjectReturn Success(String message) {
        return Success(message, (Object) null);
    }

    public static ObjectReturn Success(String format, Object... args) {
        return type(Type.Success, format, args);
    }

    public static ObjectReturn OK() {
        return Success((String) null);
    }

    public static ObjectReturn OK(Class<?> aClass) {
        return Success(INVALID_OBJECT_OF_TYPE, aClass);
    }

    public static ObjectReturn OK(String message) {
        return Success(message, (Object) null);
    }

    public static ObjectReturn OK(String format, Object... args) {
        return type(Type.Success, format, args);
    }

    public static ObjectReturn Created() {
        return Created((String) null);
    }

    public static ObjectReturn Created(Class<?> aClass) {
        return Created(INVALID_OBJECT_OF_TYPE, aClass);
    }

    public static ObjectReturn Created(String message) {
        return Created(message, (Object) null);
    }

    public static ObjectReturn Created(String format, Object... args) {
        return type(Type.Created, format, args);
    }

    public static ObjectReturn Accepted() {
        return Accepted((String) null);
    }

    public static ObjectReturn Accepted(Class<?> aClass) {
        return Accepted(INVALID_OBJECT_OF_TYPE, aClass);
    }

    public static ObjectReturn Accepted(String message) {
        return Accepted(message, (Object) null);
    }

    public static ObjectReturn Accepted(String format, Object... args) {
        return type(Type.Accepted, format, args);
    }

    public static ObjectReturn NoContent() {
        return NoContent((String) null);
    }

    public static ObjectReturn NoContent(Class<?> aClass) {
        return NoContent(INVALID_OBJECT_OF_TYPE, aClass);
    }

    public static ObjectReturn NoContent(String message) {
        return NoContent(message, (Object) null);
    }

    public static ObjectReturn NoContent(String format, Object... args) {
        return type(Type.NoContent, format, args);
    }

    public static ObjectReturn BadRequest() {
        return BadRequest((String) null);
    }

    public static ObjectReturn BadRequest(Class<?> aClass) {
        return BadRequest(INVALID_OBJECT_OF_TYPE, aClass);
    }

    public static ObjectReturn BadRequest(String message) {
        return BadRequest(message, (Object) null);
    }

    public static ObjectReturn BadRequest(String format, Object... args) {
        return type(Type.BadRequest, format, args);
    }

    public static ObjectReturn Forbidden() {
        return Forbidden((String) null);
    }

    public static ObjectReturn Forbidden(Class<?> aClass) {
        return Forbidden(INVALID_OBJECT_OF_TYPE, aClass);
    }

    public static ObjectReturn Forbidden(String message) {
        return Forbidden(message, (Object) null);
    }

    public static ObjectReturn Forbidden(String format, Object... args) {
        return type(Type.Forbidden, format, args);
    }

    public static ObjectReturn NotFound() {
        return NotFound((String) null);
    }

    public static ObjectReturn NotFound(Class<?> aClass) {
        return NotFound(INVALID_OBJECT_OF_TYPE, aClass);
    }

    public static ObjectReturn NotFound(String message) {
        return NotFound(message, (Object) null);
    }

    public static ObjectReturn NotFound(String format, Object... args) {
        return type(Type.NotFound, format, args);
    }

    public static ObjectReturn Conflict() {
        return Conflict((String) null);
    }

    public static ObjectReturn Conflict(Class<?> aClass) {
        return Conflict(INVALID_OBJECT_OF_TYPE, aClass);
    }

    public static ObjectReturn Conflict(String message) {
        return Conflict(message, (Object) null);
    }

    public static ObjectReturn Conflict(String format, Object... args) {
        return type(Type.Conflict, format, args);
    }

    public static ObjectReturn NotAllowed() {
        return NotAllowed((String) null);
    }

    public static ObjectReturn NotAllowed(Class<?> aClass) {
        return NotAllowed(INVALID_OBJECT_OF_TYPE, aClass);
    }

    public static ObjectReturn NotAllowed(String message) {
        return NotAllowed(message, (Object) null);
    }

    public static ObjectReturn NotAllowed(String format, Object... args) {
        return type(Type.NotAllowed, format, args);
    }

    public static ObjectReturn NotAcceptable() {
        return NotAcceptable((String) null);
    }

    public static ObjectReturn NotAcceptable(Class<?> aClass) {
        return NotAcceptable(INVALID_OBJECT_OF_TYPE, aClass);
    }

    public static ObjectReturn NotAcceptable(String message) {
        return NotAcceptable(message, (Object) null);
    }

    public static ObjectReturn NotAcceptable(String format, Object... args) {
        return type(Type.NotAcceptable, format, args);
    }

    public static ObjectReturn Unauthorized() {
        return Unauthorized((String) null);
    }

    public static ObjectReturn Unauthorized(Class<?> aClass) {
        return Unauthorized(INVALID_OBJECT_OF_TYPE, aClass);
    }

    public static ObjectReturn Unauthorized(String message) {
        return Unauthorized(message, (Object) null);
    }

    public static ObjectReturn Unauthorized(String format, Object... args) {
        return type(Type.Unauthorized, format, args);
    }

    public static ObjectReturn Fail() {
        return Fail((String) null);
    }

    public static ObjectReturn Fail(Class<?> aClass) {
        return Fail(INVALID_OBJECT_OF_TYPE, aClass);
    }

    public static ObjectReturn Fail(String message) {
        return Fail(message, (Object) null);
    }

    public static ObjectReturn Fail(String format, Object... args) {
        return type(Type.Fail, format, args);
    }

    public static ObjectReturn NotImplemented() {
        return NotImplemented((String) null);
    }

    public static ObjectReturn NotImplemented(Class<?> aClass) {
        return NotImplemented(INVALID_OBJECT_OF_TYPE, aClass);
    }

    public static ObjectReturn NotImplemented(String message) {
        return NotImplemented(message, (Object) null);
    }

    public static ObjectReturn NotImplemented(String format, Object... args) {
        return type(Type.NotImplemented, format, args);
    }

    public static ObjectReturn type(Type type) {
        return type(type, (String) null);
    }

    public static ObjectReturn type(Type type, Class<?> aClass) {
        return type(type, aClass == null ? "" : aClass.toString());
    }

    public static ObjectReturn type(Type type, String message) {
        return type(type, message, (Object) null);
    }

    public static ObjectReturn type(Type type, String format, Object... args) {
        return ObjectReturn
                .create()
                .type(type, format, args)
                .end();
    }

    public static ObjectReturn of(Object body) {
        return ObjectReturn
                .create()
                .end().body(body);
    }

    public static ObjectReturn of(Exception e) {
        return ObjectReturn
                .create()
                .type(Type.Fail)
                .message(e == null ? UNKNOWN_ERROR : e.getMessage())
                .end();
    }

    public static ObjectReturn of(ObjectReturn objectReturn) {
        return new ObjectReturn(objectReturn);
    }

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj))
            return true;
        var md5A = HashUtil.toMd5Uuid(this);
        var md5B = HashUtil.toMd5Uuid(obj);
        return md5A.equals(md5B);
    }

    @Override
    public int hashCode() {
        return Objects.hash(HashUtil.toMd5Uuid(this));
    }

    public ObjectReturn body(Object body) {
        this.body = body;
        return this;
    }

    public <T> T cast(Class<T> aClass) {
        try {
            return this.body == null
                    ? null
                    : aClass.cast(this.body);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public ObjectReturn.Type getType() {
        if (!internalMessages.isEmpty()) {
            for (var e : internalMessages) {
                if (!e.isSuccess())
                    return e.type;
            }
            for (var e : internalMessages) {
                if (e.isSuccess())
                    return e.type;
            }
        }
        return Type.Success;
    }

    public int getStatus() {
        return this.getType().getValue();
    }

    public HttpStatusCode getStatusCode() {
        return HttpStatusCode.valueOf(this.getType().getValue());
    }

    public List<Message> getErrors() {
        List<Message> errors = new ArrayList<>();
        for (var m : this.internalMessages) {
            if (m.isSuccess())
                continue;
            errors.add(m);
        }
        return errors;
    }

    public ObjectReturn clear() {
        this.body = null;
        this.internalMessages.clear();
        return this;
    }

    public boolean isOK() {
        return this.getStatusCode().is2xxSuccessful();
    }

    public boolean isError() {
        return this.getStatusCode().isError();
    }

    public boolean is1xxInformational() {
        return this.getStatusCode().is1xxInformational();
    }

    public boolean is2xxSuccessful() {
        return this.getStatusCode().is2xxSuccessful();
    }

    public boolean is3xxRedirection() {
        return this.getStatusCode().is3xxRedirection();
    }

    public boolean is4xxClientError() {
        return this.getStatusCode().is4xxClientError();
    }

    public boolean is5xxServerError() {
        return this.getStatusCode().is5xxServerError();
    }

    public boolean isCreated() {
        var status = this.getStatus();
        return status == HttpStatus.CREATED.value();
    }

    public boolean isAccepted() {
        var status = this.getStatus();
        return status == HttpStatus.ACCEPTED.value();
    }

    public boolean isBadRequest() {
        var status = this.getStatus();
        return status == HttpStatus.BAD_REQUEST.value();
    }

    public boolean isNotFound() {
        var status = this.getStatus();
        return status == HttpStatus.NOT_FOUND.value();
    }

    public boolean isConflict() {
        var status = this.getStatus();
        return status == HttpStatus.CONFLICT.value();
    }

    public MessageMaker message() {
        return message.clear();
    }

    public MessageMaker m() {
        return message();
    }

    private List<String> internalGetErrors(List<Message> errors) {
        if (errors.isEmpty())
            return new ArrayList<>();
        List<String> stringErrors = new ArrayList<>();
        for (var e : errors) {
            var msg = e.getMessage();
            if (msg == null)
                continue;
            if (msg.trim().isEmpty())
                continue;
            msg = msg.trim();
            if (stringErrors.contains(msg))
                continue;
            stringErrors.add(msg);
        }
        return stringErrors;
    }

    public ResponseEntity<?> asResultHttp() {
        final var errors = this.getErrors();
        if (errors.isEmpty())
            return ResponseEntity.ok(this.body);
        var statusCode = this.getType();
        var stringErrors = this.internalGetErrors(errors);
        HashMap<String, Object> responseBody = new HashMap<>(Map.of(CODE_NAME, statusCode.getValue(), ERRORS_NAME, stringErrors));
        if (this.body != null)
            responseBody.put(BODY_NAME, this.body);
        return ResponseEntity
                .status(statusCode.getValue())
                .body(responseBody);
    }

    public ResponseEntity<?> asResultPagination() {
        var resultInfo = asResultInfo();
        return ResponseEntity
                .status(resultInfo.code)
                .body(resultInfo);
    }

    public ResultInfo asResultInfo() {
        final var errors = this.getErrors();
        return ResultInfo
                .builder()
                .success(this.isOK())
                .code(this.getType().value)
                .messages(this.internalGetErrors(errors))
                .count(0)
                .page(0)
                .perPage(0)
                .totalCount(0)
                .totalPages(0)
                .result(this.body)
                .build();
    }

    @Getter
    public enum Type {
        Success(200),
        Created(201),
        Accepted(202),
        NoContent(204),
        BadRequest(400),
        Forbidden(403),
        NotFound(404),
        Conflict(409),
        NotAllowed(405),
        NotAcceptable(406),
        Unauthorized(401),
        Fail(500),
        NotImplemented(501);
        private final int value;

        Type(int value) {
            this.value = value;
        }
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ResultInfo {
        private boolean success;
        private int code;
        private int count;
        private int page;
        private int perPage;
        private int totalCount;
        private int totalPages;
        private Object result;
        private List<String> messages;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Message {
        private Type type;
        private Object code;
        private String message;

        public boolean isSuccess() {
            return this.type != null && (this.type.equals(Type.Success) || this.type.equals(Type.Accepted));
        }
    }

    @RequiredArgsConstructor
    public static class MessageMaker {
        private final ObjectReturn objectReturn;
        private Type type = Type.Success;
        private Object code = null;
        private String message = null;

        public MessageMaker clear() {
            return this
                    .type(Type.Success)
                    .code(null)
                    .message("");
        }

        public ObjectReturn end() {
            if (this.type != null) {
                this.objectReturn
                        .internalMessages
                        .add(
                                Message
                                        .builder()
                                        .type(this.type)
                                        .code(this.code)
                                        .message(message)
                                        .build()
                        );
            }
            return this.objectReturn;
        }

        public MessageMaker type(Type type) {
            return this.type(type, message);
        }

        public MessageMaker type(Type type, String message) {
            this.type = type;
            this.message = message;
            return this;
        }

        public MessageMaker type(Type type, String format, Object... args) {
            this.type = type;
            if (format != null && args != null)
                this.message = String.format(format, args);
            else if (format != null)
                this.message = format;
            else if (args != null)
                this.message = Arrays.toString(args);
            else
                this.message = null;
            return this;
        }

        public MessageMaker code(Object code) {
            this.code = code;
            return this;
        }

        public MessageMaker message(String message) {
            this.message = message == null ? "" : message;
            return this;
        }

        public <T> MessageMaker message(Class<T> aClass) {
            return aClass == null
                    ? this
                    : this.message(String.format(INVALID_OBJECT_OF_TYPE, aClass));
        }

        public MessageMaker message(String format, Object... args) {
            if (format != null && args != null)
                return this.message(String.format(format, args));
            return this;
        }

        public MessageMaker Fail() {
            return this.Fail(null);
        }

        public MessageMaker Fail(String message) {
            return this.Fail(message, (Object) null);
        }

        public MessageMaker Fail(String format, Object... args) {
            return this.type(Type.Fail, format, args);
        }

        public MessageMaker Success() {
            return this.Success(null);
        }

        public MessageMaker Success(String message) {
            return this.Success(message, (Object) null);
        }

        public MessageMaker Success(String format, Object... args) {
            return this.type(Type.Success, format, args);
        }

        public MessageMaker OK() {
            return this.Success();
        }

        public MessageMaker OK(String message) {
            return this.Success(message);
        }

        public MessageMaker OK(String format, Object... args) {
            return this.Success(format, args);
        }

        public MessageMaker Created() {
            return this.Created(null);
        }

        public MessageMaker Created(String message) {
            return this.Created(message, (Object) null);
        }

        public MessageMaker Created(String format, Object... args) {
            return this.type(Type.Created, format, args);
        }


        public MessageMaker Accepted() {
            return this.Accepted(null);
        }

        public MessageMaker Accepted(String message) {
            return this.Accepted(message, (Object) null);
        }

        public MessageMaker Accepted(String format, Object... args) {
            return this.type(Type.Accepted, format, args);
        }

        public MessageMaker NoContent() {
            return this.NoContent(null);
        }

        public MessageMaker NoContent(String message) {
            return this.NoContent(message, (Object) null);
        }

        public MessageMaker NoContent(String format, Object... args) {
            return this.type(Type.NoContent, format, args);
        }

        public MessageMaker BadRequest() {
            return this.BadRequest(null);
        }

        public MessageMaker BadRequest(String message) {
            return this.BadRequest(message, (Object) null);
        }

        public MessageMaker BadRequest(String format, Object... args) {
            return this.type(Type.BadRequest, format, args);
        }

        public MessageMaker Forbidden() {
            return this.Forbidden(null);
        }

        public MessageMaker Forbidden(String message) {
            return this.Forbidden(message, (Object) null);
        }

        public MessageMaker Forbidden(String format, Object... args) {
            return this.type(Type.Forbidden, format, args);
        }

        public MessageMaker NotFound() {
            return this.NotFound(null);
        }

        public MessageMaker NotFound(String message) {
            return this.NotFound(message, (Object) null);
        }

        public MessageMaker NotFound(String format, Object... args) {
            return this.type(Type.NotFound, format, args);
        }

        public MessageMaker Conflict() {
            return this.Conflict(null);
        }

        public MessageMaker Conflict(String message) {
            return this.Conflict(message, (Object) null);
        }

        public MessageMaker Conflict(String format, Object... args) {
            return this.type(Type.Conflict, format, args);
        }

        public MessageMaker NotAllowed() {
            return this.NotAllowed(null);
        }

        public MessageMaker NotAllowed(String message) {
            return this.NotAllowed(message, (Object) null);
        }

        public MessageMaker NotAllowed(String format, Object... args) {
            return this.type(Type.NotAllowed, format, args);
        }

        public MessageMaker NotAcceptable() {
            return this.NotAcceptable(null);
        }

        public MessageMaker NotAcceptable(String message) {
            return this.NotAcceptable(message, (Object) null);
        }

        public MessageMaker NotAcceptable(String format, Object... args) {
            return this.type(Type.NotAcceptable, format, args);
        }

        public MessageMaker Unauthorized() {
            return this.Unauthorized(null);
        }

        public MessageMaker Unauthorized(String message) {
            return this.Unauthorized(message, (Object) null);
        }

        public MessageMaker Unauthorized(String format, Object... args) {
            return this.type(Type.Unauthorized, format, args);
        }

        public MessageMaker NotImplemented() {
            return this.NotImplemented(null);
        }

        public MessageMaker NotImplemented(String message) {
            return this.NotImplemented(message, (Object) null);
        }

        public MessageMaker NotImplemented(String format, Object... args) {
            return this.type(Type.NotImplemented, format, args);
        }

    }

}
