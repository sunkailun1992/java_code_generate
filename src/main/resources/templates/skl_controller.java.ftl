package ${packageName}.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import com.gb.utils.annotations.RequestRequired;
import com.gb.utils.annotations.Methods;
import com.gb.utils.Json;
import com.gb.utils.RedisUtils;
import com.gb.utils.annotations.PreventRepeat;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.annotation.Validated;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.gb.utils.enumResources.ReturnCode;
import ${packageName}.service.${ClassName}Service;
import ${packageName}.entity.query.${ClassName}Query;
import ${packageName}.entity.vo.${ClassName}VO;
import ${packageName}.entity.bo.${ClassName}BO;


/**
 * Created with IntelliJ IDEA.
 * @author:     	${author}
 * @since:   	    ${date}
 * @description:	TODO  ${functionName}，Comment请求层
 * @source:  	    代码生成器
 */
@Slf4j
@RequestRequired
@ApiSupport(author = "${author}")
@Setter(onMethod_ = {@Autowired})
@RestController
@RequestMapping("/${className}")
@Api(tags = "${functionName}")
public class ${ClassName}Controller {


    /**
     * ${functionName}
     */
    private ${ClassName}Service ${className}Service;
    /**
     * redis操作
     */
    private StringRedisTemplate stringRedisTemplate;


    /**
     * ${functionName}集合分页查询
     *
     * @param current:
     * @param size:
     * @param ${className}Query:
     * @return com.utils.Json
     * @author ${author}
     * @since ${date}
     */
    @Methods(methodsName = "${functionName}集合分页查询", methods = "select")
    @ApiOperation(value = "${functionName}集合分页查询", httpMethod = "GET", notes = "${functionName}集合查询", response = Json.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", dataType = "int", required = true),
            @ApiImplicitParam(name = "size", value = "分页显示数量", dataType = "int", required = true),
    })
    @GetMapping("/select")
    public Json<Page<${ClassName}VO>> select(@Validated(value = ${ClassName}Query.Select.class) ${ClassName}Query ${className}Query, Integer current, Integer size) {
        //返回内容
        return new Json(ReturnCode.成功, ${className}Service.pageEnhance(new Page(current, size), ${className}Query));
    }


    /**
     * ${functionName}集合查询
     *
     * @param ${className}Query:
     * @return com.utils.Json
     * @author ${author}
     * @since ${date}
     */
    @Methods(methodsName = "${functionName}集合查询", methods = "selectList")
    @ApiOperation(value = "${functionName}集合查询", httpMethod = "GET", notes = "${functionName}集合查询", response = Json.class)
    @GetMapping("/selectList")
    public Json<List<${ClassName}VO>> selectList(@Validated(value = ${ClassName}Query.SelectList.class) ${ClassName}Query ${className}Query) {
        //返回内容
        return new Json(ReturnCode.成功, ${className}Service.listEnhance(${className}Query));
    }


    /**
     * ${functionName}单条查询
     *
     * @param ${className}Query:
     * @return com.utils.Json
     * @author ${author}
     * @since ${date}
     */
    @Methods(methodsName = "${functionName}单条查询", methods = "selectOne")
    @ApiOperation(value = "${functionName}单条查询", httpMethod = "GET", notes = "${functionName}单条查询", response = Json.class)
    @GetMapping("/selectOne")
    public Json<${ClassName}VO> selectOne(@Validated(value = ${ClassName}Query.SelectOne.class) ${ClassName}Query ${className}Query) {
        //返回内容
        return new Json(ReturnCode.成功, ${className}Service.getOneEnhance(${className}Query));
    }


    /**
     * ${functionName}总数查询
     *
     * @param ${className}Query:
     * @return com.utils.Json
     * @author ${author}
     * @since ${date}
     */
    @Methods(methodsName = "${functionName}总数查询", methods = "count")
    @ApiOperation(value = "${functionName}总数查询", httpMethod = "GET", notes = "${functionName}总数查询", response = Json.class)
    @GetMapping(value = "/count")
    public Json<Integer> count(@Validated(value = ${ClassName}Query.Count.class) ${ClassName}Query ${className}Query) {
        //返回内容
        return new Json(ReturnCode.成功, ${className}Service.countEnhance(${className}Query));
    }


   /**
    * ${functionName}新增
    *
    * @param ${className}BO:
    * @return com.utils.Json
    * @author ${author}
    * @since ${date}
    */
    @PreventRepeat
    @Methods(methodsName = "${functionName}新增", methods = "save")
    @ApiOperation(value = "${functionName}新增", httpMethod = "POST", notes = "${functionName}新增", response = Json.class)
    @ApiOperationSupport(ignoreParameters = {"id","createDateTime","createName","modifyDateTime","modifyName","isDelete","version"})
    @PostMapping("/save")
    public Json<String> save(@Validated(value = ${ClassName}BO.Save.class) ${ClassName}BO ${className}BO, HttpServletRequest httpServletRequest) {
        //缓存取出用户
        Map<String, Object> u = RedisUtils.getToken(stringRedisTemplate, httpServletRequest);
        if(u != null){
            ${className}BO.setCreateName(u.get("name") + "-" + u.get("id"));
        }
        //返回内容
        return new Json(ReturnCode.成功, ${className}Service.saveEnhance(${className}BO));
    }


    /**
     * ${functionName}修改
     *
     * @param ${className}BO:
     * @return com.utils.Json
     * @author ${author}
     * @since ${date}
     */
    @PreventRepeat
    @Methods(methodsName = "${functionName}修改", methods = "update")
    @ApiOperation(value = "${functionName}修改", httpMethod = "PUT", notes = "${functionName}修改", response = Json.class)
    @ApiOperationSupport(ignoreParameters = {"createDateTime","createName","modifyDateTime","modifyName","isDelete","version"})
    @PutMapping("/update")
    public Json<Boolean> update(@Validated(value = ${ClassName}BO.Update.class) ${ClassName}BO ${className}BO, HttpServletRequest httpServletRequest) {
        //缓存取出用户
        Map<String, Object> u = RedisUtils.getToken(stringRedisTemplate, httpServletRequest);
        if(u != null){
            ${className}BO.setModifyName(u.get("name") + "-" + u.get("id"));
        }
        //返回内容
        return new Json(ReturnCode.成功, ${className}Service.updateEnhance(${className}BO));
    }


    /**
     * ${functionName}删除
     *
     * @param ${className}BO:
     * @return com.utils.Json
     * @author ${author}
     * @since ${date}
     */
    @Methods(methodsName = "${functionName}删除", methods = "remove")
    @ApiOperation(value = "${functionName}删除", httpMethod = "DELETE", notes = "${functionName}删除", response = Json.class)
    @ApiOperationSupport(includeParameters = {"id"})
    @DeleteMapping("/remove")
    public Json<Boolean> remove(@Validated(value = ${ClassName}BO.Remove.class) ${ClassName}BO ${className}BO) {
        return new Json(ReturnCode.成功, ${className}Service.removeEnhance(${className}BO));
    }


}