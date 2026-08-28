package com.example.service

import com.example.model.ActivityStatus
import com.example.model.AiInsightItem
import com.example.model.CopilotMessage
import com.example.model.DailySiteSummary
import com.example.model.ExtractedDprItem
import com.example.model.ProjectOverview
import com.example.model.RecoveryStrategy
import com.example.model.ReportSource
import com.example.model.RiskItem
import com.example.model.SiteActivity
import com.example.model.SiteEvidenceReport
import com.example.model.SpreadsheetRowItem
import com.example.model.WeatherForecastItem

object SiteGptDataProvider {

    fun getInitialProjectOverview(): ProjectOverview {
        return ProjectOverview(
            projectId = "CHE-HW-2026",
            projectName = "Chennai Elevated Highway Project",
            location = "Outer Ring Corridor (Ch. 0+000 to 24+500), TN",
            startDate = "15 Jan 2026",
            plannedEndDate = "30 Nov 2026",
            forecastEndDate = "18 Dec 2026",
            plannedProgress = 68.4,
            actualProgress = 59.2,
            scheduleVariance = -9.2,
            daysBehindAhead = -18,
            healthIndicator = "CRITICAL PATH DELAY (B04 -18d)",
            totalBudgetInrCr = 1420.0,
            spentBudgetInrCr = 840.5,
            totalActivities = 24,
            completedActivities = 7,
            onTrackActivities = 9,
            atRiskActivities = 4,
            delayedActivities = 2,
            criticalActivities = 2
        )
    }

