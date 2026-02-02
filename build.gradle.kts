plugins {
    id("java")
    id("org.jetbrains.intellij") version "1.17.4"
    id("io.freefair.lombok") version "6.3.0"
}

group = "com"
version = "2.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.freemarker:freemarker:2.3.29")
    implementation("org.apache.commons:commons-lang3:3.9")
    implementation("com.google.guava:guava:27.0.1-jre")
    implementation("cn.hutool:hutool-all:5.6.3")
    implementation("redis.clients:jedis:3.1.0")
    implementation("mysql:mysql-connector-java:8.0.19")

    compileOnly("org.projectlombok:lombok:1.18.12")
}

intellij {
    // ⚠️ 锁定 2023.3（非常稳定，不用 JCEF）
    version.set("2023.3.6")
    type.set("IC")
    plugins.set(emptyList())
}

tasks {

    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
        options.encoding = "UTF-8"
    }

    patchPluginXml {
        sinceBuild.set("233")
        untilBuild.set("241.*")
    }

    runIde {
        jvmArgs(
            "-Xmx2048m",
            "-Dfile.encoding=UTF-8",
            "-Dide.browser.jcef.enabled=false" // 🔥 关键，防止 macOS ARM 134
        )
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}
