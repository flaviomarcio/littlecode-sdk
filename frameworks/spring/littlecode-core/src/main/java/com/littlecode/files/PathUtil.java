package com.littlecode.files;

import com.littlecode.exceptions.FrameworkException;
import com.littlecode.util.SystemUtil;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.net.URI;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Builder
public class PathUtil {
    private static final String DIR_SEPARATOR=String.valueOf(File.separatorChar);
    public final List<String> pathParts = new ArrayList<>();
    @Getter
    @Setter
    public Path target;

    public static PathUtil target(Path target) {
        return PathUtil.builder().target(target).build();
    }

    public static PathUtil target(String target) {
        return PathUtil.builder().target(Path.of(target)).build();
    }

    public static PathUtil target(File target) {
        return PathUtil.builder().target(target.toPath()).build();
    }

    public static PathUtil target(URI target) {
        return PathUtil.builder().target(Path.of(target.toString())).build();
    }

    public static PathUtil targetFromTempDir() {
        return PathUtil.target(SystemUtil.Env.JAVA_TEMP_DIR);
    }

    public static PathUtil targetFromHomeDir() {
        return PathUtil.target(SystemUtil.Env.JAVA_HOME);
    }

    public static Path tempFile(UUID fileName) {
        return Path.of(SystemUtil.Env.JAVA_TEMP_DIR.toString(), fileName.toString());
    }

    public static Path tempFile(String fileName) {
        return Path.of(SystemUtil.Env.JAVA_TEMP_DIR.toString(), fileName);
    }

    public static Path tempFile(URI fileName) {
        return Path.of(fileName.toString());
    }

    public static Path tempFile() {
        return Path.of(SystemUtil.Env.JAVA_TEMP_DIR.toString(), UUID.randomUUID().toString());
    }

    public static List<File> listFiles(Path target) {
        return target == null
                ? new ArrayList<>()
                : listFiles(target.toFile());
    }

    public static List<File> listFiles(File target) {
        if (target != null && target.exists() && target.isDirectory()) {
            File[] files = target.listFiles();

            if (files == null)
                return new ArrayList<>();
            List<File> __return = new ArrayList<>();
            for (File itemFile : files) {
                if (itemFile.isDirectory())
                    __return.addAll(listFiles(itemFile.toPath()));
                else
                    __return.add(itemFile.toPath().toFile());
            }
            return __return;
        }
        return new ArrayList<>();
    }

    public static int countFiles(File target) {
        if (target != null && target.exists() && target.isDirectory()) {
            File[] files = target.listFiles();

            if (files == null)
                return 0;

            int __return = 0;
            for (File file : files) {
                if (file.isDirectory())
                    __return += countFiles(file.toPath());
                else
                    ++__return;
            }
            return __return;
        }
        return 0;
    }

    public static int countFiles(Path target) {
        return (target == null) ? 0 : countFiles(target.toFile());
    }

    public static int countFiles(String target) {
        return (target == null) ? 0 : countFiles(new File(target));
    }

    public static Path fileName(Path directory, Object fileName) {
        return Path.of(directory.toString(), fileName.toString());
    }

    public static boolean exists(String target) {
        return target != null && (new File(target).exists());
    }

    public static boolean exists(Path target) {
        return target != null && target.toFile().exists();
    }

    public static boolean exists(File target) {
        return target != null && target.exists();
    }

    public static boolean mkDir(File target) {
        return target != null && (target.exists() || target.mkdirs());
    }

    public static boolean mkDir(String target) {
        return target!=null && mkDir(new File(target));
    }

    public static boolean mkDir(Path target) {
        return target!=null && mkDir(target.toFile());
    }

    public static boolean rmDir(File file) {
        return (file != null && file.exists() && !file.delete());
    }

    public static boolean rmDir(Path target) {
        return target!=null && mkDir(target.toFile());
    }

    public static boolean rmDir(String target) {
        return target!=null && mkDir(new File(target));
    }

    public PathUtil clear() {
        this.target = null;
        return this.clean();
    }

    public PathUtil clean() {
        this.pathParts.clear();
        return this;
    }

    public PathUtil part(String part) {
        if (part != null)
            this.pathParts.add(part);
        return this;
    }

    public PathUtil part(UUID part) {
        if (part != null)
            this.part(part.toString());
        return this;
    }

    public Path toPath() {
        var path = new StringBuilder(this.target == null ? "" : this.target.toString());
        this.pathParts
                .forEach(s -> {
                    if (s == null)
                        return;
                    s = s.trim();
                    if (!s.isEmpty())
                        path.append(DIR_SEPARATOR).append(s);
                });
        var finalPath = path.toString().trim();
        return Path.of(finalPath);
    }

    public String toString() {
        var path = this.toPath();
        return (path == null) ? "" : path.toString();
    }

    public File toFile() {
        var path = this.toPath();
        return path == null ? null : path.toFile();
    }

    public List<File> listFiles() {
        return listFiles(this.toFile());
    }

    public Integer countFiles() {
        return countFiles(this.toFile());
    }

    public Path fileName(Object fileName) {
        return fileName == null ? null : Path.of(this.toFile().getAbsolutePath(), fileName.toString());
    }

    public boolean exists() {
        return exists(target);
    }

    public PathUtil mkDir() {
        mkDir(this.toPath());
        return this;
    }

    public PathUtil rmDir() {
        rmDir(this.toFile());
        return this;
    }

}
