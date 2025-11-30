package dev.scarlet.logger.filter

import dev.scarlet.logger.Logger.Level.INFO

internal actual val platformFilter: Filter = Filter.atLeast(INFO)