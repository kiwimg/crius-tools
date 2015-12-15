package org.kiwi.crius.tools;
import org.kiwi.crius.tools.meta.mapping.oom.AnnotationInfo;
import org.kiwi.crius.tools.meta.mapping.oom.InterfaceInfo;
import org.kiwi.crius.tools.meta.mapping.oom.MethodInfo;
import org.kiwi.crius.tools.meta.mapping.oom.ParameterInfo;


/**
 * the model pattern of the xpath.
 * @author xujun
 *
 */
public enum FindPattern {
	
	INTERFACE("Interface",InterfaceInfo.class),
	METHOD("Operation",MethodInfo.class),
	CLASS("",null),
	PARAMETER("Parameter",ParameterInfo.class),
	ANNOTATION("Annotation",AnnotationInfo.class);
	
	private final Class<?> clazz;
	private final String findName;
	
	private FindPattern(String name,Class<?> cls){
		this.clazz =cls;
		this.findName =name;
	}
	
	public final String getXPath(){
		return this.findName;
	}
	
	public Class<?> getCls(){
		return this.clazz;
	}

}
