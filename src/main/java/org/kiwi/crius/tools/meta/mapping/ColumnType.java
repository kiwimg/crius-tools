package org.kiwi.crius.tools.meta.mapping;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-4-9
 * Time: 上午11:07
 * To change this template use File | Settings | File Templates.
 */
public enum ColumnType {
    PrimaryKey("primaryKey"), ForeignKey("ForeignKey"), Column("Column"), PrimaryKeyRef("PrimaryKeyRef");
    private String name;


    private ColumnType(String name) {
        this.name = name;

    }
    public String getType() {
        return name;
    }
}
