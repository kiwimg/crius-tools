package org.kiwi.crius.tools.meta;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-4-6
 * Time: 下午2:50
 * To change this template use File | Settings | File Templates.
 */
public class MetaClass {
    private String packageName;
    private String className;
    private List declaredProperties;
    private String name;
    private String metaType;
    private IdentifierGeneratorStrategy  identifierGeneratorStrategy;


    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public List getDeclaredProperties() {
        return declaredProperties;
    }

    public void setDeclaredProperties(List declaredProperties) {
        this.declaredProperties = declaredProperties;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMetaType() {
        return metaType;
    }

    public void setMetaType(String metaType) {
        this.metaType = metaType;
    }

    public IdentifierGeneratorStrategy getIdentifierGeneratorStrategy() {
        return identifierGeneratorStrategy;
    }

    public void setIdentifierGeneratorStrategy(IdentifierGeneratorStrategy identifierGeneratorStrategy) {
        this.identifierGeneratorStrategy = identifierGeneratorStrategy;
    }

}
