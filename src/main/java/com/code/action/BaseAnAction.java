package com.code.action;

import com.intellij.ide.IdeView;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;

/**
 * 基础 Action 父类（IntelliJ IDEA 2024 推荐写法）
 *
 * 设计原则：
 * 1. Action 无状态
 * 2. 不缓存 AnActionEvent
 * 3. 不使用 getRequiredData
 */
public abstract class BaseAnAction extends AnAction {

    /**
     * 获取当前 Project
     */
    protected Project getProject(AnActionEvent e) {
        return e.getProject();
    }

    /**
     * 获取用户当前选中的目录
     */
    protected PsiDirectory getTargetDirectory(AnActionEvent e) {
        IdeView ideView = e.getData(LangDataKeys.IDE_VIEW);
        if (ideView == null) {
            return null;
        }
        return ideView.getOrChooseDirectory();
    }
}
