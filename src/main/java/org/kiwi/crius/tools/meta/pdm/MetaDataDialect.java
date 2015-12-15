package org.kiwi.crius.tools.meta.pdm;

import org.kiwi.crius.tools.meta.IdentifierGeneratorStrategy;
import org.kiwi.crius.tools.meta.mapping.Table;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-4-2
 * Time: 下午12:52
 * To change this template use File | Settings | File Templates.
 */
public interface MetaDataDialect {

    public List<Table> getTables();

    public String getReferencedEntityName(String columnId);
    public String getChildTableEntityName(String columnId);
    public String getMetaType();

    public IdentifierGeneratorStrategy getIdentifierGeneratorStrategy(String tableId);

}
