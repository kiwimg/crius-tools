package ${package}.dao;

import java.io.Serializable;
import java.util.List;
<#if oom.isHasMethods()>
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.Repository;
import javax.persistence.*;
import ${package}.domain;
</#if>


/**
  *  ${oom.getComment()}
  *
  *//

public interface ${oom.getName()} <#if oom.isExtendsRef()> extends ${oom.getExtendsRef()}</#if>{

<#if oom.isHasMethods()>${oom.getMethods()}</#if>

}


