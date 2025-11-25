# Logger

A **Kotlin Multiplatform (KMP)** logging library that lets you **write once, log everywhere** — with full support for **Android, iOS, JVM, and JavaScript**, using a familiar Android-style API (`d()`, `i()`, `w()`, `e()`).  

It supports **log composition**, **tag-based filtering**, **level control**, and **lazy message evaluation** — all while requiring **no `expect/actual`**, **no platform-specific code**, and **no build-time code generation**. Just shared Kotlin that works out of the box.

[![Maven Central](https://img.shields.io/maven-central/v/io.github.scarlet-pan/logger-jvm?label=Maven%20Central)](https://search.maven.org/artifact/io.github.scarlet-pan/logger-jvm)
[![Kotlin Multiplatform](https://img.shields.io/badge/Kotlin-Multiplatform-blue?logo=kotlin)](https://kotlinlang.org/docs/multiplatform.html)
[![Platforms: Android | iOS | JVM | JS](https://img.shields.io/badge/Platforms-Android%20%7C%20iOS%20%7C%20JVM%20%7C%20JS-lightgrey)]()
[![Kotlin ≥1.9.0](https://img.shields.io/badge/Kotlin-≥1.9.0-orange?logo=kotlin)](https://kotlinlang.org)
[![Test Status](https://github.com/scarlet-pen/logger/actions/workflows/test.yml/badge.svg?branch=main)](https://github.com/scarlet-pen/logger/actions/workflows/test.yml)
[![License](https://img.shields.io/github/license/scarlet-pan/logger)](LICENSE)

---

## 🌐 Why Use It?

Logger is built from the ground up for **Kotlin Multiplatform**. You can call `Logger.d("Tag", "Message")` directly in `commonMain` — no `expect/actual` declarations, no conditional compilation, and no platform abstraction layers.

The API mirrors **Android’s `Log` class** exactly: `d()`, `i()`, `w()`, and `e()` with optional `Throwable`. This means your shared code feels like native Android logging, even when running on iOS or JVM.

Under the hood, it automatically routes logs to each platform’s standard output:
- On **Android**, logs appear in **Logcat** via `android.util.Log`.
- On **iOS**, logs are printed via `println()` and visible in the **Xcode console**.
- On **JVM**, output goes to `System.out` (or SLF4J if bridged).
- On **JavaScript**, it uses `console.log`.

> 🎯 Example:  
> ```kotlin
> Logger.i("Network", "Request sent")
> Logger.e("Auth", "Login failed", exception)
> ```
> This snippet works identically in `commonMain`, an Android app, a Spring Boot service, or an iOS app consuming your KMP framework.

---

## 📦 Installation

### Kotlin Multiplatform Projects

Add to your shared module:

```kotlin
// build.gradle.kts
dependencies {
    implementation("io.github.scarlet-pan:logger:1.1.0")
}
```

This enables logging across all declared targets (Android, iOS, JVM, JS).

---

### Pure Android Apps

For standard Android apps (non-KMP):

```kotlin
dependencies {
    implementation("io.github.scarlet-pan:logger:1.1.0")
}
```

The API behaves exactly like `android.util.Log`, with full Logcat integration.

---

### Pure iOS Apps

If your iOS app integrates a KMP framework that uses `Logger`, add via CocoaPods:

```ruby
pod 'KmpLogger', :git => 'https://github.com/scarlet-pen/logger.git', :tag => '1.1.0'
```

Logs from Kotlin will appear in the Xcode console via `print`.  
Optionally, you can include [`Loggers.swift`](https://github.com/scarlet-pen/logger/blob/main/swift-template/Loggers.swift) to get a more idiomatic Swift API (`SharedLogger.d(...)`) — but this is **not required**; logging works out of the box without it.

---

### Pure JVM / Java Projects

#### Option 1: Direct to `System.out`

Use the built-in JVM logger:

```kotlin
implementation("io.github.scarlet-pan:logger-jvm:1.1.0")
```

```xml
<!-- Maven -->
<dependency>
    <groupId>io.github.scarlet-pan</groupId>
    <artifactId>logger-jvm</artifactId>
    <version>1.1.0</version>
</dependency>
```

Logs are printed to `System.out`.

#### Option 2: Bridge to SLF4J (for Spring Boot, Quarkus, etc.)

If you're already using SLF4J (e.g., with Logback or Log4j2), use the SLF4J bridge:

```kotlin
implementation("io.github.scarlet-pan:logger-slf4j:1.1.0")
```

```xml
<!-- Maven -->
<dependency>
    <groupId>io.github.scarlet-pan</groupId>
    <artifactId>logger-slf4j</artifactId>
    <version>1.1.0</version>
</dependency>
```

Then initialize once at startup:

```kotlin
import dev.scarlet.logger.Logger
import dev.scarlet.logger.slf4j.Slf4jLogger

Logger.default = Slf4jLogger()
```

> ✅ All `Logger.d("Tag", "...")` calls will:
> - Map to SLF4J levels (`d/i → debug/info`, `w → warn`, `e → error`)
> - Use `"Tag"` as the logger name (`LoggerFactory.getLogger("Tag")`)
> - Preserve exceptions and support MDC (via underlying SLF4J impl)

> ⚠️ Note:  
> - The `logger` artifact is a KMP metadata package and **cannot be used in Java-only projects**.  
> - When using `logger-slf4j`, ensure an SLF4J binding (e.g., `logback-classic`) is on the classpath.

---

> 🔧 **Requirements**  
> - Kotlin **≥ 1.9.0**  
> - Android minSdk **≥ 21**  
> - iOS deployment target **≥ 12.0**

---

## 🚀 Usage

### Kotlin (KMP & Android)

```kotlin
import dev.scarlet.logger.Logger

Logger.d("Network", "Request sent")
Logger.i("DB", "Query succeeded")
Logger.w("Cache", "Stale data used")
Logger.e("Auth", "Login failed", exception)
```

Works in `commonMain`, `androidMain`, and `jvmMain` without changes.

---

### iOS (Swift)

Even without any extra setup, logs from Kotlin appear in Xcode.  
For a more natural Swift interface, you may optionally use:

```swift
SharedLogger.i("Network", "Request sent")
SharedLogger.e("Database", "Failed to open", error: dbError)
```

This requires adding `Loggers.swift`, but **is purely optional**.

---

### JVM (Java)

With `logger-jvm`:

```java
import dev.scarlet.logger.DefaultLogger;

DefaultLogger.d("Service", "Task started");
DefaultLogger.e("DB", "Connection lost", exception);
```

Or with SLF4J bridge (after setting `Logger.default = Slf4jLogger()` in Kotlin init):

```java
// Still use DefaultLogger in Java — it delegates to the configured backend
DefaultLogger.i("App", "Started successfully");
```

---

## 🔧 Advanced: Customization & Composition

### Combine Multiple Loggers

Send logs to multiple destinations using the `+` operator:

```kotlin
Logger.default = Logger.SYSTEM + FileLogger() + RemoteLogger()
```

All subsequent `Logger.*()` calls will dispatch to every logger in the chain.

### Implement a Custom Logger

```kotlin
object CrashlyticsLogger : Logger {
    override fun e(tag: String, msg: String, tr: Throwable?) {
        FirebaseCrashlytics.getInstance().log("[$tag] $msg")
        tr?.let { FirebaseCrashlytics.getInstance().recordException(it) }
    }
    override fun d(tag: String, msg: String, tr: Throwable?) = Unit
    override fun i(tag: String, msg: String, tr: Throwable?) = Unit
    override fun w(tag: String, msg: String, tr: Throwable?) = Unit
}
```

You can then include it in a combined logger as shown above.

---

## 📚 API Reference

```kotlin
fun d(tag: String, msg: String, tr: Throwable? = null)
fun i(tag: String, msg: String, tr: Throwable? = null)
fun w(tag: String, msg: String, tr: Throwable? = null)
fun e(tag: String, msg: String, tr: Throwable? = null)
```

- Tag-first design for easy filtering
- Optional `Throwable` with automatic stack trace
- Fully thread-safe and coroutine-friendly
- Supports **lazy evaluation**: pass a lambda for expensive messages (e.g., `Logger.d { "Expensive debug info" }`)

> 💡 Lazy logging is available via extension functions and only evaluates the message if the log level is enabled.

---

## 🌍 Platform Backends

| Platform | Backend               |
|--------|------------------------|
| Android | `android.util.Log`     |
| iOS     | `println()`            |
| JVM     | `System.out` or SLF4J  |
| JS      | `console.log`          |

---

## 🤝 Contributing

Contributions welcome! See [CONTRIBUTING.md](CONTRIBUTING.md).

---

## 📄 License

MIT License – see [LICENSE](LICENSE).  
Copyright © 2025 Scarlet Pan

<br><br>

<div align="center">
  <hr width="80%" />
  <p><em>—— 中文文档 Chinese Documentation ——</em></p>
  <hr width="80%" />
</div>
<br><br>

# Logger

一个 **Kotlin 多平台（Kotlin Multiplatform, KMP）** 日志库，让你 **一次编写，处处打日志** —— 完整支持 **Android、iOS、JVM 和 JavaScript**，并提供熟悉的 Android 风格 API（`d()`、`i()`、`w()`、`e()`）。

它支持 **日志组合**、**基于标签的过滤**、**日志级别控制** 和 **惰性消息求值（lazy evaluation）**，同时 **无需 `expect/actual`**、**无需平台专属代码**、**无需编译期代码生成**。只需一份共享 Kotlin，开箱即用。

[![Maven Central](https://img.shields.io/maven-central/v/io.github.scarlet-pan/logger-jvm?label=Maven%20Central)](https://search.maven.org/artifact/io.github.scarlet-pan/logger-jvm)
[![Kotlin 多平台](https://img.shields.io/badge/Kotlin-Multiplatform-blue?logo=kotlin)](https://kotlinlang.org/docs/multiplatform.html)
[![支持平台: Android | iOS | JVM | JS](https://img.shields.io/badge/平台-Android%20%7C%20iOS%20%7C%20JVM%20%7C%20JS-lightgrey)]()
[![Kotlin ≥1.9.0](https://img.shields.io/badge/Kotlin-≥1.9.0-orange?logo=kotlin)](https://kotlinlang.org)
[![测试状态](https://github.com/scarlet-pen/logger/actions/workflows/test.yml/badge.svg?branch=main)](https://github.com/scarlet-pen/logger/actions/workflows/test.yml)
[![许可证](https://img.shields.io/github/license/scarlet-pan/logger)](LICENSE)

---

## 🌐 为什么选择它？

Logger 从底层为 **Kotlin 多平台**而设计。你可以在 `commonMain` 中直接调用 `Logger.d("Tag", "Message")` —— 无需 `expect/actual` 声明，无需条件编译，也无需额外的抽象层。

其 API 与 **Android 的 `Log` 类完全一致**：提供 `d()`、`i()`、`w()` 和 `e()` 方法，并支持可选的 `Throwable` 参数。这意味着你的共享代码在 iOS 或 JVM 上运行时，依然保持原生 Android 日志的使用体验。

底层会自动将日志路由到各平台的标准输出通道：
- 在 **Android** 上，日志通过 `android.util.Log` 输出到 **Logcat**。
- 在 **iOS** 上，日志通过 `println()` 打印，在 **Xcode 控制台**中可见。
- 在 **JVM** 上，输出到 `System.out`（或桥接到 SLF4J）。
- 在 **JavaScript** 上，使用 `console.log`。

> 🎯 示例：  
> ```kotlin
> Logger.i("Network", "请求已发送")
> Logger.e("Auth", "登录失败", exception)
> ```
> 这段代码在 `commonMain`、Android 应用、Spring Boot 服务或集成 KMP 框架的 iOS 应用中行为完全一致。

---

## 📦 安装方式

### Kotlin 多平台项目

在共享模块中添加：

```kotlin
// build.gradle.kts
dependencies {
    implementation("io.github.scarlet-pan:logger:1.1.0")
}
```

该依赖会自动启用所有已声明目标平台的日志功能。

---

### 纯 Android 应用

对于标准 Android 应用（非 KMP）：

```kotlin
dependencies {
    implementation("io.github.scarlet-pan:logger:1.1.0")
}
```

API 行为与 `android.util.Log` 完全一致，日志直接显示在 Logcat 中。

---

### 纯 iOS 应用

若你的 iOS 应用集成了使用 `Logger` 的 KMP 框架，请通过 CocoaPods 添加：

```ruby
pod 'KmpLogger', :git => 'https://github.com/scarlet-pen/logger.git', :tag => '1.1.0'
```

Kotlin 中的日志会通过 `print` 自动显示在 Xcode 控制台。  
你可以**选择性地**加入 [`Loggers.swift`](https://github.com/scarlet-pen/logger/blob/main/swift-template/Loggers.swift)，以获得更符合 Swift 习惯的 API（如 `SharedLogger.d(...)`）——但**并非必需**；即使不加，日志功能也完全可用。

---

### 纯 JVM / Java 项目

#### 方式一：直接输出到标准输出（System.out）

使用内置的 JVM 日志器：

```kotlin
implementation("io.github.scarlet-pan:logger-jvm:1.1.0")
```

```xml
<!-- Maven -->
<dependency>
    <groupId>io.github.scarlet-pan</groupId>
    <artifactId>logger-jvm</artifactId>
    <version>1.1.0</version>
</dependency>
```

日志将打印到 `System.out`。

#### 方式二：桥接到 SLF4J（推荐用于 Spring Boot、Quarkus 等）

如果项目已使用 **SLF4J + Logback / Log4j2** 等成熟日志体系，可通过 `logger-slf4j` 将 `Logger` 调用无缝转发到 SLF4J：

```kotlin
implementation("io.github.scarlet-pan:logger-slf4j:1.1.0")
```

```xml
<!-- Maven -->
<dependency>
    <groupId>io.github.scarlet-pan</groupId>
    <artifactId>logger-slf4j</artifactId>
    <version>1.1.0</version>
</dependency>
```

然后在初始化时设置默认日志器：

```kotlin
import dev.scarlet.logger.Logger
import dev.scarlet.logger.slf4j.Slf4jLogger

Logger.default = Slf4jLogger()
```

> ✅ 此后所有 `Logger.d("Tag", "...")` 调用都会：
> - 自动映射为 SLF4J 的对应级别（`d/i → debug/info`，`w → warn`，`e → error`）
> - 使用 `"Tag"` 作为 logger 名称（即 `LoggerFactory.getLogger("Tag")`）
> - 支持异常传递和 MDC 上下文（由底层 SLF4J 实现决定）

> ⚠️ 注意：  
> - `logger`（KMP 元数据包）不能用于纯 Java 项目。  
> - 若使用 `logger-slf4j`，请确保 classpath 中已包含 SLF4J 绑定（如 `logback-classic`）。

---

> 🔧 **最低要求**  
> - Kotlin **≥ 1.9.0**  
> - Android minSdk **≥ 21**  
> - iOS 部署目标 **≥ 12.0**

---

## 🚀 使用方法

### Kotlin（KMP 与 Android）

```kotlin
import dev.scarlet.logger.Logger

Logger.d("Network", "请求已发送")
Logger.i("DB", "查询成功")
Logger.w("Cache", "使用了过期缓存")
Logger.e("Auth", "登录失败", exception)
```

在 `commonMain`、`androidMain` 和 `jvmMain` 中无需任何修改即可运行。

---

### iOS（Swift）

即使不做任何额外配置，Kotlin 日志也会出现在 Xcode 控制台。  
若希望使用更自然的 Swift 接口，可选择使用：

```swift
SharedLogger.i("Network", "请求已发送")
SharedLogger.e("Database", "打开失败", error: dbError)
```

这需要添加 `Loggers.swift`，但**完全是可选的**。

---

### JVM（Java）

使用 `logger-jvm` 时：

```java
import dev.scarlet.logger.DefaultLogger;

DefaultLogger.d("Service", "任务启动");
DefaultLogger.e("DB", "连接丢失", exception);
```

或使用 SLF4J 桥接（需先在 Kotlin 初始化 `Logger.default = Slf4jLogger()`）：

```java
// Java 中仍使用 DefaultLogger，它会委托给已配置的后端
DefaultLogger.i("App", "启动成功");
```

---

## 🔧 进阶：自定义与组合

### 组合多个日志器

使用 `+` 操作符将日志同时发送到多个目标：

```kotlin
Logger.default = Logger.SYSTEM + FileLogger() + RemoteLogger()
```

此后所有 `Logger.*()` 调用都会分发到链中的每个日志器。

### 实现自定义日志器

```kotlin
object CrashlyticsLogger : Logger {
    override fun e(tag: String, msg: String, tr: Throwable?) {
        FirebaseCrashlytics.getInstance().log("[$tag] $msg")
        tr?.let { FirebaseCrashlytics.getInstance().recordException(it) }
    }
    override fun d(tag: String, msg: String, tr: Throwable?) = Unit
    override fun i(tag: String, msg: String, tr: Throwable?) = Unit
    override fun w(tag: String, msg: String, tr: Throwable?) = Unit
}
```

可将其加入组合日志器中使用。

---

## 📚 API 说明

```kotlin
fun d(tag: String, msg: String, tr: Throwable? = null)
fun i(tag: String, msg: String, tr: Throwable? = null)
fun w(tag: String, msg: String, tr: Throwable? = null)
fun e(tag: String, msg: String, tr: Throwable? = null)
```

- 标签优先，便于过滤
- 可选异常，自动打印堆栈
- 线程安全，协程友好
- 支持 **惰性求值**：对开销大的日志内容，可传入 lambda（例如 `Logger.d { "昂贵的调试信息" }`）

> 💡 惰性日志通过扩展函数实现，仅在日志级别启用时才会执行消息构造。

---

## 🌍 各平台后端

| 平台    | 日志后端              |
|---------|-----------------------|
| Android | `android.util.Log`    |
| iOS     | `println()`           |
| JVM     | `System.out` 或 SLF4J |
| JS      | `console.log`         |

---

## 🤝 贡献

欢迎贡献！详见 [CONTRIBUTING.md](CONTRIBUTING.md)。

---

## 📄 许可证

MIT 许可证 —— 详见 [LICENSE](LICENSE)。  
版权所有 © 2025 Scarlet Pan
