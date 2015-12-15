package org.kiwi.crius.tools.meta.mapping.oom;

import java.util.List;

/**
 * 
 * The description of the function that belong to some class or interface.
 * @author xujun -xujun@damai.cn
 *
 */
public class MethodInfo extends AbstractFetchInfo {

	private String returnType;

	private List<ParameterInfo> parameters;
	private List<AnnotationInfo> annotations;

	public List<ParameterInfo> getParameters() {
		return parameters;
	}

	public void setParameters(List<ParameterInfo> parameters) {
		this.parameters = parameters;
	}

	public List<AnnotationInfo> getAnnotations() {
		return annotations;
	}

	public void setAnnotations(List<AnnotationInfo> annotations) {
		this.annotations = annotations;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	public String getReturnType() {
		return returnType;
	}

}
