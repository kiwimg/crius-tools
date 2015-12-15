package org.kiwi.crius.tools; /**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-4-1
 * Time: 下午3:53
 * To change this template use File | Settings | File Templates.
 */

import org.kiwi.crius.tools.config.Configuration;
import org.kiwi.crius.tools.gen.DomainCodeGenerate;
import org.kiwi.crius.tools.gen.OomCodeGenerate;
import org.kiwi.crius.tools.meta.MetaClass;
import org.kiwi.crius.tools.meta.MetaDataDialectFactory;
import org.kiwi.crius.tools.meta.mapping.oom.InterfaceInfo;
import freemarker.template.TemplateException;
import org.apache.xmlbeans.impl.tool.CommandLine;
import org.dom4j.DocumentException;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GenObjects {
    public static void main(String[] args) throws IOException,
            TemplateException {

        if (args==null || args.length == 0)
        {
            printHelp();
            System.exit(0);
            return;
        }
        Set flags = new HashSet();
        flags.add("debug");

        Set opts = new HashSet();
        opts.add("filepath");
        opts.add("outDir");
        opts.add("packageName");
        opts.add("oomFilePath");


        CommandLine cl = new CommandLine(args, flags, opts);
        if ((cl.getOpt("filepath") == null && cl.getOpt("oomFilePath")==null) || cl.getOpt("outDir") == null || cl.getOpt("packageName") == null )
        {
            printHelp();
            System.exit(0);
            return;
        }

        String filepath = cl.getOpt("filepath");
        //String out = "D:\\javaproject\\test\\src";
        String out =  cl.getOpt("outDir");
        //String packageName = "cn.damai.student.core.domain";
        String packageName = cl.getOpt("packageName") ;
        
        String oomFilePath =cl.getOpt("oomFilePath");
        
     
        

       // String packageName = "cn.damai.student.core.test";
        Configuration configuration = new Configuration(filepath, out, packageName,oomFilePath);
        if(StringUtils.isNotEmpty(filepath)){
        	List<MetaClass> metaClasses = MetaDataDialectFactory.getInstance().getMetaClasses(configuration);
        	configuration.setMetaClasses(metaClasses);

        	DomainCodeGenerate domainCodeGenerate = new DomainCodeGenerate(configuration);
        	domainCodeGenerate.setTemplatePath(new String[0]);
        	domainCodeGenerate.getProperties().setProperty("ejb3", "true");
        	domainCodeGenerate.getProperties().setProperty("jdk5", "true");
        	domainCodeGenerate.start();
        }
        //parse oom file and generate file.
        try {
        	
        	if(StringUtils.isNotEmpty(oomFilePath)){
        		
        		List<InterfaceInfo> interfaces =MetaDataDialectFactory.getInstance().createOomMetaDataDialect(configuration).getInterfaceMetaData();
			
        		configuration.setInterfaces(interfaces);
			
        		OomCodeGenerate oomCodeGenerate =new OomCodeGenerate(configuration);
        		oomCodeGenerate.setTemplatePath(new String[0]);
			
        		oomCodeGenerate.getProperties().setProperty("ejb3", "true");
        		oomCodeGenerate.getProperties().setProperty("jdk5", "true");
        		oomCodeGenerate.start();
        	}	
			
		} catch (DocumentException e) {
			e.printStackTrace();
		}

    }
    private static void printHelp()
    {
        System.out.println("Generates javaCode from pdm or oom documents.");
        System.out.println("Usage: GenObjects [opts] ");
        System.out.println("Options include:");
        System.out.println("    -filepath [dir] - 模型文件路径");
        System.out.println("    -outDir [dir] -代码输出路径");
        System.out.println("    -packageName 定义的包名称'");
        System.out.println("    -oomFilePath[dir] -oom文件路径");
    }

}
