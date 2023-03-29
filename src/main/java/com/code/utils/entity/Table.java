package com.code.utils.entity;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User:    孙凯伦
 * email:   skl@erongdu.com
 * Date:    2016/2/1
 * Time:    14:40
 * details: 数据库信息
 * @author sunkailun
 */
@Data
public class Table {
	/**
	 * 实体类名
	 */
	private String columnName;
	/**
	 * 数据库类型
	 */
	private String dataType;
	/**
	 * 数据库注释
	 */
	private String columnComment;
	/**
	 * 首字母大写列名
	 */
	private String columnNameUpper;
	/**
	 * 数据无转换列名
	 */
	private String typeName;
}