    fun getInitialActivities(): List<SiteActivity> {
        return listOf(
            // --- Standard Activity ACT-024 (Column C12 Reinforcement) ---
            SiteActivity(
                activityId = "ACT-024",
                activityName = "Column C12 Reinforcement",
                hierarchyLevel = "L6 Work Package",
                hierarchyPath = "Highway Project > Bridge Package B04 > Substructure > Column C12 > Reinforcement",
                parentActivityId = "ACT-B04-COL12",
                packageName = "Bridge Package B04",
                location = "Column C12, Ch. 12+800",
                plannedStart = "14 Aug 2026",
                plannedEnd = "28 Aug 2026",
                actualStart = "15 Aug 2026",
                actualEnd = null,
                forecastEnd = "30 Aug 2026",
                plannedQuantity = 48.0,
                actualQuantity = 38.4,
                unit = "MT",
                plannedProgress = 90,
                actualProgress = 80,
                status = ActivityStatus.DELAYED,
                dependencies = emptyList(),
                isCriticalPath = true,
                manpower = 16,
                plannedManpower = 18,
                equipment = "1x 30T Crane, Rebar Cutting Machine",
                productivityRate = 3.2,
                plannedProductivityRate = 3.6,
                delayDays = 2,
                delayProbability = 65,
                delayReason = "Rain stoppage (2 hours) + minor rebar staging wait",
                riskScore = 58,
                durationDays = 14,
                matchConfidence = 96
            ),
            // --- Bridge Package B04 (Critical Section) ---
            SiteActivity(
                activityId = "ACT-B04-01",
                activityName = "Pier P12 Concrete Pouring",
                hierarchyLevel = "L6 Work Package",
                hierarchyPath = "Highway Project > Bridge Package B04 > Pier Construction > Pier P12 > Concrete Pouring",
                parentActivityId = "ACT-B04-PIER12",
                packageName = "Bridge Package B04",
                location = "Pier P12, Ch. 14+200",
                plannedStart = "10 Aug 2026",
                plannedEnd = "24 Aug 2026",
                actualStart = "12 Aug 2026",
                actualEnd = null,
                forecastEnd = "01 Sep 2026",
                plannedQuantity = 450.0,
                actualQuantity = 220.0,
                unit = "m³",
                plannedProgress = 80,
                actualProgress = 48,
                status = ActivityStatus.CRITICAL,
                dependencies = listOf("ACT-B04-02"),
                isCriticalPath = true,
                manpower = 18,
                plannedManpower = 26,
                equipment = "1x Putzmeister Boom Pump, 3x Transit Mixers (6m³)",
                productivityRate = 18.3,
                plannedProductivityRate = 26.0,
                delayDays = 6,
                delayProbability = 88,
                delayReason = "Batching plant mixer maintenance + 14% manpower shortage causing casting cycles to slip",
                riskScore = 92,
                durationDays = 14,
                matchConfidence = 98
            ),
            SiteActivity(
                activityId = "ACT-B04-02",
                activityName = "Pier P12 Reinforcement & Formwork Fixing",
                hierarchyLevel = "L6 Work Package",
                hierarchyPath = "Highway Project > Bridge Package B04 > Pier Construction > Pier P12 > Rebar & Shuttering",
                parentActivityId = "ACT-B04-PIER12",
                packageName = "Bridge Package B04",
                location = "Pier P12, Ch. 14+200",
                plannedStart = "28 Jul 2026",
                plannedEnd = "10 Aug 2026",
                actualStart = "29 Jul 2026",
                actualEnd = "12 Aug 2026",
                forecastEnd = "12 Aug 2026",
                plannedQuantity = 64.0,
                actualQuantity = 64.0,
                unit = "MT",
                plannedProgress = 100,
                actualProgress = 100,
                status = ActivityStatus.COMPLETED,
                dependencies = listOf("ACT-B04-03"),
                isCriticalPath = true,
                manpower = 22,
                plannedManpower = 22,
                equipment = "1x 50T Mobile Crane, Bar Bending Yard Unit 2",
                productivityRate = 5.3,
                plannedProductivityRate = 5.0,
                delayDays = 0,
                delayProbability = 10,
                delayReason = "Completed and inspected by QA/QC team",
                riskScore = 15,
                durationDays = 13,
                matchConfidence = 96
            ),
            SiteActivity(
                activityId = "ACT-B04-03",
                activityName = "Pier Cap P12 Staging & Pre-stressing Duct Fixing",
                hierarchyLevel = "L6 Work Package",
                hierarchyPath = "Highway Project > Bridge Package B04 > Pier Cap Construction > Pier Cap P12",
                parentActivityId = "ACT-B04-PIER12",
                packageName = "Bridge Package B04",
                location = "Pier P12, Ch. 14+200",
                plannedStart = "25 Aug 2026",
                plannedEnd = "10 Sep 2026",
                actualStart = "28 Aug 2026",
                actualEnd = null,
                forecastEnd = "16 Sep 2026",
                plannedQuantity = 1.0,
                actualQuantity = 0.2,
                unit = "Span",
                plannedProgress = 30,
                actualProgress = 10,
                status = ActivityStatus.DELAYED,
                dependencies = listOf("ACT-B04-01"),
                isCriticalPath = true,
                manpower = 14,
                plannedManpower = 20,
                equipment = "Heavy Duty Cuplock Staging System, 75T Crane",
                productivityRate = 0.05,
                plannedProductivityRate = 0.10,
                delayDays = 5,
                delayProbability = 76,
                delayReason = "Predecessor Pier P12 casting delay holding staging release",
                riskScore = 80,
                durationDays = 16,
                matchConfidence = 94
            ),
            SiteActivity(
                activityId = "ACT-B04-04",
                activityName = "Deck Slab Segmental Casting Span 12-13",
                hierarchyLevel = "L5 Component",
                hierarchyPath = "Highway Project > Bridge Package B04 > Superstructure > Span 12-13 Deck",
                parentActivityId = "ACT-B04-DECK",
                packageName = "Bridge Package B04",
                location = "Span 12-13, Ch. 14+250",
                plannedStart = "12 Sep 2026",
                plannedEnd = "05 Oct 2026",
                actualStart = "Pending",
                actualEnd = null,
                forecastEnd = "14 Oct 2026",
                plannedQuantity = 820.0,
                actualQuantity = 0.0,
                unit = "m³",
                plannedProgress = 0,
                actualProgress = 0,
                status = ActivityStatus.AT_RISK,
                dependencies = listOf("ACT-B04-03"),
                isCriticalPath = true,
                manpower = 0,
                plannedManpower = 32,
                equipment = "Launching Gantry LG-01 (Under Pre-commissioning)",
                productivityRate = 0.0,
                plannedProductivityRate = 35.0,
                delayDays = 7,
                delayProbability = 65,
                delayReason = "Downstream delay cascade from Pier Cap P12 completion",
                riskScore = 74,
                durationDays = 23,
                matchConfidence = 91
            ),

            // --- Bridge Package B01 (Viaduct South Section) ---
            SiteActivity(
                activityId = "ACT-B01-01",
                activityName = "Pile Foundation (1200mm Dia) Piles 45-56",
                hierarchyLevel = "L6 Work Package",
                hierarchyPath = "Highway Project > Bridge Package B01 > Substructure > Piling Works",
                parentActivityId = "ACT-B01-FOUND",
                packageName = "Bridge Package B01",
                location = "Pier P04 to P07, Ch. 4+800",
                plannedStart = "01 Jul 2026",
                plannedEnd = "30 Jul 2026",
                actualStart = "02 Jul 2026",
                actualEnd = "28 Jul 2026",
                forecastEnd = "28 Jul 2026",
                plannedQuantity = 12.0,
                actualQuantity = 12.0,
                unit = "Nos",
                plannedProgress = 100,
                actualProgress = 100,
                status = ActivityStatus.COMPLETED,
                dependencies = emptyList(),
                isCriticalPath = false,
                manpower = 16,
                plannedManpower = 16,
                equipment = "2x Soilmec Hydraulic Rotary Drilling Rig",
                productivityRate = 0.5,
                plannedProductivityRate = 0.45,
                delayDays = 0,
                delayProbability = 5,
                delayReason = "Ahead of schedule due to rock strata stability",
                riskScore = 10,
                durationDays = 29,
                matchConfidence = 99
            ),
            SiteActivity(
                activityId = "ACT-B01-02",
                activityName = "Pile Cap Casting & Waterproofing (Cap C04-C06)",
                hierarchyLevel = "L6 Work Package",
                hierarchyPath = "Highway Project > Bridge Package B01 > Substructure > Pile Caps",
                parentActivityId = "ACT-B01-FOUND",
                packageName = "Bridge Package B01",
                location = "Pier P04 to P06, Ch. 5+100",
                plannedStart = "05 Aug 2026",
                plannedEnd = "28 Aug 2026",
                actualStart = "06 Aug 2026",
                actualEnd = null,
                forecastEnd = "29 Aug 2026",
                plannedQuantity = 380.0,
                actualQuantity = 320.0,
                unit = "m³",
                plannedProgress = 85,
                actualProgress = 82,
                status = ActivityStatus.ON_TRACK,
                dependencies = listOf("ACT-B01-01"),
                isCriticalPath = false,
                manpower = 24,
                plannedManpower = 24,
                equipment = "Batching Plant Unit 1, 2x Transit Mixers",
                productivityRate = 18.0,
                plannedProductivityRate = 18.5,
                delayDays = 1,
                delayProbability = 20,
                delayReason = "Minor localized rain interruption",
                riskScore = 22,
                durationDays = 23,
                matchConfidence = 95
            ),
            SiteActivity(
                activityId = "ACT-B01-03",
                activityName = "Precast I-Girder Fabrication (Yard Lot 4)",
                hierarchyLevel = "L5 Component",
                hierarchyPath = "Highway Project > Bridge Package B01 > Superstructure > Girder Casting Yard",
                parentActivityId = "ACT-B01-SUPER",
                packageName = "Bridge Package B01",
                location = "Central Casting Yard, Ch. 8+000",
                plannedStart = "15 Jul 2026",
                plannedEnd = "30 Aug 2026",
                actualStart = "15 Jul 2026",
                actualEnd = null,
                forecastEnd = "30 Aug 2026",
                plannedQuantity = 36.0,
                actualQuantity = 32.0,
                unit = "Nos",
                plannedProgress = 90,
                actualProgress = 88,
                status = ActivityStatus.ON_TRACK,
                dependencies = emptyList(),
                isCriticalPath = false,
                manpower = 30,
                plannedManpower = 30,
                equipment = "2x Gantry Cranes 80T, Steam Curing Boilers",
                productivityRate = 0.85,
                plannedProductivityRate = 0.80,
                delayDays = 0,
                delayProbability = 12,
                delayReason = "Steam curing cycle optimization maintained velocity",
                riskScore = 18,
                durationDays = 46,
                matchConfidence = 97
            ),

            // --- Road Package R01 (Main Highway At-Grade Corridor) ---
            SiteActivity(
                activityId = "ACT-R01-01",
                activityName = "Earthwork Excavation & Embankment Filling",
                hierarchyLevel = "L6 Work Package",
                hierarchyPath = "Highway Project > Road Package R01 > Earthworks > Embankment",
                parentActivityId = "ACT-R01-EARTH",
                packageName = "Road Package R01",
                location = "Ch. 0+000 to Ch. 6+500",
                plannedStart = "01 Jun 2026",
                plannedEnd = "15 Jul 2026",
                actualStart = "01 Jun 2026",
                actualEnd = "14 Jul 2026",
                forecastEnd = "14 Jul 2026",
                plannedQuantity = 85000.0,
                actualQuantity = 85000.0,
                unit = "m³",
                plannedProgress = 100,
                actualProgress = 100,
                status = ActivityStatus.COMPLETED,
                dependencies = emptyList(),
                isCriticalPath = false,
                manpower = 28,
                plannedManpower = 28,
                equipment = "4x CAT Excavators, 12x 16T Tippers, 2x CAT Graders",
                productivityRate = 1950.0,
                plannedProductivityRate = 1900.0,
                delayDays = 0,
                delayProbability = 5,
                delayReason = "Soil borrow pit approvals finalized early",
                riskScore = 10,
                durationDays = 44,
                matchConfidence = 99
            ),
            SiteActivity(
                activityId = "ACT-R01-02",
                activityName = "Granular Sub-Base (GSB) & Wet Mix Macadam (WMM)",
                hierarchyLevel = "L6 Work Package",
                hierarchyPath = "Highway Project > Road Package R01 > Pavement Layers > Sub-Base & Base",
                parentActivityId = "ACT-R01-PAVE",
                packageName = "Road Package R01",
                location = "Ch. 0+000 to Ch. 4+200",
                plannedStart = "15 Jul 2026",
                plannedEnd = "20 Aug 2026",
                actualStart = "18 Jul 2026",
                actualEnd = null,
                forecastEnd = "25 Aug 2026",
                plannedQuantity = 24000.0,
                actualQuantity = 19500.0,
                unit = "m³",
                plannedProgress = 90,
                actualProgress = 81,
                status = ActivityStatus.AT_RISK,
                dependencies = listOf("ACT-R01-01"),
                isCriticalPath = false,
                manpower = 20,
                plannedManpower = 24,
                equipment = "1x WMM Paver Finisher, 2x Vibratory Rollers (12T)",
                productivityRate = 610.0,
                plannedProductivityRate = 700.0,
                delayDays = 3,
                delayProbability = 45,
                delayReason = "Aggregate crusher delivery bottleneck from quarry 2",
                riskScore = 48,
                durationDays = 36,
                matchConfidence = 94
            ),
            SiteActivity(
                activityId = "ACT-R01-03",
                activityName = "Dense Bituminous Macadam (DBM) & Asphalt Surface Course",
                hierarchyLevel = "L5 Component",
                hierarchyPath = "Highway Project > Road Package R01 > Pavement Layers > Bituminous Surfacing",
                parentActivityId = "ACT-R01-PAVE",
                packageName = "Road Package R01",
                location = "Ch. 0+000 to Ch. 3+000",
                plannedStart = "22 Aug 2026",
                plannedEnd = "15 Sep 2026",
                actualStart = "24 Aug 2026",
                actualEnd = null,
                forecastEnd = "17 Sep 2026",
                plannedQuantity = 18500.0,
                actualQuantity = 4200.0,
                unit = "MT",
                plannedProgress = 35,
                actualProgress = 22,
                status = ActivityStatus.DELAYED,
                dependencies = listOf("ACT-R01-02"),
                isCriticalPath = true,
                manpower = 26,
                plannedManpower = 30,
                equipment = "1x Vögele Asphalt Sensor Paver, 3x Tandem Steel Rollers, 1x Pneumatic Tire Roller",
                productivityRate = 420.0,
                plannedProductivityRate = 550.0,
                delayDays = 4,
                delayProbability = 60,
                delayReason = "Bitumen emulsion tanker supply delays & wet pavement waiting times",
                riskScore = 65,
                durationDays = 24,
                matchConfidence = 92
            ),

            // --- Drainage Package D02 (Side Drains & Culverts) ---
            SiteActivity(
                activityId = "ACT-D02-01",
                activityName = "Precast Box Culvert Installation (Culvert C1-C4)",
                hierarchyLevel = "L6 Work Package",
                hierarchyPath = "Highway Project > Drainage Package D02 > Cross Drainage > Box Culverts",
                parentActivityId = "ACT-D02-CD",
                packageName = "Drainage Package D02",
                location = "Ch. 3+400 & Ch. 7+200",
                plannedStart = "10 Jun 2026",
                plannedEnd = "15 Jul 2026",
                actualStart = "10 Jun 2026",
                actualEnd = "12 Jul 2026",
                forecastEnd = "12 Jul 2026",
                plannedQuantity = 4.0,
                actualQuantity = 4.0,
                unit = "Nos",
                plannedProgress = 100,
                actualProgress = 100,
                status = ActivityStatus.COMPLETED,
                dependencies = emptyList(),
                isCriticalPath = false,
                manpower = 15,
                plannedManpower = 15,
                equipment = "1x 80T Crane, Precast Yard Delivery Bed",
                productivityRate = 0.12,
                plannedProductivityRate = 0.11,
                delayDays = 0,
                delayProbability = 5,
                delayReason = "Smooth installation ahead of monsoon schedule",
                riskScore = 8,
                durationDays = 35,
                matchConfidence = 99
            ),
            SiteActivity(
                activityId = "ACT-D02-02",
                activityName = "RCC Longitudinal Median Drain Construction",
                hierarchyLevel = "L6 Work Package",
                hierarchyPath = "Highway Project > Drainage Package D02 > Surface Drainage > Median Drain",
                parentActivityId = "ACT-D02-SURF",
                packageName = "Drainage Package D02",
                location = "Ch. 0+000 to Ch. 8+500",
                plannedStart = "20 Jul 2026",
                plannedEnd = "10 Sep 2026",
                actualStart = "22 Jul 2026",
                actualEnd = null,
                forecastEnd = "08 Sep 2026",
                plannedQuantity = 8500.0,
                actualQuantity = 6100.0,
                unit = "LM",
                plannedProgress = 70,
                actualProgress = 71,
                status = ActivityStatus.ON_TRACK,
                dependencies = listOf("ACT-D02-01"),
                isCriticalPath = false,
                manpower = 18,
                plannedManpower = 18,
                equipment = "Slipform Drain Paver, 1x Mini Excavator",
                productivityRate = 180.0,
                plannedProductivityRate = 175.0,
                delayDays = 0,
                delayProbability = 15,
                delayReason = "Slipform casting maintaining strong steady output",
                riskScore = 18,
                durationDays = 52,
                matchConfidence = 96
            )
        )
    }

