package com.babychev.sqlcmd.model;

public class DataSet {

    private Data [] datas;
    private int index;

    public DataSet(){
        datas = new Data[100]; //TODO set correct number
    }

    public void put (String column, Object value) {
        datas[index++] = new Data(column, value);
    }

    public Object getValue(int index){
        return datas[index].value();
    }

    public String getColumn(int index){
        return datas[index].getColumn();
    }

    public String[] getValues(){
        String [] result = new String[index];
        for (int i = 0; i < index; i++) {
            result[i] = (String)this.getValue(i);
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
        String [] result = new String[index];
        for (int i = 0; i < index; i++) {
            result[i] = (String)this.getColumn(i);
        }
        return result;
    }

    @Override
    public String toString () {
        String result = "";
        for (int i = 0; i < index; i++) {
            result += datas[i].toString() + '\n';
        }
        return result;
    }

    private class Data{
        private String column;
        private Object value;

        public Data(String column, Object value){
            this.column = column;
            this.value = value;
        }

        public String getColumn(){
            return  column;
        }

        public Object value(){
            return value;
        }

        @Override
        public String toString(){
            return "column : " + column + '\n' + "value : " + value;
        }
    }
}
