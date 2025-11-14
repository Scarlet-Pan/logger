# Scarlet Logger

[![Maven Central](https://img.shields.io/maven-central/v/io.github.scarlet-pan/logger)](https://search.maven.org/artifact/io.github.scarlet-pan/logger)  
[![Kotlin Multiplatform](https://img.shields.io/badge/Kotlin-Multiplatform-blue?logo=kotlin)](https://kotlinlang.org/docs/multiplatform.html)  
[![License](https://img.shields.io/github/license/scarlet-pan/logger)](LICENSE)

---

## 🌐 Why Use It?

- ✅ **Built for Kotlin Multiplatform (KMP)**: Use `Logger` directly in `commonMain` — no `expect/actual`, no conditional compilation.
- ✅ **Android Log Style**: Familiar `d()`, `i()`, `w()`, `e()` APIs, just like `android.util.Log`. Perfect for Android and Kotlin developers.
- ✅ **Truly Cross-Platform**: Works out of the box in **KMP libraries**, **KMP apps**, and even **standalone projects** (Android, iOS, JVM, JS).
- ✅ **Zero Platform Code**: Automatically uses native logging:
    - Android → `Log.d`
    - iOS → `NSLog`
    - JVM → `System.out`
    - JS → `console.log`

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
Logger.default = Logger.SYSTEM + MyCustomLogger()
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
- Optional `Throwable` support
- Fully thread-safe

---

## 🤝 Contributing

Contributions welcome! See [CONTRIBUTING.md](CONTRIBUTING.md).

---

## 📄 License

MIT — see [LICENSE](LICENSE).

> Made for Kotlin Multiplatform developers who miss `Log.d()` in common code.

---

<br><br>
<div align="center">
  <hr width="80%" />
  <p><em>—— 中文文档 Chinese Documentation ——</em></p>
  <hr width="80%" />
</div>
<br><br>

# Scarlet Logger

[![Maven Central](https://img.shields.io/maven-central/v/io.github.scarlet-pan/logger)](https://search.maven.org/artifact/io.github.scarlet-pan/logger)  
[![Kotlin Multiplatform](https://img.shields.io/badge/Kotlin-Multiplatform-blue?logo=kotlin)](https://kotlinlang.org/docs/multiplatform.html)  
[![License](https://img.shields.io/github/license/scarlet-pan/logger)](LICENSE)

---

## 🌐 为什么选择它？

- ✅ **专为 Kotlin 多平台（KMP）设计**：在 `commonMain` 中直接调用 `Logger`，无需 `expect/actual`，无需条件编译。
- ✅ **Android 日志风格**：提供与 `android.util.Log` 完全一致的 `d()`、`i()`、`w()`、`e()` 接口，Android/Kotlin 开发者秒上手。
- ✅ **真正跨平台**：既适用于 **KMP 库** 和 **KMP 应用**，也适用于 **纯 Android、iOS、JVM 或 JS 项目**。
- ✅ **零平台代码**：自动桥接各平台原生日志系统：
    - Android → `Log.d`
    - iOS → `NSLog`
    - JVM → `System.out`
    - JS → `console.log`

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

---

## 🚀 使用方法

### 在公共代码中直接打日志

```kotlin
import dev.scarlet.logger.Logger

Logger.d("Network", "请求已发送")
Logger.i("Database", "查询成功")
Logger.w("App", "使用了废弃 API")
Logger.e("Crash", "发生未预期错误", exception)
```

✅ 在 **KMP 库或应用的 `commonMain`** 中直接调用；  
✅ 在 **纯 Android / iOS / JVM / JS 项目** 中同样可用；  
✅ **无需任何平台判断或依赖**，就像写普通 Kotlin 代码一样自然。

### 使用 `+` 组合日志器

想同时输出到多个目标？一行代码即可：

```kotlin
Logger.default = Logger.SYSTEM + MyCustomLogger()
```

此后所有日志将自动分发给系统日志器和你的自定义日志器。

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

## 🤝 贡献

欢迎贡献！详见 [CONTRIBUTING.md](CONTRIBUTING.md)。

---

## 📄 许可证

MIT 许可证 —— 详见 [LICENSE](LICENSE)。

> 为那些在 `common` 代码中怀念 `Log.d()` 的 Kotlin 多平台开发者而生。
