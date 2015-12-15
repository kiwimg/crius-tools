package org.kiwi.crius.tools.meta.oom;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;

import org.kiwi.crius.tools.Constracts;
import org.kiwi.crius.tools.FindPattern;
import org.kiwi.crius.tools.StringUtils;
import org.kiwi.crius.tools.config.Configuration;
import org.kiwi.crius.tools.meta.mapping.oom.AbstractFetchInfo;
import org.kiwi.crius.tools.meta.mapping.oom.AnnotationInfo;
import org.kiwi.crius.tools.meta.mapping.oom.InterfaceInfo;
import org.kiwi.crius.tools.meta.mapping.oom.MethodInfo;
import org.kiwi.crius.tools.meta.mapping.oom.ParameterInfo;

/**
 * 
 * parser for specify oom file.
 * @author xujun -xujun@damai.cn
 *
 */
public class OomMetaDataDialect implements Constracts {

	private Configuration confige;
	
	public OomMetaDataDialect(Configuration confige){this.confige =confige;}
	
	public Configuration getConfiguration(){return confige;}
	
	/**
	 * get meta data.
	 * @author xujun -xujun@damai.cn
	 */
	public List<InterfaceInfo> getInterfaceMetaData() throws DocumentException{
		checkNotNull(confige,false);
		String path =confige.getOomModelFilePath();
		if(StringUtils.isEmpty(path)){
			System.out.println("The oom file's path is ivalid. exit -1.");
			return null;
		}
		File file =new File(path);
		if(!file.exists()){
			System.out.println("The oom file's path is not exists. exit -1.");
			return null;
		}
		
		SAXReader saxReader =new SAXReader();
    	Document  document =saxReader.read(file);
    	//create namespace
    	Map<String,String> xmlNamespaceMap =new HashMap<String,String>();
    	xmlNamespaceMap.put("c", "collection");
    	XPath interfacePath =document.createXPath("//c:Interfaces");
    	interfacePath.setNamespaceURIs(xmlNamespaceMap);
    	Element interfaceElements =(Element)interfacePath.selectSingleNode(document);
    	if(interfaceElements==null) System.out.println("xpath failured to find:"+interfacePath.toString());
    	List<InterfaceInfo> interfaces =null;
    	Map<String,HashSet<String>> gmap =getGeneralizes(document,xmlNamespaceMap);
    	@SuppressWarnings("unchecked")
		Iterator<Element> it=interfaceElements.elementIterator("Interface");
    	InterfaceInfo item =null;
    	while(it.hasNext()){  //interface
    		if(interfaces==null) interfaces =new ArrayList<InterfaceInfo>();
    		item =new  InterfaceInfo();
    		Element node =null;
    		if((node=it.next())==null) continue;
    		item.setIdentifier(node.attributeValue(SEARCH_OOM_NODE_ID));
    		item.setComment(node.elementText(SEARCH_OOM_NODE_COMMENT));
    		item.setCreateDate(node.elementText(SEARCH_OOM_NODE_CREATIONDATE));
    		item.setCreator(node.elementText(SEARCH_OOM_NODE_CREATOR));
    		item.setName(node.elementText(SEARCH_OOM_MODE_CODE));
    		//handle all operations.
    		Element eleOperation =node.element("Operations");
    		if(eleOperation!=null){
    		@SuppressWarnings("unchecked")
			Iterator<Element> optIteator =eleOperation.elementIterator("Operation");
    		List<MethodInfo> opts = new ArrayList<MethodInfo>();
    		MethodInfo 		 mi   =null;
    		//if(opts.size()>0) opts.clear();
    		while(optIteator.hasNext()){  //method.
    			Element methodInfo= optIteator.next();
    			mi =new MethodInfo();
    			mi.setIdentifier(methodInfo.attributeValue(SEARCH_OOM_NODE_ID));
    			mi.setComment(methodInfo.elementText(SEARCH_OOM_NODE_COMMENT));
    			mi.setName(methodInfo.elementText(SEARCH_OOM_MODE_CODE));
    			mi.setReturnType(methodInfo.elementText(SEARCH_OOM_NODE_RETURNTYPE));
    			List<ParameterInfo> params =new ArrayList<ParameterInfo>();
    			Element paraElement =methodInfo.element("Parameters");
    			if(paraElement!=null){
    				@SuppressWarnings("unchecked")
					Iterator<Element> pit=paraElement.elementIterator("Parameter"); //参数
    				while(pit.hasNext()){
    					ParameterInfo pi =new ParameterInfo();
    					Element p =pit.next();
    					pi.setName(p.elementText(SEARCH_OOM_MODE_CODE));
    					pi.setType(p.elementText(SEARCH_OOM_NODE_PARAMETER_DATATYPE));
    					params.add(pi);
    				}
    				if(params.size()>0) mi.setParameters(params);
    			}
    			
    			List<AnnotationInfo> o=getElement(methodInfo,FindPattern.ANNOTATION);
    			if(o!=null && o.size()>0){
    				mi.setAnnotations(o);
    			}
    			opts.add(mi);
    		}
    		if(opts.size()>0) item.setOperations(opts);
    		}
    		interfaces.add(item);
    	}
    	
    	bindExtends(interfaces,gmap);
    		
		return interfaces;
	}
	
	
	
	
	/**
	 * 
	 * @param mainElement
	 * @param pattern
	 * @return 
	 * @author xujun -xujun@damai.cn
	 */
    @SuppressWarnings("unchecked")
	public <T extends AbstractFetchInfo> List<T> getElement(Element mainElement,FindPattern pattern){
    
    	if(mainElement==null) return null;
    	Element element =mainElement.element(pattern.getXPath()+"s");
    	if(element==null) return null;
    	@SuppressWarnings("unchecked")
		Iterator<Element> it =element.elementIterator(pattern.getXPath());
    	List<T> list =new ArrayList<T>();
    	while(it.hasNext()){
    		Element o =it.next();
    		switch(pattern){
        	case PARAMETER:
        		ParameterInfo pi =new ParameterInfo();
				pi.setName(o.elementText(SEARCH_OOM_MODE_CODE));
				pi.setType(o.elementText(SEARCH_OOM_NODE_PARAMETER_DATATYPE));
				list.add((T)pi);
        		break;
        	case ANNOTATION:
        		AnnotationInfo aif =new AnnotationInfo();
        		aif.setName(o.elementText(SEARCH_OOM_NODE_ANNOTATION_NAME));
        		String v=o.elementText(SEARCH_OOM_ANNOtATION_TEXT);
        		if(v!=null && v.length()>0)
        			aif.setValue(v);
        		list.add((T)aif);
        		default:break;
        	}
    	}
    	return list;
    }
	
	
	/**
	 * 
	 * @param document  --the document of the current context 
	 * @param xmlNamespace -- the namespace of the xml.
	 * @return <code>Map<String,HashSet<String>></code>
	 * @author xujun -xujun@damai.cn
	 */
	public Map<String,HashSet<String>> getGeneralizes(Document document,Map<String,String> xmlNamespace){
    	Map<String,HashSet<String>> map =null;
    	if( document==null ) return map;
    	XPath path =document.createXPath("//c:Generalizations");
    	if(xmlNamespace!=null && xmlNamespace.size()>0) path.setNamespaceURIs(xmlNamespace);
    	Element root =(Element)path.selectSingleNode(document);
    	if(root==null){
    		System.out.println("find Generalizations failured.");
    		return map;
    	}
    	@SuppressWarnings("unchecked")
		Iterator<Element> git =root.elementIterator("Generalization");
    	while(git.hasNext()){
    		Element element =git.next();
    		String ref,self;
    		ref=null;
    		self=null;
    		for(int ix=1;ix<3;ix++){
    		@SuppressWarnings("unchecked")
    		Element childElement =element.element("Object"+ix);
			@SuppressWarnings("unchecked")
			Iterator<Element> genlizer =childElement.elementIterator("Interface");
    		if(genlizer.hasNext()){
    			if(ix==1) ref= genlizer.next().attributeValue("Ref");
    			else self =genlizer.next().attributeValue("Ref");
    		} 
    	}
    		if(map ==null) map =new HashMap<String,HashSet<String>>();
    		if(self==null || self.length()==0 || self.isEmpty()
    				|| ref==null || ref.isEmpty()) continue;
    		HashSet<String> set =map.get(self);
    		if(set==null || set.size()<=0){
    			synchronized (map) {
					set= new HashSet<String>();
					set.add(ref);
					map.put(self, set);
				}
    		}else{
    			if(!set.contains(ref)){
    				synchronized(map){
    					set.add(ref);
    					//map.put(self, set);
    					//do anymore?.
    				}
    			}
    		}
    	}
    	return map;
    }
	
	
	
	
	/**
	 * bind relations
	 * @param itList -the collection of <code>InterfaceInfo</code>
	 * @param map	 -the relations from oomFile
	 * @author xujun -xujun@dama.cn
	 */
    public void bindExtends(List<InterfaceInfo> itList,Map<String,? extends Set<String>> map){
    	if(map ==null) return;
    	if(itList==null) return;
    	for(InterfaceInfo ifi : itList){
    		Set<String> extendsRef =map.get(ifi.getIdentifier());
        	if(extendsRef!=null && extendsRef.size()>0){
        		Set<String> tmp =new HashSet<String>();
        		for(String s:extendsRef){
        			 String t=getMappingName(s,itList);
        			 if(t!=null)
        				 tmp.add(t);
        		}
        		if(tmp.size()>0)
        			ifi.setExtendsRef(tmp);
        	}	
    	}
    }
	
	
	/**
	 * find the <code>class</code> or <interface> according to specify <code>identifier</code>
	 * @param identifier -the unique identifier of the current object.
	 * @param itList     -the collection of <code>InterfaceInfo</code>
	 * @return if find return the match name else return null pointer.
	 * @author xujun  -xujun@damai.cn
	 */
	public  String getMappingName(String identifier,List<InterfaceInfo> itList){
    	for(InterfaceInfo ifi : itList){
    		if(ifi.getIdentifier().equals(identifier)) return ifi.getName();
    	}
    	return null;
    }
	
	
	<T> void checkNotNull(T o,boolean nullable){
		 if( o==null )
			 if( !nullable )
				 throw new IllegalArgumentException("parameter is null.");//i want to record the type,such as typeof(T).
	 }
	
}
