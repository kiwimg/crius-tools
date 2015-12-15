<#if ejb3>
<#if pojo.hasIdentifierProperty(property)>
${pojo.generateAnnIdGenerator()}
</#if>
 <#if pojo.isManyToOne(property)>
${pojo.generateManyToOneAnnotation(property)}
<#--TODO support optional and targetEntity-->
${pojo.generateJoinColumnsAnnotation(property)}
<#elseif pojo.isCollection(property)>
${pojo.generateCollectionAnnotation(property)}
<#else>
${pojo.generateBasicAnnotation(property)}
${pojo.generateAnnColumnAnnotation(property)}
</#if>
</#if>