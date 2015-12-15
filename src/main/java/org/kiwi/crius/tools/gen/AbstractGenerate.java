package org.kiwi.crius.tools.gen;

import cn.damai.crius.tools.*;
import org.kiwi.crius.tools.*;
import org.kiwi.crius.tools.config.Configuration;
import org.kiwi.crius.tools.meta.MetaClass;
import org.kiwi.crius.tools.modle.DomainPOJOClass;
import org.kiwi.crius.tools.modle.POJOClass;
import org.hibernate.internal.util.ReflectHelper;
import org.hibernate.internal.util.StringHelper;

import java.io.File;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-4-2
 * Time: 下午12:05
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractGenerate implements Generate {
    private Cfg2JavaTool c2j;
    private ArtifactCollector collector = new ArtifactCollector();
    private String templateName;
    private String filePattern;
    private TemplateHelper vh;
    private Configuration configuration;
    private File outputdir;
    private Properties properties = new Properties();
    protected String[] templatePaths = new String[0];
    private Iterator iterator;
    private String forEach;

    static abstract class ModelIterator {
        abstract void process(AbstractGenerate ge);
    }


    static Map modelIterators = new HashMap();
    static {
        modelIterators.put("entity", new ModelIterator() {

            void process(AbstractGenerate ge) {

                List<POJOClass> pojoList = ge.getPOJOList(ge.getConfiguration().getMetaClasses());
                Map additionalContext = new HashMap();
               for(POJOClass element:pojoList){
                    ge.exportPersistentClass( additionalContext, element );
                }
            }
        });

    }

    public AbstractGenerate(Configuration cfg, File outputdir) {
        this();
        setConfiguration(cfg);
        setOutputDirectory(outputdir);

    }



    /**
     * @param file basedirectory to be used for generated files.
     */
    public void setOutputDirectory(File file) {
        outputdir = file;
    }

    public File getOutputDirectory() {
        return outputdir;
    }
    /**
     * @param properties set of properties to be used by exporter.
     */
    public void setProperties(Properties properties) {
       this.properties =properties;
    }

    public Properties getProperties() {
        return properties;
    }
    public AbstractGenerate() {
        c2j = new Cfg2JavaTool();
    }
    protected void exportComponent(Map additionalContext, POJOClass element) {
        exportPOJO(additionalContext, element);
    }

    protected void exportPersistentClass(Map additionalContext, POJOClass element) {
        exportPOJO(additionalContext, element);
    }

    protected void exportPOJO(Map additionalContext, POJOClass element) {
        TemplateProducer producer = new TemplateProducer(getTemplateHelper(),getArtifactCollector());
        additionalContext.put("pojo", element);
        additionalContext.put("clazztablename", element.getDecoratedObject());
        String filename = resolveFilename( element );
//        if(filename.endsWith(".java") && filename.indexOf('$')>=0) {
//            log.warn("Filename for " + getClassNameForFile( element ) + " contains a $. Innerclass generation is not supported.");
//        }
        producer.produce(additionalContext, getTemplateName(), new File(getOutputDirectory(),filename), templateName, element.toString());
    }
    protected String resolveFilename(POJOClass element) {
        String filename = StringHelper.replace(filePattern, "{class-name}", getClassNameForFile(element));
        String packageLocation = StringHelper.replace(getPackageNameForFile( element ),".", "/");
        if(StringHelper.isEmpty(packageLocation)) {
            packageLocation = "."; // done to ensure default package classes doesn't end up in the root of the filesystem when outputdir=""
        }
        filename = StringHelper.replace(filename, "{package-name}", packageLocation);
        return filename;
    }

    protected String getPackageNameForFile(POJOClass element) {
        return element.getPackageName();
    }
    protected String getClassNameForFile(POJOClass element) {
        return element.getDeclarationName();
    }
    public void setConfiguration(Configuration cfg) {
        configuration = cfg;
    }

    public Configuration getConfiguration() {
        return configuration;
    }
    public Cfg2JavaTool getCfg2JavaTool() {
        return c2j;
    }
    public List<POJOClass> getPOJOList(List<MetaClass> metaClasses){
        List<POJOClass> pojoClasses = new ArrayList<POJOClass>();
        for(MetaClass metaClass:metaClasses) {
            DomainPOJOClass domainPOJOClass = new DomainPOJOClass(metaClass) ;
            pojoClasses.add(domainPOJOClass);
        }

      return pojoClasses;
    }
    public void setArtifactCollector(ArtifactCollector collector) {
        this.collector = collector;
    }

    public ArtifactCollector getArtifactCollector() {
        return collector;
    }
    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }
    protected void setTemplateHelper(TemplateHelper vh) {
        this.vh = vh;
    }

    protected TemplateHelper getTemplateHelper() {
        return vh;
    }

    public void setFilePattern(String filePattern) {
        this.filePattern = filePattern;
    }

    public String getFilePattern() {
        return filePattern;
    }
    protected void setupTemplates() {

        getTemplateHelper().init(getOutputDirectory(), templatePaths);
    }
    public String[] getTemplatePaths() {
        return templatePaths;
    }


    /**
     * @param templatePath array of directories used sequentially to lookup templates
     */
    public void setTemplatePath(String[] templatePath) {
        templatePaths =templatePath;
    }

    public String[] getTemplatePath() {
        return new String[0];  //To change body of implemented methods use File | Settings | File Templates.
    }
    public void setForEach(String foreach) {
        this.forEach = foreach;
    }

    /**
     * Setup the context variables used by the exporter. Subclasses should call super.setupContext() to ensure all needed variables are in the context.
     **/
    protected void setupContext() {
        getTemplateHelper().setupContext();
        getTemplateHelper().putInContext("exporter", this);
       // getTemplateHelper().putInContext("c2h", getCfg2HbmTool());
        getTemplateHelper().putInContext("c2j", getCfg2JavaTool());

        if(getOutputDirectory()!=null) getTemplateHelper().putInContext("outputdir", getOutputDirectory());
        if(getTemplatePaths()!=null) getTemplateHelper().putInContext("template_path", getTemplatePaths());

        if(getProperties()!=null) {
            iterator = getProperties().entrySet().iterator();
            while ( iterator.hasNext() ) {
                Map.Entry element = (Map.Entry) iterator.next();
                String key = element.getKey().toString();
                Object value = transformValue(element.getValue());
                getTemplateHelper().putInContext(key, value);
                if(key.startsWith(ExporterSettings.PREFIX_KEY)) {
                    getTemplateHelper().putInContext(key.substring(ExporterSettings.PREFIX_KEY.length()), value);
                    if(key.endsWith(".toolclass")) {
                        try {
                            Class toolClass = ReflectHelper.classForName(value.toString(), this.getClass());
                            Object object = toolClass.newInstance();
                            getTemplateHelper().putInContext(key.substring(ExporterSettings.PREFIX_KEY.length(),key.length()-".toolclass".length()), object);
                        }
                        catch (Exception e) {
                            throw new ExporterException("Exception when instantiating tool " + element.getKey() + " with " + value,e);
                        }
                    }
                }
            }
        }
        getTemplateHelper().putInContext("artifacts", collector);
        if(getConfiguration()!=null) {
            getTemplateHelper().putInContext("cfg", getConfiguration());
        }
    }
    private Object transformValue(Object value) {
        if("true".equals(value)) {
            return Boolean.TRUE;
        }
        if("false".equals(value)) {
            return Boolean.FALSE;
        }
        return value;
    }
    protected void doStart() {

        if(filePattern==null) {
            throw new ExporterException("File pattern not set on " + this.getClass());
        }
        if(templateName==null) {
            throw new ExporterException("Template name not set on " + this.getClass());
        }

        List exporters = new ArrayList();

        if(StringHelper.isEmpty( forEach )) {
            if(filePattern.indexOf("{class-name}")>=0) {
                exporters.add( modelIterators.get( "entity" ) );
                //exporters.add( modelIterators.get( "component") );
            } else {
                exporters.add( modelIterators.get( "configuration" ));
            }
        } else {
            StringTokenizer tokens = new StringTokenizer(forEach, ",");

            while ( tokens.hasMoreTokens() ) {
                String nextToken = tokens.nextToken();
                Object object = modelIterators.get(nextToken);
                if(object==null) {
                    throw new ExporterException("for-each does not support [" + nextToken + "]");
                }
                exporters.add( object );
            }
        }

        Iterator it = exporters.iterator();
        while(it.hasNext()) {
            ModelIterator mit = (ModelIterator) it.next();
            mit.process( this );
        }
    }

    protected void cleanUpContext() {
        if(getProperties()!=null) {
            iterator = getProperties().entrySet().iterator();
            while ( iterator.hasNext() ) {
                Map.Entry element = (Map.Entry) iterator.next();
                Object value = transformValue(element.getValue());
                String key = element.getKey().toString();
                if(key.startsWith(ExporterSettings.PREFIX_KEY)) {
                    getTemplateHelper().removeFromContext(key.substring(ExporterSettings.PREFIX_KEY.length()), value);
                }
                getTemplateHelper().removeFromContext(key, value);
            }
        }

        if(getOutputDirectory()!=null) getTemplateHelper().removeFromContext("outputdir", getOutputDirectory());
        if(getTemplatePaths()!=null) getTemplateHelper().removeFromContext("template_path", getTemplatePaths());

        getTemplateHelper().removeFromContext("exporter", this);
        getTemplateHelper().removeFromContext("artifacts", collector);
        if(getConfiguration()!=null) {
            getTemplateHelper().removeFromContext("cfg", getConfiguration());
        }

      //  getTemplateHelper().removeFromContext("c2h", getCfg2HbmTool());
        getTemplateHelper().removeFromContext("c2j", getCfg2JavaTool());
    }
    /**
     * Called when exporter should start generating its output
     */
    public void start() {
        setTemplateHelper( new TemplateHelper() );
        setupTemplates();
        setupContext();
        doStart();
        cleanUpContext();
        setTemplateHelper(null);
    }
    
    
}
