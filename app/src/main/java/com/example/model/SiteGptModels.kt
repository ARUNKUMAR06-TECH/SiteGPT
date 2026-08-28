package com.example.model

enum class ActivityStatus(
    val label: String,
    val shortLabel: String
) {
    COMPLETED("Completed", "COMPLETED"),
    ON_TRACK("On Track", "ON TRACK"),
    AT_RISK("At Risk", "AT RISK"),
    DELAYED("Delayed", "DELAYED"),
    CRITICAL("Critical", "CRITICAL")
}

enum class HierarchyLevel(val code: String, val description: String) {
    L1("L1", "Project Master"),
    L2("L2", "Corridor / Phase"),
    L3("L3", "Contract Package"),
    L4("L4", "Structure / Bridge / Span"),
    L5("L5", "Component / Substructure"),
    L6("L6", "Execution Work Package / Task")
}

data class SiteActivity(
    val activityId: String,
    val activityName: String,
    val hierarchyLevel: String,
    val hierarchyPath: String,
    val parentActivityId: String?,
    val packageName: String,
    val location: String,
    val plannedStart: String,
    val plannedEnd: String,
    val actualStart: String,
    val actualEnd: String? = null,
    val forecastEnd: String,
    val plannedQuantity: Double,
    val actualQuantity: Double,
    val unit: String,
    val plannedProgress: Int, // 0..100
    val actualProgress: Int,  // 0..100
    val status: ActivityStatus,
    val dependencies: List<String>, // Predecessors
    val isCriticalPath: Boolean,
    val manpower: Int,
    val plannedManpower: Int,
    val equipment: String,
    val productivityRate: Double, // current actual unit/day
    val plannedProductivityRate: Double, // planned unit/day
    val delayDays: Int,
    val delayProbability: Int, // 0..100
    val delayReason: String = "",
    val riskScore: Int = 0, // 0..100
    val durationDays: Int = 14,
    val matchConfidence: Int = 95
) {
    val variance: Int
        get() = actualProgress - plannedProgress

    val quantityProgress: Double
        get() = if (plannedQuantity > 0) (actualQuantity / plannedQuantity) * 100.0 else 0.0

    val productivityVariance: Double
        get() = if (plannedProductivityRate > 0) {
            ((productivityRate - plannedProductivityRate) / plannedProductivityRate) * 100.0
        } else 0.0
}

data class ProjectOverview(
    val projectId: String = "PRJ-CHE-2026",
    val projectName: String = "Chennai Elevated Highway Project",
    val location: String = "Chennai Outer Corridor, TN",
    val startDate: String = "15 Jan 2026",
    val plannedEndDate: String = "30 Nov 2026",
    val forecastEndDate: String = "18 Dec 2026",
    val plannedProgress: Double = 68.4,
    val actualProgress: Double = 59.2,
    val scheduleVariance: Double = -9.2,
    val daysBehindAhead: Int = -18, // -18 days behind
    val healthIndicator: String = "CRITICAL PATH DELAY",
    val totalBudgetInrCr: Double = 1420.0,
    val spentBudgetInrCr: Double = 840.5,
    val totalActivities: Int = 42,
    val completedActivities: Int = 14,
    val onTrackActivities: Int = 12,
    val atRiskActivities: Int = 7,
    val delayedActivities: Int = 5,
    val criticalActivities: Int = 4
)

data class ExtractedDprItem(
    val id: String,
    val activityId: String,
    val activityName: String,
    val packageName: String,
    val location: String,
    val plannedQuantity: Double,
    val executedQuantity: Double,
    val unit: String,
    val completionPercent: Int,
    val manpower: Int,
    val equipment: String,
    val startDate: String,
    val endDate: String,
    val actualProgress: Int,
    val issues: String,
    val delays: String,
    val remarks: String,
    val confidence: Int = 94,
    val matchStatus: String = "AI Matched",
    val hierarchyPath: String = "Highway Project > Bridge Package B04 > Pier P12"
)

data class RiskItem(
    val riskId: String,
    val activityId: String,
    val activityName: String,
    val packageName: String,
    val category: String, // Critical Path, Resource Bottleneck, Dependency Conflict, Productivity, Weather
    val probability: Int, // 0..100 %
    val impactDays: Int,
    val impactCostLakhs: Double,
    val severity: String, // CRITICAL, HIGH, MEDIUM, LOW
    val score: Int, // Probability * Impact / 10
    val cause: String,
    val mitigation: String,
    val status: String = "Active"
)

