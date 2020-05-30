package com.novigo.fiori.trackerapp;

public class DataSetFire {
    String truck_id,execution,ending_date,fo_desc,dloc_name,sloc_name;
    long fo_id;

    public String getDloc_name() {
        return dloc_name;
    }

    public void setDloc_name(String dloc_name) {
        this.dloc_name = dloc_name;
    }

    public String getSloc_name() {
        return sloc_name;
    }

    public void setSloc_name(String sloc_name) {
        this.sloc_name = sloc_name;
    }

    public DataSetFire(long fo_id, String truck_id, String execution, String ending_date, String fo_desc, String dloc_name, String sloc_name) {
        this.fo_id = fo_id;
        this.truck_id = truck_id;
        this.execution = execution;
        this.ending_date = ending_date;
        this.fo_desc = "Freight Order";
        this.dloc_name = dloc_name;
        this.sloc_name = sloc_name;

    }

    public DataSetFire() {
    }

    public long getFo_id() {
        return fo_id;
    }

    public void setFo_id(long fo_id) {
        this.fo_id = fo_id;
    }

    public String getTruck_id() {
        return truck_id;
    }

    public void setTruck_id(String truck_id) {
        this.truck_id = truck_id;
    }

    public String getExecution() {
        return execution;
    }

    public void setExecution(String execution) {
        this.execution = execution;
    }

    public String getEnding_date() {
        return ending_date;
    }

    public void setEnding_date(String ending_date) {
        this.ending_date = ending_date;
    }

    public String getFo_desc() {
        return fo_desc;
    }

    public void setFo_desc(String fo_desc) {
        this.fo_desc = fo_desc;
    }
}
