package dev.scarlet.logger.filter

import dev.scarlet.logger.Logger.Level.DEBUG

internal actual val platformFilter: Filter = Filter.atLeast(DEBUG)