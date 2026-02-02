package com.code.utils;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nullable;

/**
 * @ClassName MyNotifier
 * @Description 消息通知工具类（兼容 IDEA 2023+）
 * @Author 孙凯伦
 */
public class MyNotifier {

    /**
     * plugin.xml 中定义的 NotificationGroup ID
     */
    private static final String GROUP_ID = "SklCodeGenerateNotification";

    /**
     * 获取通知组（防止 IDEA 尚未完成初始化时返回 null）
     */
    private static NotificationGroup getGroup() {
        NotificationGroup group =
                NotificationGroupManager.getInstance().getNotificationGroup(GROUP_ID);

        if (group == null) {
            // 兜底，避免 NPE（官方推荐做法）
            group = NotificationGroup.balloonGroup(GROUP_ID);
        }
        return group;
    }

    /**
     * 错误通知
     */
    public static void notifyError(@Nullable Project project, String content) {
        ApplicationManager.getApplication().invokeLater(() -> {
            Notification notification =
                    getGroup().createNotification(content, NotificationType.ERROR);
            notification.notify(project);
        });
    }

    /**
     * 信息通知
     */
    public static void notifyInformation(@Nullable Project project, String content) {
        ApplicationManager.getApplication().invokeLater(() -> {
            Notification notification =
                    getGroup().createNotification(content, NotificationType.INFORMATION);
            notification.notify(project);
        });
    }
}
