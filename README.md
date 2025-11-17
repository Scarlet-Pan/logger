# Logger

[[Maven Central](https://img.shields.io/maven-central/v/io.github.scarlet-pan/logger-jvm)](https://search.maven.org/artifact/io.github.scarlet-pan/logger-jvm)  
[[Kotlin Multiplatform](https://img.shields.io/badge/Kotlin-Multiplatform-blue?logo=kotlin)](https://kotlinlang.org/docs/multiplatform.html)  
[[Platforms: Android | iOS | JVM | JS](https://img.shields.io/badge/Platforms-Android%20%7C%20iOS%20%7C%20JVM%20%7C%20JS-lightgrey)]()  
[[Kotlin ≥1.9.0](https://img.shields.io/badge/Kotlin-≥1.9.0-orange?logo=kotlin)](https://kotlinlang.org)  
[[Test Status](https://github.com/scarlet-pan/logger/actions/workflows/test.yml/badge.svg?branch=main)](https://github.com/scarlet-pan/logger/actions/workflows/test.yml)  
[[License](https://img.shields.io/github/license/scarlet-pan/logger)](LICENSE)

---

## 🌐 Why Use It?

- ✅ **Built for Kotlin Multiplatform (KMP)**: Use `Logger` directly in `commonMain` — no `expect/actual`, no conditional compilation.
- ✅ **Android Log Style**: Familiar `d()`, `i()`, `w()`, `e()` APIs, just like `android.util.Log`.
- ✅ **Works Everywhere**: In **KMP libraries**, **Android apps**, **JVM services**, **iOS**, and **JS** — same code, everywhere.
- ✅ **Zero Platform Code**: Automatically adapts to each platform’s standard output:
    - **Android** → `android.util.Log`
    - **iOS** → Standard output (`println`, visible in Xcode console)
    - **JVM** → `System.out`
    - **JS** → `console.log`

> 🎯 **Log in shared Kotlin code like you always did on Android — it just works.**

---

## 📦 Installation

### Kotlin Multiplatform Projects (Recommended)

Add to your `shared` module’s `build.gradle.kts`:

```kotlin
dependencies {
    implementation("io.github.scarlet-pan:logger:1.0.0")
}
```

✅ No extra setup. No platform-specific configuration. Works automatically across all targets.

---

### Pure JVM / Java Projects

If you're using this library in a **non-KMP JVM project** (e.g., Spring Boot, Quarkus, or plain Java), you must depend on the **JVM-specific artifact**:

```kotlin
// Gradle (Kotlin DSL)
implementation("io.github.scarlet-pan:logger-jvm:1.0.0")
```

```groovy
// Gradle (Groovy DSL)
implementation 'io.github.scarlet-pan:logger-jvm:1.0.0'
```

```xml
<!-- Maven -->
<dependency>
    <groupId>io.github.scarlet-pan</groupId>
    <artifactId>logger-jvm</artifactId>
    <version>1.0.0</version>
</dependency>
```

> ⚠️ **Important**: The `logger` artifact is a Kotlin Multiplatform metadata package and **cannot be used directly in Java-only projects**. Always use `logger-jvm` for pure JVM environments.

---

> 🔧 **Minimum Requirements**
> - Kotlin **≥ 1.9.0** (built with **1.9.24**)
> - Android minSdk **≥ 21**
> - iOS deployment target **≥ 12.0**

---

## 🚀 Usage (Kotlin First)

Whether you're building a **Kotlin Multiplatform library**, an **Android app**, or a **JVM service**, the API is identical:

```kotlin
import dev.scarlet.logger.Logger

Logger.d("Network", "Request sent")
Logger.i("Database", "Query succeeded")
Logger.w("App", "Deprecated API used")
Logger.e("Crash", "Unexpected error", exception)
```

✅ Works out-of-the-box in:
- `commonMain` of KMP projects
- Android app modules (`androidMain`)
- Pure JVM applications (`jvmMain`)
- iOS and JS targets (via KMP)

No platform-specific code. No conditional compilation. Just Kotlin.

> 💡 This is the **primary and recommended** way to use `Logger`.

---

### 🔧 Customize Logging Behavior

Replace or combine loggers using the `+` operator:

```kotlin
// Send logs to both system and your custom logger
Logger.default = Logger.SYSTEM + CustomLogger()
```

All subsequent calls to `Logger.*()` will dispatch to both destinations.

You can also implement your own `Logger` by overriding the interface:

```kotlin
object FileLogger : Logger {
    override fun d(tag: String, msg: String, tr: Throwable?) {
        // Write to file, send to remote, etc.
    }
    // ... implement i, w, e
}
```

Then set it as default:

```kotlin
Logger.default = FileLogger
```

---

### ☕ Java Interoperability (Optional)

Although designed for Kotlin, this library is fully usable from **Java projects targeting the JVM** thanks to Kotlin/JVM bytecode compatibility.

Use the static facade for convenience:

```java
import dev.scarlet.logger.DefaultLogger;

DefaultLogger.d("MainActivity", "User clicked button");
DefaultLogger.e("Network", "Failed to load data", exception);
```

To customize the logging pipeline (e.g., combine with a custom logger):

```java
import dev.scarlet.logger.Logger;
import dev.scarlet.logger.Loggers;

Logger combined = Loggers.combine(Logger.getDefault(), new CustomLogger());
Logger.setDefault(combined);
```

> ⚠️ Note:  
> - The `DefaultLogger` class exists **only for Java interop**.  
> - All advanced features (e.g., custom loggers) should be configured via Kotlin.  
> - This is **not a Java-first logging framework** — Kotlin is the primary language.  
> - **Make sure you’re using `logger-jvm`**, not `logger`, in your Java project!

---

## 📚 API

Mirrors Android’s `Log` for instant familiarity:

```kotlin
fun d(tag: String, msg: String, tr: Throwable? = null)
fun i(tag: String, msg: String, tr: Throwable? = null)
fun w(tag: String, msg: String, tr: Throwable? = null)
fun e(tag: String, msg: String, tr: Throwable? = null)
```

- Tag-first design (easy filtering)
- Optional `Throwable` support (auto stack trace)
- Fully thread-safe and coroutine-friendly

---

## 🌍 Supported Platforms

| Platform | Logging Backend        |
|----------|------------------------|
| Android  | `android.util.Log`     |
| iOS      | `println()`            |
| JVM      | `System.out`           |
| JS (IR)  | `console.log`          |

> ℹ️ Built with **Kotlin 1.9.24**, compatible with **Kotlin 1.9.0+**.  
> Java projects can consume the **JVM artifact** via standard Maven/Gradle dependencies.

---

## 🤝 Contributing

Contributions welcome! See [CONTRIBUTING.md](CONTRIBUTING.md).

---

## 📄 License

This project is licensed under the **MIT License** – see [LICENSE](LICENSE).

<br><br>
<div align="center">
  <hr width="80%" />
  <p><em>—— 中文文档 Chinese Documentation ——</em></p>
  <hr width="80%" />
</div>
<br><br>

# Logger

[[Maven Central](https://img.shields.io/maven-central/v/io.github.scarlet-pan/logger-jvm)](https://search.maven.org/artifact/io.github.scarlet-pan/logger-jvm)  
[[Kotlin 多平台](https://img.shields.io/badge/Kotlin-Multiplatform-blue?logo=kotlin)](https://kotlinlang.org/docs/multiplatform.html)  
[[支持平台: Android | iOS | JVM | JS](https://img.shields.io/badge/平台-Android%20%7C%20iOS%20%7C%20JVM%20%7C%20JS-lightgrey)]()  
[[Kotlin ≥1.9.0](https://img.shields.io/badge/Kotlin-≥1.9.0-orange?logo=kotlin)](https://kotlinlang.org)  
[[测试状态](https://github.com/scarlet-pan/logger/actions/workflows/test.yml/badge.svg?branch=main)](https://github.com/scarlet-pan/logger/actions/workflows/test.yml)  
[[许可证](https://img.shields.io/github/license/scarlet-pan/logger)](LICENSE)

---

## 🌐 为什么选择它？

- ✅ **专为 Kotlin 多平台（KMP）设计**：在 `commonMain` 中直接调用 `Logger`，无需 `expect/actual`，无需条件编译。
- ✅ **Android 日志风格**：提供与 `android.util.Log` 完全一致的 `d()`、`i()`、`w()`、`e()` 接口。
- ✅ **全平台通用**：适用于 **KMP 库**、**Android 应用**、**JVM 服务**、**iOS** 和 **JS** —— 同一份代码，处处运行。
- ✅ **零平台代码**：自动适配各平台的标准输出方式：
  - **Android** → `android.util.Log`
  - **iOS** → 标准输出（`println`，Xcode 控制台可见）
  - **JVM** → `System.out`
  - **JS** → `console.log`

> 🎯 **在共享 Kotlin 代码中像写 Android 一样打日志 —— 开箱即用。**

---

## 📦 安装

### Kotlin 多平台项目（推荐）

在你的 `shared` 模块的 `build.gradle.kts` 中添加：

```kotlin
dependencies {
    implementation("io.github.scarlet-pan:logger:1.0.0")
}
```

✅ 无需额外配置，自动适配所有目标平台。

---

### 纯 JVM / Java 项目

如果你在 **非 KMP 的 JVM 项目** 中使用本库（如 Spring Boot、Quarkus 或普通 Java 项目），请务必使用 **JVM 专用工件**：

```kotlin
// Gradle（Kotlin DSL）
implementation("io.github.scarlet-pan:logger-jvm:1.0.0")
```

```groovy
// Gradle（Groovy DSL）
implementation 'io.github.scarlet-pan:logger-jvm:1.0.0'
```

```xml
<!-- Maven -->
<dependency>
    <groupId>io.github.scarlet-pan</groupId>
    <artifactId>logger-jvm</artifactId>
    <version>1.0.0</version>
</dependency>
```

> ⚠️ **重要提示**：`logger` 是一个 Kotlin 多平台元数据包，**不能在纯 Java 项目中直接使用**。请始终在 JVM 环境中使用 `logger-jvm`。

---

> 🔧 **最低要求**
> - Kotlin **≥ 1.9.0**（基于 **1.9.24** 构建）
> - Android minSdk **≥ 21**
> - iOS 部署目标 **≥ 12.0**

---

## 🚀 使用方法（Kotlin 优先）

无论你是在开发 **Kotlin 多平台库**、**Android 应用**，还是 **JVM 服务**，API 完全一致：

```kotlin
import dev.scarlet.logger.Logger

Logger.d("Network", "Request sent")
Logger.i("Database", "Query succeeded")
Logger.w("App", "Deprecated API used")
Logger.e("Crash", "Unexpected error", exception)
```

✅ 开箱即用于：
- KMP 项目的 `commonMain`
- Android 应用模块（`androidMain`）
- 纯 JVM 应用（`jvmMain`）
- iOS 和 JS 目标（通过 KMP）

无需平台判断，无需条件编译，只需标准 Kotlin。

> 💡 这是 **推荐且主要** 的使用方式。

---

### 🔧 自定义日志行为

使用 `+` 操作符组合多个日志器：

```kotlin
// 同时输出到系统和自定义日志器
Logger.default = Logger.SYSTEM + CustomLogger()
```

后续所有 `Logger.*()` 调用都会分发到两个目标。

你也可以实现自己的 `Logger`：

```kotlin
object FileLogger : Logger {
    override fun d(tag: String, msg: String, tr: Throwable?) {
        // 写入文件、发送到远程等
    }
    // ... 实现 i, w, e
}
```

然后设为默认：

```kotlin
Logger.default = FileLogger
```

---

### ☕ Java 互操作支持（可选）

虽然本库为 Kotlin 设计，但得益于 Kotlin/JVM 的字节码兼容性，**纯 Java 项目也可正常使用 JVM 版本**。

可通过静态门面类便捷调用：

```java
import dev.scarlet.logger.DefaultLogger;

DefaultLogger.d("MainActivity", "User clicked button");
DefaultLogger.e("Network", "Failed to load data", exception);
```

如需自定义日志管道（例如组合自定义日志器）：

```java
import dev.scarlet.logger.Logger;
import dev.scarlet.logger.Loggers;

Logger combined = Loggers.combine(Logger.getDefault(), new CustomLogger());
Logger.setDefault(combined);
```

> ⚠️ 注意：  
> - `DefaultLogger` 类 **仅用于 Java 互操作**。  
> - 所有高级功能（如自定义日志器）建议通过 Kotlin 配置。  
> - 本库 **并非 Java 优先的日志框架** —— Kotlin 是主要语言。  
> - **请确保你在 Java 项目中使用的是 `logger-jvm`，而非 `logger`！**

---

## 📚 API 设计

完全对标 Android `Log` 类，降低迁移成本：

```kotlin
fun d(tag: String, msg: String, tr: Throwable? = null)
fun i(tag: String, msg: String, tr: Throwable? = null)
fun w(tag: String, msg: String, tr: Throwable? = null)
fun e(tag: String, msg: String, tr: Throwable? = null)
```

- 标签优先（便于过滤）
- 可选异常参数（自动打印堆栈）
- 线程安全（协程/多线程无忧）

---

## 🌍 支持的平台

| 平台    | 日志后端              |
|---------|-----------------------|
| Android | `android.util.Log`    |
| iOS     | `println()`           |
| JVM     | `System.out`          |
| JS (IR) | `console.log`         |

> ℹ️ 基于 **Kotlin 1.9.24** 构建，兼容 **Kotlin 1.9.0 及以上版本**。  
> Java 项目可通过标准 Maven/Gradle 依赖使用 **JVM 工件**。

---

## 🤝 贡献

欢迎贡献！详见 [CONTRIBUTING.md](CONTRIBUTING.md)。

---

## 📄 许可证

本项目采用 **MIT 许可证** —— 详见 [LICENSE](LICENSE)。
