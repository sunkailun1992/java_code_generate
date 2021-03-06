package com.code.action;

import com.code.utils.CodeGenerate;
import com.code.utils.MyNotifier;
import com.google.common.collect.Maps;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiDirectory;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

/**
 * @author: 孙凯伦
 * @mobile: 13777579028
 * @email: 376253703@qq.com
 * @description: TODO 代码生成器
 * @date: 2021/8/12 5:54 下午
 */
public class ServiceAction extends BaseAnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        this.init(anActionEvent);
        /**
         * 用户输入参数
         */
        String author = Messages.showInputDialog("作者名称", "作者", Messages.getInformationIcon());
        String moduleName = Messages.showInputDialog("模块名称", "模块", Messages.getInformationIcon());
        String tableName = Messages.showInputDialog("表名称(多表逗号分隔)", "数据库表", Messages.getInformationIcon());
        if (StringUtils.isNotBlank(author) && StringUtils.isNotBlank(tableName) && StringUtils.isNotBlank(moduleName)) {
            /**
             * 参数
             */
            final PsiDirectory psiDirectory = this.getPsiDirectory();
            /**
             * 项目地址:/Users/sunkailun/Desktop/工作/工保网/finance/src/main/java/com/gb
             */
            String path = psiDirectory.getVirtualFile().getPath().split("src/main/")[0];
            if (StringUtils.isBlank(path)) {
                MyNotifier.notifyError(this.getProject(), "目录信息未找到，请检查生成地址");
            }
            //参数
            Map<String, Object> map = Maps.newHashMap();
            map.put("author", author);
            map.put("moduleName", moduleName);
            map.put("include", tableName);
            map.put("controller", "n");
            map.put("entity", "n");
            map.put("entityBO", "n");
            map.put("entityVO", "n");
            map.put("entityQuery", "n");
            map.put("enums", "n");
            map.put("service", "y");
            map.put("serviceResults", "n");
            map.put("serviceQuery", "n");
            map.put("mapper", "n");
            try {
                CodeGenerate.beanQuery(map, tableName, path);
                CodeGenerate.serviceQuery(map, tableName, path);
                String[] includeS = map.get("include").toString().split(",");
                for (String s : includeS) {
                    //重写代码生成器
                    CodeGenerate.foundCode(s, String.valueOf(map.get("moduleName")), String.valueOf(map.get("author")), map, path);
                }
                MyNotifier.notifyInformation(this.getProject(), "代码生成成功，请刷新目录");
            } catch (FileNotFoundException e) {
                MyNotifier.notifyError(this.getProject(), "未找到对应文件：" + e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                MyNotifier.notifyError(this.getProject(), "获取文件失败：" + e.getMessage());
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
