package com.littlecode.setup;

import com.littlecode.parsers.PrimitiveUtil;
import com.littlecode.setup.privates.SetupClassesDB;
import com.littlecode.setup.privates.SetupConfig;
import com.littlecode.util.SystemUtil;
import lombok.*;
import org.springframework.orm.jpa.vendor.Database;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

@Data
@Deprecated(since = "Descontinuar o uso")
public class SetupSetting {
    public static final String env_auto_start = "littlecode.setup.auto-start";
    public static final String env_database_auto_apply = "littlecode.setup.database.auto-apply";
    public static final String env_database_auto_start = "littlecode.setup.database.auto-start";
    public static final String env_database_target_auto_start = "littlecode.setup.database.target.auto-start";
    public static final String env_database_target_databases = "littlecode.setup.database.target.databases";
    public static final String env_database_target_tags = "littlecode.setup.database.target.tags";
    public static final String env_database_target_auto_update = "littlecode.setup.database.target.auto-update";
    public static final String env_database_target_model_scan = "littlecode.setup.database.target.model-scan";
    public static final String env_database_ddl_auto_start = "littlecode.setup.database.ddl.auto-start";
    public static final String env_database_ddl_auto_save = "littlecode.setup.database.ddl.auto-save";
    public static final String env_database_ddl_safe_drops = "littlecode.setup.database.ddl.safe-drops";
    public static final String env_database_ddl_exporter_dir = "littlecode.setup.database.ddl.exporter.dir";
    public static final String env_database_ddl_package_names = "littlecode.setup.database.ddl.package-names";
    public static final String env_database_ddl_package_auto_scan = "littlecode.setup.database.ddl.package-scan";
    public static final String env_business_auto_start = "littlecode.setup.business.auto-start";
    private static final String SETUP_NAME_BASE_PATH = "littlecode.setup/ddl";
    private static final Path SETUP_REPOSITORY_PATH = Path.of(SystemUtil.Env.USER_HOME.toString(), SETUP_NAME_BASE_PATH);
    private final SetupConfig config;
    private SetupSetting.Started started;
    private DatabaseConfig database;
    private SetupSetting.Business business;

    public SetupSetting(SetupConfig config) {
        this.config = config;
        this.load();
    }

    public void load() {
        var envUtil = config.getEnvironmentUtil();
        this.started = Started
                .builder()
                .autoStart(envUtil.asBool(env_auto_start, true))
                .build();

        this.database = DatabaseConfig
                .builder()
                .autoApply(envUtil.asBool(env_database_auto_apply, false))
                .autoStart(envUtil.asBool(env_database_auto_start, this.started.isAutoStart()))
                .DDL(
                        DatabaseDDL
                                .builder()
                                .autoStart(envUtil.asBool(env_database_ddl_auto_start, this.started.isAutoStart()))
                                .autoSave(envUtil.asBool(env_database_ddl_auto_save, true))
                                .safeDrops(envUtil.asBool(env_database_ddl_safe_drops, true))
                                .exporterDirName(envUtil.asString(env_database_ddl_exporter_dir))
                                .packageNames(envUtil.asString(env_database_ddl_package_names))
                                .packageAutoScan(envUtil.asBool(env_database_ddl_package_auto_scan, true))
                                .build()
                )
                .target(
                        DatabaseTarget
                                .builder()
                                .autoStart(envUtil.asBool(env_database_target_auto_start, this.started.isAutoStart()))
                                .databases(envUtil.asEnums(env_database_target_databases, Database.class))
                                .tags(envUtil.asListOfString(env_database_target_tags, SetupClassesDB.Target.toList()))
                                .build()
                )
                .build();
        this.business = Business
                .builder()
                .autoStart(envUtil.asBool(env_business_auto_start, this.started.isAutoStart()))
                .build();
    }


    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Started {
        private boolean autoStart;
    }


    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DatabaseConfig {
        private boolean autoApply;
        private boolean autoStart;
        private DatabaseTarget target;
        private DatabaseDDL DDL;
    }


    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DatabaseTarget {
        private boolean autoStart;
        private List<Database> databases;
        private List<String> tags;
    }

    @Builder
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DatabaseDDL {
        @Getter
        private boolean autoSave;
        @Getter
        private boolean autoStart;
        private String exporterDirName;
        @Getter
        private String packageNames;
        @Getter
        private boolean packageAutoScan;
        @Getter
        private boolean safeDrops;

        @SuppressWarnings("unused")
        public void setPackageNames(Class<?> aClass) {
            this.setPackageNames((aClass == null) ? null : aClass.getPackageName());
        }

        public void setPackageNames(String newValue) {
            this.packageNames = newValue == null ? "" : newValue.trim();
        }

        public void setPackageNames(String[] newValues) {
            if (newValues == null || newValues.length == 0) {
                this.packageNames = "";
                return;
            }
            this.setPackageNames(List.of(newValues));
        }

        public void setPackageNames(List<String> newValues) {
            if (newValues == null || newValues.isEmpty()) {
                this.packageNames = "";
                return;
            }
            var names = new StringBuilder();
            newValues
                    .forEach(s -> names.append(s).append("."));
            var name = names.toString();
            name = name.substring(0, name.length() - 1);
            this.packageNames = name;
        }

        public File getExporterDirName() {
            return getExporterDirName("");
        }

        public File getExporterDirName(String finalPath) {
            var appName = PrimitiveUtil.isEmpty(this.exporterDirName)
                    ? SystemUtil.Env.getAppName()
                    : this.exporterDirName;
            if (PrimitiveUtil.isEmpty(appName))
                appName = UUID.randomUUID().toString();

            if (finalPath == null)
                finalPath = "";

            return Path.of(SETUP_REPOSITORY_PATH.toString(), appName, finalPath).toFile();
        }


    }

    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Business {
        private boolean autoStart;
    }

}
