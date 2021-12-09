package ${packageName}.service.query;

import ${packageName}.entity.${ClassName};
import ${packageName}.entity.query.${ClassName}Query;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;


/**
 * Created with IntelliJ IDEA.
 * @author:     	${author}
 * @since:   	    ${date}
 * @description:	TODO  ${functionName},Service查询实现
 * @source:  	    代码生成器
 */
@Slf4j
@Service
@Setter(onMethod_ = {@Autowired})
public class ${ClassName}ServiceQuery{


	/**
	 * 查询增强
     *
     * @author     	${author}
	 * @since   	${date}
	 * @param       ${className}Query ${functionName}
	 * @return      QueryWrapper
	 */
    public QueryWrapper query(${ClassName}Query ${className}Query, QueryWrapper<${ClassName}> queryWrapper) {
        /**
         * 排序
         */
        if(${className}Query.getCollation() != null && StringUtils.isNotBlank(${className}Query.getCollationFields())){
            if(${className}Query.getCollation()){
                queryWrapper.orderByAsc(${className}Query.getCollationFields());
            }else{
                queryWrapper.orderByDesc(${className}Query.getCollationFields());
            }
        }else{
            queryWrapper.orderByDesc(${className}Query.getCollationFields());
        }

        /**
        * 显示字段
        */
        if(StringUtils.isNotBlank(${className}Query.getFields())){
            queryWrapper.select(${className}Query.getFields());
        }
<#if serviceQueryList ??>
        <#list serviceQueryList as item>


            <#if item.type == "0">
                    <#if item.parameterType>
        /**
         * 代码生成器，等于查询
         */
        if(StringUtils.isNotBlank(${className}Query.get${item.parameter}())){
            queryWrapper.eq("${item.column}", ${className}Query.get${item.parameter}());
        }
                    <#else>
        /**
         * 代码生成器，等于查询
         */
        if(${className}Query.get${item.parameter}() != null){
            queryWrapper.eq("${item.column}", ${className}Query.get${item.parameter}());
        }
                    </#if>
            <#elseif item.type == "1">
                    <#if item.parameterType>
        /**
         * 代码生成器，模糊查询
         */
        if(StringUtils.isNotBlank(${className}Query.get${item.parameter}())){
            queryWrapper.likeRight("${item.column}", ${className}Query.get${item.parameter}());
        }
                    <#else>
        /**
         * 代码生成器，模糊查询
         */
        if(${className}Query.get${item.parameter}() != null){
            queryWrapper.likeRight("${item.column}", ${className}Query.get${item.parameter}());
        }
                    </#if>
            <#elseif item.type == "2">
                    <#if item.parameterType>
        /**
         * 代码生成器，为空查询
         */
        if(StringUtils.isNotBlank(${className}Query.get${item.parameter}())){
            queryWrapper.isNull("${item.column}", ${className}Query.get${item.parameter}());
        }
                    <#else>
        /**
         * 代码生成器，为空查询
         */
        if(${className}Query.get${item.parameter}() != null){
            queryWrapper.isNull("${item.column}", ${className}Query.get${item.parameter}());
        }
                    </#if>
            <#elseif item.type == "3">
                    <#if item.parameterType>
        /**
         * 代码生成器，不为空查询
         */
        if(StringUtils.isNotBlank(${className}Query.get${item.parameter}())){
            queryWrapper.isNotNull("${item.column}", ${className}Query.get${item.parameter}());
        }
                    <#else>
        /**
         * 代码生成器，不为空查询
         */
        if(${className}Query.get${item.parameter}() != null){
            queryWrapper.isNotNull("${item.column}", ${className}Query.get${item.parameter}());
        }
                    </#if>
            <#elseif item.type == "4">
                    <#if item.parameterType>
        /**
         * 代码生成器，in查询
         */
        if(StringUtils.isNotBlank(${className}Query.get${item.parameter}())){
            queryWrapper.in("${item.column}", ${className}Query.get${item.parameter}());
        }
                    <#else>
        /**
         * 代码生成器，in查询
         */
        if(${className}Query.get${item.parameter}() != null){
            queryWrapper.in("${item.column}", ${className}Query.get${item.parameter}());
        }
                    </#if>
            <#elseif item.type == "5">
                    <#if item.parameterType>
        /**
         * 代码生成器，notIn查询
         */
        if(StringUtils.isNotBlank(${className}Query.get${item.parameter}())){
            queryWrapper.notIn("${item.column}", ${className}Query.get${item.parameter}());
        }
                    <#else>
        /**
         * 代码生成器，notIn查询
         */
        if(${className}Query.get${item.parameter}() != null){
            queryWrapper.notIn("${item.column}", ${className}Query.get${item.parameter}());
        }
                    </#if>
            <#elseif item.type == "6">
                    <#if item.parameterType>
        /**
         * 代码生成器，inSql查询
         */
        if(StringUtils.isNotBlank(${className}Query.get${item.parameter}())){
            queryWrapper.inSql("${item.column}", ${item.sql});
        }
                    <#else>
        /**
         * 代码生成器，inSql查询
         */
        if(${className}Query.get${item.parameter}() != null){
            queryWrapper.inSql("${item.column}", ${item.sql});
        }
                    </#if>
            <#elseif item.type == "7">
                    <#if item.parameterType>
        /**
         * 代码生成器，notInSql查询
         */
        if(StringUtils.isNotBlank(${className}Query.get${item.parameter}())){
            queryWrapper.notInSql("${item.column}", ${item.sql});
        }
                    <#else>
        /**
         * 代码生成器，notInSql查询
         */
        if(${className}Query.get${item.parameter}() != null){
            queryWrapper.notInSql("${item.column}", ${item.sql});
        }
                    </#if>
            <#elseif item.type == "8">
                    <#if item.parameterType>
        /**
         * 代码生成器，区间查询
         */
        if(StringUtils.isNotBlank(${className}Query.get${item.start}()) && StringUtils.isNotBlank(${className}Query.get${item.end}())){
            queryWrapper.between("${item.column}", ${className}Query.get${item.start}(), ${className}Query.get${item.end}());
        }
                    <#else>
        /**
         * 代码生成器，区间查询
         */
        if(${className}Query.get${item.start}() != null && ${className}Query.get${item.end}() != null){
            queryWrapper.between("${item.column}", ${className}Query.get${item.start}(), ${className}Query.get${item.end}());
        }
                    </#if>
            <#elseif item.type == "9">
                    <#if item.parameterType>
        /**
         * 代码生成器，非该区间查询
         */
        if(StringUtils.isNotBlank(${className}Query.get${item.start}()) && StringUtils.isNotBlank(${className}Query.get${item.end}())){
            queryWrapper.notBetween("${item.column}", ${className}Query.get${item.start}(), ${className}Query.get${item.end}());
        }
                    <#else>
        /**
        * 代码生成器，非该区间查询
        */
        if(${className}Query.get${item.start}() != null && ${className}Query.get${item.end}() != null){
            queryWrapper.notBetween("${item.column}", ${className}Query.get${item.start}(), ${className}Query.get${item.end}());
        }
                    </#if>
            </#if>
        </#list>
</#if>
        return queryWrapper;
    }


}
