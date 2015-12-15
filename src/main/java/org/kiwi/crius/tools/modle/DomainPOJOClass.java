package org.kiwi.crius.tools.modle;

import org.kiwi.crius.tools.Cfg2JavaTool;
import org.kiwi.crius.tools.SqlDataType;
import org.kiwi.crius.tools.StringUtils;
import org.kiwi.crius.tools.meta.IdentifierGeneratorStrategy;
import org.kiwi.crius.tools.meta.MetaClass;
import org.kiwi.crius.tools.meta.Property;
import org.kiwi.crius.tools.meta.mapping.ColumnType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.mapping.Column;

import javax.activation.UnsupportedDataTypeException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-4-3
 * Time: 下午4:53
 * To change this template use File | Settings | File Templates.
 */
public class DomainPOJOClass implements POJOClass {
    private static final Log log = LogFactory.getLog(DomainPOJOClass.class);

    private MetaClass metaClass;
    protected ImportContext importContext;

    public DomainPOJOClass(MetaClass metaClass) {
        this.metaClass = metaClass;
        importContext = new ImportContextImpl(getPackageName());
    }


    public String getPackageDeclaration() {
        String pkgName = getPackageName();
        if (pkgName != null && pkgName.trim().length() != 0) {
            return "package " + pkgName + ";";
        } else {
            return "// default package";
        }
    }


    public String getPackageName() {
        return this.metaClass.getPackageName();
    }

    public String getClassModifiers() {
        return "public";
    }