    fun getInitialRisks(): List<RiskItem> {
        return listOf(
            RiskItem(
                riskId = "RSK-001",
                activityId = "ACT-B04-01",
                activityName = "Pier P12 Concrete Pouring",
                packageName = "Bridge Package B04",
                category = "Critical Path & Productivity",
                probability = 88,
                impactDays = 6,
                impactCostLakhs = 24.5,
                severity = "CRITICAL",
                score = 85,
                cause = "Actual concrete pouring velocity (18.3 m³/day) is 29% below planned benchmark (26 m³/day) due to transit mixer turnaround bottlenecks.",
                mitigation = "Contract 2 additional dedicated 8m³ transit mixers; mobilize secondary night shift casting crew.",
                status = "Active"
            ),
            RiskItem(
                riskId = "RSK-002",
                activityId = "ACT-B04-01",
                activityName = "Pier P12 Concrete Pouring",
                packageName = "Bridge Package B04",
                category = "Weather Disruption",
                probability = 85,
                impactDays = 2,
                impactCostLakhs = 8.0,
                severity = "HIGH",
                score = 72,
                cause = "Heavy monsoon rain predicted tomorrow (85% probability) which will prohibit open deck and high-elevation pier cap concrete pouring.",
                mitigation = "Reschedule casting window to dry morning slot; deploy waterproof tarpaulins and fast-track steel reinforcement fixing during wet hours.",
                status = "Active"
            ),
            RiskItem(
                riskId = "RSK-003",
                activityId = "ACT-R01-03",
                activityName = "Dense Bituminous Macadam (DBM)",
                packageName = "Road Package R01",
                category = "Resource Bottleneck",
                probability = 60,
                impactDays = 4,
                impactCostLakhs = 14.2,
                severity = "HIGH",
                score = 62,
                cause = "Bitumen VG-40 refinery allocation delay from Chennai petroleum terminal causing intermittent asphalt plant shutdown.",
                mitigation = "Issue procurement purchase order to backup refinery supplier in Manali with guaranteed 24h delivery SLA.",
                status = "Active"
            ),
            RiskItem(
                riskId = "RSK-004",
                activityId = "ACT-B04-04",
                activityName = "Deck Slab Segmental Casting Span 12-13",
                packageName = "Bridge Package B04",
                category = "Dependency Cascade",
                probability = 75,
                impactDays = 7,
                impactCostLakhs = 32.0,
                severity = "CRITICAL",
                score = 78,
                cause = "Pier Cap P12 completion delay directly freezes Launching Gantry (LG-01) mobilization and span load transfer.",
                mitigation = "Implement parallel pre-assembly of segmental launching trusses while Pier P12 cures.",
                status = "Active"
            ),
            RiskItem(
                riskId = "RSK-005",
                activityId = "ACT-R01-02",
                activityName = "Granular Sub-Base (GSB) & WMM",
                packageName = "Road Package R01",
                category = "Productivity Problem",
                probability = 45,
                impactDays = 3,
                impactCostLakhs = 6.5,
                severity = "MEDIUM",
                score = 42,
                cause = "Aggregate supply truck transit congestion along bypass section during peak morning hours.",
                mitigation = "Shift aggregate hauling logistics to night hours (22:00 to 06:00) with local toll bypass permits.",
                status = "Mitigating"
            )
        )
    }

