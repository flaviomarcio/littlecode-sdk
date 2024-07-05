package com.littlecode.parsers;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
public class StringUtil {

    public static String SNAKE_CASE_SEPARATOR = "_";
    public static List<String> NUMERIC_CHARS = List.of("0", "1", "2", "3", "4", "5", "6", "7", "8", "9");

    private String target;

    public static StringUtil target(String target) {
        return StringUtil.builder().target(target).build();
    }

    public static String toWord(String target) {
        if (target == null || target.trim().isEmpty())
            return "";
        target = target.trim();
        if (target.length() == 1)
            return target.toUpperCase();
        return target.substring(0, 1).toUpperCase() + target.substring(1).toLowerCase();
    }

    public static String toCamelCase(String target) {
        if (target == null || target.trim().isEmpty())
            return "";

        target = target.trim().replaceAll("[^a-zA-Z0-9_]", "_");

        while (target.startsWith(SNAKE_CASE_SEPARATOR))
            target = target.substring(1);

        while (target.endsWith(SNAKE_CASE_SEPARATOR))
            target = target.substring(0, target.length() - 1);

        if (target.contains(SNAKE_CASE_SEPARATOR)) {
            var wordSplit = target.split(SNAKE_CASE_SEPARATOR);
            var words = new StringBuilder();
            for (var word : wordSplit) {
                if (!word.isEmpty()) {
                    words.append(
                                    (word.equals(word.toUpperCase()) || word.equals(target.toLowerCase()))
                                            ? word.toLowerCase()
                                            : word
                            )
                            .append(SNAKE_CASE_SEPARATOR);
                }
            }
            target = words.toString();
        } else {
            if (target.equals(target.toUpperCase()) || target.equals(target.toLowerCase()))
                target = target.toLowerCase();
        }


        if (!target.isEmpty()) {//split word
            var chars = target.toCharArray();
            var isUpperLast = false;
            var word = new StringBuilder();
            for (var c : chars) {
                String chr = Character.toString(c);
                if (chr.equals(SNAKE_CASE_SEPARATOR)) {
                    word.append(chr);
                    isUpperLast = false;
                } else {
                    if (word.isEmpty())
                        chr = chr.toUpperCase();
                    if (isUpperLast)
                        word.append(chr);
                    else
                        word.append(chr);
                    isUpperLast = chr.equals(chr.toUpperCase());
                }
            }
            target = word.toString();
            ;
        }

        if (!target.isEmpty()) {
            var chars = target.toCharArray();
            var lastIsNumber = false;
            var word = new StringBuilder();
            for (var c : chars) {
                String chr = Character.toString(c);
                if (lastIsNumber)
                    chr = chr.toUpperCase();
                lastIsNumber = NUMERIC_CHARS.contains(chr);
                word.append(chr);
            }
            target = word.toString();
            ;
        }


        if (target.contains(SNAKE_CASE_SEPARATOR)) {
            var wordSplit = target.split(SNAKE_CASE_SEPARATOR);
            var words = new StringBuilder();
            for (var word : wordSplit) {
                if (!word.isEmpty()) {
                    words
                            .append(word.substring(0, 1).toUpperCase())
                            .append(word.substring(1));
                }
            }
            target = words.toString();
        }
        return target.substring(0, 1).toLowerCase() + target.substring(1);
    }

    public static boolean isCamelCase(String target) {
        if (target == null || target.isEmpty())
            return false;
        return !target.contains(SNAKE_CASE_SEPARATOR);
    }

    public static boolean isSnakeCase(String target) {
        if (target == null || target.isEmpty())
            return false;
        return target.equals(target.toLowerCase());
    }

    public static String toSnakeCase(String target) {
        if (target == null || target.isEmpty())
            return "";
        var chars = toCamelCase(target).toCharArray();
        StringBuilder word = null;
        for (var c : chars) {
            String chr = Character.toString(c);
            if (word == null)
                word = new StringBuilder(chr);
            else if (chr.equals(chr.toUpperCase()) && !NUMERIC_CHARS.contains(chr))
                word.append(SNAKE_CASE_SEPARATOR).append(chr);
            else
                word.append(chr);
        }
        return (word == null) ? "" : word.toString().toLowerCase();
    }

    public static String padLeft(int length, char padChar, String value) {
        if (value == null)
            value = "";
        while (value.length() < length)
            value = padChar + value;
        return value;
    }

    public static String padRight(int length, char padChar, String value) {
        if (value == null)
            value = "";
        while (value.length() < length)
            value += padChar;
        return value;
    }

    public static String padCenter(int length, char padChar, String value) {
        if (value == null)
            value = "";
        var odd = true;
        while (value.length() < length) {
            if (odd)
                value += padChar;
            else
                value = padChar + value;
            odd = !odd;
        }
        return value;
    }

    public String toWord() {
        return toWord(this.getTarget());
    }

    public String toCamelCase() {
        return toCamelCase(this.getTarget());
    }

    public boolean isCamelCase() {
        return isCamelCase(this.getTarget());
    }

    public boolean isSnakeCase() {
        return isSnakeCase(this.getTarget());
    }

    public String toSnakeCase() {
        return toSnakeCase(this.getTarget());
    }
}
