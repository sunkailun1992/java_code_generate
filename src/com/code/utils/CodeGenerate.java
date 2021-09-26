package com.code.utils;

import com.code.utils.entity.*;
import com.code.utils.freemarker.FreeMarkers;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.intellij.openapi.ui.Messages;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User:    孙凯伦
 * email:   skl@erongdu.com
 * Date:    2016/2/1
 * Time:    14:40
 * details: MySql数据库类型,代码生成器
 */
public class CodeGenerate {


    /**
     * @param table        需要生成的数据库表,例:pfa_asset_package_record
     * @param moduleName   生成代码的模块名称
     * @param author       作者
     * @param parameterMap 代码生成规则参数
     * @param path         生成文件地址
     * @author: 孙凯伦
     * @mobile: 13777579028
     * @email: 376253703@qq.com
     * @name: FoundCode
     * @description: TODO
     * @return: void
     * @date: 2021/8/12 5:20 下午
     */
    public static void foundCode(String table, String moduleName, String author, Map<String, Object> parameterMap, String path) throws Exception {
        //获得数据库配置
        PropertiesUtil propertiesUtil = PropertiesUtil.getMySqlProperties(path);
        //数据库
        String database = propertiesUtil.getProperty("database");
        //包地址
        String packageName = propertiesUtil.getProperty("packageName") + "." + moduleName;
        try {
            String table_name = "";
            //链接数据库操作
            Connection connection = CodeGenerateUtils.getDblink(path);
            //返回链接的Statement
            Statement stateMent_table = connection.createStatement();
            //查询该数据库所有的表名
            ResultSet tables = stateMent_table.executeQuery("show tables");
            //循环
            while (tables.next()) {
                //查询出列名为Tables_in_good_loan_dev,所有数据库表名
                table_name = tables.getString("Tables_in_" + database);
                //判断执行那个表的,生成代码操作
                if (!table_name.equals(table)) {
                    continue;
                }
                String cn = table_name;

                //获得,类名
                String className = CodeGenerateUtils.toUpper(cn.substring(0));

                //获得，表名注释
                ResultSet table_comment = stateMent_table.executeQuery("select TABLE_COMMENT from information_schema.tables where table_schema='" + database + "' and table_name='" + table + "'");
                String functionName = "";
                while (table_comment.next()) {
                    functionName = table_comment.getString("TABLE_COMMENT");
                }
                table_comment.close();
                //指定项目地址
                String address = path + "/src/main/java/" + packageName;
                //模板路径
                String freeMarkersName = path + "src/main/resources/templates/";
                // 代码模板配置
                Configuration cfg = new Configuration(Configuration.VERSION_2_3_29);
                cfg.setEncoding(Locale.getDefault(), "UTF-8");
                cfg.setDirectoryForTemplateLoading(new File(freeMarkersName));
                cfg.setOutputEncoding("UTF-8");
                cfg.setDefaultEncoding("UTF-8");
                // 定义模板变量
                Map<String, Object> model = Maps.newHashMap();
                //包地址,全部转换为小写
                model.put("packageName", StringUtils.lowerCase(packageName));
                //小写类名,全部转为首字母小写
                model.put("className", StringUtils.uncapitalize(className));
                //大写类名,全部转为首字母大写
                model.put("ClassName", className);
                //作者,判断字段是否为空
                model.put("author", author);
                //获得当前时间
                model.put("date", CodeGenerateUtils.getDate());
                //模块名称
                model.put("functionName", functionName);
                //数据库表名
                model.put("tableName", table_name);
                //表数据取得list
                List<Table> list = CodeGenerateUtils.getList(path, table_name, database);
                model.put("list", list);
                //括号
                model.put("braceL", "{");
                model.put("braceR", "}");
                //参数
                model.put("serviceQueryList", parameterMap.get("serviceQueryList"));
                model.put("beanQuery", parameterMap.get("beanQuery"));


                /**
                 * 生成 mapper
                 */
                //FreeMarkers模板地址
                Template daoTemplate = cfg.getTemplate("skl_mapper.java.ftl","UTF-8");
                daoTemplate.setEncoding("UTF-8");
                //渲染生成模板
                String daoContent = FreeMarkers.renderTemplate(daoTemplate, model);
                //生成的文件名称,判断如果有二号项目,生成在二号项目
                String daoFile = address.replace(".", "/") + "/mapper/" + model.get("ClassName") + "Mapper.java";
                //将内容写入文件
                if (StringUtils.equalsIgnoreCase(String.valueOf(parameterMap.get("mapper")), "y")) {
                    CodeGenerateUtils.writeFile(daoContent, daoFile);
                }

                /**
                 * 生成状态枚举类
                 */
                //给予实体类值对象
                List<EnumsEntityName> enumsList = Lists.newArrayList();
                //生成的枚举对象
                List<EnumsEntityContent> enumsEntitiesList = Lists.newArrayList();
                for (Table t : list) {
                    //判断是否取固定枚举
                    if(parameterMap.get("fixedEnums") != null){
                        if(!t.getTypeName().equals(parameterMap.get("fixedEnums"))){
                            continue;
                        }
                    }
                    //获得注释
                    String a = StringUtils.deleteWhitespace(t.getColumnComment());
                    //截取备注的状态
                    String b = StringUtils.substringBetween(a, "（", "）");
                    //判断是否为空
                    if (StringUtils.isNotBlank(b)) {
                        //截取多个状态
                        String[] c = b.split("，");
                        //循环
                        for (String cc : c) {
                            String[] dd = cc.split("：");
                            //判断是否有参数
                            if (dd.length >= 2) {
                                //根据多个状态循环
                                EnumsEntityContent enumsEntity = new EnumsEntityContent();
                                enumsEntity.setState(dd[0]);
                                enumsEntity.setNote(dd[1]);
                                enumsEntitiesList.add(enumsEntity);
                            }
                        }
                        //放入数据
                        enumsList.add(new EnumsEntityName() {{
                            setSmallName(t.getColumnName());
                            setBigName(t.getColumnNameUpper());
                        }});
                        //名称
                        model.put("enumsEntityName", t.getColumnNameUpper());
                        //状态集合
                        model.put("enumsEntitiesList", enumsEntitiesList);
                        /**
                         * 生成状态枚举类
                         */
                        //FreeMarkers模板地址
                        Template beanTemplateStateEnums = cfg.getTemplate("skl_entity_enums.java.ftl","UTF-8");
                        beanTemplateStateEnums.setEncoding("UTF-8");
                        //渲染生成模板
                        String beanContentStateEnums = FreeMarkers.renderTemplate(beanTemplateStateEnums, model);
                        //生成的文件地址
                        String beanFileStateEnums = address.replace(".", "/") + "/entity/enums/" + model.get("ClassName") + t.getColumnNameUpper() + "Enum" + ".java";
                        //将内容写入文件
                        if (StringUtils.equalsIgnoreCase(String.valueOf(parameterMap.get("enums")), "y")) {
                            CodeGenerateUtils.writeFile(beanContentStateEnums, beanFileStateEnums);
                        }
                    }
                    enumsEntitiesList = Lists.newArrayList();
                }
                model.put("enumsList", enumsList);
                /**
                 * 生成实体类
                 */
                //FreeMarkers模板地址
                Template beanTemplate = cfg.getTemplate("skl_entity.java.ftl","UTF-8");
                beanTemplate.setEncoding("UTF-8");
                //渲染生成模板
                String beanContent = FreeMarkers.renderTemplate(beanTemplate, model);
                //生成的文件地址
                String beanFile = address.replace(".", "/") + "/entity/" + model.get("ClassName") + ".java";
                //将内容写入文件
                if (StringUtils.equalsIgnoreCase(String.valueOf(parameterMap.get("entity")), "y")) {
                    CodeGenerateUtils.writeFile(beanContent, beanFile);
                }

                /**
                 * 生成VO类
                 */
                //FreeMarkers模板地址
                Template beanTemplateVO = cfg.getTemplate("skl_entity_vo.java.ftl","UTF-8");
                beanTemplateVO.setEncoding("UTF-8");
                //渲染生成模板
                String beanContentVO = FreeMarkers.renderTemplate(beanTemplateVO, model);
                //生成的文件地址
                String beanFileVO = address.replace(".", "/") + "/entity/vo/" + model.get("ClassName") + "VO" + ".java";
                //将内容写入文件
                if (StringUtils.equalsIgnoreCase(String.valueOf(parameterMap.get("entityVO")), "y")) {
                    CodeGenerateUtils.writeFile(beanContentVO, beanFileVO);
                }

                /**
                 * 生成VO类
                 */
                //FreeMarkers模板地址
                Template beanTemplateBO = cfg.getTemplate("skl_entity_bo.java.ftl","UTF-8");
                beanTemplateBO.setEncoding("UTF-8");
                //渲染生成模板
                String beanContentBO = FreeMarkers.renderTemplate(beanTemplateBO, model);
                //生成的文件地址
                String beanFileBO = address.replace(".", "/") + "/entity/bo/" + model.get("ClassName") + "BO" + ".java";
                //将内容写入文件
                if (StringUtils.equalsIgnoreCase(String.valueOf(parameterMap.get("entityBO")), "y")) {
                    CodeGenerateUtils.writeFile(beanContentBO, beanFileBO);
                }

                /**
                 * 生成Query类
                 */
                //FreeMarkers模板地址
                Template beanTemplateQuery = cfg.getTemplate("skl_entity_query.java.ftl","UTF-8");
                beanTemplateQuery.setEncoding("UTF-8");
                //渲染生成模板
                String beanContentQuery = FreeMarkers.renderTemplate(beanTemplateQuery, model);
                //生成的文件地址
                String beanFileQuery = address.replace(".", "/") + "/entity/query/" + model.get("ClassName") + "Query" + ".java";
                //将内容写入文件
                if (StringUtils.equalsIgnoreCase(String.valueOf(parameterMap.get("entityQuery")), "y")) {
                    CodeGenerateUtils.writeFile(beanContentQuery, beanFileQuery);
                }

                /**
                 * 生成 Service 代码
                 */
                //FreeMarkers模板地址
                Template serviceTemplate = cfg.getTemplate("skl_service.java.ftl","UTF-8");
                serviceTemplate.setEncoding("UTF-8");
                //渲染生成模板
                String serviceContent = FreeMarkers.renderTemplate(serviceTemplate, model);
                //生成的文件名称
                String serviceFile = address.replace(".", "/") + "/service/" + model.get("ClassName") + "Service.java";
                //将内容写入文件
                if (StringUtils.equalsIgnoreCase(String.valueOf(parameterMap.get("service")), "y")) {
                    CodeGenerateUtils.writeFile(serviceContent, serviceFile);
                }

                /**
                 * 生成 Service 实现代码
                 */
                //FreeMarkers模板地址
                Template serviceImplTemplate = cfg.getTemplate("skl_serviceImpl.java.ftl","UTF-8");
                serviceImplTemplate.setEncoding("UTF-8");
                //渲染生成模板
                String serviceI_Content = FreeMarkers.renderTemplate(serviceImplTemplate, model);
                //生成的文件名称
                String serviceI_File = address.replace(".", "/") + "/service/impl/" + model.get("ClassName") + "ServiceImpl.java";
                //将内容写入文件
                if (StringUtils.equalsIgnoreCase(String.valueOf(parameterMap.get("service")), "y")) {
                    CodeGenerateUtils.writeFile(serviceI_Content, serviceI_File);
                }

                /**
                 * 生成 Service 查询代码
                 */
                //FreeMarkers模板地址
                Template serviceTemplateQuery = cfg.getTemplate("skl_service_query.java.ftl","UTF-8");
                serviceTemplateQuery.setEncoding("UTF-8");
                //渲染生成模板
                String serviceContentQuery = FreeMarkers.renderTemplate(serviceTemplateQuery, model);
                //生成的文件名称
                String serviceFileQuery = address.replace(".", "/") + "/service/query/" + model.get("ClassName") + "ServiceQuery.java";
                //将内容写入文件
                if (StringUtils.equalsIgnoreCase(String.valueOf(parameterMap.get("serviceQuery")), "y")) {
                    CodeGenerateUtils.writeFile(serviceContentQuery, serviceFileQuery);
                }


                /**
                 * 生成 Service 返回代码
                 */
                //FreeMarkers模板地址
                Template serviceTemplateResults = cfg.getTemplate("skl_service_results.java.ftl","UTF-8");
                serviceTemplateResults.setEncoding("UTF-8");
                //渲染生成模板
                String serviceContentResults = FreeMarkers.renderTemplate(serviceTemplateResults, model);
                //生成的文件名称
                String serviceFileResults = address.replace(".", "/") + "/service/results/" + model.get("ClassName") + "ServiceResults.java";
                //将内容写入文件
                if (StringUtils.equalsIgnoreCase(String.valueOf(parameterMap.get("serviceResults")), "y")) {
                    CodeGenerateUtils.writeFile(serviceContentResults, serviceFileResults);
                }

                /**
                 * 生成 Action代码
                 */
                //FreeMarkers模板地址
                Template actionTemplate = cfg.getTemplate("skl_controller.java.ftl","UTF-8");
                actionTemplate.setEncoding("UTF-8");
                //渲染生成模板
                String actionContent = FreeMarkers.renderTemplate(actionTemplate, model);
                //生成的文件名称
                String actionFile = address.replace(".", "/") + "/controller/" + model.get("ClassName") + "Controller.java";
                //将内容写入文件
                if (StringUtils.equalsIgnoreCase(String.valueOf(parameterMap.get("controller")), "y")) {
                    CodeGenerateUtils.writeFile(actionContent, actionFile);
                }


                break;
            }
            tables.close();
            stateMent_table.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 自定义枚举类
     * @param parameterMap
     */
    public static void fixedEnums(Map<String, Object> parameterMap) {
        if (StringUtils.equalsIgnoreCase(String.valueOf(parameterMap.get("enums")), "y")) {
            //判断是设置枚举类
            String a = Messages.showInputDialog("枚举字段名称（不填写全部生成，填写只生成填写字段）", "是否设置对应枚举类", Messages.getInformationIcon());
            if (StringUtils.isNotBlank(a)) {
                parameterMap.put("fixedEnums", a);
            }
        }
    }

    /**
     * 自定义类查询
     *
     * @param parameterMap
     * @param tableName
     */
    public static void beanQuery(Map<String, Object> parameterMap, String tableName, String path) throws Exception {
        //取出之前参数
        String hget = RedisUtils.hget(RedisUtils.getJedis(path), "beanQuery", tableName);
        //判断存在，复用
        List<BeanQuery> mapList = StringUtils.isNotBlank(hget) ? JsonUtil.list(hget, BeanQuery.class) : Lists.newArrayList();
        if (StringUtils.equalsIgnoreCase(String.valueOf(parameterMap.get("entityQuery")), "y")) {
            Boolean b;
            do {
                //判断是否自定义查询条件
                Integer queryB = Messages.showYesNoCancelDialog("是否自定义查询变量", "是否自定义查询变量", Messages.getInformationIcon());
                //赋值
                b = queryB == 0;
                //判断自定义的话，赋值新map
                if (b) {
                    BeanQuery query = new BeanQuery();
                    //自定义数据类型
                    String [] values = {"String","Integer","Double","BigDecimal","LocalDateTime","Date","Long","byte[]"};
                    String type = String.valueOf(Messages.showChooseDialog("变量数据类型", "变量数据类型",values,"String", Messages.getInformationIcon()));
                    //字段名称
                    String name = Messages.showInputDialog("变量名称", "变量", Messages.getInformationIcon());
                    //描述
                    String description = Messages.showInputDialog("变量描述", "变量", Messages.getInformationIcon());
                    query.setType(type);
                    query.setName(name);
                    query.setDescription(description);
                    /**
                     * 类型转换
                     */
                    switch (type) {
                        case "0":
                            query.setFormat("String");
                            break;
                        case "1":
                            query.setFormat("Integer");
                            break;
                        case "2":
                            query.setFormat("Double");
                            break;
                        case "3":
                            query.setFormat("BigDecimal");
                            break;
                        case "4":
                            query.setFormat("LocalDateTime");
                            break;
                        case "5":
                            query.setFormat("Date");
                            break;
                        case "6":
                            query.setFormat("Long");
                            break;
                        case "7":
                            query.setFormat("byte[]");
                            break;
                    }
                    /**
                     * 判断是否重复赋值
                     */
                    Boolean c = true;
                    for (BeanQuery q : mapList) {
                        //判断传值查询
                        if (q.getName().equals(query.getName())) {
                            c = false;
                        }
                    }
                    if (c) {
                        mapList.add(query);
                    }
                }
            } while (b);
        }
        //放入缓存
        RedisUtils.hset(RedisUtils.getJedis(path), "beanQuery", tableName, JsonUtil.json(mapList));
        //放入模板参数
        parameterMap.put("beanQuery", mapList);
    }


    /**
     * 自定义查询规则
     *
     * @param parameterMap
     * @param tableName
     */
    public static void serviceQuery(Map<String, Object> parameterMap, String tableName, String path) throws Exception {
        //取出之前参数
        String hget = RedisUtils.hget(RedisUtils.getJedis(path), "serviceQuery", tableName);
        //判断存在，复用
        List<ServiceQuery> mapList = StringUtils.isNotBlank(hget) ? JsonUtil.list(hget, ServiceQuery.class) : Lists.newArrayList();
        if (StringUtils.equalsIgnoreCase(String.valueOf(parameterMap.get("serviceQuery")), "y")) {
            Boolean b;
            do {
                //判断是否自定义查询条件
                Integer queryB = Messages.showYesNoCancelDialog("是否自定义查询条件", "是否自定义查询条件", Messages.getInformationIcon());
                //赋值
                b = queryB == 0;
                //判断自定义的话，赋值新map
                if (b) {
                    ServiceQuery query = new ServiceQuery();
                    //自定义查询条件
                    String [] values = {"等于","模糊查询","为空查询","不为空查询","in查询","notIn查询","inSql查询","notInSql查询","notInSql查询","区间查询","非该区间查询"};
                    String type = String.valueOf(Messages.showChooseDialog("查询类型", "查询类型",values,"等于", Messages.getInformationIcon()));
                    //查询的表列名
                    String column = "`" + Messages.showInputDialog("数据库字段名称", "数据库字段", Messages.getInformationIcon()) + "`";
                    //判断是否区间查询
                    if (type.equals("8") || type.equals("9")) {
                        //区间开始
                        String start = StringUtils.capitalize(Messages.showInputDialog("传值实体类-变量名", "区间时间-开始查询", Messages.getInformationIcon()));
                        //区间结束
                        String end = StringUtils.capitalize(Messages.showInputDialog("传值实体类-变量名", "区间时间-结束查询", Messages.getInformationIcon()));
                        //传值实体类字段名-是否字符串类型
                        Boolean parameterType = Messages.showYesNoCancelDialog("是否字符串类型", "是否字符串类型", Messages.getInformationIcon()) == 0;
                        query.setParameterType(parameterType);
                        query.setStart(start);
                        query.setEnd(end);
                    }
                    //判断是否sql查询
                    if (type.equals("6") || type.equals("7")) {
                        //传值实体类字段名
                        String parameter = StringUtils.capitalize(Messages.showInputDialog("传值实体类-变量名称", "传值实体类-变量", Messages.getInformationIcon()));
                        query.setParameter(parameter);
                        //传值实体类字段名-是否字符串类型
                        Boolean parameterType = Messages.showYesNoCancelDialog("是否字符串类型", "是否字符串类型", Messages.getInformationIcon()) == 0;
                        query.setParameterType(parameterType);
                        //执行sql
                        String sql = Messages.showInputDialog("执行sql", "数据库语句", Messages.getInformationIcon());
                        query.setSql("\"" + sql + "\"");
                    }
                    //判断传值查询
                    if (type.equals("0") || type.equals("1") || type.equals("2") || type.equals("3") || type.equals("4") || type.equals("5")) {
                        //传值实体类字段名
                        String parameter = StringUtils.capitalize(Messages.showInputDialog("传值实体类-变量名称", "传值实体类-变量", Messages.getInformationIcon()));
                        query.setParameter(parameter);
                        //传值实体类字段名-是否字符串类型
                        Boolean parameterType = Messages.showYesNoCancelDialog("是否字符串类型", "是否字符串类型", Messages.getInformationIcon()) == 0;
                        query.setParameterType(parameterType);
                    }
                    query.setType(type);
                    query.setColumn(column);
                    /**
                     * 判断是否重复赋值
                     */
                    Boolean c = true;
                    for (ServiceQuery q : mapList) {
                        //判断是否区间查询
                        if (type.equals("8") || type.equals("9")) {
                            if (q.getColumn().equals(query.getColumn()) && q.getStart().equals(query.getStart()) && q.getEnd().equals(query.getEnd())) {
                                c = false;
                            }
                        }
                        //判断是否sql查询
                        if (type.equals("6") || type.equals("7")) {
                            if (q.getColumn().equals(query.getColumn()) && q.getParameter().equals(query.getParameter()) && q.getSql().equals(query.getSql())) {
                                c = false;
                            }
                        }
                        //判断传值查询
                        if (type.equals("0") || type.equals("1") || type.equals("2") || type.equals("3") || type.equals("4") || type.equals("5")) {
                            if (q.getParameter().equals(query.getParameter()) && q.getColumn().equals(query.getColumn())) {
                                c = false;
                            }
                        }
                    }
                    if (c) {
                        mapList.add(query);
                    }
                }
            } while (b);
        }
        //放入缓存
        RedisUtils.hset(RedisUtils.getJedis(path), "serviceQuery", tableName, JsonUtil.json(mapList));
        //放入模板参数
        parameterMap.put("serviceQueryList", mapList);
    }

    public static void main(String[] args) {
        //获得注释
        String a = StringUtils.deleteWhitespace("类型（0：首页，1：百万年薪，2：报价方案，  3  ：  工保百科）");
        //截取备注的状态
        String b = StringUtils.substringBetween(a, "（", "）");
        //判断是否为空
        if (StringUtils.isNotBlank(b)) {
            //截取多个状态
            String[] c = b.split("，");
            //循环
            for (String cc : c) {
                String[] dd = cc.split("：");
                //判断是否有参数
                if (dd.length >= 2) {
                    //根据多个状态循环
                    EnumsEntityContent enumsEntity = new EnumsEntityContent();
                    enumsEntity.setState(dd[0]);
                    enumsEntity.setNote(dd[1]);
                    System.out.println(enumsEntity.toString());
                }
            }
        }
    }
}
