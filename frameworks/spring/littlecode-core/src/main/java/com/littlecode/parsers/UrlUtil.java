package com.littlecode.parsers;

import java.util.List;

public class UrlUtil {
    public static final String PATH_DELIMIT = "/";
    public static final String PATH_AUTH_END = "/**";
    public static final List<String> DEFAULT_TRUSTED_URLS = List.of("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html");
    public static final List<String> DEFAULT_TRUSTED_URLS_OPEN = List.of("/login/**", "/swagger-ui/**", "/v3/api-docs/**", "/actuator/**", "/prometheus/**");
    private static final String PATH_LIST_DELIMIT = " ";
    private String contextPath;
    private List<String> trustedUrls;
    private List<String> trustedUrlsOpen;

    public UrlUtil() {
        this.contextPath = "";
        this.trustedUrls = List.copyOf(DEFAULT_TRUSTED_URLS);
        this.trustedUrlsOpen = List.copyOf(DEFAULT_TRUSTED_URLS_OPEN);
    }

    public static UrlUtil builder() {
        return new UrlUtil();
    }

    public UrlUtil build() {
        return this;
    }

    public String getContextPath() {
        if (this.contextPath == null)
            this.contextPath = "";
        return this.contextPath;
    }

    public UrlUtil contextPath(String contextPath) {
        this.contextPath = contextPath == null ? null : pathParse(contextPath);
        return this;
    }

    public UrlUtil trustedUrls(List<String> values) {
        this.trustedUrls = values;
        return this;
    }

    public UrlUtil trustedUrlsOpen(List<String> values) {
        this.trustedUrlsOpen = values;
        return this;
    }

    public String pathParse(String url) {
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

    public String pathMaker(String path) {
        var lst = pathMaker(List.of(path));
        return List.of(lst).get(0);
    }

    public String[] pathMaker(final List<String> pathList) {
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

    public boolean pathMatch(final List<String> pathList, final String urlPath) {
        final var pathCheck = urlPath == null ? "" : urlPath.trim().toLowerCase();
        final var paths = List.of(pathMaker(pathList));
        for (var path : paths) {
            path = path.toLowerCase().trim();
            if (path.equals(pathCheck))
                return true;
            if (path.endsWith(PATH_AUTH_END)) {
                var auxA = path.replace(PATH_AUTH_END, "");
                if (pathCheck.startsWith(auxA))
                    return true;
            }
        }
        return false;
    }

    public String[] getTrustedUrl() {
        var paths = new java.util.ArrayList<>(List.copyOf(trustedUrls));
        paths.add(getContextPath() + PATH_AUTH_END);
        StringBuilder output = new StringBuilder();
        paths.forEach(path -> {
            if (!path.trim().isEmpty())
                output.append(path).append(PATH_LIST_DELIMIT);
        });
        return output.toString().trim().split(PATH_LIST_DELIMIT);
    }

    public String[] getTrustedOpenUrl() {
        return pathMaker(trustedUrls);
    }

    public boolean isTrustedOpenUrl(final String urlPath) {
        return pathMatch(trustedUrlsOpen, urlPath);
    }

    public boolean isTrustedUrl(final String urlPath) {
        return pathMatch(trustedUrls, urlPath);
    }
}
