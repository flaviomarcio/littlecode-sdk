package com.littlecode.setup.privates;

import com.littlecode.setup.Setup;
import com.littlecode.setup.SetupSetting;
import com.littlecode.setup.db.metadata.MetaDataEngine;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.jpa.vendor.Database;

import java.sql.Connection;
import java.util.List;

@Slf4j
@Builder
public class SetupExecutorDbDDL {
    private Connection connection;
    private SetupSetting setting;
    private Setup.ExecutorDataBase executorDataBase;
    private MetaDataEngine metaDataEngine;

    public SetupExecutorDbDDL clear() {
        return this;
    }

    public MetaDataEngine metaDataEngine() {
        if (metaDataEngine != null)
            return metaDataEngine;
        var ddlSetting = setting.getDatabase().getDDL();
        return metaDataEngine = MetaDataEngine
                .builder()
                .connection(connection)
                .setting(this.setting)
                .packagesBase(ddlSetting.getPackageNames().split(","))
                .packagesIgnored(new String[0])
                .build()
                .load();
    }

    public List<SetupClassesDB.StatementItem> getStatementItems() {
        return metaDataEngine().getStatements();
    }

    public List<SetupClassesDB.StatementItem> getStatementItems(Database database) {
        return metaDataEngine().getStatements(database);
    }


}