    fun getInitialWeatherForecast(): List<WeatherForecastItem> {
        return listOf(
            WeatherForecastItem(
                date = "27 Aug 2026",
                dayName = "Tomorrow",
                condition = "Heavy Rain / Monsoon Storm",
                tempC = 28,
                rainProbability = 85,
                windSpeedKmh = 38,
                affectedActivities = listOf("Pier P12 Concrete Pouring", "Asphalt Laying (DBM) Ch. 0+000"),
                riskLevel = "HIGH",
                expectedDelayDays = 1,
                recommendedAction = "Halt exposed concrete pouring and asphalt laying. Reassign crews to indoor bar bending yard and precast culvert staging under covered sheds.",
                isSimulated = true
            ),
            WeatherForecastItem(
                date = "28 Aug 2026",
                dayName = "Friday",
                condition = "Moderate Showers & High Humidity",
                tempC = 30,
                rainProbability = 60,
                windSpeedKmh = 24,
                affectedActivities = listOf("Sub-Base WMM Laying", "Pier Cap P12 Staging"),
                riskLevel = "MEDIUM",
                expectedDelayDays = 0,
                recommendedAction = "Inspect moisture content in subgrade before rolling WMM. Proceed with heavy crane lifts only after wind gusts drop below 25 km/h.",
                isSimulated = true
            ),
            WeatherForecastItem(
                date = "29 Aug 2026",
                dayName = "Saturday",
                condition = "Clear Skies & Dry Window",
                tempC = 34,
                rainProbability = 15,
                windSpeedKmh = 14,
                affectedActivities = listOf("Pier P12 Casting Window", "Span 12-13 Gantry Assembly"),
                riskLevel = "LOW",
                expectedDelayDays = 0,
                recommendedAction = "HIGH PRODUCTIVITY WINDOW: Execute continuous 14-hour casting shift for Pier P12 with dual transit mixer teams.",
                isSimulated = true
            ),
            WeatherForecastItem(
                date = "30 Aug 2026",
                dayName = "Sunday",
                condition = "Sunny & Warm",
                tempC = 35,
                rainProbability = 10,
                windSpeedKmh = 12,
                affectedActivities = listOf("All Road & Bridge Packages"),
                riskLevel = "LOW",
                expectedDelayDays = 0,
                recommendedAction = "Ideal weather for asphalt sensor paver calibration and longitudinal drain slipforming.",
                isSimulated = true
            )
        )
    }

