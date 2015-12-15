package org.kiwi.crius.tools.gen;

import java.io.File;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-4-2
 * Time: 上午10:58
 * To change this template use File | Settings | File Templates.
 */
public interface Generate {


    /**
     * @param file basedirectory to be used for generated files.
     */
    public void setOutputDirectory(File file);

    public File getOutputDirectory();

    /**
     * @param templatePath array of directories used sequentially to lookup templates
     */
    public void setTemplatePath(String[] templatePath);

    public String[] getTemplatePath();

    /**
     *
     * @param properties set of properties to be used by exporter.
     */
    public void setProperties(Properties properties);

    public Properties getProperties();


    /**
     * Called when exporter should start generating its output
     */
    public void start();

}
