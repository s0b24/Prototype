package com.example.pcbuilderapp

object ConfigurationAnalysisResults {
    var cpuScore: Double = 0.0
    var gpuScore: Double = 0.0
    var bottleneck: Double = 0.0
    var bottleneckResult: CpuGpuBalanceResults? = null

    fun clear() {
        cpuScore = 0.0
        gpuScore = 0.0
        bottleneck = 0.0
        bottleneckResult = null
    }
}