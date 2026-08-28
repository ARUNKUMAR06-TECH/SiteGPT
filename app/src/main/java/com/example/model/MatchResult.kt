package com.example.model

data class FactorBreakdown(
    val descriptionSimilarity: Int, // e.g. 92% (weight 40%)
    val locationMatch: Int,          // e.g. 100% (weight 20%)
    val activityCodeMatch: Int,      // e.g. 100% (weight 15%)
    val projectPhaseMatch: Int,      // e.g. 95% (weight 10%)
    val dateContextMatch: Int,       // e.g. 90% (weight 10%)
    val quantityUnitMatch: Int       // e.g. 100% (weight 5%)
)

enum class ConfidenceTier {
    HIGH,   // 90-100%
    MEDIUM, // 70-89%
    LOW     // < 70%
}

data class ScoredMatch(
    val activity: L6Activity,
    val overallConfidence: Int,
    val factors: FactorBreakdown,
    val rankBadge: String, // "🥇 Recommended", "🥈 Candidate", "🥉 Rollup Match"
    val explanation: String
) {
    val confidenceTier: ConfidenceTier
        get() = when {
            overallConfidence >= 90 -> ConfidenceTier.HIGH
            overallConfidence >= 70 -> ConfidenceTier.MEDIUM
            else -> ConfidenceTier.LOW
        }
}
