package com.code.action;

import com.code.utils.CodeGenerate;
import com.code.utils.MyNotifier;
import com.google.common.collect.Maps;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiDirectory;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * 一键生成全部代码 Action（IDEA 2023+ / 2024 正确线程模型）
 */
public class CodeAllAction extends BaseAnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {

        Project project = getProject(e);
        if (project == null) {
            return;
        }

        PsiDirectory psiDirectory = getTargetDirectory(e);
        if (psiDirectory == null) {
            MyNotifier.notifyError(project, "未选中目标目录");
            return;
        }

        String author = Messages.showInputDialog(
                project,
                "请输入作者名称",
                "作者",
                Messages.getInformationIcon()
        );

        String moduleName = Messages.showInputDialog(
                project,
                "请输入模块名称",
                "模块",
                Messages.getInformationIcon()
        );

        String tableName = Messages.showInputDialog(
                project,
                "请输入表名称（多表用逗号分隔）",
                "数据库表",
                Messages.getInformationIcon()
        );

        if (StringUtils.isAnyBlank(author, moduleName, tableName)) {
            return;
        }

        String path = psiDirectory.getVirtualFile()
                .getPath()
                .split("src/main/")[0];

        if (StringUtils.isBlank(path)) {
            MyNotifier.notifyError(project, "生成路径解析失败");
            return;
        }

        Map<String, Object> params = Maps.newHashMap();
        params.put("author", author);
        params.put("moduleName", moduleName);
        params.put("include", tableName);
        params.put("controller", "y");
        params.put("entity", "y");
        params.put("entityBO", "y");
        params.put("entityVO", "y");
        params.put("entityQuery", "y");
        params.put("enums", "y");
        params.put("service", "y");
        params.put("serviceResults", "y");
        params.put("serviceQuery", "y");
        params.put("mapper", "y");

        // 1️⃣ 后台线程（只负责耗时调度）
        ProgressManager.getInstance().run(
                new Task.Backgroundable(project, "Generating Code...", false) {

                    @Override
                    public void run(@NotNull ProgressIndicator indicator) {

                        // 2️⃣ 切回 EDT
                        ApplicationManager.getApplication().invokeLater(() -> {

                            // 3️⃣ 写命令（唯一合法的 PSI / 文件写入口）
                            WriteCommandAction.runWriteCommandAction(project, () -> {
                                try {
                                    CodeGenerate.beanQuery(params, tableName, path);
                                    CodeGenerate.serviceQuery(params, tableName, path);

                                    for (String table : tableName.split(",")) {
                                        CodeGenerate.foundCode(
                                                table.trim(),
                                                moduleName,
                                                author,
                                                params,
                                                path
                                        );
                                    }

                                    MyNotifier.notifyInformation(
                                            project,
                                            "代码生成完成，请刷新目录"
                                    );

                                } catch (Exception ex) {
                                    MyNotifier.notifyError(
                                            project,
                                            "生成失败：" + ex.getMessage()
                                    );
                                    ex.printStackTrace();
                                }
                            });
                        });
                    }
                }
        );
    }
}
