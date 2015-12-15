package org.kiwi.crius.tools.meta.mapping.oom;

import java.util.List;
import java.util.Set;

/**
 * The description of <code>Interface</code>
 * @author xujun --xujun@damai.cn
 *
 */
public class InterfaceInfo extends AbstractFetchInfo {
	
	private List<MethodInfo> operations;
	private Set<String> extendsRef;
	
	public Set<String> getExtendsRef() {
		return extendsRef;
	}

	public void setExtendsRef(Set<String> extendsRef) {
		this.extendsRef = extendsRef;
	}

	public List<MethodInfo> getOperations() {
		return operations;
	}

	public void setOperations(List<MethodInfo> operations) {
		this.operations = operations;
	}
	
}
