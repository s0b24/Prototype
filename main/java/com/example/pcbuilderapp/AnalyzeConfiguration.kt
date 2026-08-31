package com.example.pcbuilderapp

import android.content.Context
import kotlin.text.toDoubleOrNull

class AnalyzeConfiguration(private val context: Context) {
    fun calculateCpuScore(specs: Map<String, String>, maxValues: Map<String, Double>): Double {
        val cores = specs[SpecKeys.CPU_Cores]?.toDoubleOrNull() ?: 0.0
        val threads = specs[SpecKeys.CPU_Threads]?.toDoubleOrNull() ?: 0.0
        val boostClock = specs[SpecKeys.CPU_Boost_Clock]?.toDoubleOrNull() ?: 0.0

        val maxCores = maxValues[SpecKeys.CPU_Cores] ?: 0.0
        val maxThreads = maxValues[SpecKeys.CPU_Threads] ?: 0.0
        val maxBoostClock = maxValues[SpecKeys.CPU_Boost_Clock] ?: 0.0

        return ((cores / maxCores) + (threads / maxThreads) + (boostClock / maxBoostClock)) / 3 * 100
    }

    fun calculateGpuScore(specs: Map<String, String>, maxValues: Map<String, Double>): Double {
        val clock = specs[SpecKeys.GPU_Clock]?.toDoubleOrNull() ?: 0.0
        val memoryCapacity = specs[SpecKeys.GPU_Memory_Capacity]?.toDoubleOrNull() ?: 0.0
        val memorySpeed = specs[SpecKeys.GPU_Memory_Speed]?.toDoubleOrNull() ?: 0.0
        val memoryBus = specs[SpecKeys.GPU_Memory_Bus]?.toDoubleOrNull() ?: 0.0

        val maxClock = maxValues[SpecKeys.GPU_Clock] ?: 0.0
        val maxMemoryCapacity = maxValues[SpecKeys.GPU_Memory_Capacity] ?: 0.0
        val maxMemorySpeed = maxValues[SpecKeys.GPU_Memory_Speed] ?: 0.0
        val maxMemoryBus = maxValues[SpecKeys.GPU_Memory_Bus] ?: 0.0

        return ((clock / maxClock) + (memoryCapacity / maxMemoryCapacity) + (memorySpeed / maxMemorySpeed) + (memoryBus / maxMemoryBus)) / 4 * 100
    }

    fun calculateRamScore(specs: Map<String, String>, maxValues: Map<String, Double>): Double {
        val capacity = specs[SpecKeys.Ram_Capacity]?.toDoubleOrNull() ?: 0.0
        val frequency = specs[SpecKeys.Ram_Frequency]?.toDoubleOrNull() ?: 0.0
        val cl = specs[SpecKeys.Ram_CL]?.toDoubleOrNull() ?: 0.0

        val maxCapacity = maxValues[SpecKeys.Ram_Capacity] ?: 0.0
        val maxFrequency = maxValues[SpecKeys.Ram_Frequency] ?: 0.0
        val maxCl = maxValues[SpecKeys.Ram_CL] ?: 0.0

        return ((capacity / maxCapacity) * ((frequency / maxFrequency) + (cl / maxCl))) / 3 * 100
    }

    fun calculateCpuGpuBottleneck(cpuScore: Double, gpuScore: Double): Double {
        val strongerComponentScore = maxOf(cpuScore, gpuScore)
        val weakerComponentScore = minOf(cpuScore, gpuScore)

        return ((strongerComponentScore - weakerComponentScore) / strongerComponentScore) * 100
    }

    fun getBottleneckStatus(bottleneck: Double): CpuGpuBalanceResults {
        return when {
            bottleneck >= 0.0 && bottleneck <= 5.0 -> CpuGpuBalanceResults(
                context.getString(R.string.balance_status_1),
                context.getString(R.string.balance_description_1)
            )

            bottleneck >= 5.0 && bottleneck <= 15.0 -> CpuGpuBalanceResults(
                context.getString(R.string.balance_status_2),
                context.getString(R.string.balance_description_2)
            )

            bottleneck >= 15.0 && bottleneck <= 30.0 -> CpuGpuBalanceResults(
                context.getString(R.string.balance_status_3),
                context.getString(R.string.balance_description_3)
            )

            else -> CpuGpuBalanceResults(
                context.getString(R.string.balance_status_4),
                context.getString(R.string.balance_description_4)
            )

        }
    }

