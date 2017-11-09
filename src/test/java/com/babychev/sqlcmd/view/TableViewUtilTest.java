package com.babychev.sqlcmd.view;

import com.babychev.sqlcmd.model.DataSet;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class TableViewUtilTest {

    private static List<DataSet> datas;
    private static List<DataSet> nullColumnData;
    private static List<DataSet> nullColumnDatas;
    private static List<DataSet> emptyData;
    private static List<DataSet> nullData;
    private static List<DataSet> dataClear;
    private static final int SIZE_FOR_DATAS = 2;
    private static final int SIZE_FOR_EMPTY_DATAS = 1;
    private static final int SIZE_FOR_DATAS_WITH_NULL_COLUMNS = 3;

    @BeforeClass
    public static void setup () {
        datas = new LinkedList<>();
        DataSet row1 = new DataSet();
        row1.put("id", "1");
        row1.put("column", "somelongvue");
        row1.put("ups", "somelongvalue");
        DataSet row2 = new DataSet();
        row2.put("id", "256");
        row2.put("column", "som");
        row2.put("ups", "somelongvalue");
        datas.add(row1);
        datas.add(row2);;

        dataClear = new LinkedList<>();
        DataSet rowc = new DataSet();
        rowc.put("id", null);
        rowc.put("column", null);
        rowc.put("ups", null);
        dataClear.add(rowc);

        emptyData = new LinkedList<>();
        DataSet emptyRow = new DataSet();
        emptyRow.put("id", "");
        emptyRow.put("column", "");
        emptyRow.put("ups", "");
        emptyData.add(emptyRow);

        nullData = new LinkedList<>();
        DataSet nullValueRow = new DataSet();
        nullValueRow.put("id", "1");
        nullValueRow.put("column", null);
        nullValueRow.put("ups", "some");
        nullData.add(nullValueRow);

        nullColumnData = new LinkedList<>();
        DataSet nullColumnRow = new DataSet();
        nullColumnRow.put("id", "1");
        nullColumnRow.put("column", "columnwithsomevalue");
        nullColumnRow.put(null, "some");
        nullColumnData.add(nullColumnRow);

        nullColumnDatas = new LinkedList<>();
        DataSet nullColumnsRow1 = new DataSet();
        DataSet nullColumnsRow2 = new DataSet();
        DataSet nullColumnsRow3 = new DataSet();
        nullColumnsRow1.put("id", "1");
        nullColumnsRow1.put("column", "columnwithsomevalue");
        nullColumnsRow1.put(null, "someval");
        nullColumnsRow2.put("id", "2");
        nullColumnsRow2.put("column", "somevalue");
        nullColumnsRow2.put(null, "some");
        nullColumnsRow3.put("id", "3");
        nullColumnsRow3.put("column", "columnwithsomevalue");
        nullColumnsRow3.put("ups", "other");
        nullColumnDatas.add(nullColumnsRow1);
        nullColumnDatas.add(nullColumnsRow2);
        nullColumnDatas.add(nullColumnsRow3);
    }

    @Test
    public void testGetViewTable () {
        //given
        String expected = "+---+-----------+-------------+\n" +
                          "| id|     column|          ups|\n" +
                          "+---+-----------+-------------+\n" +
                          "|  1|somelongvue|somelongvalue|\n" +
                          "+---+-----------+-------------+\n" +
                          "|256|        som|somelongvalue|\n" +
                          "+---+-----------+-------------+\n";
        //when
        //then
        String actual = TableViewUtil.getViewTable(datas);
        assertEquals(expected, actual);
    }

    @Test
    public void testEmptyGetViewTable () {
        //given
        String expected = "+--+------+---+\n" +
                          "|id|column|ups|\n" +
                          "+--+------+---+\n" +
                          "|  |      |   |\n" +
                          "+--+------+---+\n";
        //when
        //then
        String actual = TableViewUtil.getViewTable(emptyData);
        assertEquals(expected, actual);
    }

    @Test
    public void testNullValueGetViewTable () {
        //given
        String expected = "+--+------+----+\n" +
                          "|id|column| ups|\n" +
                          "+--+------+----+\n" +
                          "| 1|  null|some|\n" +
                          "+--+------+----+\n";
        //when
        //then
        String actual = TableViewUtil.getViewTable(nullData);
        assertEquals(expected, actual);
    }

    @Test
    public void testNullColumnGetViewTable () {
        //given
        String expected = "+--+-------------------+\n" +
                          "|id|             column|\n" +
                          "+--+-------------------+\n" +
                          "| 1|columnwithsomevalue|\n" +
                          "+--+-------------------+\n";
        //when
        //then
        String actual = TableViewUtil.getViewTable(nullColumnData);
        assertEquals(expected, actual);
    }

    @Test
    public void testNullColumnsGetViewTable () {
        //given
        String expected = "+--+-------------------+-----+\n" +
                          "|id|             column|  ups|\n" +
                          "+--+-------------------+-----+\n" +
                          "| 1|columnwithsomevalue|     |\n" +
                          "+--+-------------------+-----+\n" +
                          "| 2|          somevalue|     |\n" +
                          "+--+-------------------+-----+\n" +
                          "| 3|columnwithsomevalue|other|\n" +
                          "+--+-------------------+-----+\n";
        //when
        //then
        String actual = TableViewUtil.getViewTable(nullColumnDatas);
        assertEquals(expected, actual);
    }

    @Test
    public void testClearGetViewTable () {
        //given
        String expected = "+--+------+---+\n" +
                          "|id|column|ups|\n" +
                          "+--+------+---+\n";
        //when
        //then
        String actual = TableViewUtil.getViewTable(dataClear);
        assertEquals(expected, actual);
    }

}
