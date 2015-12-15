package org.kiwi.crius.tools.meta;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-4-8
 * Time: 下午4:55
 * To change this template use File | Settings | File Templates.
 */
public class IdentifierGeneratorStrategy {
    private String name;
    private String value;

    public IdentifierGeneratorStrategy(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
