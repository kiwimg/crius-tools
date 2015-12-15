package org.kiwi.crius.tools.gen;


import org.kiwi.crius.tools.config.Configuration;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-4-2
 * Time: 上午11:03
 * To change this template use File | Settings | File Templates.
 */
public class DomainCodeGenerate extends AbstractGenerate {

    private static final String POJO_JAVACLASS_FTL = "org/kiwi/crius/tools/template/pojo/Pojo.ftl";

    public DomainCodeGenerate(Configuration cfg) {
        super(cfg, new File(cfg.getOutDir()));
        init();
    }

    protected void init() {
        setTemplateName(POJO_JAVACLASS_FTL);
        setFilePattern("{package-name}/{class-name}.java");

    }

    public DomainCodeGenerate() {
        init();
    }



    protected void setupContext() {
        //TODO: this safe guard should be in the root templates instead for each variable they depend on.
        if(!getProperties().containsKey("ejb3")) {
            getProperties().put("ejb3", "false");
        }
        if(!getProperties().containsKey("jdk5")) {
            getProperties().put("jdk5", "false");
        }
        super.setupContext();
    }
    


}
