# Logger

[[Maven Central](https://img.shields.io/maven-central/v/io.github.scarlet-pan/logger)](https://search.maven.org/artifact/io.github.scarlet-pan/logger)  
[[Kotlin Multiplatform](https://img.shields.io/badge/Kotlin-Multiplatform-blue?logo=kotlin)](https://kotlinlang.org/docs/multiplatform.html)  
[[Platforms: Android | iOS | JVM | JS](https://img.shields.io/badge/Platforms-Android%20%7C%20iOS%20%7C%20JVM%20%7C%20JS-lightgrey)]()  
[[Kotlin ≥1.9.0](https://img.shields.io/badge/Kotlin-≥1.9.0-orange?logo=kotlin)](https://kotlinlang.org)  
[[License](https://img.shields.io/github/license/scarlet-pan/logger)](LICENSE)

---

## 🌐 Why Use It?

- ✅ **Built for Kotlin Multiplatform (KMP)**: Use `Logger` directly in `commonMain` — no `expect/actual`, no conditional compilation.
- ✅ **Android Log Style**: Familiar `d()`, `i()`, `w()`, `e()` APIs, just like `android.util.Log`. Perfect for Android and Kotlin developers.
- ✅ **Truly Cross-Platform**: Works out of the box in **KMP libraries**, **KMP apps**, and even **standalone projects** (Android, iOS, JVM, JS).
- ✅ **Zero Platform Code**: Automatically uses native logging:
    - **Android** → `android.util.Log`
    - **iOS** → `NSLog`
    - **JVM** → `System.out`
    - **JS** → `console.log`

> 🎯 **Log in shared code like you always did on Android — it just works everywhere.**

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
> - Kotlin **≥ 1.9.0**  
> - Android minSdk **≥ 21**  
> - iOS deployment target **≥ 12.0**

---

## 🚀 Usage

### Log Directly in Common Code

```kotlin
import dev.scarlet.logger.Logger

Logger.d("Network", "Request sent")
Logger.i("Database", "Query succeeded")
Logger.w("App", "Deprecated API used")
Logger.e("Crash", "Unexpected error", exception)
```

✅ Call these from `commonMain` in a KMP library or app.  
✅ Also works in pure Android, iOS, JVM, or JS projects.  
✅ No platform checks. No wrappers. Just log.

### Combine Loggers with `+`

Send logs to multiple destinations in one line:

```kotlin
// Send logs to both system and your custom logger
Logger.default = Logger.SYSTEM + CustomLogger()
```

All subsequent logs will be dispatched to both loggers automatically.

---

## 📚 API

Mirrors Android’s `Log` for instant familiarity:

```kotlin
fun d(tag: String, msg: String, tr: Throwable? = null)
fun i(tag: String, msg: String, tr: Throwable? = null)
fun w(tag: String, msg: String, tr: Throwable? = null)
fun e(tag: String, msg: String, tr: Throwable? = null)
```

- Tag-first design
- Optional `Throwable` support (auto stack trace)
- Fully thread-safe

---

## 🌍 Supported Platforms

| Platform | Target | Output Target        |
|----------|--------|----------------------|
| Android  | Android| `android.util.Log`   |
| iOS      | Native | `NSLog`              |
| JVM      | JVM    | `System.out`         |
| JS       | JS (IR)| `console.log`        |

> ℹ️ Built with **Kotlin 1.9.23**, compatible with **Kotlin 1.9.0+**.

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
- ✅ **Android 日志风格**：提供与 `android.util.Log` 完全一致的 `d()`、`i()`、`w()`、`e()` 接口，Android/Kotlin 开发者秒上手。
- ✅ **真正跨平台**：既适用于 **KMP 库** 和 **KMP 应用**，也适用于 **纯 Android、iOS、JVM 或 JS 项目**。
- ✅ **零平台代码**：自动桥接各平台原生日志系统：
    - **Android** → `android.util.Log`
    - **iOS** → `NSLog`
    - **JVM** → `System.out`
    - **JS** → `console.log`

> 🎯 **在共享代码中像写 Android 一样打日志 —— 一次编写，处处运行。**

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
> - Kotlin **≥ 1.9.0**  
> - Android minSdk **≥ 21**  
> - iOS 部署目标 **≥ 12.0**

---

## 🚀 使用方法

### 在公共代码中直接打日志

```kotlin
import dev.scarlet.logger.Logger

Logger.d("Network", "Request sent")
Logger.i("Database", "Query succeeded")
Logger.w("App", "Deprecated API used")
Logger.e("Crash", "Unexpected error", exception)
```

✅ 在 **KMP 库或应用的 `commonMain`** 中直接调用；  
✅ 在 **纯 Android / iOS / JVM / JS 项目** 中同样可用；  
✅ **无需任何平台判断或依赖**，就像写普通 Kotlin 代码一样自然。

### 使用 `+` 组合日志器

想同时输出到多个目标？一行代码即可：

```kotlin
// 同时发送日志到系统日志器和你的自定义日志器
Logger.default = Logger.SYSTEM + CustomLogger()
```

此后所有日志将自动分发给两个日志器。

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

| 平台    | 目标平台 | 输出目标             |
|---------|----------|----------------------|
| Android | Android  | `android.util.Log`   |
| iOS     | Native   | `NSLog`              |
| JVM     | JVM      | `System.out`         |
| JS      | JS (IR)  | `console.log`        |

> ℹ️ 基于 **Kotlin 1.9.23** 构建，兼容 **Kotlin 1.9.0 及以上版本**。

---

## 🤝 贡献

欢迎贡献！详见 [CONTRIBUTING.md](CONTRIBUTING.md)。

---

## 📄 许可证

本项目采用 **MIT 许可证** —— 详见 [LICENSE](LICENSE)。
