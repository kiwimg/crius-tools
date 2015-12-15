${pojo.getPackageDeclaration()}
// Generated ${date} by Crius Tools ${version}

<#assign classbody>
<#include "PojoTypeDeclaration.ftl"/> {
<#if !pojo.isInterface()>
private static final long serialVersionUID = 1L;
    <#include "PojoFields.ftl"/>

<#include "PojoConstructors.ftl"/>
   
<#include "PojoPropertyAccessors.ftl"/>
<#else>
<#include "PojoInterfacePropertyAccessors.ftl"/>
</#if>
}
</#assign>

${pojo.generateImports()}
${classbody}

