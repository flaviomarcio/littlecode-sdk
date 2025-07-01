package com.littlecode.parsers;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class UrlHelperUtil {
    private String basePath="/";
    private List<String> openPaths = new ArrayList<>();
    private List<String> acceptedPaths = new ArrayList<>();

    public void setBasePath(String basePath) {
        basePath = basePath==null?"/":basePath.trim();
        this.basePath = basePath.isEmpty()?"/":basePath;
    }

    public void setOpenPaths(List<String> openPaths) {
        if(openPaths==null){
            this.openPaths = new ArrayList<>();
            return;
        }

        this.openPaths.clear();
        this.openPaths.addAll(
                openPaths
                        .stream()
                        .distinct()
                        .toList()
        );
    }

    public void setAcceptedPaths(List<String> acceptedPaths) {
        if(acceptedPaths==null){
            this.acceptedPaths = new ArrayList<>();
            return;
        }

        this.acceptedPaths.clear();
        this.acceptedPaths.addAll(
                acceptedPaths
                        .stream()
                        .distinct()
                        .toList()
        );
    }


    public static String extractContextPath(String path){
        if (path == null)
            return "/";
        path=path.trim();
        if (path.isBlank() || path.equals("/") || !path.contains("/"))
            return "/";

        // Remove trailing slash (ex: /abc/ → /abc)
        String cleanPath = path.endsWith("/")
                ? path.substring(0, path.length() - 1).trim()
                : path.trim();

        int lastSlash = cleanPath.lastIndexOf('/');
        if (lastSlash <= 0)
            return "/";

        return cleanPath.substring(0, lastSlash).trim();
    }

    public boolean isOpenPath(String requestPath) {
        if(requestPath==null || requestPath.trim().isEmpty())
            return false;
        requestPath = requestPath.trim().endsWith("/") && requestPath.length()>1
                ?requestPath.substring(0,requestPath.length()-1)
                :requestPath.trim();
        for (var v : this.getOpenPaths()) {
            var url = (this.getBasePath() + v).replace("//", "/");
            if (url.endsWith("**")){
                var copyPath=extractContextPath(requestPath);
                url=extractContextPath(url);
                if(copyPath.startsWith(url))
                    return true;
            }
            else if(requestPath.equals(url)){
                return true;
            }
        }
        return false;
    }

    public boolean isAcceptedPath(String requestPath) {
        if (PrimitiveUtil.isEmpty(requestPath))
            return false;
        requestPath = requestPath.trim();
        for (var path : this.acceptedPaths) {
            var v = path.trim().toLowerCase();
            if (!v.endsWith("**") && requestPath.equals(v))
                return true;

            v = v.substring(0, v.length() - 2);
            if (requestPath.startsWith(v))
                return true;
        }
        return false;
    }

}
