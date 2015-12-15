package org.kiwi.crius.tools.meta;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-4-8
 * Time: 下午3:11
 * To change this template use File | Settings | File Templates.
 */
public enum MetaType {
    //1图片2文件3压缩包4其他
    ORACLE("ORACLE"), MYSQL("MYSQL");
    private String name;


    private MetaType(String name) {
        this.name = name;

    }
    public String getType() {
        return name;
    }

    public String toString() {
        return String.valueOf(this.name);
    }
//    public static String valueOf(String name) {
//        String name = null;
//        for (MetaType c : MetaType.values()) {
//            if (c.getType().equalsIgnoreCase(name)) {
//                name = c.getName();
//                break;
//            }
//        }
//        return name;
//    }
}