    fun getComponentsRecommendation(
        db: DatabaseHelper,
        selected: Map<String, ComponentSpecs>,
        countryCode: String,
        cpuBudget: Double?,
        gpuBudget: Double?,
        ramBudget: Double?
    ): List<ComponentCard> {
        val result = mutableListOf<ComponentCard>()
        val case = selected["case"]
        val maxCpuValues = db.getMaxCpuSpecsValues()
        val maxGpuValues = db.getMaxGpuSpecsValues()
        val maxRamValues = db.getMaxRamSpecsValues()
        var bestCpu: ComponentCard? = null
        var bestGpu: ComponentCard? = null
        var bestRam: ComponentCard? = null
        var bestCpuScore = 0.0
        var bestGpuScore = 0.0
        var bestRamScore = 0.0

        selected.values.forEach { selectedComponent ->
            val components = db.getComponentByType(selectedComponent.type)

            when (selectedComponent.type) {
                "cpu" -> {
                    val selectedComponentScore = calculateCpuScore(selectedComponent.specs, maxCpuValues)
                    bestCpuScore = selectedComponentScore
                    val selectedCpuSocket = selectedComponent.specs[SpecKeys.Socket]

                    for (component in components) {
                        if (component.id == selectedComponent.id) {
                            continue
                        }

                        val specs = db.getComponentSpecs(component.id)
                        val price = db.getAveragePriceByCountry(component.id, countryCode) ?: 0.0
                        if (cpuBudget != null && price > cpuBudget) {
                            continue
                        }

                        val newComponent = ComponentCard(
                            id = component.id,
                            type = component.type,
                            name = component.name,
                            specs = specs,
                            averagePrice = price
                        )

                        val socket = newComponent.specs[SpecKeys.Socket]
                        if (socket != selectedCpuSocket) {
                            continue
                        }

                        val newScore = calculateCpuScore(newComponent.specs, maxCpuValues)
                        if (newScore > bestCpuScore) {
                            bestCpuScore = newScore
                            bestCpu = newComponent
                        }
                    }
                }

                "gpu" -> {
                    val selectedComponentScore = calculateCpuScore(selectedComponent.specs, maxCpuValues)
                    bestGpuScore = selectedComponentScore
                    val supportedLength = case?.specs?.get(SpecKeys.GPU_Length)?.toIntOrNull() ?: 500

                    for (component in components) {
                        if (component.id == selectedComponent.id) {
                            continue
                        }

                        val specs = db.getComponentSpecs(component.id)
                        val price = db.getAveragePriceByCountry(component.id, countryCode) ?: 0.0
                        if (gpuBudget != null && price > gpuBudget) {
                            continue
                        }

                        val newComponent = ComponentCard(
                            id = component.id,
                            type = component.type,
                            name = component.name,
                            specs = specs,
                            averagePrice = price
                        )

                        val length = newComponent.specs[SpecKeys.GPU_Length]?.toIntOrNull() ?: 0
                        if (length > supportedLength) {
                            continue
                        }

                        val newScore = calculateGpuScore(newComponent.specs, maxGpuValues)
                        if (newScore > bestGpuScore) {
                            bestGpuScore = newScore
                            bestGpu = newComponent
                        }
                    }
                }

                "ram" -> {
                    val selectedComponentScore = calculateRamScore(selectedComponent.specs, maxRamValues)
                    bestRamScore = selectedComponentScore
                    val selectedRamMemoryType = selectedComponent.specs[SpecKeys.Memory_Type]

                    for (component in components) {
                        if (component.id == selectedComponent.id) {
                            continue
                        }

                        val specs = db.getComponentSpecs(component.id)
                        val price = db.getAveragePriceByCountry(component.id, countryCode) ?: 0.0
                        if (ramBudget != null && price > ramBudget) {
                            continue
                        }

                        val newComponent = ComponentCard(
                            id = component.id,
                            type = component.type,
                            name = component.name,
                            specs = specs,
                            averagePrice = price
                        )

                        val memoryType = newComponent.specs[SpecKeys.Memory_Type]
                        if (memoryType != selectedRamMemoryType) {
                            continue
                        }

                        val newScore = calculateRamScore(newComponent.specs, maxRamValues)
                        if (newScore > bestRamScore) {
                            bestRamScore = newScore
                            bestRam = newComponent
                        }
                    }
                }
            }
        }
        bestCpu?.let { result.add(it) }
        bestGpu?.let { result.add(it) }
        bestRam?.let { result.add(it) }
        return result
    }
}