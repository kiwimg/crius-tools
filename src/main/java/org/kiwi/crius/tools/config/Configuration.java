package org.kiwi.crius.tools.config;

import org.kiwi.crius.tools.meta.MetaClass;
import org.kiwi.crius.tools.meta.mapping.oom.InterfaceInfo;

import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-4-3
 * Time: 上午11:20
 * To change this template use File | Settings | File Templates.
 */
public class Configuration implements Serializable {
    private String modelSrc;  //模型文件路径
    private String packageName;//生成代码的包命名
    private String outDir;   //输出文件路径
    private List<MetaClass> metaClasses;
    
    private List<InterfaceInfo> interfaces;
    
    public List<InterfaceInfo> getInterfaces() {
		return interfaces;
	}

	public void setInterfaces(List<InterfaceInfo> interfaces) {
		this.interfaces = interfaces;
	}

	private String oomModelFilePath;    //oom file's path.

    public Configuration(String modelSrc, String outDir, String packageName) {
        this.modelSrc = modelSrc;
        this.outDir = outDir;
        this.packageName = packageName;
    }
    
    public Configuration(String modelSrc, String outDir, String packageName,String oomModelFilePath) {
        this.modelSrc = modelSrc;
        this.outDir = outDir;
        this.packageName = packageName;
        this.oomModelFilePath =oomModelFilePath;
    }

    public Configuration() {
    }

    public String getModelSrc() {
        return modelSrc;
    }

    public void setModelSrc(String modelSrc) {
        this.modelSrc = modelSrc;
    }

    public String getOutDir() {
        return outDir;
    }

    public void setOutDir(String outDir) {
        this.outDir = outDir;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public List<MetaClass> getMetaClasses() {
        return metaClasses;
    }

    public void setMetaClasses(List<MetaClass> metaClasses) {
        this.metaClasses = metaClasses;
    }
    
    public void setOomModelFilePath(String path){
    	this.oomModelFilePath =path;
    }
    
    public String getOomModelFilePath(){
    	return oomModelFilePath;
    }
    
}
