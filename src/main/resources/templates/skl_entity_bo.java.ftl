package ${packageName}.entity.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import ${packageName}.entity.enums.${ClassName}StateEnum;
import ${packageName}.entity.enums.${ClassName}TypeEnum;


/**
 * Created with IntelliJ IDEA.
 * @author:     	${author}
 * @since:   	    ${date}
 * @description:	TODO  ${functionName}传输
 * @source:  	    代码生成器
 */
@Data
@ApiModel(value = "${functionName}传输")
public class ${ClassName}BO implements Serializable {
	<#list list as item>

    @ApiModelProperty(value = "${item.columnComment}")
    <#if item.columnName == "id">
    @NotBlank(groups = {${ClassName}BO.Update.class,${ClassName}BO.Remove.class},message = "id不能为空")
    </#if>
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


    /**
    * 新增
    */
    public interface Save{}

    /**
    * 修改
    */
    public interface Update{}

    /**
    * 删除
    */
    public interface Remove{}
}


