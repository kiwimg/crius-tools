package org.kiwi.crius.tools.meta.mapping;

import org.hibernate.mapping.ForeignKey;
import org.hibernate.mapping.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-4-9
 * Time: 下午12:20
 * To change this template use File | Settings | File Templates.
 */
public class Table {
    private String name;
    private String schema;
    private String catalog;
    private String id;
    private String comment;
    private PrimaryKey primaryKey;
    private List<Column>   columns=null;

    public Table() {
        columns = new ArrayList<Column>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PrimaryKey getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(PrimaryKey primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void addColumn(Column column) {
        columns.add(column);
    }

    public List<Column> getColumns() {
        return columns;
    }

    public ForeignKey createForeignKey(String reference,String referencedEntityName){
        return null;
    }
}
