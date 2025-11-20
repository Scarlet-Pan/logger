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
import loggerKit // ← Must match your XCFramework module name

/// A typealias for the underlying Logger protocol.
public typealias LoggerProtocol = Logger_

extension Logger {
    /// The default logger instance used by static methods.
    ///
    /// Usage:
    /// ```swift
    /// Logger.default = MyCustomLogger()
    /// ```
    public static var `default`: LoggerProtocol {
        get { Logger.shared.default }
        set { Logger.shared.default = newValue }
    }

    /// Logs a debug message with an optional error.
    ///
    /// - Parameters:
    ///   - tag: The tag associated with the log message.
    ///   - message: The message to log.
    ///   - error: An optional error object to include in the log.
    public static func d(_ tag: String, _ message: String, error: Error? = nil) {
        Logger.shared.d(tag: tag, msg: message, tr: error as NSError?)
    }

    /// Logs an informational message with an optional error.
    ///
    /// - Parameters:
    ///   - tag: The tag associated with the log message.
    ///   - message: The message to log.
    ///   - error: An optional error object to include in the log.
    public static func i(_ tag: String, _ message: String, error: Error? = nil) {
        Logger.shared.i(tag: tag, msg: message, tr: error as NSError?)
    }

    /// Logs a warning message with an optional error.
    ///
    /// - Parameters:
    ///   - tag: The tag associated with the log message.
    ///   - message: The message to log. Defaults to an empty string.
    ///   - error: An optional error object to include in the log.
    public static func w(_ tag: String, _ message: String = "", error: Error? = nil) {
        Logger.shared.w(tag: tag, msg: message, tr: error as NSError?)
    }

    /// Logs an error message with an optional error.
    ///
    /// - Parameters:
    ///   - tag: The tag associated with the log message.
    ///   - message: The message to log.
    ///   - error: An optional error object to include in the log.
    public static func e(_ tag: String, _ message: String, error: Error? = nil) {
        Logger.shared.e(tag: tag, msg: message, tr: error as NSError?)
    }
}