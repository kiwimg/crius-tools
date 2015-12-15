package org.kiwi.crius.tools;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-4-2
 * Time: 下午12:08
 * To change this template use File | Settings | File Templates.
 */
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.util.Iterator;
import java.util.Map;


public class TemplateProducer {
    private static final  Log log = LogFactory.getLog(TemplateProducer.class);
//    private static final Logger log = LoggerFactory.getLogger(TemplateProducer.class);
    private final TemplateHelper th;
    private ArtifactCollector ac;

    public TemplateProducer(TemplateHelper th, ArtifactCollector ac) {
        this.th = th;
        this.ac = ac;
    }

    public void produce(Map additionalContext, String templateName, File destination, String identifier, String fileType, String rootContext) {

        String tempResult = produceToString( additionalContext, templateName, rootContext );

        if(tempResult.trim().length()==0) {
            //log.warn("Generated output is empty. Skipped creation for file " + destination);
            return;
        }
        FileWriter fileWriter = null;
        try {

            th.ensureExistence( destination );

            ac.addFile(destination, fileType);
            //log.debug("Writing " + identifier + " to " + destination.getAbsolutePath() );
            fileWriter = new FileWriter(destination);
            fileWriter.write(tempResult);
            log.info(destination.getName());
        }
        catch (Exception e) {
            throw new ExporterException("Error while writing result to file", e);
        } finally {
            if(fileWriter!=null) {
                try {
                    fileWriter.flush();
                    fileWriter.close();
                }
                catch (IOException e) {
                    //log.warn("Exception while flushing/closing " + destination,e);
                }
            }
        }

    }


    private String produceToString(Map additionalContext, String templateName, String rootContext) {
        Map contextForFirstPass = additionalContext;
        putInContext( th, contextForFirstPass );
        StringWriter tempWriter = new StringWriter();
        BufferedWriter bw = new BufferedWriter(tempWriter);
        // First run - writes to in-memory string
        th.processTemplate(templateName, bw, rootContext);
        removeFromContext( th, contextForFirstPass );
        try {
            bw.flush();
        }
        catch (IOException e) {
            throw new RuntimeException("Error while flushing to string",e);
        }
        return tempWriter.toString();
    }

    private void removeFromContext(TemplateHelper templateHelper, Map context) {
        Iterator iterator = context.entrySet().iterator();
        while ( iterator.hasNext() ) {
            Map.Entry element = (Map.Entry) iterator.next();
            templateHelper.removeFromContext((String) element.getKey(), element.getValue());
        }
    }

    private void putInContext(TemplateHelper templateHelper, Map context) {
        Iterator iterator = context.entrySet().iterator();
        while ( iterator.hasNext() ) {
            Map.Entry element = (Map.Entry) iterator.next();
            templateHelper.putInContext((String) element.getKey(), element.getValue());
        }
    }

    public void produce(Map additionalContext, String templateName, File outputFile, String identifier) {
        String fileType = outputFile.getName();
        fileType = fileType.substring(fileType.indexOf('.')+1);
        produce(additionalContext, templateName, outputFile, identifier, fileType, null);
    }

    public void produce(Map additionalContext, String templateName, File outputFile, String identifier, String rootContext) {
        String fileType = outputFile.getName();
        fileType = fileType.substring(fileType.indexOf('.')+1);
        produce(additionalContext, templateName, outputFile, identifier, fileType, rootContext);
    }
}

