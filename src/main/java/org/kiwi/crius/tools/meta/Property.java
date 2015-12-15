package org.kiwi.crius.tools.meta;

import org.kiwi.crius.tools.meta.mapping.ColumnType;
import org.hibernate.mapping.Value;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-4-8
 * Time: 下午2:22
 * To change this template use File | Settings | File Templates.
 */
public class Property {
    public static  int DEFAULT_LENGTH = 255;
    public static  int DEFAULT_PRECISION = 19;
    public static  int DEFAULT_SCALE = 2;
    private Value value;
    private String name;
    private String referenceName;
    private String childReferenceNameName;
    private boolean nullable = true;
    private String sqlType;
    private Integer sqlTypeCode;
    private String comment;
    private String defaultValue;
    private boolean unique=false;
    private int length=DEFAULT_LENGTH;
    private int precision=DEFAULT_PRECISION;
    private int scale=DEFAULT_SCALE;
    private ColumnType propertyType;

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getSqlType() {
        return sqlType;
    }

    public void setSqlType(String sqlType) {
        this.sqlType = sqlType;
    }

    public Integer getSqlTypeCode() {
        return sqlTypeCode;
    }

    public void setSqlTypeCode(Integer sqlTypeCode) {
        this.sqlTypeCode = sqlTypeCode;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public int getPrecision() {
        return precision;
    }

    public void setPrecision(int precision) {
        this.precision = precision;
    }

    public boolean isUnique() {
        return unique;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    public ColumnType getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(ColumnType propertyType) {
        this.propertyType = propertyType;
    }

    public String getReferenceName() {
        return referenceName;
    }

    public void setReferenceName(String referenceName) {
        this.referenceName = referenceName;
    }

    public String getChildReferenceNameName() {
        return childReferenceNameName;
    }

    public void setChildReferenceNameName(String childReferenceNameName) {
        this.childReferenceNameName = childReferenceNameName;
    }
}
