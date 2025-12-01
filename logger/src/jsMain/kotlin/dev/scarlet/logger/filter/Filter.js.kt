package dev.scarlet.logger.filter

import dev.scarlet.logger.Logger.Level.WARN

internal actual val platformFilter: Filter = Filter.atLeast(WARN)