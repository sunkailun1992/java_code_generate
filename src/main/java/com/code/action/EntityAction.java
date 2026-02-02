package com.code.action;

import com.code.utils.CodeGenerate;
import com.code.utils.MyNotifier;
import com.google.common.collect.Maps;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiDirectory;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

/**
 * Entity 实体类代码生成 Action
 *
 * 适配 IntelliJ IDEA 2024.x
 * 所有耗时操作放入后台线程
 *
 * @author 孙凯伦
 */
public class EntityAction extends BaseAnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {

        // 获取当前项目
        Project project = getProject(e);
        if (project == null) {
            return;
        }

        // 获取用户选中的目标目录
        PsiDirectory psiDirectory = getTargetDirectory(e);
        if (psiDirectory == null) {
            MyNotifier.notifyError(project, "未选中目标目录");
            return;
        }

        // 弹窗获取作者名称
        String author = Messages.showInputDialog(
                project,
                "作者名称",
                "作者",
                Messages.getInformationIcon()
        );

        // 弹窗获取模块名称
        String moduleName = Messages.showInputDialog(
                project,
                "模块名称",
                "模块",
                Messages.getInformationIcon()
        );

        // 弹窗获取数据库表名（支持多表）
        String tableName = Messages.showInputDialog(
                project,
                "表名称（多表用逗号分隔）",
                "数据库表",
                Messages.getInformationIcon()
        );

        // 参数校验，任意为空直接返回
        if (StringUtils.isAnyBlank(author, moduleName, tableName)) {
            return;
        }

        // 解析项目根路径（src/main 之前）
        String path = psiDirectory
                .getVirtualFile()
                .getPath()
                .split("src/main/")[0];

        // 路径校验
        if (StringUtils.isBlank(path)) {
            MyNotifier.notifyError(project, "目录信息未找到，请检查生成地址");
            return;
        }

        // 构建代码生成参数 Map
        Map<String, Object> params = Maps.newHashMap();
        params.put("author", author);             // 作者
        params.put("moduleName", moduleName);     // 模块名
        params.put("include", tableName);         // 表名
        params.put("controller", "n");            // 不生成 Controller
        params.put("entity", "y");                // 生成 Entity
        params.put("entityBO", "n");              // 不生成 BO
        params.put("entityVO", "n");              // 不生成 VO
        params.put("entityQuery", "n");           // 不生成 Query
        params.put("enums", "n");                 // 不生成 Enum
        params.put("service", "n");               // 不生成 Service
        params.put("serviceResults", "n");        // 不生成 ServiceResults
        params.put("serviceQuery", "n");          // 不生成 ServiceQuery
        params.put("mapper", "n");                // 不生成 Mapper

        // 在后台线程执行代码生成，避免阻塞 UI
        ProgressManager.getInstance().run(
                new Task.Backgroundable(project, "Generating Entity Code...", false) {

                    @Override
                    public void run(@NotNull ProgressIndicator indicator) {

                        try {
                            // 生成 Query 相关代码
                            CodeGenerate.beanQuery(params, tableName, path);

                            // 生成 Service Query 相关代码
                            CodeGenerate.serviceQuery(params, tableName, path);

                            // 支持多表生成
                            String[] tables = tableName.split(",");

                            for (String table : tables) {
                                // 调用核心代码生成方法
                                CodeGenerate.foundCode(
                                        table.trim(),      // 表名
                                        moduleName,        // 模块名
                                        author,            // 作者
                                        params,            // 参数 Map
                                        path               // 项目路径
                                );
                            }

                            // 成功提示
                            MyNotifier.notifyInformation(
                                    project,
                                    "Entity 代码生成成功，请刷新目录"
                            );

                        } catch (FileNotFoundException ex) {
                            MyNotifier.notifyError(
                                    project,
                                    "未找到对应文件：" + ex.getMessage()
                            );
                        } catch (IOException ex) {
                            MyNotifier.notifyError(
                                    project,
                                    "获取文件失败：" + ex.getMessage()
                            );
                        } catch (Exception ex) {
                            MyNotifier.notifyError(
                                    project,
                                    "代码生成异常：" + ex.getMessage()
                            );
                        }
                    }
                }
        );
    }
}
