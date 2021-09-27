package ${packageName}.service.results;

import ${packageName}.entity.${ClassName};
import ${packageName}.entity.vo.${ClassName}VO;
import ${packageName}.entity.bo.${ClassName}BO;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import com.gb.utils.GeneralConvertor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * @author:     	${author}
 * @since:   	    ${date}
 * @description:	TODO  ${functionName},Service返回实现
 * @source:  	    代码生成器
 */
@Slf4j
@Service
@Setter(onMethod_ = {@Autowired})
public class ${ClassName}ServiceResults{


    /**
     * 单条，增强返回参数追加
     *
     * @author     	${author}
     * @since   	${date}
     * @param       ${className}VO ${functionName}
     * @return      ${ClassName}VO
     */
    public ${ClassName}VO assignment(${ClassName}VO ${className}VO) {
        if(${className}VO != null){
            return ${className}VO;
        }else{
            return ${className}VO;
        }
    }


    /**
     * 分页，增强返回参数追加
     *
     * @author     	${author}
     * @since   	${date}
     * @param       ${className}VOList ${functionName}
     * @return      Page<${ClassName}VO>
     */
    public Page<${ClassName}VO> assignment(Page<${ClassName}VO> ${className}VOList) {
        ${className}VOList.getRecords().forEach(${className}VO -> {
        });
        return ${className}VOList;
    }


    /**
     * 集合，增强返回参数追加
     *
     * @author     	${author}
     * @since   	${date}
     * @param       ${className}VOList ${functionName}
     * @return      List<${ClassName}VO>
     */
    public List<${ClassName}VO> assignment(List<${ClassName}VO> ${className}VOList) {
        ${className}VOList.forEach(${className}VO -> {
        });
        return ${className}VOList;
    }


    /**
     * DO转化VO
     *
     * @author     	${author}
     * @since   	${date}
     * @param       pageDO ${functionName}
     * @return      com.baomidou.mybatisplus.extension.plugins.pagination.Page
     */
    public Page<${ClassName}VO> toPageVO(Page<${ClassName}> pageDO) {
        Page<${ClassName}VO> pageVO = new Page<${ClassName}VO>();
        pageVO.setRecords(GeneralConvertor.convertor(pageDO.getRecords(), ${ClassName}VO.class));
        pageVO.setCurrent(pageDO.getCurrent());
        pageVO.setPages(pageDO.getPages());
        pageVO.setSize(pageDO.getSize());
        pageVO.setTotal(pageDO.getTotal());
        return pageVO;
    }


}