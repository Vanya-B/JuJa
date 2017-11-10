package com.babychev.sqlcmd.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class DataSet {

    private Map<String, Object> datas;

    public DataSet(){
        datas = new LinkedHashMap<>();
    }

    public void put (String column, Object value) {
        datas.put(column, value);
    }

    public String[] getValues(){
        String [] result = new String[datas.size()];
        int index = 0;
        for (Map.Entry<String, Object> entry : datas.entrySet()) {
            result[index++] = (String) entry.getValue();
        }
        return result;
    }

    public boolean isValues (String [] values) {
        for (int i = 0; i < values.length; i++) {
            if (values[i] != null) {
                return true;
            }
        }
        return false;
    }

    public String[] getColumns(){
        String [] result = new String[datas.size()];
        int index = 0;
        for (Map.Entry<String, Object> entry : datas.entrySet()) {
            result[index++] = entry.getKey();
        }
        return result;
    }

    @Override
    public String toString () {
        String result = "";
        for (Map.Entry<String, Object> entry : datas.entrySet()) {
            result += "column : " + entry.getKey() + "\n" + "value : " + entry.getValue() + "\n";
        }
        return result;
    }
}
