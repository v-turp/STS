package com.score.sts.presentation.model;

/**
 * Created by Who Dat on 11/28/2016.
 * This class is for the different flavors of the application
 * whose data does not fit a specific category in the
 * list of the FeatureListFragment
 */
public class GeneralContentDescription implements IGeneralContentDescription {

    private String columnData1;
    private String columnData2;
    private String columnData3;
    private String columnData4;

    public GeneralContentDescription(){}

    public GeneralContentDescription(String col1, String col2, String col3, String col4){
        this.columnData1 = col1;
        this.columnData2 = col2;
        this.columnData3 = col3;
        this.columnData4 = col4;
    }

    public String getColumnData1() {
        return columnData1;
    }

    public void setColumnData1(String columnData1) {
        this.columnData1 = columnData1;
    }

    public String getColumnData2() {
        return columnData2;
    }

    public void setColumnData2(String columnData2) {
        this.columnData2 = columnData2;
    }

    public String getColumnData3() {
        return columnData3;
    }

    public void setColumnData3(String columnData3) {
        this.columnData3 = columnData3;
    }

    public String getColumnData4() {
        return columnData4;
    }

    public void setColumnData4(String columnData4) {
        this.columnData4 = columnData4;
    }
}