    fun getInitialRecoveryStrategies(): List<RecoveryStrategy> {
        return listOf(
            RecoveryStrategy(
                id = "REC-01",
                name = "Deploy Secondary Night Shift on Bridge B04",
                category = "Extra Shifts",
                targetActivityOrPackage = "Bridge Package B04 (Pier P12 & Cap)",
                expectedDaysRecovered = 8,
                resourceRequirement = "12 Skilled Bar Benders + 1 Concrete Supervisor + 2 Lighting Towers",
                costImpactLakhs = 18.5,
                riskLevel = "Low",
                advantages = listOf(
                    "Accelerates critical path directly by 38%",
                    "Recovers 8 days before deck span milestone",
                    "Takes advantage of cooler nighttime concrete curing"
                ),
                disadvantages = listOf(
                    "Nighttime overtime rate (+40% wage multiplier)",
                    "Requires certified electrical lighting setup"
                ),
                isApplied = false
            ),
            RecoveryStrategy(
                id = "REC-02",
                name = "Mobilize 25% Additional Manpower & 2 Transit Mixers",
                category = "Manpower & Equipment Boost",
                targetActivityOrPackage = "Bridge Package B04 & Road Package R01",
                expectedDaysRecovered = 6,
                resourceRequirement = "18 Additional Laborers + 2x 8m³ Transit Mixers on monthly lease",
                costImpactLakhs = 14.2,
                riskLevel = "Medium",
                advantages = listOf(
                    "Increases daily pour rate from 18.3 to 27.5 m³/day",
                    "Eliminates batching plant queue standby"
                ),
                disadvantages = listOf(
                    "Congestion on narrow pier access road",
                    "Short-term equipment mobilization lead time (2 days)"
                ),
                isApplied = false
            ),
            RecoveryStrategy(
                id = "REC-03",
                name = "Parallel Pre-assembly of Deck Gantry LG-01",
                category = "Resequencing / Fast-Tracking",
                targetActivityOrPackage = "Span 12-13 Superstructure",
                expectedDaysRecovered = 5,
                resourceRequirement = "1x 100T Hydraulic Crane + 6 Riggers",
                costImpactLakhs = 9.8,
                riskLevel = "Low",
                advantages = listOf(
                    "Overlaps gantry load testing with pier cap curing",
                    "Zero structural dependency violation"
                ),
                disadvantages = listOf(
                    "Requires staging space allocation near pier base"
                ),
                isApplied = false
            ),
            RecoveryStrategy(
                id = "REC-04",
                name = "Authorize Weekend Continuous Paving & Casting",
                category = "Weekend Work",
                targetActivityOrPackage = "Road Package R01 (DBM Paving)",
                expectedDaysRecovered = 4,
                resourceRequirement = "Full asphalt crew + Police traffic diversion permit",
                costImpactLakhs = 11.0,
                riskLevel = "Medium",
                advantages = listOf(
                    "Takes advantage of 60% lighter weekend highway traffic",
                    "Recovers road milestone float"
                ),
                disadvantages = listOf(
                    "Overtime payroll allowance",
                    "Quarry Sunday aggregate supply surcharge"
                ),
                isApplied = false
            )
        )
    }

