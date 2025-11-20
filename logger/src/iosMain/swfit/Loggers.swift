// Loggers.swift
//
// Add this file to your iOS/macOS target to enable static-style logging:
//   Logger.i("Tag", "Message")
//
// This file is part of the Scarlet Logger KMP library.
// https://github.com/scarlet-pan/logger
//
// MIT License
// Copyright (c) 2025 Scarlet Pan

import Foundation
import KmpLogger

/// A unified logging interface for cross-platform applications.
///
/// This protocol defines a minimal, consistent API for logging across Kotlin Multiplatform targets.
/// It supports four standard severity levels: DEBUG, INFO, WARN, and ERROR.
///
/// The interface itself contains no implementation — actual behavior is determined by the concrete logger
/// assigned to ``Logger/default``. By default, logs are printed to standard output (visible in Xcode console).
///
/// All methods are thread-safe and may be called from any thread or async context.
///
/// ### Default Behavior
/// - On iOS/macOS: Messages are formatted and sent to `print()`, appearing in the Xcode console.
/// - The initial value of ``Logger/default`` is the system-provided logger (`SYSTEM`).
///
/// ### Customization
/// Replace the global logger to route logs to files, network, third-party services, or apply filtering:
/// ```swift
/// Logger.default = MyCustomLogger()
/// Logger.i("App", "Now using custom logger")
/// ```
///
/// ### Log Levels
/// | Level | Use Case |
/// |-------|---------|
/// | `d()` (DEBUG) | Detailed diagnostic info, typically disabled in production. |
/// | `i()` (INFO)  | General operational status (e.g., app startup, state changes). |
/// | `w()` (WARN)  | Unexpected but recoverable conditions. |
/// | `e()` (ERROR) | Serious failures; always include an error if available. |
///
/// Example:
/// ```swift
/// Logger.d("Network", "Request body: \(json)")
/// Logger.i("Auth", "User signed in")
/// Logger.w("Cache", "Missed entry; fetching from network")
/// Logger.e("Database", "Failed to open", error: dbError)
/// ```
extension Logger {
    /// The global default logger instance used by all static logging methods.
    ///
    /// This property acts as a delegate: every call like `Logger.i(...)` is forwarded to the current value
    /// of this property. Changing it takes effect immediately.
    ///
    /// On assignment, the new logger automatically logs an INFO message:
    /// ```
    /// [Logger] Default logger changed to MyCustomLogger().
    /// ```
    /// This helps diagnose logger pipeline changes at runtime.
    ///
    /// The initial value is the platform-specific system logger (equivalent to `SYSTEM` in Kotlin).
    public static var `default`: Logging {
        get { Logger.shared.default_}
        set { Logger.shared.default_ = newValue }
    }

    /// Logs a message at the **DEBUG** level.
    ///
    /// Typically used for fine-grained diagnostic information during development.
    /// Whether the message appears depends on the underlying logger implementation
    /// (e.g., some production loggers may discard DEBUG logs).
    ///
    /// - Parameters:
    ///   - tag: A short identifier for the source (e.g., `"Network"`, `"DB"`).
    ///   - message: The log message (must not be nil).
    ///   - error: An optional error; if provided, its description and stack trace (if available) should be included.
    public static func d(_ tag: String, _ message: String, error: Error? = nil) {
        Logger.shared.d(tag: tag, msg: message, tr: error?.asException())
    }

    /// Logs a message at the **INFO** level.
    ///
    /// Used for general operational messages that confirm expected behavior,
    /// such as service initialization or key user actions.
    ///
    /// - Parameters:
    ///   - tag: A short identifier for the source.
    ///   - message: The log message.
    ///   - error: An optional error to attach.
    public static func i(_ tag: String, _ message: String, error: Error? = nil) {
        Logger.shared.i(tag: tag, msg: message, tr: error?.asException())
    }

    /// Logs a message at the **WARN** level.
    ///
    /// Indicates an unexpected or unusual condition that does not prevent continued execution,
    /// but may indicate a future problem (e.g., deprecated API usage, fallback behavior).
    ///
    /// - Parameters:
    ///   - tag: A short identifier for the source.
    ///   - message: The warning message. Defaults to an empty string if only an error is relevant.
    ///   - error: An optional error to attach.
    public static func w(_ tag: String, _ message: String = "", error: Error? = nil) {
        Logger.shared.w(tag: tag, msg: message, tr: error?.asException())
    }

    /// Logs a message at the **ERROR** level.
    ///
    /// Represents a serious failure that impacts functionality (e.g., unhandled exception, I/O failure).
    /// It is strongly recommended to provide an `error` when logging errors to preserve full context.
    ///
    /// - Parameters:
    ///   - tag: A short identifier for the source.
    ///   - message: A description of the error condition.
    ///   - error: An optional but highly recommended error object.
    public static func e(_ tag: String, _ message: String, error: Error? = nil) {
        Logger.shared.e(tag: tag, msg: message, tr: error?.asException())
    }
}

extension Error {

    /// Converts this Swift `Error` into a `KotlinThrowable` using standard `NSError` fields.
    ///
    /// The resulting message has the format: `[domain] Code=code: localizedDescription`.
    /// If an underlying error exists (via `NSUnderlyingErrorKey`), it is recursively converted
    /// and set as the `cause`.
    ///
    /// - Note: No stack trace is included, as `NSError` does not capture one by default.
    /// - Returns: A `KotlinThrowable` representing this error.
    func asException() -> KotlinThrowable {
        let nsError = self as NSError

        let cause: KotlinThrowable? =
            (nsError.userInfo[NSUnderlyingErrorKey] as? NSError)?.asException()

        let message = [
            "[\(nsError.domain)] Code=\(nsError.code)",
            nsError.localizedDescription.isEmpty ? nil : nsError.localizedDescription
        ]
        .compactMap { $0 }
        .joined(separator: ": ")

        return cause != nil
            ? KotlinThrowable(message: message, cause: cause)
            : KotlinThrowable(message: message)
    }
}