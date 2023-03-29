package com.code.utils.entity;

import lombok.Data;

/**
 * @ClassName BeanQuery
 * @Description 查询类
 * @Author 孙凯伦
 * @Mobile 13777579028
 * @Email 376253703@qq.com
 * @Time 2021/8/10 3:26 下午
 */
@Data
public class BeanQuery {
    /**
     * 数据类型（1：String，2：Integer，3：Double，4：BigDecimal，5：LocalDateTime，6：Date，7：Long，8：byte[]）
     */
    private String type;
    /**
     * 名称
     */
    private String name;
    /**
     * 类型转换
     */
    private String format;
    /**
     * 描述
     */
    private String description;
}