    fun getInitialAiInsights(): List<AiInsightItem> {
        return listOf(
            AiInsightItem(
                id = "INS-01",
                tier = "CRITICAL",
                title = "Critical Path Alert: Pier P12 Casting Delay",
                message = "Pier P12 concrete pouring is running 6 days behind planned schedule (48% vs 80% planned). If unmitigated, downstream Launching Gantry milestone will slip by 18 days.",
                activityId = "ACT-B04-01",
                actionText = "Simulate Night Shift Recovery"
            ),
            AiInsightItem(
                id = "INS-02",
                tier = "WARNING",
                title = "Monsoon Weather Window Approaching",
                message = "Heavy rainfall (85% probability) forecast for tomorrow. Open deck casting and asphalt paving will face high water contamination risk.",
                activityId = "ACT-B04-01",
                actionText = "View Weather Action Plan"
            ),
            AiInsightItem(
                id = "INS-03",
                tier = "WARNING",
                title = "Bitumen Supply Pipeline Strain",
                message = "Road Package R01 DBM asphalt laying rate dropped by 24% this week due to terminal tanker arrival delays.",
                activityId = "ACT-R01-03",
                actionText = "Switch to Backup Supplier"
            ),
            AiInsightItem(
                id = "INS-04",
                tier = "OPPORTUNITY",
                title = "Fast-Track Substructure Rebar Fixing",
                message = "Precast yard lot 4 is running 2 days ahead of schedule. Rebar crews can be pre-deployed to Pier P13 footing during tomorrow's rain lull.",
                activityId = "ACT-B01-03",
                actionText = "Approve Crew Reallocation"
            )
        )
    }

    fun getInitialDailySummary(): DailySiteSummary {
        return DailySiteSummary(
            date = "26 Aug 2026 (Day 224)",
            executiveSummary = "Project overall progress stands at 59.2% against planned 68.4% (-9.2% schedule variance, 18 days behind baseline). Critical path constraint remains Bridge Package B04 Pier P12 casting. Concrete batching plant output improved slightly to 18.3 m³/day. Weather outlook requires immediate postponement of exposed deck pours tomorrow morning.",
            completedActivities = listOf(
                "ACT-B01-01: Pile Foundation Piles 45-56 (100% complete)",
                "ACT-R01-01: Earthwork Excavation Ch. 0+000-6+500 (100% complete)",
                "ACT-D02-01: Precast Box Culvert Installation C1-C4 (100% complete)"
            ),
            delayedActivities = listOf(
                "ACT-B04-01: Pier P12 Concrete Pouring (-6 days, 48% vs 80%)",
                "ACT-B04-03: Pier Cap P12 Staging (-5 days, 10% vs 30%)",
                "ACT-R01-03: Asphalt DBM Paving (-4 days, 22% vs 35%)"
            ),
            newRisksIdentified = listOf(
                "RSK-002: Tomorrow heavy rain storm (85% rain probability)",
                "RSK-003: Bitumen tanker delivery backlog from Chennai refinery"
            ),
            weatherImpactSummary = "Tomorrow: Heavy rainfall predicted (85% prob, 38 km/h winds). High risk for open concrete casting. Recommend shifting crews to covered precast bar bending yard.",
            resourceIssues = listOf(
                "Bridge B04 active manpower is 18 vs planned 26 (-8 workers)",
                "1x Putzmeister Boom Pump scheduled for hydraulic valve seal check"
            ),
            criticalActivities = listOf(
                "ACT-B04-01: Pier P12 Concrete Pouring (Critical Path)",
                "ACT-B04-04: Deck Slab Segmental Casting Span 12-13 (Critical Path)"
            ),
            tomorrowPriorities = listOf(
                "1. Enforce rain shelter protocols across electrical panels & cement silos.",
                "2. Accelerate Pier P13 rebar cage pre-fabrication in covered yard Lot 2.",
                "3. Finalize approval for Secondary Night Shift on Bridge B04 Pier P12.",
                "4. Mobilize 2 standby transit mixers for Saturday clear weather window."
            )
        )
    }

    fun getSampleDprExtraction(): ExtractedDprItem {
        return ExtractedDprItem(
            id = "EXT-DPR-0826",
            activityId = "ACT-B04-01",
            activityName = "Pier P12 Concrete Pouring",
            packageName = "Bridge Package B04",
            location = "Pier P12, Ch. 14+200",
            plannedQuantity = 450.0,
            executedQuantity = 220.0,
            unit = "m³",
            completionPercent = 48,
            manpower = 18,
            equipment = "1x Putzmeister Boom Pump, 3x Transit Mixers (6m³)",
            startDate = "12 Aug 2026",
            endDate = "01 Sep 2026",
            actualProgress = 48,
            issues = "Transit mixer cycle time delayed by 35 min due to bypass road aggregate truck congestion. 8 workers absent from subcontractor gang.",
            delays = "6 days cumulative delay behind baseline schedule.",
            remarks = "Cube test results for 7-day strength achieved 32.4 MPa (satisfactory for M45 grade). Recommend night shift.",
            confidence = 96,
            matchStatus = "AI Matched to WBS L6",
            hierarchyPath = "Highway Project > Bridge Package B04 > Pier Construction > Pier P12"
        )
    }

