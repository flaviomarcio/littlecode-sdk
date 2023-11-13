package com.littlecode.setup.privates;

import com.littlecode.setup.Setup;
import com.littlecode.setup.SetupSetting;
import com.littlecode.setup.db.metadata.MetaDataEngine;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Builder
public class SetupExecutorDbDDL {

    @Getter
    private final List<SetupClassesDB.StatementItem> statementItems = new ArrayList<>();
    private SetupSetting setting;
    private Setup.ExecutorDataBase executorDataBase;
    private Connection connection;

    public SetupExecutorDbDDL clear() {
        statementItems.clear();
        return this;
    }


    public SetupExecutorDbDDL execute() {
        var ddlSetting = setting.getDatabase().getDDL();
        this
                .clear()
                .statementItems.addAll(
                        MetaDataEngine
                                .builder()
                                .setting(this.setting)
                                .packagesBase(ddlSetting.getPackageNames().split(","))
                                .packagesIgnored(new String[0])
                                .build()
                                .load()
                                .getStatements()
                );


        return this;
    }


}
