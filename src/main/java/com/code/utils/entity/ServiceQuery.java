package com.code.utils.entity;

import lombok.Data;

/**
 * @ClassName ServiceQuery
 * @Description 自定义查询条件
 * @Author 孙凯伦
 * @Mobile 13777579028
 * @Email 376253703@qq.com
 * @Time 2021/8/10 11:14 上午
 */
@Data
public class ServiceQuery {
    /**
     * 自定义查询条件类型（1：等于，2：模糊查询，3：为空查询，4：不为空查询，5：in查询，6：notIn查询，7：inSql查询，8：notInSql查询，9：区间查询，10：非这个区间查询）
     */
    private String type;
    /**
     * 数据库字段名
     */
    private String column;
    /**
     * 传值实体类字段名
     */
    private String parameter;
    /**
     * 传值实体类字段名-是否字符串类型
     */
    private Boolean parameterType;
    /**
     * sql值
     */
    private String sql;
    /**
     * 区间开始
     */
    private String start;
    /**
     * 区间结束
     */
    private String end;
}
