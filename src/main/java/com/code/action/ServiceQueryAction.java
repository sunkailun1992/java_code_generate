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
 * ServiceQuery 代码生成 Action
 *
 * 生成：
 *  - ServiceQuery
 *
 * 适配 IntelliJ IDEA 2024+
 *
 * @author 孙凯伦
 */
public class ServiceQueryAction extends BaseAnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {

        // 获取当前 Project
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

        // 输入作者名称
        String author = Messages.showInputDialog(
                project,
                "作者名称",
                "作者",
                Messages.getInformationIcon()
        );

        // 输入模块名称
        String moduleName = Messages.showInputDialog(
                project,
                "模块名称",
                "模块",
                Messages.getInformationIcon()
        );

        // 输入表名称（支持多表）
        String tableName = Messages.showInputDialog(
                project,
                "表名称（多表用逗号分隔）",
                "数据库表",
                Messages.getInformationIcon()
        );

        // 校验输入
        if (StringUtils.isAnyBlank(author, moduleName, tableName)) {
            return;
        }

        // 解析项目根路径
        String path = psiDirectory
                .getVirtualFile()
                .getPath()
                .split("src/main/")[0];

        if (StringUtils.isBlank(path)) {
            MyNotifier.notifyError(project, "目录信息未找到，请检查生成地址");
            return;
        }

        // 构建生成参数
        Map<String, Object> params = Maps.newHashMap();
        params.put("author", author);               // 作者
        params.put("moduleName", moduleName);       // 模块名
        params.put("include", tableName);           // 表名
        params.put("controller", "n");              // 不生成 Controller
        params.put("entity", "n");                  // 不生成 Entity
        params.put("entityBO", "n");                // 不生成 BO
        params.put("entityVO", "n");                // 不生成 VO
        params.put("entityQuery", "n");             // 不生成 EntityQuery
        params.put("enums", "n");                   // 不生成 Enum
        params.put("service", "n");                 // 不生成 Service
        params.put("serviceResults", "n");          // 不生成 ServiceResults
        params.put("serviceQuery", "y");            // ✅ 生成 ServiceQuery
        params.put("mapper", "n");                  // 不生成 Mapper

        // 后台执行代码生成
        ProgressManager.getInstance().run(
                new Task.Backgroundable(project, "Generating ServiceQuery Code...", false) {

                    @Override
                    public void run(@NotNull ProgressIndicator indicator) {

                        try {
                            // 初始化依赖
                            CodeGenerate.beanQuery(params, tableName, path);
                            CodeGenerate.serviceQuery(params, tableName, path);

                            // 支持多表
                            String[] tables = tableName.split(",");

                            for (String table : tables) {
                                CodeGenerate.foundCode(
                                        table.trim(),
                                        moduleName,
                                        author,
                                        params,
                                        path
                                );
                            }

                            // 成功提示
                            MyNotifier.notifyInformation(
                                    project,
                                    "ServiceQuery 代码生成成功，请刷新目录"
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
