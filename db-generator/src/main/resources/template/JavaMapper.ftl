package com.kingdowin.newlol.db.mapper;

import java.util.List;


import ${javaPackage};
import com.kingdowin.newlol.endpoint.annotation.Param;


public interface ${javaClass}Mapper { 

<#list ${loads} as load>
	${javaClass} load${javaClass}(<#list ${pkeys} as x>@Param("${x.name}") ${x.type} ${x.name}<#if x_has_next>, </#if></#list>);
</#list>
	List<${javaClass}> loadAll${javaClass}s();

	<#list ${loads} as x>@Param("${x}") long ${x}<#if x_has_next>, </#if>
		
	</#list>
	List<${javaClass}> loadUsersByLevel(@Param("level") int level);

	void saveUser(User user);


	void updateUser(User user);

	void updateUserLevel(User user);

	void deleteUser(User user);

}