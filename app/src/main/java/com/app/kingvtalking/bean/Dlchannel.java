package com.app.kingvtalking.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by wang55 on 2017/1/10.
 */
@DatabaseTable(tableName = "tb_dlchannel")
public class Dlchannel {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName ="dlchannel_id")
    private String dlchannel_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getChannel_id() {
        return dlchannel_id;
    }

    public void setChannel_id(String dlchannel_id) {
        this.dlchannel_id = dlchannel_id;
    }
}
