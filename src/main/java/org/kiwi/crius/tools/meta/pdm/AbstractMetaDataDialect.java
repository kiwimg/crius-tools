package org.kiwi.crius.tools.meta.pdm;

import cn.damai.crius.codegen.pdm.ModelDocument;
import cn.damai.crius.codegen.pdm.collection.ReferencesDocument;
import cn.damai.crius.codegen.pdm.collection.TablesDocument;
import cn.damai.crius.codegen.pdm.object.ColumnDocument;
import cn.damai.crius.codegen.pdm.object.KeyDocument;
import cn.damai.crius.codegen.pdm.object.TableDocument;
import org.kiwi.crius.tools.config.Configuration;
import org.kiwi.crius.tools.meta.IdentifierGeneratorStrategy;
import org.kiwi.crius.tools.meta.MetaType;
import org.kiwi.crius.tools.meta.mapping.Column;
import org.kiwi.crius.tools.meta.mapping.ManyToOne;
import org.kiwi.crius.tools.meta.mapping.OneToMany;
import org.kiwi.crius.tools.meta.mapping.Table;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.hibernate.mapping.PrimaryKey;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-4-2
 * Time: 下午12:53
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractMetaDataDialect implements MetaDataDialect {
    protected static final String namespaceAttribute = "attribute.pdm.codegen.crius.damai.cn";
    protected static final String namespaceCollection = "collection.pdm.codegen.crius.damai.cn";
    protected static final String namespaceObject = "object.pdm.codegen.crius.damai.cn";
    protected static final String namespaceDeclaration = "declare namespace c='" + namespaceCollection + "';declare namespace o='" + namespaceObject + "';declare namespace a='" + namespaceAttribute + "';";
    private ModelDocument modelDocument = null;
    private HashMap<String, Table> tablesMap;
    private static XmlOptions opts = null;

    static {
        HashMap suggestedPrefixes = new HashMap();
        suggestedPrefixes.put("attribute", "attribute.pdm.codegen.crius.damai.cn");
        suggestedPrefixes.put("collection", "collection.pdm.codegen.crius.damai.cn");
        suggestedPrefixes.put("object", "object.pdm.codegen.crius.damai.cn");
        opts = new XmlOptions();
        opts.setLoadSubstituteNamespaces(suggestedPrefixes);
    }

    protected AbstractMetaDataDialect(Configuration configuration) {
        tablesMap = new HashMap<String, Table>();
        try {
            modelDocument = ModelDocument.Factory.parse(new File(configuration.getModelSrc()), opts);
        } catch (XmlException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public IdentifierGeneratorStrategy getIdentifierGeneratorStrategy(String tableId) {
        //String type =  getMetaType();
        return new IdentifierGeneratorStrategy("identity", "identity");
    }

    public List<Table> getTables() {
        TablesDocument.Tables tables = getModelDocument().getModel().getRootObject().getChildren().getModel().getTables();
        String schema = getModelDocument().getModel().getRootObject().getChildren().getModel().getCode();
        ArrayList<Table> arrayList = new ArrayList<Table>();
        for (int i = 0; i < tables.getTableArray().length; i++) {
            TableDocument.Table tablexsd = tables.getTableArray()[i];
            Table table = new Table();

            table.setName(tablexsd.getCode());
            table.setComment(tablexsd.getComment());
            table.setSchema(schema);
            for (int j = 0; j < tablexsd.getColumns().sizeOfColumnArray(); j++) {
                ColumnDocument.Column columnxsd = tablexsd.getColumns().getColumnArray(j);
                Column column = parserColumn(columnxsd, table);
                table.addColumn(column);
            }

            cn.damai.crius.codegen.pdm.object.KeyDocument.Key keyxsd = tablexsd.getPrimaryKey().getKey();
            if (keyxsd != null) {
                String keyPathExpression = "//c:Keys/o:Key[@Id='" + tablexsd.getPrimaryKey().getKey().getRef().getStringValue() + "']";
                org.apache.xmlbeans.XmlObject[] xmlObjectsKey = getModelDocument().selectPath(namespaceDeclaration + keyPathExpression);
                if (xmlObjectsKey != null && xmlObjectsKey.length > 0) {
                    String refColumnId = ((KeyDocument.Key) xmlObjectsKey[0]).getKeyColumns().getColumn().getRef();
                    String columnPathExpression = "//c:Columns/o:Column[@Id='" + refColumnId + "']";
                    org.apache.xmlbeans.XmlObject[] columnXmlObjects = getModelDocument().selectPath(namespaceDeclaration + columnPathExpression);

                    PrimaryKey primaryKey = new PrimaryKey();
                    primaryKey.setName(((ColumnDocument.Column) columnXmlObjects[0]).getCode());
                    //primaryKey.setTable(table);
                    table.setPrimaryKey(primaryKey);
                }
            }

            arrayList.add(table);
            tablesMap.put(tablexsd.getCode(), table);
        }
        return arrayList;
    }

    public String getReferencedEntityName(String columnId) {
        String referencedEntityName = null;
        ReferencesDocument.References references = getModelDocument().getModel().getRootObject().getChildren().getModel().getReferences();
        String columnPathExpression2 = "./o:Reference/c:Joins/o:ReferenceJoin/c:Object2/o:Column[@Ref='" + columnId + "']"; //外键列
        org.apache.xmlbeans.XmlObject[] columnXmlObjects = references.selectPath(namespaceDeclaration + columnPathExpression2);
        NodeList nodeList = ((XmlObject) columnXmlObjects[0]).getDomNode().getParentNode().getParentNode().getParentNode().getParentNode().getChildNodes();
        String ref = null;
        for (int j = 0; j < nodeList.getLength(); j++) {
            Node node = nodeList.item(j);
            if ("c:ParentTable".equalsIgnoreCase(node.getNodeName())) {

                Node oTableNode = node.getChildNodes().item(1);
                ref = oTableNode.getAttributes().item(0).getNodeValue();
            }

        }
        if (ref != null) {
            String pablePtabathExpression = "//c:Tables/o:Table[@Id='" + ref + "']"; //外键列
            org.apache.xmlbeans.XmlObject[] xmlObjects = modelDocument.selectPath(namespaceDeclaration + pablePtabathExpression);

            TableDocument.Table tablexsd = (TableDocument.Table) xmlObjects[0];
            referencedEntityName = tablexsd.getCode();
        }
        return referencedEntityName;
    }

    public String getChildTableEntityName(String columnId) {
        String referencedEntityName = null;
        ReferencesDocument.References references = getModelDocument().getModel().getRootObject().getChildren().getModel().getReferences();
        String columnPathExpression2 = "./o:Reference/c:Joins/o:ReferenceJoin/c:Object1/o:Column[@Ref='" + columnId + "']"; //外键列
        org.apache.xmlbeans.XmlObject[] columnXmlObjects = references.selectPath(namespaceDeclaration + columnPathExpression2);
        NodeList nodeList = ((XmlObject) columnXmlObjects[0]).getDomNode().getParentNode().getParentNode().getParentNode().getParentNode().getChildNodes();
        String ref = null;
        for (int j = 0; j < nodeList.getLength(); j++) {
            Node node = nodeList.item(j);
            if ("c:ChildTable".equalsIgnoreCase(node.getNodeName())) {

                Node oTableNode = node.getChildNodes().item(1);
                ref = oTableNode.getAttributes().item(0).getNodeValue();
            }

        }
        if (ref != null) {
            String pablePtabathExpression = "//c:Tables/o:Table[@Id='" + ref + "']"; //外键列
            org.apache.xmlbeans.XmlObject[] xmlObjects = modelDocument.selectPath(namespaceDeclaration + pablePtabathExpression);

            TableDocument.Table tablexsd = (TableDocument.Table) xmlObjects[0];
            referencedEntityName = tablexsd.getCode();
        }
        return referencedEntityName;
    }

    public ModelDocument getModelDocument() {
        return modelDocument;
    }

    private Column parserColumn(ColumnDocument.Column columnxsd, Table table) {

        Column column = new Column();
        String id = columnxsd.getId();
        column.setId(id);
        cn.damai.crius.codegen.pdm.object.ModelDocument.Model model = getModelDocument().getModel().getRootObject().getChildren().getModel();

        String columnPathExpression1 = "./o:Reference/c:Joins/o:ReferenceJoin/c:Object1/o:Column[@Ref='" + id + "']";//主键列
        String columnPathExpression2 = "./o:Reference/c:Joins/o:ReferenceJoin/c:Object2/o:Column[@Ref='" + id + "']"; //外键列
        ReferencesDocument.References references = model.getReferences();

        if (references != null) {
            org.apache.xmlbeans.XmlObject[] columnXmlObjects1 = references.selectPath(namespaceDeclaration + columnPathExpression1);
            if (columnXmlObjects1 != null && columnXmlObjects1.length > 0) {
                column.setValue(new OneToMany());
            }
            org.apache.xmlbeans.XmlObject[] columnXmlObjects2 = references.selectPath(namespaceDeclaration + columnPathExpression2);
            if (columnXmlObjects2 != null && columnXmlObjects2.length > 0) {
                column.setValue(new ManyToOne());
                // column.setReferencedEntityName();
            }
        }
        column.setName(columnxsd.getCode());
        column.setLength(columnxsd.getLength());
        column.setPrecision(columnxsd.getLength());
        column.setSqlType(getSqlType(columnxsd.getDataType()));
        column.setComment(columnxsd.getComment());
        column.setDefaultValue(columnxsd.getDefaultValue());
        column.setNullable(columnxsd.getColumnMandatory() == 1);

        int scale = columnxsd.getPrecision();
        column.setScale(scale);
        return column;
    }

    public String getSqlType(String pdmDatatype) {

        int i = pdmDatatype.lastIndexOf('(');

        if (i > -1) {
            return pdmDatatype.substring(0, i);
        } else {
            return pdmDatatype;
        }

    }

    public int getScale(String pdmDatatype) {
        int i = pdmDatatype.lastIndexOf('(');
        if (i > -1) {
            int j = pdmDatatype.lastIndexOf(')');
            String[] strings = pdmDatatype.substring(i + 1, j).split(",");
            int scale = 0;
            if (strings.length == 2) {
                scale = Integer.parseInt(strings[1]);
            }
            return scale;
        } else {
            return -1;
        }
    }

    public String getMetaType() {
        String[] names = modelDocument.getModel().getRootObject().getChildren().getModel().getDBMS().getShortcut().getName().split(" ");
        if (names == null) {
            throw new TypeNotPresentException("PDM没有设置数据库类型", new Throwable("PDM没有设置数据库类型"));
        }
        String name = names[0];

        return MetaType.valueOf(name.toUpperCase()).toString();
    }

    public static void main(String[] args) {
        String d = "MySQL 5.0";
        System.out.println(MetaType.valueOf("ORACLEx").toString());

    }
}
