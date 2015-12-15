package org.kiwi.crius.tools;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-4-2
 * Time: 上午10:54
 * To change this template use File | Settings | File Templates.
 */
public class ExporterException extends RuntimeException {

    public ExporterException() {
        super();
    }
    public ExporterException(String message) {
        super(message);
    }
    public ExporterException(String message, Throwable cause) {
        super(message, cause);
    }
    public ExporterException(Throwable cause) {
        super(cause);
    }
}

