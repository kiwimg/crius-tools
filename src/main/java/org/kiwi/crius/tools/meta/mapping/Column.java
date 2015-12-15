package org.kiwi.crius.tools.meta.mapping;



/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-4-9
 * Time: 下午12:22
 * To change this template use File | Settings | File Templates.
 */
public class Column {
    public static final int DEFAULT_LENGTH = 255;
    public static final int DEFAULT_PRECISION = 19;
    public static final int DEFAULT_SCALE = 2;

    private int length=DEFAULT_LENGTH;
    private int precision=DEFAULT_PRECISION;
    private int scale=DEFAULT_SCALE;
    private Value value;
    private int typeIndex = 0;
    private String id;
    private String name;
    private boolean nullable=true;
    private boolean unique=false;
    private String sqlType;
    private Integer sqlTypeCode;
    private boolean quoted=false;
    int uniqueInteger;
    private String checkConstraint;
    private String comment;
    private String defaultValue;
    private String referencedEntityName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getPrecision() {
        return precision;
    }

    public void setPrecision(int precision) {
        this.precision = precision;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }

    public int getTypeIndex() {
        return typeIndex;
    }

    public void setTypeIndex(int typeIndex) {
        this.typeIndex = typeIndex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public boolean isUnique() {
        return unique;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
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

    public boolean isQuoted() {
        return quoted;
    }

    public void setQuoted(boolean quoted) {
        this.quoted = quoted;
    }

    public int getUniqueInteger() {
        return uniqueInteger;
    }

    public void setUniqueInteger(int uniqueInteger) {
        this.uniqueInteger = uniqueInteger;
    }

    public String getCheckConstraint() {
        return checkConstraint;
    }

    public void setCheckConstraint(String checkConstraint) {
        this.checkConstraint = checkConstraint;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getReferencedEntityName() {
        return referencedEntityName;
    }

    public void setReferencedEntityName(String referencedEntityName) {
        this.referencedEntityName = referencedEntityName;
    }
}
