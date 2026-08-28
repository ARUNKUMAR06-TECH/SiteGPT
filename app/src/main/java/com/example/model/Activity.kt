package com.example.model

enum class RiskTier(
    val label: String,
    val shortLabel: String
) {
    ON_TRACK("On Track", "ON TRACK"),
    AT_RISK("At Risk", "AT RISK"),
    CRITICAL("Critical", "CRITICAL")
}

data class L6Activity(
    val id: String,
    val name: String,
    val location: String,
    val phase: String,
    val unit: String,
    val plannedQuantity: Double?,
    val actualQuantity: Double,
    val plannedProgress: Int, // e.g. 70 (%)
    val actualProgress: Int,  // e.g. 45 (%)
    val recommendation: String = ""
) {
    val variance: Int
        get() = actualProgress - plannedProgress

    val riskTier: RiskTier
        get() = when {
            variance >= 0 -> RiskTier.ON_TRACK
            variance >= -10 -> RiskTier.AT_RISK
            else -> RiskTier.CRITICAL
        }
}

data class L5Phase(
    val id: String,
    val name: String,
    val activities: List<L6Activity>
) {
    val progress: Int
        get() = if (activities.isNotEmpty()) (activities.map { it.actualProgress }.average()).toInt() else 0
}

data class ProjectMeta(
    val code: String = "CM-SA-2026",
    val title: String = "Chennai Metro — Station A",
    val plannedProgress: Int = 64,
    val initialOverallProgress: Int = 57,
    val hierarchyLevel: String = "L1 Chennai Metro Project > L2 Station Construction > L3 Station A > L4 Structural Works > L5 Foundation Works"
)
