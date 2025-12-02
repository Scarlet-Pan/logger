# Logger

A **Kotlin Multiplatform (KMP)** logging library that lets you **write once, log everywhere** — with full support for **Android, iOS, JVM, and JavaScript**, using a familiar Android-style API (`d()`, `i()`, `w()`, `e()`).

It supports **lazy logging**, **log composition**, **tag-based filtering**, and **level control** — all while requiring **no platform-specific code** and **no build-time code generation**. Just shared Kotlin that works out of the box.

[![Maven Central](https://img.shields.io/maven-central/v/io.github.scarlet-pan/logger?label=Maven%20Central)](https://search.maven.org/artifact/io.github.scarlet-pan/logger)
[![Kotlin Multiplatform](https://img.shields.io/badge/Kotlin-Multiplatform-blue?logo=kotlin)](https://kotlinlang.org/docs/multiplatform.html)
[![Platforms: Android | iOS | JVM | JS](https://img.shields.io/badge/Platforms-Android%20%7C%20iOS%20%7C%20JVM%20%7C%20JS-lightgrey)]()
[![Kotlin ≥1.9.0](https://img.shields.io/badge/Kotlin-≥1.9.0-orange?logo=kotlin)](https://kotlinlang.org)
[![Test Status](https://github.com/scarlet-pan/logger/actions/workflows/test.yml/badge.svg?branch=main)](https://github.com/scarlet-pan/logger/actions/workflows/test.yml)
[![License](https://img.shields.io/github/license/scarlet-pan/logger)](LICENSE)

---

## 🌟 Why Choose Logger?

### 1. Write once, run anywhere — built for KMP, but works great everywhere  
Logger is **designed first and foremost for Kotlin Multiplatform (KMP)**, so you can write logging code in `commonMain` and have it work identically across Android, iOS, JVM, and JavaScript.

But you don’t need KMP to benefit! It also works perfectly as a **standalone logging library** in pure Android, iOS, JVM, or Spring Boot projects — with zero extra setup.

```kotlin
Logger.i("Network", "Request sent")
Logger.e("Auth", "Login failed", exception)
```

**Familiar Android-style API**: tag-first, concise methods (`d()`, `i()`, `w()`, `e()`). Android developers can adopt it instantly — no learning curve.  

### 2. Elegant and zero-overhead lazy logging  
Skip expensive message construction when logs are disabled — with clean, idiomatic syntax:

```kotlin
Logger.d("Heavy") { "Only evaluated if enabled: ${expensiveCall()}" }
Logger.w("IO") { "Read failed" with ioException }
```

> Zero runtime cost when disabled. Full expressiveness when enabled.

### 3. Compose your logging system like basic math: simple, natural, and elegant  
Build your logger like you’d write an arithmetic expression — intuitive and expressive:

```kotlin
// Add destinations (+): multi-channel output
Logger.default = Logger.SYSTEM + FileLogger("app.log") + RemoteLogger

// Remove components (−): exclude a channel
val offline = Logger.default - RemoteLogger

// Combine filters (logical AND): stricter filtering by stacking rules
val policy = LevelFilter.atLeast(WARN) + TagFilter.include("Security")
val secureLogger = Logger.withFilter(policy)
secureLogger.w("Security", "Suspicious activity")
```

> All results are standard `Logger` or `Filter` instances — fully composable, just like numbers.

---

## 📦 Installation

### Kotlin Multiplatform Projects

In your shared module (`build.gradle.kts`):

```kotlin
dependencies {
    implementation("io.github.scarlet-pan:logger:1.2.0")
}
```

This enables logging on all declared targets (Android, iOS, JVM, JS).

---

### Pure Android Apps

```kotlin
implementation("io.github.scarlet-pan:logger-android:1.2.0")
```

Behaves exactly like `android.util.Log` with full Logcat integration.

---

### Pure iOS Apps

Add via CocoaPods (`Podfile`):

```ruby
pod 'KmpLogger', :git => 'https://github.com/scarlet-pen/logger.git', :tag => '1.2.0'
```

Logs appear in Xcode console automatically. For a more native Swift experience, **we recommend adding** [`Loggers.swift`](https://github.com/scarlet-pen/logger/blob/main/swift-template/Loggers.swift).

---

### Pure JVM / Java Projects

For standard JVM apps:

```kotlin
implementation("io.github.scarlet-pan:logger-jvm:1.2.0")
```

If you’re using **SLF4J** (e.g., Spring Boot), prefer the bridge:

```kotlin
implementation("io.github.scarlet-pan:logger-slf4j:1.2.0")
```

Then set `Logger.default = Slf4jLogger()` at startup.

> ⚠️ The base `logger` artifact is KMP-only. Use platform-specific artifacts (`-android`, `-jvm`, etc.) for non-KMP projects.

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

Works unchanged in `commonMain`, `androidMain`, `jvmMain`, or `iosMain`.

---

### iOS (Swift)

Kotlin logs appear via `print`. For better Swift integration, **add [`Loggers.swift`](https://github.com/scarlet-pen/logger/blob/main/swift-template/Loggers.swift)**:

```swift
SharedLogger.i("Network", "Request sent")
SharedLogger.e("Database", "Failed to open", error: dbError)
```

> Optional but recommended for production iOS apps.

---

### JVM (Java)

```java
import dev.scarlet.logger.DefaultLogger;

DefaultLogger.d("Service", "Task started");
DefaultLogger.e("DB", "Connection lost", exception);
```

---

## 🔧 Advanced Usage

#### Lazy Logging
```kotlin
Logger.d("Debug") { "Expensive debug info: ${computeHeavyData()}" }
Logger.w("IO") { "Read failed" with ioException }
```
> Lambda only invoked if log level is enabled — truly zero overhead.

#### Scoped Loggers
```kotlin
val debugLogger = Logger.withFilter(Filter.ALL)
debugLogger.d("FeatureX") { "Dev-only log" }
```

#### Global Level Control
```kotlin
Filter.default = Filter.atLeast(INFO) // DEBUG logs are skipped
```

#### Composable Loggers
```kotlin
Logger.default = Logger.SYSTEM + FileLogger("app.log")
val offline = Logger.default - RemoteLogger
```

#### Composable Filters
```kotlin
val policy = LevelFilter.atLeast(WARN) + TagFilter.include("Security")
val secureLogger = Logger.withFilter(policy)
secureLogger.w("Security", "Suspicious activity")
```

All results are standard `Logger` or `Filter` instances — fully composable.

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

---

## 🌍 Platform Backends

| Platform | Backend               |
|----------|------------------------|
| Android  | `android.util.Log`     |
| iOS      | `println()`            |
| JVM      | `System.out` or SLF4J  |
| JS       | `console.log`          |

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

它支持 **惰性日志**、**日志组合**、**基于标签的过滤** 和 **日志级别控制**，同时 **无需平台专属代码**、**无需编译期代码生成**。只需一份共享 Kotlin，开箱即用。

[![Maven Central](https://img.shields.io/maven-central/v/io.github.scarlet-pan/logger?label=Maven%20Central)](https://search.maven.org/artifact/io.github.scarlet-pan/logger)
[![Kotlin 多平台](https://img.shields.io/badge/Kotlin-Multiplatform-blue?logo=kotlin)](https://kotlinlang.org/docs/multiplatform.html)
[![支持平台: Android | iOS | JVM | JS](https://img.shields.io/badge/平台-Android%20%7C%20iOS%20%7C%20JVM%20%7C%20JS-lightgrey)]()
[![Kotlin ≥1.9.0](https://img.shields.io/badge/Kotlin-≥1.9.0-orange?logo=kotlin)](https://kotlinlang.org)
[![测试状态](https://github.com/scarlet-pan/logger/actions/workflows/test.yml/badge.svg?branch=main)](https://github.com/scarlet-pan/logger/actions/workflows/test.yml)
[![许可证](https://img.shields.io/github/license/scarlet-pan/logger)](LICENSE)

---

## 🌟 为什么选择 Logger？

### 1. 一次编写，处处运行 —— 专为 KMP 打造，各平台也能独立使用  
Logger **首先为 Kotlin 多平台（KMP）而设计**，让你在 `commonMain` 中写一次日志代码，即可在 Android、iOS、JVM 和 JavaScript 上行为完全一致。

但即使你不用 KMP，它同样出色！Logger 也可以作为**独立日志库**，直接用于纯 Android、纯 iOS、纯 JVM（包括 Spring Boot）等项目 —— 无需额外配置。

```kotlin
Logger.i("Network", "请求已发送")
Logger.e("Auth", "登录失败", exception)
```

**完全沿用 Android 的日志风格**：标签优先、方法简洁（`d()`/`i()`/`w()`/`e()`），Android 开发者几乎无需学习成本，开箱即用。  

### 2. 优雅且零开销的惰性日志  
当日志被禁用时，自动跳过昂贵的消息构造，语法简洁，性能无损：

```kotlin
Logger.d("Heavy") { "仅在启用时执行：${expensiveCall()}" }
Logger.w("IO") { "读取失败" with ioException }
```

> 禁用时零运行时开销，启用时表达力十足。

### 3. 像加减乘除一样组合日志系统：简单、自然、优雅  
把日志系统当作算式来写 —— 就像做加减乘除一样直观：

```kotlin
// 添加日志目标（加法 +）：多路输出
Logger.default = Logger.SYSTEM + FileLogger("app.log") + RemoteLogger

// 移除组件（减法 −）：剔除某一路输出
val offline = Logger.default - RemoteLogger

// 叠加过滤规则（逻辑 AND）：同时满足多个条件，筛选更严格
val policy = LevelFilter.atLeast(WARN) + TagFilter.include("Security")
val secureLogger = Logger.withFilter(policy)
secureLogger.w("Security", "可疑操作")
```

> 所有结果都是标准的 `Logger` 或 `Filter` 实例 —— 完全可组合，就像数字一样自由运算。

---

## 📦 安装方式

### Kotlin 多平台项目

在共享模块中（`build.gradle.kts`）：

```kotlin
dependencies {
    implementation("io.github.scarlet-pan:logger:1.2.0")
}
```

自动启用所有平台的日志功能。

---

### 纯 Android 应用

```kotlin
implementation("io.github.scarlet-pan:logger-android:1.2.0")
```

行为与 `android.util.Log` 完全一致，日志直接显示在 Logcat。

---

### 纯 iOS 应用

通过 CocoaPods（`Podfile`）：

```ruby
pod 'KmpLogger', :git => 'https://github.com/scarlet-pen/logger.git', :tag => '1.2.0'
```

Kotlin 日志会自动出现在 Xcode 控制台。为获得更符合 Swift 习惯的体验，**建议添加** [`Loggers.swift`](https://github.com/scarlet-pen/logger/blob/main/swift-template/Loggers.swift)。

---

### 纯 JVM / Java 项目

标准 JVM 项目使用：

```kotlin
implementation("io.github.scarlet-pan:logger-jvm:1.2.0")
```

若使用 **SLF4J**（如 Spring Boot），推荐桥接版本：

```kotlin
implementation("io.github.scarlet-pan:logger-slf4j:1.2.0")
```

并在启动时设置 `Logger.default = Slf4jLogger()`。

> ⚠️ 基础 `logger` 包仅适用于 KMP 项目。非 KMP 项目请使用平台专属包（如 `-android`、`-jvm` 等）。

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

这段代码在 `commonMain`、`androidMain`、`jvmMain` 或 `iosMain` 中**无需任何修改即可运行**。

---

### iOS（Swift）

Kotlin 日志会通过 `print` 自动出现在 Xcode 控制台。  
为获得更符合 Swift 习惯的体验，**建议添加** [`Loggers.swift`](https://github.com/scarlet-pen/logger/blob/main/swift-template/Loggers.swift)：

```swift
SharedLogger.i("Network", "请求已发送")
SharedLogger.e("Database", "打开失败", error: dbError)
```

> 此封装是可选的，但推荐用于生产级 iOS 应用。

---

### JVM（Java）

```java
import dev.scarlet.logger.DefaultLogger;

DefaultLogger.d("Service", "任务启动");
DefaultLogger.e("DB", "连接丢失", exception);
```

---

## 🔧 进阶用法

#### 惰性日志
```kotlin
Logger.d("Debug") { "调试信息：${computeHeavyData()}" }
Logger.w("IO") { "读取失败" with ioException }
```
> Lambda 仅在日志启用时执行 —— 真正零开销。

#### 作用域日志器
```kotlin
val debugLogger = Logger.withFilter(Filter.ALL)
debugLogger.d("FeatureX") { "开发专用日志" }
```

#### 全局级别控制
```kotlin
Filter.default = Filter.atLeast(INFO) // DEBUG 日志将被跳过
```

#### 可组合日志器
```kotlin
Logger.default = Logger.SYSTEM + FileLogger("app.log")
val offline = Logger.default - RemoteLogger
```

#### 可组合过滤器
```kotlin
val policy = LevelFilter.atLeast(WARN) + TagFilter.include("Security")
val secureLogger = Logger.withFilter(policy)
secureLogger.w("Security", "可疑操作")
```

所有结果均为标准 `Logger` 或 `Filter` 实例，可继续组合。

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
