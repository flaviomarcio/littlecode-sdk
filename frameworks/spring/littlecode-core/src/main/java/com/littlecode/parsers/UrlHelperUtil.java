package com.littlecode.parsers;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Utilitario para manipular paths
 */
@Getter
@NoArgsConstructor
public class UrlHelperUtil {
    private String basePath="/";
    private List<String> openPaths = new ArrayList<>();
    private List<String> acceptedPaths = new ArrayList<>();
    private List<String> blockedPath = new ArrayList<>();
    private List<String> backendPath = new ArrayList<>();
    private List<String> backendOpenPath = new ArrayList<>();

    public static boolean isValidUrl(String url) {
        if (url != null && !url.trim().isEmpty()){
            try {
                var uri = new URI(url);
                return (uri.getScheme() != null && uri.getHost() != null);
            } catch (Exception ignored) {
            }
        }
        return false;
    }

    public void setBasePath(String basePath) {
        basePath = basePath==null?"/":basePath.trim();
        this.basePath = basePath.isEmpty()?"/":basePath;
    }

    public static boolean isValidUrl(URI uri) {
        return uri != null && isValidUrl(uri.toString());
    }

    /**
     * rotina para adicionar paths usando Lista
     * @param newPaths
     * @return
     */
    public static List<String> parserPaths(List<String> newPaths) {
        if(newPaths==null || newPaths.isEmpty())
            return new ArrayList<>();
        List<String> result=new ArrayList<>();
        for(var row: newPaths){
            var rowValue=row.trim().toLowerCase().trim();
            if(!rowValue.isEmpty() && !result.contains(rowValue))
                result.add(rowValue);
        }
        return result;
    }

    /**
     * rotina para adicionar paths usando array
     * @param newPaths
     * @return
     */
    public static List<String> parserPaths(String ... newPaths) {
        return parserPaths(List.of(newPaths));
    }

    /**
     * rotina para inserir open paths
     * @param newPaths
     */
    public void setOpenPaths(List<String> newPaths) {
        this.openPaths = parserPaths(newPaths);
    }

    /**
     * rotina para inserir accepted paths
     * @param newPaths
     */
    public void setAcceptedPaths(List<String> newPaths) {
        this.acceptedPaths = parserPaths(newPaths);
    }


    /**
     * rotina para inserir accepted paths
     * @param newPaths
     */
    public void setBlockedPaths(List<String> newPaths) {
        this.blockedPath = parserPaths(newPaths);
    }

    /**
     * rotina para inserir backend paths
     * @param newPaths
     */
    public void setBackendPaths(List<String> newPaths) {
        this.backendPath = parserPaths(newPaths);
    }

    /**
     * rotina para inserir backend open paths
     * @param newPaths
     */
    public void setBackendOpenPaths(List<String> newPaths) {
        this.backendOpenPath = parserPaths(newPaths);
    }

    /**
     * verifica open paths
     * @param requestPath
     * @return
     */
    public boolean isOpenPath(String requestPath) {
        return matchPath(this.openPaths, requestPath);
    }

    /**
     * verifica accepted paths
     * @param requestPath
     * @return
     */
    public boolean isAcceptedPath(String requestPath) {
        return matchPath(this.acceptedPaths, requestPath);
    }

    /**
     * verifica blocked paths
     * @param requestPath
     * @return
     */
    public boolean isBlockedPath(String requestPath) {
        return matchPath(this.blockedPath, requestPath);
    }

    /**
     * verifica backend paths
     * @param requestPath
     * @return
     */
    public boolean isBackendPath(String requestPath) {
        return matchPath(this.backendPath, requestPath);
    }

    /**
     * verifica backend open paths
     * @param requestPath
     * @return
     */
    public boolean isBackendOpenPath(String requestPath) {
        return matchPath(this.backendOpenPath, requestPath);
    }

    /**
     * verifica matcher do requestPath em pathFilters
     * @param pathFilters
     * @param requestPath
     * @return
     */
    public boolean matchPath(List<String> pathFilters, String requestPath) {
        if(requestPath==null || requestPath.trim().isEmpty() || pathFilters.isEmpty())
            return false;
        requestPath=requestPath.trim().toLowerCase().trim();
        requestPath = requestPath.trim().endsWith("/") && requestPath.length()>1
                ?requestPath.substring(0,requestPath.length()-1)
                :requestPath.trim();
        for (var v : pathFilters) {
            var url = (this.getBasePath() + v).replace("//", "/").toLowerCase().trim();
            if(requestPath.equals(url))
                return true;

            if (url.endsWith("**")){
                var copyPath=requestPath;
                url=url.substring(0,url.length()-2).trim();
                if(url.endsWith("/"))
                    url=url.substring(0,url.length()-1).trim();
                if(copyPath.endsWith("/"))
                    copyPath=copyPath.substring(0,copyPath.length()-1).trim();

                if(url.isEmpty())
                    url="";

                if(copyPath.isEmpty())
                    copyPath="/";

                if(copyPath.startsWith(url))
                    return true;
            }
        }
        return false;
    }


}
