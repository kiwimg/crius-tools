package org.kiwi.crius.tools.modle;

import org.kiwi.crius.tools.meta.Property;

import java.util.List;

/**
 * Wrapper class over PersistentClass used in hbm2java and hbm2doc tool
 * @author max
 * @author <a href="mailto:abhayani@jboss.org">Amit Bhayani</a>
 *
 */
public interface POJOClass extends ImportContext {

    public boolean isManyToOne(Property property);
    public boolean isCollection(Property property);
	public String getPackageDeclaration();
	
	public String getClassModifiers();

	public String getClassJavaDoc(String fallback, int indent);

	public String getDeclarationType();
	public String getDeclarationName();
	public String getImplementsDeclaration();
	public String getImplements();
	public String generateEquals(String thisName, String otherName, boolean useGenerics);
	public boolean isComponent();
	public boolean needsEqualsHashCode();
	public boolean hasIdentifierProperty(Property property);
	public String generateAnnColumnAnnotation(Property property);
	public String generateAnnIdGenerator();
	public String generateAnnTableUniqueConstraint();
	public String generateBasicAnnotation(Property property);
	public List getAllPropertiesIterator();
	public String getPackageName();
	public boolean needsToString();
	public String getFieldJavaDoc(Property property, int indent);
	public String getFieldDescription(Property property);
	public Object getDecoratedObject();
	public boolean isInterface();
	public boolean isSubclass();
	public boolean needsMinimalConstructor();
	public boolean needsFullConstructor();
	public String getJavaTypeName(Property property, boolean useGenerics);
	public String getFieldInitialization(Property property, boolean useGenerics);
	public boolean hasVersionProperty();
	public Property getVersionProperty();

}