    fun getInitialCopilotFaqs(): List<Pair<String, CopilotMessage>> {
        return listOf(
            "Which activities are currently delayed?" to CopilotMessage(
                id = "MSG-FAQ-1",
                isUser = false,
                timestamp = "Just now",
                text = "Currently, 2 activities are critically delayed and 2 are at immediate risk on the Chennai Elevated Highway Project:",
                supportingData = "• ACT-B04-01: Pier P12 Concrete Pouring — 6 days behind (48% actual vs 80% planned)\n• ACT-B04-03: Pier Cap P12 Staging — 5 days behind (10% actual vs 30% planned)\n• ACT-R01-03: Asphalt DBM Paving — 4 days behind (22% actual vs 35% planned)\n• ACT-R01-02: Granular Sub-Base & WMM — 3 days behind (81% actual vs 90% planned)",
                affectedActivities = listOf("ACT-B04-01", "ACT-B04-03", "ACT-R01-03", "ACT-R01-02"),
                reasoning = "The critical bottleneck is Bridge Package B04 Pier P12 where manpower deficit (-8 workers) and transit mixer cycle delays have reduced daily casting speed by 29%.",
                recommendedAction = "Deploy the approved 'Secondary Night Shift' recovery strategy on Bridge B04 and prioritize Pier P12 casting during Saturday's dry weather window."
            ),
            "Why is Bridge Package B04 behind schedule?" to CopilotMessage(
                id = "MSG-FAQ-2",
                isUser = false,
                timestamp = "Just now",
                text = "Bridge Package B04 is running 18 days behind milestone baseline due to 3 compound factors:",
                supportingData = "1. Productivity Gap: Actual pour rate is 18.3 m³/day vs planned 26.0 m³/day (-29% productivity variance).\n2. Manpower Shortage: Current active gang is 18 workers vs 26 planned (-31% labor capacity).\n3. Equipment Waiting Times: Transit mixers are experiencing 35-minute turnaround delays along the bypass access corridor.",
                affectedActivities = listOf("ACT-B04-01", "ACT-B04-03", "ACT-B04-04"),
                reasoning = "Because Pier P12 is on the project's zero-float Critical Path, every day lost directly cascades into Pier Cap P12 and the Launching Gantry Span 12-13 superstructure.",
                recommendedAction = "1. Mobilize 8 additional certified bar benders from standby pool.\n2. Lease 2 dedicated 8m³ transit mixers to eliminate queue bottlenecks.\n3. Add 1 secondary night shift (19:00-03:00) to recover 8 days."
            ),
            "Will the project finish on time?" to CopilotMessage(
                id = "MSG-FAQ-3",
                isUser = false,
                timestamp = "Just now",
                text = "Based on deterministic CPM analysis, the project is currently projected to finish on 18 Dec 2026 — 18 days behind the contractual deadline of 30 Nov 2026.",
                supportingData = "• Baseline Planned Finish: 30 Nov 2026\n• AI Forecast Completion: 18 Dec 2026 (+18 days variance)\n• Overall Progress: 59.2% actual vs 68.4% planned (-9.2%)\n• Critical Path Float: -18 working days",
                affectedActivities = listOf("ACT-B04-01", "ACT-B04-04", "ACT-R01-03"),
                reasoning = "Without recovery intervention, the delay will breach the Q4 milestone. However, activating Recovery Strategy REC-01 (Night Shift) + REC-03 (Parallel Gantry Pre-assembly) will recover 13 of the 18 days, bringing completion within acceptable float.",
                recommendedAction = "Execute the What-If Simulator with 'Add 1 extra shift to Bridge Package B04' and authorize the recovery budget of ₹18.5 Lakhs."
            ),
            "How can we recover 5 days?" to CopilotMessage(
                id = "MSG-FAQ-4",
                isUser = false,
                timestamp = "Just now",
                text = "You can recover 5 to 8 days by executing either of these two high-feasibility recovery strategies:",
                supportingData = "Option A: Deploy Secondary Night Shift on Bridge B04 (Recovers 8 days, Cost: ₹18.5 Lakhs, Risk: Low)\nOption B: Parallel Pre-assembly of Deck Gantry LG-01 (Recovers 5 days, Cost: ₹9.8 Lakhs, Risk: Low)\nOption C: Increase Manpower by 20% across Critical Path (Recovers 6 days, Cost: ₹14.2 Lakhs, Risk: Medium)",
                affectedActivities = listOf("ACT-B04-01", "ACT-B04-04"),
                reasoning = "Parallel pre-assembly of the Launching Gantry requires no structural risk and compresses the setup phase by 5 days with low capital overhead.",
                recommendedAction = "Click on 'Risk & Recovery' tab -> Select 'Parallel Pre-assembly of Deck Gantry LG-01' -> Click 'Apply Recovery Strategy'."
            )
        )
    }

