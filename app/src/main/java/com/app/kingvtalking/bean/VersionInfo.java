package com.app.kingvtalking.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by wang55 on 2017/1/10.
 */
@DatabaseTable(tableName = "tb_version")
public class VersionInfo {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName ="version_name")
    private String version_name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVersion_name() {
        return version_name;
    }

    public void setVersion_name(String version_name) {
        this.version_name = version_name;
    }
}
