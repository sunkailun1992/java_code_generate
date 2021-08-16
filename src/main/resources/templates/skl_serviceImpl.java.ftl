package ${packageName}.service.impl;

import ${packageName}.entity.query.${ClassName}Query;
import ${packageName}.entity.vo.${ClassName}VO;
import ${packageName}.entity.bo.${ClassName}BO;
import ${packageName}.entity.${ClassName};
import ${packageName}.mapper.${ClassName}Mapper;
import ${packageName}.service.${ClassName}Service;
import ${packageName}.service.query.${ClassName}ServiceQuery;
import ${packageName}.service.results.${ClassName}ServiceResults;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Setter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import com.gb.utils.GeneralConvertor;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * @author:     	${author}
 * @since:   	    ${date}
 * @description:	TODO  ${functionName}，Service服务实现层
 * @source:  	    代码生成器
 */
@Slf4j
@Service
@Setter(onMethod_ = {@Autowired})
public class ${ClassName}ServiceImpl extends ServiceImpl<${ClassName}Mapper, ${ClassName}> implements ${ClassName}Service {


    /**
     * ${functionName}
     */
    private ${ClassName}Mapper ${className}Mapper;


    /**
     * 集合条件查询
     * @author  ${author}
     * @since   ${date}
     * @param   ${className}Query:
     * @return  java.util.List<com.entity.${ClassName}VO>
     */
    @Override
    public List<${ClassName}VO> listEnhance(${ClassName}Query ${className}Query) {
        ${ClassName} ${className} = GeneralConvertor.convertor(${className}Query, ${ClassName}.class);
        QueryWrapper<${ClassName}> queryWrapper = new QueryWrapper<>(${className});
        //TODO 自动生成查询，禁止手动写语句
        ${ClassName}ServiceQuery.query(${className}Query, queryWrapper);
        // TODO 人工查询条件
        queryArtificial(${className}Query, queryWrapper);
        //DO数据
        List<${ClassName}> ${className}DO = ${className}Mapper.selectList(queryWrapper);
        //VO数据
        List<${ClassName}VO> ${className}VO = GeneralConvertor.convertor(${className}DO, ${ClassName}VO.class);
        return ${ClassName}ServiceResults.assignment(${className}VO);
    }


    /**
     * 分页条件查询
     * @author  ${author}
     * @since   ${date}
     * @param   page:
     * @param   ${className}Query:
     * @return  com.baomidou.mybatisplus.core.metadata.IPage
     */
    @Override
    public Page<${ClassName}VO> pageEnhance(Page page, ${ClassName}Query ${className}Query) {
        ${ClassName} ${className} = GeneralConvertor.convertor(${className}Query, ${ClassName}.class);
        QueryWrapper<${ClassName}> queryWrapper = new QueryWrapper<>(${className});
        //TODO 自动生成查询，禁止手动写语句
        ${ClassName}ServiceQuery.query(${className}Query, queryWrapper);
        // TODO 人工查询条件
        queryArtificial(${className}Query, queryWrapper);
        //DO数据
        Page<${ClassName}> pageDO = ${className}Mapper.selectPage(page, queryWrapper);
        //VO数据
        Page<${ClassName}VO> pageVO = ${ClassName}ServiceResults.toPageVO(pageDO);
        return ${ClassName}ServiceResults.assignment(pageVO);
    }


    /**
     * 单条条件查询
     * @author  ${author}
     * @since   ${date}
     * @param   ${className}Query:
     * @return  java.util.List<com.entity.${ClassName}VO>
     */
    @Override
    public ${ClassName}VO getOneEnhance(${ClassName}Query ${className}Query) {
        ${ClassName} ${className} = GeneralConvertor.convertor(${className}Query, ${ClassName}.class);
        QueryWrapper<${ClassName}> queryWrapper = new QueryWrapper<>(${className});
        //TODO 自动生成查询，禁止手动写语句
        ${ClassName}ServiceQuery.query(${className}Query, queryWrapper);
        // TODO 人工查询条件
        queryArtificial(${className}Query, queryWrapper);
        //DO数据
        ${ClassName} ${className}DO = ${className}Mapper.selectOne(queryWrapper);
        //VO数据
        ${ClassName}VO ${className}VO = GeneralConvertor.convertor(${className}DO, ${ClassName}VO.class);
        return ${ClassName}ServiceResults.assignment(${className}VO);
    }


    /**
     * 总数
     * @author  ${author}
     * @since   ${date}
     * @param   ${className}Query:
     * @return  java.lang.Integer
     */
    @Override
    public Integer countEnhance(${ClassName}Query ${className}Query) {
        ${ClassName} ${className} = GeneralConvertor.convertor(${className}Query, ${ClassName}.class);
        QueryWrapper<${ClassName}> queryWrapper = new QueryWrapper<>(${className});
        //TODO 自动生成查询，禁止手动写语句
        ${ClassName}ServiceQuery.query(${className}Query, queryWrapper);
        // TODO 人工查询条件
        queryArtificial(${className}Query, queryWrapper);
        return ${className}Mapper.selectCount(queryWrapper);
    }


    /**
     * 新增
     * @author  ${author}
     * @since   ${date}
     * @param   ${className}BO:
     * @return  java.lang.String
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
    public String saveEnhance(${ClassName}BO ${className}BO) {
        ${ClassName} ${className} = GeneralConvertor.convertor(${className}BO, ${ClassName}.class);
        ${className}Mapper.insert(${className});
        return ${className}.getId();
    }


    /**
     * 修改
     * @author  ${author}
     * @since   ${date}
     * @param   ${className}BO:
     * @return  java.lang.Boolean
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
    public Boolean updateEnhance(${ClassName}BO ${className}BO) {
        ${ClassName} ${className} = GeneralConvertor.convertor(${className}BO, ${ClassName}.class);
        UpdateWrapper<${ClassName} > updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", ${className}BO.getId());
        Integer i = ${className}Mapper.update(${className}, updateWrapper);
        return i > 0 ? true : false;
    }


    /**
     * 删除
     * @author  ${author}
     * @since   ${date}
     * @param   ${className}BO:
     * @return  java.lang.Boolean
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
    public Boolean removeEnhance(${ClassName}BO ${className}BO) {
        ${ClassName} ${className} = GeneralConvertor.convertor(${className}BO, ${ClassName}.class);
        QueryWrapper<${ClassName}> queryWrapper = new QueryWrapper<>(${className});
        Integer i = ${className}Mapper.delete(queryWrapper);
        return i > 0 ? true : false;
    }


    /**
     * 查询人工查询条件
     *
     * @author     	${author}
     * @since   	${date}
     * @param       ${className}Query ${functionName}
     * @return      QueryWrapper
     */
     private QueryWrapper queryArtificial(${ClassName}Query ${className}Query, QueryWrapper<${ClassName}> queryWrapper) {
        return queryWrapper;
    }
}