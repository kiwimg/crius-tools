<#-- // Property accessors -->
<#foreach property in pojo.getAllPropertiesIterator()>
    <#include "GetPropertyAnnotation.ftl"/>
    ${pojo.getPropertyGetModifiers()} ${pojo.getJavaTypeName(property, jdk5)} ${pojo.getGetterSignature(property)}() {
         return ${pojo.getFieldName(property)};
    }

    ${pojo.getPropertySetModifiers(property)} void ${pojo.getSetterSignature(property)} (${pojo.getJavaTypeName(property, jdk5)} ${pojo.getFieldName(property)}) {
        this.${pojo.getFieldName(property)} =  ${pojo.getFieldName(property)};
    }

</#foreach>
