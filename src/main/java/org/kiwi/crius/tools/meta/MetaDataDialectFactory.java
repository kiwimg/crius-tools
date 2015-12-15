package org.kiwi.crius.tools.meta;

import org.kiwi.crius.tools.StringUtils;
import org.kiwi.crius.tools.config.Configuration;
import org.kiwi.crius.tools.meta.mapping.*;
import org.kiwi.crius.tools.meta.oom.OomMetaDataDialect;
import org.kiwi.crius.tools.meta.pdm.MetaDataDialect;
import org.kiwi.crius.tools.meta.pdm.OracleMetaDataDialect;
import org.hibernate.mapping.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-4-3
 * Time: 下午4:37
 * To change this template use File | Settings | File Templates.
 */
public class MetaDataDialectFactory {
    private static MetaDataDialectFactory ourInstance = new MetaDataDialectFactory();

    public static MetaDataDialectFactory getInstance() {
        return ourInstance;
    }
 
    private MetaDataDialectFactory() {
    }

    public MetaDataDialect createMetaDataDialect(Configuration configuration) {
        return new OracleMetaDataDialect(configuration);
    }
    
    /**
     *
     * @param configuration
     * @return
     * @author xujun -xujun@damai.cn
     */
    public OomMetaDataDialect createOomMetaDataDialect(Configuration configuration) {
        return new OomMetaDataDialect(configuration);
    }
    
    

    public List<MetaClass> getMetaClasses(Configuration configuration) {
        MetaDataDialect metaDataDialect = createMetaDataDialect(configuration);
        List<Table> tables = metaDataDialect.getTables();
        List<MetaClass> metaClasses = new ArrayList<MetaClass>();
        MetaClass metaClass = null;

        for (Table table : tables) {
            metaClass = new MetaClass();
            metaClass.setName(table.getName());
            metaClass.setClassName(StringUtils.camel(table.getName()));
            metaClass.setPackageName(configuration.getPackageName());
            metaClass.setMetaType(metaDataDialect.getMetaType());
            List<Column> columnIterator = table.getColumns();
            List<Property> propertyList = new ArrayList<Property>();

            PrimaryKey primaryKey = table.getPrimaryKey();

            for (Column column : columnIterator) {

                Property property = new Property();

                Value value = column.getValue();
                if (value != null && value instanceof ManyToOne) {
                    property.setPropertyType(ColumnType.ForeignKey);
                    property.setName(column.getName());
                    property.setSqlType("Object");
                    String referencedEntityName = metaDataDialect.getReferencedEntityName(column.getId());
                    if (referencedEntityName != null) {
                        property.setReferenceName(referencedEntityName);
                    }
                    propertyList.add(property);
                } else{
                    if (primaryKey != null && primaryKey.getName().equalsIgnoreCase(column.getName())) {
                        property.setPropertyType(ColumnType.PrimaryKey);
                    } else {
                        property.setPropertyType(ColumnType.Column);
                    }
                    property.setComment(column.getComment());
                    property.setName(column.getName());
                    property.setDefaultValue(column.getDefaultValue());
                    property.setSqlType(column.getSqlType());
                    property.setPrecision(column.getPrecision());
                    property.setLength(column.getLength());
                    property.setNullable(column.isNullable());
                    property.setScale(column.getScale());
                    propertyList.add(property);
                    if(value != null && value instanceof OneToMany){

                        Property propertyOneToMany = new Property();
                        propertyOneToMany.setPropertyType(ColumnType.PrimaryKeyRef);
                        propertyOneToMany.setName(column.getName());
                        propertyOneToMany.setSqlType("Object");
                        String referencedEntityName = metaDataDialect.getChildTableEntityName(column.getId());
                        if (referencedEntityName != null) {
                            propertyOneToMany.setChildReferenceNameName(referencedEntityName);
                        }
                        propertyList.add(propertyOneToMany);
                    }
                }
            }
            metaClass.setDeclaredProperties(propertyList);
            metaClass.setIdentifierGeneratorStrategy(metaDataDialect.getIdentifierGeneratorStrategy(table.getId()));

            metaClasses.add(metaClass);
        }
        return metaClasses;
    }


}
