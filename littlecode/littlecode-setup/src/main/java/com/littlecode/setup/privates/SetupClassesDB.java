package com.littlecode.setup.privates;

import com.littlecode.exceptions.FrameworkException;
import com.littlecode.files.IOUtil;
import com.littlecode.files.PathUtil;
import com.littlecode.setup.db.metadata.MetaDataClasses;
import com.littlecode.util.SystemUtil;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Builder
public class SetupClassesDB {

    public static final String SQL_EXTENSION = ".sql";

    @Getter
    public enum Target {
        Drops("drops"),
        Schemas("schemas"),
        Tables("tables"),
        ConstraintsPK("constraints-pk"),
        ConstraintsFK("constraints-fk"),
        Indexes("indexes"),
        Functions("functions"),
        Triggers("triggers"),
        Views("views"),
        InitData("init-data");
        private final String value;

        Target(String value) {
            this.value = value;
        }

        public static List<String> toList() {
            List<String> __return = new ArrayList<>();
            for (var e : Target.values())
                __return.add(e.getValue());
            return __return;
        }

        public static Target of(String value) {
            try {
                for (var e : Target.values()) {
                    if (e.getValue().equalsIgnoreCase(value))
                        return e;
                }
                return null;
            } catch (Exception e) {
                return null;
            }
        }
    }

    public interface ObjectBase {
        void clear();
//
//        void makeSources();

        default boolean isValid() {
            return true;
        }
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StatementItem {
        @Setter
        private Target target;
        @Setter
        private File directory;
        private File fileName;
        private List<String> sources;
        @Setter
        private String fail;

        public String toString() {
            if (this.fail == null || this.fail.trim().isEmpty())
                return String.format("target: %s, fileName: %s", this.target, this.getFileName());
            return String.format("target: %s, fileName: %s, fail:%s", this.target, this.getFileName(), this.fail);
        }

        public File getDirectory() {
            if (this.directory != null)
                return this.directory;
            log.info("Directory is empty, using default directory JAVA_TEMP_DIR: [{}]", SystemUtil.Env.JAVA_TEMP_DIR.toFile());
            return (this.directory = SystemUtil.Env.JAVA_TEMP_DIR.toFile());
        }

        public File getFileName() {
            if (this.fileName != null)
                return this.fileName;
            return
                    this.fileName = PathUtil
                            .target(getDirectory())
                            .part(this.target.getValue() + SQL_EXTENSION)
                            .toFile();
        }


        @SuppressWarnings("unused")
        public StatementItem save() {
            return this.save(directory);
        }

        public StatementItem save(File directory) {
            if (directory == null)
                throw new FrameworkException("Invalid directory, directory is null");
            if (this.getTarget() == null)
                throw new FrameworkException("invalid target, target is null");
            this.directory = directory;

            if (this.getFileName() == null)
                throw new FrameworkException("Invalid file, file is null");

            var basePath = IOUtil.basePath(this.fileName);
            if (!basePath.exists() && !basePath.mkdirs())
                throw new FrameworkException("Invalid mkdirs directory: " + basePath);
            else if (basePath.exists() && !basePath.isDirectory())
                throw new FrameworkException("Invalid basePath directory: " + basePath);

            IOUtil.writeLines(this.fileName, toScriptLines(null));
            return this;
        }

        public List<String> toScriptLines(String separator) {
            List<String> __return = new ArrayList<>();
            var commandSeparator =
                    (separator == null || separator.trim().isEmpty())
                            ? MetaDataClasses.SQL_COMMAND_DELIMITER
                            : separator;
            this.getSource()
                    .forEach(s -> __return.add(s + commandSeparator));
            return __return;
        }

        @SuppressWarnings("unused")
        public String toScript() {
            return toScript(MetaDataClasses.SQL_COMMAND_DELIMITER);
        }

        public String toScript(String separator) {
            var strings = new StringBuilder();
            this.toScriptLines(separator)
                    .forEach(strings::append);
            return strings.toString();
        }

        public List<String> getSource() {
            if (this.sources == null)
                this.sources = new ArrayList<>();
            return this.sources;
        }
    }
}