data class WeatherForecastItem(
    val date: String,
    val dayName: String,
    val condition: String,
    val tempC: Int,
    val rainProbability: Int,
    val windSpeedKmh: Int,
    val affectedActivities: List<String>,
    val riskLevel: String, // HIGH, MEDIUM, LOW
    val expectedDelayDays: Int,
    val recommendedAction: String,
    val isSimulated: Boolean = true
)

data class WhatIfScenario(
    val id: String,
    val query: String,
    val targetPackage: String,
    val targetActivity: String,
    val changeType: String, // EXTRA_SHIFT, MANPOWER_INCREASE, DELAY, WEEKEND_WORK, RESEQUENCE
    val magnitude: Double,
    val currentFinishDate: String,
    val projectedFinishDate: String,
    val daysImpact: Int, // -7 (recovers 7 days) or +4 (delays 4 days)
    val costImpactLakhs: Double, // +/- ₹ Lakhs
    val riskLevel: String, // Low, Medium, High
    val recommendation: String,
    val deterministicExplanation: String
)

data class RecoveryStrategy(
    val id: String,
    val name: String,
    val category: String,
    val targetActivityOrPackage: String,
    val expectedDaysRecovered: Int,
    val resourceRequirement: String,
    val costImpactLakhs: Double,
    val riskLevel: String, // Low, Medium, High
    val advantages: List<String>,
    val disadvantages: List<String>,
    val isApplied: Boolean = false
)

data class AiInsightItem(
    val id: String,
    val tier: String, // CRITICAL, WARNING, OPPORTUNITY
    val title: String,
    val message: String,
    val activityId: String,
    val actionText: String
)

data class DailySiteSummary(
    val date: String,
    val executiveSummary: String,
    val completedActivities: List<String>,
    val delayedActivities: List<String>,
    val newRisksIdentified: List<String>,
    val weatherImpactSummary: String,
    val resourceIssues: List<String>,
    val criticalActivities: List<String>,
    val tomorrowPriorities: List<String>
)

data class CopilotMessage(
    val id: String,
    val isUser: Boolean,
    val timestamp: String,
    val text: String,
    val supportingData: String? = null,
    val affectedActivities: List<String>? = null,
    val reasoning: String? = null,
    val recommendedAction: String? = null,
    val isInsufficientData: Boolean = false
)

data class ReportSource(
    val photo: Boolean = false,
    val voice: Boolean = false,
    val text: Boolean = false,
    val pdf: Boolean = false,
    val spreadsheet: Boolean = false,
    val documentName: String? = null
)

data class SpreadsheetRowItem(
    val rowId: String,
    val activityId: String,
    val activityName: String,
    val location: String,
    val plannedQty: Double,
    val actualQty: Double,
    val unit: String,
    val plannedProgress: Int,
    val actualProgress: Int,
    val delayHours: Int = 0,
    val delayReason: String = "",
    val issues: String = "",
    val contractor: String = "L&T Infrastructure"
)

data class SiteEvidenceReport(
    val id: String,
    val projectId: String = "PRJ-CHE-2026",
    val activityId: String,
    val activityName: String,
    val plannedProgress: Int,
    val actualProgress: Int,
    val progressVariance: Int,
    val status: String,
    val delayHours: Int = 0,
    val delayDays: Int = 0,
    val delayReason: String = "",
    val observation: String = "",
    val capturedAt: String,
    val source: ReportSource = ReportSource(),
    val aiConfidence: Int = 92,
    val isDemo: Boolean = false,
    val visibleElements: List<String> = emptyList(),
    val issues: List<String> = emptyList(),
    val equipment: List<String> = emptyList(),
    val materials: List<String> = emptyList(),
    val photoUri: String? = null,
    val photoBase64: String? = null,
    val syncedToFirestore: Boolean = true
)

data class AiMultimodalExtractionResult(
    val activityName: String,
    val detectedProgress: Int,
    val executedQuantity: Double? = null,
    val unit: String? = null,
    val delayHours: Int = 0,
    val delayReason: String = "",
    val status: String = "Delayed",
    val observation: String = "",
    val visibleElements: List<String> = emptyList(),
    val issues: List<String> = emptyList(),
    val equipment: List<String> = emptyList(),
    val materials: List<String> = emptyList(),
    val confidence: Int = 92,
    val matchedActivityId: String? = null,
    val matchConfidence: Int = 92,
    val isConfidentlyIdentified: Boolean = true,
    val weatherImpact: String? = null,
    val workforceEstimate: Int? = null
)

