package org.kiwi.crius.tools.meta.pdm;

import cn.damai.crius.codegen.pdm.object.SequenceDocument;
import cn.damai.crius.codegen.pdm.object.TableDocument;
import org.kiwi.crius.tools.config.Configuration;
import org.kiwi.crius.tools.meta.IdentifierGeneratorStrategy;
import org.kiwi.crius.tools.meta.MetaType;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-4-3
 * Time: 上午11:25
 * To change this template use File | Settings | File Templates.
 */
public class OracleMetaDataDialect extends AbstractMetaDataDialect {
    public OracleMetaDataDialect(Configuration configuration) {
        super(configuration);
    }

    public IdentifierGeneratorStrategy getIdentifierGeneratorStrategy(String tableId){
        String type =  getMetaType();
        IdentifierGeneratorStrategy identifierGeneratorStrategy = null;
        if(MetaType.ORACLE.toString().equalsIgnoreCase(type)){
            identifierGeneratorStrategy = new IdentifierGeneratorStrategy("sequence",getSequence(tableId)) ;
        } else  {
            identifierGeneratorStrategy = super.getIdentifierGeneratorStrategy(tableId);
        }
        return identifierGeneratorStrategy;
    }

    private String getSequence(String tableId){
        String sequencename = null;
        String tablepathExpression = "//o:Table[@Id='"+tableId+"']";
        org.apache.xmlbeans.XmlObject[] xmlObjectsTable  =  getModelDocument().selectPath(namespaceDeclaration + tablepathExpression);

        if(xmlObjectsTable != null && xmlObjectsTable.length > 0) {
            TableDocument.Table table   =( TableDocument.Table )xmlObjectsTable[0];
            String pathExpression = "./c:Columns/o:Column/c:Sequence/o:Sequence";

            org.apache.xmlbeans.XmlObject[] xmlObjects = table.selectPath(namespaceDeclaration + pathExpression);
            SequenceDocument.Sequence sequence = ((SequenceDocument.Sequence) xmlObjects[0]);

            String keyPathExpression = "//c:Sequences/o:Sequence[@Id='" + sequence.getRef() + "']";
            org.apache.xmlbeans.XmlObject[] sequencexmlObjects = getModelDocument().selectPath(namespaceDeclaration + keyPathExpression);

            if(sequencexmlObjects != null && sequencexmlObjects.length>0) {
                SequenceDocument.Sequence sequence1 =(  SequenceDocument.Sequence )sequencexmlObjects[0];
                sequencename=sequence1.getCode();
            }
        }
        return sequencename;
    }
}