    fun getInitialEvidenceReports(): List<SiteEvidenceReport> {
        return listOf(
            SiteEvidenceReport(
                id = "EV-2026-0827-01",
                projectId = "CHE-HW-2026",
                activityId = "ACT-024",
                activityName = "Column C12 Reinforcement",
                plannedProgress = 90,
                actualProgress = 80,
                progressVariance = -10,
                status = "Delayed",
                delayHours = 2,
                delayDays = 1,
                delayReason = "Rain stoppage (2 hours) halted rebar binding at height",
                observation = "Column C12 reinforcement is 80 percent complete. Rain stopped work for two hours. Rebar cages 80% tied. 16 steel fixers on site.",
                capturedAt = "Today, 08:30 AM",
                source = ReportSource(photo = true, voice = true, text = true),
                aiConfidence = 92,
                isDemo = false,
                visibleElements = listOf("Column Rebar Cage", "Scaffolding", "Tie Wire", "Weather Rain Sheeting"),
                issues = listOf("2h rain downtime", "Tie wire delivery pending"),
                equipment = listOf("1x 30T Crane", "Rebar Bender"),
                materials = listOf("Fe500D Rebar 25mm", "Binding wire"),
                photoUri = null,
                syncedToFirestore = true
            ),
            SiteEvidenceReport(
                id = "EV-2026-0826-02",
                projectId = "CHE-HW-2026",
                activityId = "ACT-B04-01",
                activityName = "Pier P12 Concrete Pouring",
                plannedProgress = 80,
                actualProgress = 48,
                progressVariance = -32,
                status = "Critical",
                delayHours = 6,
                delayDays = 6,
                delayReason = "Batching plant queue turnaround delayed by 35 min per transit mixer",
                observation = "Batching plant mixer maintenance delayed delivery cycles. Total 220 m³ poured out of 450 m³ planned.",
                capturedAt = "Yesterday, 04:15 PM",
                source = ReportSource(photo = true, voice = false, text = true),
                aiConfidence = 96,
                isDemo = true,
                visibleElements = listOf("Putzmeister Concrete Pump", "Transit Mixer 6m³", "Pier Formwork"),
                issues = listOf("Mixer queue turnaround delay", "Manpower deficit"),
                equipment = listOf("Putzmeister Boom Pump", "3x Transit Mixers"),
                materials = listOf("M45 Concrete", "Admixtures"),
                photoUri = null,
                syncedToFirestore = true
            ),
            SiteEvidenceReport(
                id = "EV-2026-0825-03",
                projectId = "CHE-HW-2026",
                activityId = "ACT-R01-03",
                activityName = "Dense Bituminous Macadam (DBM) & Asphalt",
                plannedProgress = 35,
                actualProgress = 22,
                progressVariance = -13,
                status = "Delayed",
                delayHours = 4,
                delayDays = 4,
                delayReason = "Bitumen emulsion tanker supply delays from refinery",
                observation = "Ch. 0+000 to Ch. 1+200 asphalt base course completed. Laying halted due to waiting on VG-40 bitumen tanker delivery.",
                capturedAt = "25 Aug 2026, 11:30 AM",
                source = ReportSource(photo = false, voice = true, text = true),
                aiConfidence = 88,
                isDemo = true,
                visibleElements = listOf("Vögele Asphalt Paver", "Tandem Rollers", "Hot Mix Asphalt"),
                issues = listOf("Refinery tanker delay"),
                equipment = listOf("Vögele Sensor Paver", "Tandem Roller"),
                materials = listOf("VG-40 Bitumen", "Aggregates 20mm/10mm"),
                photoUri = null,
                syncedToFirestore = true
            )
        )
    }

    fun getSampleSpreadsheetRows(): List<SpreadsheetRowItem> {
        return listOf(
            SpreadsheetRowItem(
                rowId = "ROW-1",
                activityId = "ACT-024",
                activityName = "Column C12 Reinforcement",
                location = "Column C12, Ch. 12+800",
                plannedQty = 48.0,
                actualQty = 38.4,
                unit = "MT",
                plannedProgress = 90,
                actualProgress = 80,
                delayHours = 2,
                delayReason = "Rain stoppage (2 hours) + rebar staging wait",
                issues = "2h rain downtime recorded; tie wire delivery pending",
                contractor = "L&T Infrastructure"
            ),
            SpreadsheetRowItem(
                rowId = "ROW-2",
                activityId = "ACT-B04-01",
                activityName = "Pier P12 Concrete Pouring",
                location = "Pier P12, Ch. 14+200",
                plannedQty = 450.0,
                actualQty = 220.0,
                unit = "m³",
                plannedProgress = 80,
                actualProgress = 48,
                delayHours = 6,
                delayReason = "Batching plant queue turnaround delay (35 min per mixer)",
                issues = "8 workers absent from subcontractor gang",
                contractor = "Afcons Infrastructure"
            ),
            SpreadsheetRowItem(
                rowId = "ROW-3",
                activityId = "ACT-R01-03",
                activityName = "Dense Bituminous Macadam (DBM)",
                location = "Ch. 0+000 to Ch. 6+500",
                plannedQty = 12000.0,
                actualQty = 2640.0,
                unit = "MT",
                plannedProgress = 35,
                actualProgress = 22,
                delayHours = 4,
                delayReason = "Bitumen VG-40 refinery allocation delay from Chennai terminal",
                issues = "Tanker arrival delay; asphalt paver standby",
                contractor = "Dilip Buildcon Ltd"
            ),
            SpreadsheetRowItem(
                rowId = "ROW-4",
                activityId = "ACT-D02-01",
                activityName = "Precast Box Culvert Installation",
                location = "Culvert C1-C4, Ch. 4+200",
                plannedQty = 24.0,
                actualQty = 24.0,
                unit = "Units",
                plannedProgress = 100,
                actualProgress = 100,
                delayHours = 0,
                delayReason = "On Schedule / Completed ahead of milestone",
                issues = "None. Quality inspection signoff complete",
                contractor = "L&T Infrastructure"
            )
        )
    }
}

