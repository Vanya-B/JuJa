package com.babychev.sqlcmd.view;

import com.babychev.sqlcmd.model.DataSet;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class TableViewUtil {

    public static String getViewTable(List<DataSet> data) {
        Map <String, Integer> width = getWidth(data);
        String result = getHeader(width) + "\n";
        for (DataSet dataSet : data) {
            if (dataSet != null) {
                if (dataSet.isValues(dataSet.getValues())) {
                    result += getRow(dataSet, width) + "\n";
                    result += getAlign(width) + "\n";
                }
            }
        }
        return result;
    }

    private static Map <String, Integer> getWidth(List<DataSet> datas) {
        Map <String, Integer> width = new LinkedHashMap<>();
        int tmp = 0;
        for (DataSet data: datas) {
            String [] values = data.getValues();
            String [] columns = data.getColumns();
            for (int j = 0; j < values.length; j++) {
                if (columns[j] == null) {
                    columns[j] = "";
                    continue;
                }
                if (values[j] == null) {
                    values[j] = "";
                }
                if (columns[j].length() > values[j].length()) {
                    tmp = columns[j].length();
                } else {
                    tmp = values[j].length();
                }
                if (columns[j] != "") {
                    if (width.containsKey(columns[j])) {
                        int w = width.get(columns[j]);

                        if (tmp > w) {
                            width.put(columns[j], tmp);
                        }
                    } else {
                        width.put(columns[j], tmp);
                    }
                } else {

                }
            }
        }
        return width;
    }

    private static String getHeader(Map <String, Integer> width) {
        String result = getAlign(width) + "\n";
        for (Map.Entry<String, Integer> entry : width.entrySet()) {
            result += String.format("|%" + entry.getValue() + "s" , entry.getKey());
        }
        result += "|" + "\n";
        result += getAlign(width);
        return result;
    }

    private static String getRow(DataSet data, Map <String, Integer> width) {
        String row = "";
        int index = 0;
        for (Map.Entry<String, Integer> entry : width.entrySet()) {
            if (width.get(data.getColumn(index)) != null) {
                row += String.format("|%" + entry.getValue() + "s" , data.getValues()[index++]);
            } else {
                row += String.format("|%" + entry.getValue() + "s" , "");
            }
        }
        row += "|";
        return row;
    }

    private static String getAlign(Map <String, Integer> width) {
        String align = "";
        for (Map.Entry<String, Integer> entry : width.entrySet()) {
            align+= "+";
            for (int j = 0; j < entry.getValue(); j++) {
                align += "-";
            }
        }
        align += "+";
        return align;
    }
}
