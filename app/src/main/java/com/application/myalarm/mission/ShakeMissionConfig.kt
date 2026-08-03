package com.application.myalarm.mission

data class ShakeLevelConfig(
    val level: Int,
    val requiredShakes: Int,
    val sensitivityThreshold: Float,
    val labelKey: String
)

object ShakeMissionConfig {

    val LEVELS: Map<Int, ShakeLevelConfig> = mapOf(
        1 to ShakeLevelConfig(1, requiredShakes = 40, sensitivityThreshold = 9.5f, labelKey = "Easy"),
        2 to ShakeLevelConfig(2, requiredShakes = 50, sensitivityThreshold = 11.0f, labelKey = "Simple"),
        3 to ShakeLevelConfig(3, requiredShakes = 60, sensitivityThreshold = 12.5f, labelKey = "Moderate"),
        4 to ShakeLevelConfig(4, requiredShakes = 80, sensitivityThreshold = 14.5f, labelKey = "Hard"),
        5 to ShakeLevelConfig(5, requiredShakes = 120, sensitivityThreshold = 17.0f, labelKey = "Extreme")
    )

    const val DEFAULT_LEVEL = 3

    fun getConfig(level: Int): ShakeLevelConfig {
        return LEVELS[level] ?: LEVELS[DEFAULT_LEVEL]!!
    }

    /**
     * Pure evaluator to check whether an acceleration vector magnitude meets
     * the required sensitivity threshold and time debounce interval.
     */
    fun isShakeValid(
        magnitude: Float,
        threshold: Float,
        lastShakeTime: Long,
        currentTime: Long,
        minIntervalMs: Long = 300L
    ): Boolean {
        return magnitude >= threshold && (currentTime - lastShakeTime) > minIntervalMs
    }
}
