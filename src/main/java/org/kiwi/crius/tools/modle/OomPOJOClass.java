package org.kiwi.crius.tools.modle;

import org.kiwi.crius.tools.meta.mapping.oom.AnnotationInfo;
import org.kiwi.crius.tools.meta.mapping.oom.InterfaceInfo;
import org.kiwi.crius.tools.meta.mapping.oom.MethodInfo;
import org.kiwi.crius.tools.meta.mapping.oom.ParameterInfo;

public class OomPOJOClass {

	InterfaceInfo ifi;
	public OomPOJOClass(InterfaceInfo ifi){
		this.ifi=ifi;}
	
	public InterfaceInfo getInterfaceInfo(){return ifi;}
	
	
	public boolean isExtendsRef(){
		return ifi.getExtendsRef()==null?false:true;
	}
	
	public String getComment(){
		return ifi.getComment()==null?"":ifi.getComment();
	}
	
	public String getName(){
		return ifi.getName();
	}
	
	
	public boolean isHasMethods(){
		return ifi.getOperations()==null?false:true;
	}
	
	
	public String getMethods(){
		StringBuilder builder =new StringBuilder();
		for(MethodInfo mi:ifi.getOperations()){
			getAnnocation(builder,mi);//annotation.
			builder.append(mi.getReturnType()).append(" ").append(mi.getName()).append("(");
			boolean insertedBefore=false;
			if(mi.getParameters()!=null && mi.getParameters().size()>0){
				for(ParameterInfo pif:mi.getParameters()){
				if(insertedBefore){
					builder.append(",").append(pif.getType()).append(" ").append(pif.getName());
				}else{
					builder.append(pif.getType()).append(" ").append(pif.getName());
					insertedBefore=true;
				}
			}
			}
			builder.append(");").append("\r\n");
		}
		return builder.toString();
	}
	
	private void getAnnocation(StringBuilder builder,MethodInfo mif){
		if(mif.getAnnotations()!=null && mif.getAnnotations().size()>0){
			for(AnnotationInfo aif:mif.getAnnotations()){
				builder.append("@").append(aif.getName());
				if(aif.getValue()!=null && aif.getValue().length()>0){
					builder.append("(").append(aif.getValue()).append(")");
				}
				builder.append("\r\n");
			}
		}
	}
	
	
	
	public String getExtendsRef(){
		StringBuilder sb =new StringBuilder();
		boolean insertedBefore=false;
		if(ifi.getExtendsRef()!=null){
			for(String s:ifi.getExtendsRef()){
				if(insertedBefore){
					sb.append(",").append(s);
				}else{
					sb.append(s);
					insertedBefore=true;
				}
			}
		}
		return sb.toString();
	}
	   
	
}
