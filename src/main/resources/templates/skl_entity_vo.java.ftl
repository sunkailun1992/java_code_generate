package ${packageName}.entity.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import ${packageName}.entity.enums.${ClassName}StateEnum;
import ${packageName}.entity.enums.${ClassName}TypeEnum;


/**
 * Created with IntelliJ IDEA.
 * @author:     	${author}
 * @since:   	    ${date}
 * @description:	TODO  ${functionName}渲染
 * @source:  	    代码生成器
 */
@Data
@ApiModel(value = "${functionName}渲染")
public class ${ClassName}VO implements Serializable {
	<#list list as item>

    @ApiModelProperty(value = "${item.columnComment}")
    <#if item.dataType == "LocalDateTime">
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    </#if>
    <#if item.columnName == "state">
    private ${ClassName}StateEnum ${item.columnName};
    <#elseif item.columnName == "type">
    private ${ClassName}TypeEnum ${item.columnName};
    <#else>
    private ${item.dataType} ${item.columnName};
    </#if>
    </#list>
}