    /**
     * Returns the javadoc associated with the class.
     *
     * @param fallback the default text if nothing else is found
     * @param indent   how many spaces should be added
     * @return
     */
    public String getClassJavaDoc(String fallback, int indent) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * @return declaration type "interface" or "class"
     */
    public String getDeclarationType() {
        return "class"; //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * @return unqualified classname for this class (can be changed by meta attribute "generated-class")
     */
    public String getDeclarationName() {
        return metaClass.getClassName();
    }

    public String getImplementsDeclaration() {
        return "implements java.io.Serializable";  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getImplements() {
        return "";  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String generateEquals(String thisName, String otherName, boolean useGenerics) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean isComponent() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean needsEqualsHashCode() {
        return false;
    }

    public boolean hasIdentifierProperty(Property property) {
        return property.getPropertyType().getType().equalsIgnoreCase(ColumnType.PrimaryKey.toString());
    }

    public String getFieldModifiers() {
        return "private";
    }

    public boolean hasFieldInitializor(Property p, boolean useGenerics) {
        if (p.getPropertyType().getType().equalsIgnoreCase(ColumnType.PrimaryKeyRef.toString())) {
            return true;
        } else {
            return p.getDefaultValue() != null;
        }
    }


    public String generateAnnColumnAnnotation(Property column) {
        StringBuffer annotations = new StringBuffer("    ");
        if (column != null) {
            buildColumnAnnotation(column, annotations, true, true);
        }

        return annotations.toString();
    }

    private void buildColumnAnnotation(Property column, StringBuffer annotations, boolean insertable, boolean updatable) {
        annotations.append("@" + importType("javax.persistence.Column") + "(name=\"").append(column.getName()).append("\"");

        appendCommonColumnInfo(annotations, column, insertable, updatable);
        if (column.getPrecision() <= Column.DEFAULT_PRECISION) { // the default is actually 0 in spec
            if (column.getPrecision() == Column.DEFAULT_PRECISION) {
                annotations.append(", precision=").append("18");
            } else {
                annotations.append(", precision=").append(column.getPrecision());
            }
        }
        if (column.getScale() != Column.DEFAULT_SCALE) { // default is actually 0 in spec
            annotations.append(", scale=").append(column.getScale());
        } else if (column.getLength() != 255) {
            annotations.append(", length=").append(column.getLength());
        }

        //TODO support secondary table
        annotations.append(")");
    }

    protected void appendCommonColumnInfo(StringBuffer annotations, Property column, boolean insertable, boolean updatable) {
        if (column.isUnique()) {
            annotations.append(", unique=").append(column.isUnique());
        }
        if (column.isNullable()) {
            annotations.append(", nullable=").append(column.isNullable());
        }
    }


    public String generateAnnIdGenerator() {
        StringBuffer wholeString = new StringBuffer("    ");
        StringBuffer idResult = new StringBuffer();
        AnnotationBuilder builder = AnnotationBuilder.createAnnotation(importType("javax.persistence.Id"));
        idResult.append(builder.getResult());
        idResult.append("\n");
        idResult.append("    ");

        IdentifierGeneratorStrategy identifierGeneratorStrategy = metaClass.getIdentifierGeneratorStrategy();
        String strategy = identifierGeneratorStrategy.getName();
        if ("sequence".equals(strategy)) {

//            @SequenceGenerator(name = "generator", sequenceName = "SEQ_ATTRIBUTE_CONFIG")
//                    @Id
//                    @GeneratedValue(strategy = SEQUENCE, generator = "generator")

            builder.resetAnnotation(importType("javax.persistence.GeneratedValue"))
                    .addAttribute("strategy", staticImport("javax.persistence.GenerationType", "SEQUENCE"))
                    .addQuotedAttribute("generator", "generator");
            idResult.append(builder.getResult());

            builder.resetAnnotation(importType("javax.persistence.SequenceGenerator"))
                    .addQuotedAttribute("name", "generator") // TODO: shouldn't this be unique, e.g. entityName + sequenceName (or just sequencename) ?
                    .addQuotedAttribute("sequenceName", identifierGeneratorStrategy.getValue());
            //	TODO HA does not support initialValue and allocationSize
            wholeString.append(builder.getResult());
        } else if ("identity".equals(strategy)) {
//            @Id
//            @GeneratedValue(strategy = IDENTITY)
            builder.resetAnnotation(importType("javax.persistence.GeneratedValue"));
            builder.addAttribute("strategy", staticImport("javax.persistence.GenerationType", "IDENTITY"));
            idResult.append(builder.getResult());
        }
        wholeString.append(idResult);

        return wholeString.toString();
    }

    public String generateAnnTableUniqueConstraint() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String generateManyToOneAnnotation(Property column) {
        importType("javax.persistence.FetchType");
        StringBuffer buffer = new StringBuffer(AnnotationBuilder.createAnnotation(importType("javax.persistence.ManyToOne"))
//                .addAttribute( "cascade", getCascadeTypes(property))
                .addAttribute("fetch", "FetchType.LAZY")
                .getResult());

        return buffer.toString();
    }

    public String generateCollectionAnnotation(Property column) {
        //    @OneToMany(fetch = FetchType.LAZY, mappedBy = "gcProcurementBatch")
        String fieldName = StringUtils.camel(metaClass.getClassName());
        char chars[] = fieldName.toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);

        importType("javax.persistence.FetchType");
        StringBuffer buffer = new StringBuffer("    " + AnnotationBuilder.createAnnotation(importType("javax.persistence.OneToMany"))
//                .addAttribute( "cascade", getCascadeTypes(property))
                .addAttribute("fetch", "FetchType.LAZY")
                .addAttribute("mappedBy", "\"" + new String(chars) + "\"")
                .getResult());

        return buffer.toString();
    }

    public String generateJoinColumnsAnnotation(Property column) {
        StringBuffer buffer = new StringBuffer(AnnotationBuilder.createAnnotation(importType("javax.persistence.JoinColumn"))
                .addAttribute("name", "\"" + column.getName() + "\"")
                .getResult());
        return buffer.toString();
    }

    public String generateBasicAnnotation(Property column) {
        StringBuffer annotations = new StringBuffer("    ");
        if (column != null) {
            String typeName = column.getSqlType();
            if (StringUtils.isNotEmpty(typeName)) typeName = typeName.toLowerCase();
            if (StringUtils.isEmpty(typeName)) return annotations.toString();
            if (column.getName().equals("version")) {
                buildVersionAnnotation(annotations);
            }
            if ("date".equals(typeName) || "java.sql.Date".equals(typeName)) {
                buildTemporalAnnotation(annotations, "DATE");
            } else if ("timestamp".equals(typeName) || "java.sql.Timestamp".equals(typeName)) {
                buildTemporalAnnotation(annotations, "TIMESTAMP");
            } else if ("time".equals(typeName) || "java.sql.Time".equals(typeName)) {
                buildTemporalAnnotation(annotations, "TIME");
            } //TODO: calendar etc. ?
        }
        return annotations.toString();
    }


    public List getAllPropertiesIterator() {
        return metaClass.getDeclaredProperties();
    }

    private StringBuffer buildTemporalAnnotation(StringBuffer annotations, String temporalTypeValue) {
        String temporal = importType("javax.persistence.Temporal");
        String temporalType = importType("javax.persistence.TemporalType");

        return annotations.append("@" + temporal + "(" + temporalType + "." + temporalTypeValue + ")");
    }

    private StringBuffer buildVersionAnnotation(StringBuffer annotations) {
        String version = importType("javax.persistence.Version");

        return annotations.append("@" + version);
    }

    public String getFieldName(Property p) {
        if (p.getPropertyType().getType().equalsIgnoreCase(ColumnType.ForeignKey.toString())) {

            String fieldName = StringUtils.camel(p.getReferenceName());
            char chars[] = fieldName.toCharArray();
            chars[0] = Character.toLowerCase(chars[0]);
            chars[1] = Character.toLowerCase(chars[1]);
            return new String(chars);
        }
        if (p.getPropertyType().getType().equalsIgnoreCase(ColumnType.PrimaryKeyRef.toString())) {
            String fieldName = StringUtils.camel(p.getChildReferenceNameName());
            char chars[] = fieldName.toCharArray();
            chars[0] = Character.toLowerCase(chars[0]);
            chars[1] = Character.toLowerCase(chars[1]);
            return new String(chars);
        } else {
            String fieldName = StringUtils.camel(p.getName());
            char chars[] = fieldName.toCharArray();
            chars[0] = Character.toLowerCase(chars[0]);
            return new String(chars);
        }
    }

    public String getJavaTypeName(Property p, boolean useGenerics) {
        String type = null;
        if (p.getPropertyType().getType().equalsIgnoreCase(ColumnType.ForeignKey.toString())) {
            return StringUtils.camel(p.getReferenceName());
        } else if (p.getPropertyType().getType().equalsIgnoreCase(ColumnType.PrimaryKeyRef.toString())) {
            this.importContext.importType("java.util.Set");
            return "Set<" + StringUtils.camel(p.getChildReferenceNameName()) + ">";
        }
        try {
            type = Cfg2JavaTool.getPreferredJavaType(p.getSqlType(), p.getLength(), true);
        } catch (UnsupportedDataTypeException e) {
            e.printStackTrace();
        }
        if (type != null) {
            this.importContext.importType(type);
        } else {
            log.info("p.getName(==" + p.getName());
        }

        return type;
    }

    public String getFieldInitialization(Property p, boolean useGenerics) {
        if (p.getPropertyType().getType().equalsIgnoreCase(ColumnType.PrimaryKeyRef.toString())) {
            importType("java.util.HashSet");
            return "new HashSet<" + StringUtils.camel(p.getChildReferenceNameName()) + ">(0);";
        }
        return p.getDefaultValue();
    }

    public String getPropertyGetModifiers() {
        return "public";
    }

    public String getGetterSignature(Property p) {
        // p.getSqlTypeCode()
        String prefix = null;
        if (p.getPropertyType().getType().equalsIgnoreCase(ColumnType.ForeignKey.toString())) {
            prefix = "get" + StringUtils.camel(p.getReferenceName());
        } else if (p.getPropertyType().getType().equalsIgnoreCase(ColumnType.PrimaryKeyRef.toString())) {
            prefix = "get" + StringUtils.camel(p.getChildReferenceNameName());
        } else {
            String type = SqlDataType.forType(p.getSqlType());
            prefix = type.equals("boolean") ? "is" : "get";
            prefix = prefix + StringUtils.camel(p.getName());
        }

        return prefix;
    }

    public String getSetterSignature(Property p) {
        // p.getSqlTypeCode()
        String prefix = null;
        if (p.getPropertyType().getType().equalsIgnoreCase(ColumnType.ForeignKey.toString())) {
            prefix = "set" + StringUtils.camel(p.getReferenceName());
        } else if (p.getPropertyType().getType().equalsIgnoreCase(ColumnType.PrimaryKeyRef.toString())) {
            prefix = "set" + StringUtils.camel(p.getChildReferenceNameName());
        } else {
//            String type = SqlDataType.forType(p.getSqlType());
//            prefix = type.equals("boolean") ? "is" : "set";
            prefix = "set" + StringUtils.camel(p.getName());
        }

        return prefix;
    }

    public String getPropertyName(Property p) {
        return beanCapitalize(p.getName());
    }

    static public String beanCapitalize(String fieldname) {
        if (fieldname == null || fieldname.length() == 0) {
            return fieldname;
        }

        if (fieldname.length() > 1 && Character.isUpperCase(fieldname.charAt(1))) {
            return fieldname;
        }
        char chars[] = fieldname.toCharArray();
        chars[0] = Character.toUpperCase(chars[0]);
        return new String(chars);
    }

    public String getPropertySetModifiers(Property p) {
        return "public";
    }

    public String getExtendsDeclaration() {
        return "";
    }

    public boolean needsToString() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getFieldJavaDoc(Property property, int indent) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getFieldDescription(Property property) {
        return null;  //先不实现
    }

    public boolean isManyToOne(Property property) {
        return property.getPropertyType().getType().equalsIgnoreCase(ColumnType.ForeignKey.toString());
    }

    public boolean isCollection(Property property) {
        return property.getPropertyType().getType().equalsIgnoreCase(ColumnType.PrimaryKeyRef.toString());
    }

    public Object getDecoratedObject() {
        return metaClass.getName();
    }

    public boolean isInterface() {
        return false;
    }

    public boolean isSubclass() {
        return false;
    }


    public boolean needsMinimalConstructor() {
        return false;
    }

    public boolean needsFullConstructor() {
        return false;
    }


    public boolean hasVersionProperty() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Property getVersionProperty() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Add fqcn to the import list. Returns fqcn as needed in source code.
     * Attempts to handle fqcn with array and generics references.
     * <p/>
     * e.g.
     * java.util.Collection<org.marvel.Hulk> imports java.util.Collection and returns Collection
     * org.marvel.Hulk[] imports org.marvel.Hulk and returns Hulk
     *
     * @param fqcn
     * @return import string
     */
    public String importType(String fqcn) {
        return importContext.importType(fqcn);
    }

    public String staticImport(String fqcn, String member) {
        return importContext.staticImport(fqcn, member);
    }

    public String generateImports() {
        return importContext.generateImports();
    }

}
