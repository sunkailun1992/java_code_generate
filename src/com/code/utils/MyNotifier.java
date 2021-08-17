package com.code.utils;

import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.Project;
import org.eclipse.sisu.Nullable;

/**
 * @ClassName MyNotifier
 * @Description 消息
 * @Author 孙凯伦
 * @Mobile 13777579028
 * @Email 376253703@qq.com
 * @Time 2021/8/17 9:47 上午
 */
public class MyNotifier {
    /**
     * 错误
     * @param project
     * @param content
     */
    public static void notifyError(@Nullable Project project, String content) {
        NotificationGroupManager.getInstance().getNotificationGroup("message")
                .createNotification(content, NotificationType.ERROR)
                .notify(project);
    }

    /**
     * 信息
     * @param project
     * @param content
     */
    public static void notifyInformation(@Nullable Project project, String content) {
        NotificationGroupManager.getInstance().getNotificationGroup("message")
                .createNotification(content, NotificationType.INFORMATION)
                .notify(project);
    }
}
