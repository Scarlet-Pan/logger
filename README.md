# Logger

[[Maven Central](https://img.shields.io/maven-central/v/io.github.scarlet-pan/logger)](https://search.maven.org/artifact/io.github.scarlet-pan/logger)  
[[Kotlin Multiplatform](https://img.shields.io/badge/Kotlin-Multiplatform-blue?logo=kotlin)](https://kotlinlang.org/docs/multiplatform.html)  
[[Platforms: Android | iOS | JVM | JS](https://img.shields.io/badge/Platforms-Android%20%7C%20iOS%20%7C%20JVM%20%7C%20JS-lightgrey)]()  
[[Kotlin ≥1.9.0](https://img.shields.io/badge/Kotlin-≥1.9.0-orange?logo=kotlin)](https://kotlinlang.org)  
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

Add to your `build.gradle.kts` (shared module or standalone project):

```kotlin
dependencies {
    implementation("io.github.scarlet-pan:logger:1.0.0")
}
```

No extra setup. No platform-specific configuration.

> 🔧 **Minimum Requirements**
> - Kotlin **≥ 1.9.0** (built with **1.9.24**)
> - Android minSdk **≥ 21**
> - iOS deployment target **≥ 12.0**

---

## 🚀 Usage (Kotlin First)

### ✨ Log in Common Code (KMP)

Perfect for shared logic in `commonMain`:

```kotlin
import dev.scarlet.logger.Logger

Logger.d("Network", "Request sent")
Logger.i("Database", "Query succeeded")
Logger.w("App", "Deprecated API used")
Logger.e("Crash", "Unexpected error", exception)
```

✅ Works identically in **Android**, **iOS**, **JVM**, and **JS** targets.  
✅ No conditional compilation. No wrappers. Just pure Kotlin.

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

## ☕ Java Support (Optional)

For mixed Kotlin-Java projects, use the static `DefaultLogger` facade:

```java
import dev.scarlet.logger.DefaultLogger;

DefaultLogger.d("MainActivity", "User clicked button");
DefaultLogger.e("Network", "Failed to load data", exception);
```

To customize the logging pipeline from Java:

```java
import dev.scarlet.logger.Logger;
import dev.scarlet.logger.Loggers;

var combined = Loggers.combine(Logger.getDefault(), new CustomLogger());
Logger.setDefault(combined);
```

> ℹ️ `DefaultLogger` is a thin static wrapper around `Logger.default`.  
> All configuration happens via the `Logger` class — not `DefaultLogger`.

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

| Platform | Target | Output Target                     |
|----------|--------|-----------------------------------|
| Android  | Android| `android.util.Log`                |
| iOS      | Native | Standard output (`println`)       |
| JVM      | JVM    | `System.out`                      |
| JS       | JS (IR)| `console.log`                     |

> ℹ️ Built with **Kotlin 1.9.24**, compatible with **Kotlin 1.9.0+**.

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

[[Maven Central](https://img.shields.io/maven-central/v/io.github.scarlet-pan/logger)](https://search.maven.org/artifact/io.github.scarlet-pan/logger)  
[[Kotlin 多平台](https://img.shields.io/badge/Kotlin-Multiplatform-blue?logo=kotlin)](https://kotlinlang.org/docs/multiplatform.html)  
[[支持平台: Android | iOS | JVM | JS](https://img.shields.io/badge/平台-Android%20%7C%20iOS%20%7C%20JVM%20%7C%20JS-lightgrey)]()  
[[Kotlin ≥1.9.0](https://img.shields.io/badge/Kotlin-≥1.9.0-orange?logo=kotlin)](https://kotlinlang.org)  
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

在 `build.gradle.kts` 中添加依赖（无论是 KMP 共享模块还是单平台项目）：

```kotlin
dependencies {
    implementation("io.github.scarlet-pan:logger:1.0.0")
}
```

无需额外配置，开箱即用。

> 🔧 **最低要求**
> - Kotlin **≥ 1.9.0**（基于 **1.9.24** 构建）
> - Android minSdk **≥ 21**
> - iOS 部署目标 **≥ 12.0**

---

## 🚀 使用方法（Kotlin 优先）

### ✨ 在公共代码中打日志（KMP）

适用于 `commonMain` 中的共享逻辑：

```kotlin
import dev.scarlet.logger.Logger

Logger.d("Network", "Request sent")
Logger.i("Database", "Query succeeded")
Logger.w("App", "Deprecated API used")
Logger.e("Crash", "Unexpected error", exception)
```

✅ 在 **Android**、**iOS**、**JVM** 和 **JS** 上行为一致。  
✅ 无需平台判断，无需包装，纯 Kotlin 即可。

---

### 🔧 自定义日志行为

使用 `+` 操作符组合多个日志器：

```kotlin
// Send logs to both system and your custom logger
Logger.default = Logger.SYSTEM + CustomLogger()
```

后续所有 `Logger.*()` 调用都会分发到两个目标。

你也可以实现自己的 `Logger`：

```kotlin
object FileLogger : Logger {
    override fun d(tag: String, msg: String, tr: Throwable?) {
        // Write to file, send to remote, etc.
    }
    // ... implement i, w, e
}
```

然后设为默认：

```kotlin
Logger.default = FileLogger
```

---

## ☕ Java 支持（可选）

在 Kotlin-Java 混合项目中，可通过静态门面 `DefaultLogger` 调用：

```java
import dev.scarlet.logger.DefaultLogger;

DefaultLogger.d("MainActivity", "User clicked button");
DefaultLogger.e("Network", "Failed to load data", exception);
```

如需自定义日志管道，通过 `Logger` 类配置：

```java
import dev.scarlet.logger.Logger;
import dev.scarlet.logger.Loggers;

var combined = Loggers.combine(Logger.getDefault(), new CustomLogger());
Logger.setDefault(combined);
```

> ℹ️ `DefaultLogger` 是对 `Logger.default` 的静态封装。  
> 所有配置均通过 `Logger` 类完成，而非 `DefaultLogger`。

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

| 平台    | 目标平台 | 输出目标                     |
|---------|----------|------------------------------|
| Android | Android  | `android.util.Log`           |
| iOS     | Native   | 标准输出（`println`）        |
| JVM     | JVM      | `System.out`                 |
| JS      | JS (IR)  | `console.log`                |

> ℹ️ 基于 **Kotlin 1.9.24** 构建，兼容 **Kotlin 1.9.0 及以上版本**。

---

## 🤝 贡献

欢迎贡献！详见 [CONTRIBUTING.md](CONTRIBUTING.md)。

---

## 📄 许可证

本项目采用 **MIT 许可证** —— 详见 [LICENSE](LICENSE)。