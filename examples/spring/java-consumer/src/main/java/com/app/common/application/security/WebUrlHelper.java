package com.app.common.application.security;

import java.util.List;

public class WebUrlHelper {
    private static final String PATH_LIST_DELIMIT = " ";
    private static final String PATH_DELIMIT = "/";
    private static final String PATH_AUTH_END = "/**";
    private static final List<String> TRUSTED_URLS = List.of("/safe/**", "/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html", "/actuator/**", "/prometheus/**");
    private static final List<String> TRUSTED_URLS_OPEN = List.of("/safe/**", "/login/**", "/swagger-ui.html", "/swagger-ui/index.html", "/swagger-ui/**", "/v3/api-docs/**", "/actuator/**", "/prometheus/**");
    private static String CONTEXT_PATH;

    private WebUrlHelper() {
        super();
    }

    private static String[] pathMaker(final List<String> pathList) {
        final var contextPath = getContextPath();
        final var output = new StringBuilder();
        for (var i = 0; i < pathList.size(); i++) {
            var path = pathList.get(i);
            output
                    .append(pathParse(contextPath + PATH_DELIMIT + path));
            if (i < pathList.size() - 1)
                output.append(PATH_LIST_DELIMIT);
        }
        return output.toString().split(PATH_LIST_DELIMIT);
    }

    private static boolean pathMatch(final List<String> pathList, final String urlPath) {
        final var pathCheck = String.valueOf(urlPath).trim().toLowerCase();
        final var paths = List.of(pathMaker(pathList));
        for (final var path : paths) {
            if (path.equals(pathCheck))
                return true;
            else if (path.endsWith(PATH_AUTH_END)) {
                var auxA = path.replace(PATH_AUTH_END, "");
                if (pathCheck.startsWith(auxA)) {
                    var auxB = pathCheck.length() < auxA.length()
                            ? pathCheck
                            : pathCheck.substring(0, auxA.length());
                    if (auxB.endsWith(auxA))
                        return true;
                }
            }
        }
        return false;
    }

    public static String pathParse(String url) {
        if (url.isEmpty())
            return PATH_DELIMIT;
        while (url.contains(PATH_DELIMIT + PATH_DELIMIT))
            url = url.replace(PATH_DELIMIT + PATH_DELIMIT, PATH_DELIMIT);
        List<String> paths = List.of(url.split(PATH_DELIMIT));
        if (paths.isEmpty())
            return PATH_DELIMIT;
        StringBuilder output = new StringBuilder();
        paths.forEach(path -> {
            if (!path.trim().isEmpty())
                output.append(PATH_DELIMIT).append(path);
        });
        return output.toString().toLowerCase();
    }

    public static String getContextPath() {
        return String.valueOf(CONTEXT_PATH);
    }

    public static void setContextPath(String contextPath) {
        CONTEXT_PATH = pathParse(contextPath);
    }

    public static String[] getTrustedUrl() {
        var paths = new java.util.ArrayList<>(List.copyOf(TRUSTED_URLS));
        paths.add(getContextPath() + PATH_AUTH_END);
        StringBuilder output = new StringBuilder();
        paths.forEach(path -> {
            if (!path.trim().isEmpty())
                output.append(path).append(PATH_LIST_DELIMIT);
        });
        var __return = output.toString().trim().replace("//", "/");
        return __return.split(PATH_LIST_DELIMIT);
    }

    public static String[] getTrustedOpenUrl() {
        return pathMaker(TRUSTED_URLS);
    }

    public static boolean isTrustedOpenUrl(final String urlPath) {
        return pathMatch(TRUSTED_URLS_OPEN, urlPath);
    }

    public static boolean isTrustedUrl(final String urlPath) {
        return pathMatch(TRUSTED_URLS, urlPath);
    }
}
