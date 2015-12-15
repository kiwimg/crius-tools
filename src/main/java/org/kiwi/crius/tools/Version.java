package org.kiwi.crius.tools;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-4-2
 * Time: 上午10:54
 * To change this template use File | Settings | File Templates.
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

final public class Version {

    public static final String VERSION = "1.0.0";

    private static final Version instance = new Version();

    private static Log log = LogFactory.getLog( Version.class );

    static {
        log.info( "crius Tools " + VERSION );
    }

    private Version() {
        // dont instantiate me
    }

    public String getVersion() {
        return VERSION;
    }

    public static Version getDefault() {
        return instance;
    }

    public String toString() {
        return getVersion();
    }

    public static void touch() {}

    public static void main(String[] args) {
        System.out.println(new Version());
    }
}

