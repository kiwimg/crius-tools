package org.kiwi.crius.tools.gen;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.internal.util.ReflectHelper;
import org.hibernate.internal.util.StringHelper;

import org.kiwi.crius.tools.TemplateHelper;
import org.kiwi.crius.tools.TemplateProducer;
import org.kiwi.crius.tools.config.Configuration;
import org.kiwi.crius.tools.meta.mapping.oom.InterfaceInfo;
import org.kiwi.crius.tools.modle.OomPOJOClass;

public class OomCodeGenerate  extends AbstractGenerate {

	 private static final String OOM_JAVAOOP_FTL = "org/kiwi/crius/tools/template/oom/oom.ftl";
	 
	 public OomCodeGenerate(Configuration cfg) {
	        super(cfg, new File(cfg.getOutDir()));
	        init();
	    }

	    protected void init() {
	        setTemplateName(OOM_JAVAOOP_FTL);
	        setFilePattern("{package-name}/{class-name}.java");

	    }

	    public OomCodeGenerate() {
	        init();
	    }
	    
	    @Override
	    protected void setupContext() {
	    	//System.out.println("fasdfadsfadsf");
	        //TODO: this safe guard should be in the root templates instead for each variable they depend on.
	        if(!getProperties().containsKey("ejb3")) {
	            getProperties().put("ejb3", "false");
	        }
	        if(!getProperties().containsKey("jdk5")) {
	            getProperties().put("jdk5", "false");
	        }
	       
	        getTemplateHelper().setupContext();
	        getTemplateHelper().putInContext("exporter", this);
	       // getTemplateHelper().putInContext("c2h", getCfg2HbmTool());
	        getTemplateHelper().putInContext("c2j", getCfg2JavaTool());

	        if(getOutputDirectory()!=null) getTemplateHelper().putInContext("outputdir", getOutputDirectory());
	        if(getTemplatePaths()!=null) getTemplateHelper().putInContext("template_path", getTemplatePaths());
	        //getTemplateHelper().putInContext("artifacts", collector);
	        if(getConfiguration()!=null) {
	            getTemplateHelper().putInContext("cfg", getConfiguration());
	        }
	        
	    }
	    
	    public void start() {
	    	//TemplateHelper templateHelper= ;
	        setTemplateHelper( new TemplateHelper() );
	        setupTemplates();
	        setupContext();
	        
	        doStart();
	      //  cleanUpContext();
	        //setTemplateHelper(null);
	    }
	    
	    @Override
	    protected void doStart(){
	    	Map map =new HashMap();
	    	map.put("package", getConfiguration().getPackageName());
	    	for(InterfaceInfo ifi:getConfiguration().getInterfaces()){
	    		export(map,ifi);
	    	}
	    	
	    	 
	    }
	    
	    protected void export(Map additionalContext,InterfaceInfo element) {
	        TemplateProducer producer = new TemplateProducer(getTemplateHelper(),getArtifactCollector());
	        additionalContext.put("oom", new OomPOJOClass(element));
	        String filename =resolveFileName(element);
	        producer.produce(additionalContext, getTemplateName(), new File(getOutputDirectory(),filename),  getTemplateName(), element.toString());
	      //  producer.produce(additionalContext, getTemplateName(), new File(getOutputDirectory(),filename), getTemplateName(), element.toString());
	    }
	    
	    protected String resolveFileName(InterfaceInfo element) {
	        String filename = StringHelper.replace(getFilePattern(), "{class-name}", element.getName());
	        String packageLocation = StringHelper.replace(getConfiguration().getPackageName()+".dao",".", "/");
	        if(StringHelper.isEmpty(packageLocation)) {
	            packageLocation = "."; // done to ensure default package classes doesn't end up in the root of the filesystem when outputdir=""
	        }
	        filename = StringHelper.replace(filename, "{package-name}", packageLocation);
	        return filename;
	    }
	    
}
