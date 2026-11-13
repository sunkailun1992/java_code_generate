# AGENTS.md

本文件是 `java_code_generate` 的 AI 编码入口。修改代码前先读本文件，再看 `build.gradle.kts` 与 `src/main/resources/META-INF/plugin.xml`。

## 项目定位

- IntelliJ IDEA 代码生成插件：Gradle IntelliJ 插件 `org.jetbrains.intellij` 1.17.4，目标 IDEA Community 2023.3.6，兼容 build 233 至 241.*。
- Java 17，Gradle Kotlin DSL；用 FreeMarker 模板生成代码。源码在 `src/main/java/com/code`，插件描述在 `src/main/resources/META-INF/plugin.xml`。
- 当前是不活跃的历史项目，默认分支为 `master`。

## 构建与验证

- `./gradlew build` 编译；插件包用 `./gradlew buildPlugin`；`./gradlew runIde` 启动带插件的沙箱 IDE，共享运行配置在 `.run/`。
- 仓库没有 CI 和自动化测试，提交说明写明本机验证了什么。
- 测试结果只能由输入决定，不依赖执行顺序或本机遗留状态；偶发失败按缺陷处理，不用重试、跳过或删除测试掩盖。

## 合并与边界

- 维护者本人，以及维护者本机用维护者账号运行的 Claude / Codex 会话，可以直接合并到 `master`，合并前本机验证全过；其他人的代码走 PR。代理不修改 ruleset 与仓库权限，除非用户当场明确要求。
- 不提交 `.idea/`、`*.iml`、`build/` 等 IDE 与构建产物；需要共享的运行配置只放 `.run/`。
- 不在代码、模板或配置里写数据库、Redis 等真实连接口令和本机绝对路径。
