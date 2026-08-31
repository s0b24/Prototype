package com.example.pcbuilderapp

import android.content.Context
import kotlin.text.toIntOrNull

object CheckCompatibility {
    fun checkAll(context: Context, selected: Map<String, ComponentSpecs>): List<String> {
        val cpu = selected["cpu"]
        val cpu_cooler = selected["cpu_cooler"]
        val motherboard = selected["motherboard"]
        val ram = selected["ram"]
        val gpu = selected["gpu"]
        val storage = selected["storage"]
        val psu = selected["psu"]
        val case = selected["case"]
        val case_cooler = selected["case_cooler"]

        return listOfNotNull(
            checkCpuMotherboard(context, cpu, motherboard),
            checkCpuCpuCooler(context, cpu, cpu_cooler),
            checkGpuMotherboard(context, gpu, motherboard),
            checkRamMotherboard(context, ram, motherboard),
            checkStorageMotherboard(context, storage, motherboard),
            checkPsu(context, psu, selected),
            checkCaseMotherboard(context, case, motherboard),
            checkCase(context, case, gpu, cpu_cooler, psu, case_cooler)
        )
    }

    private fun checkCpuMotherboard(context: Context, cpu: ComponentSpecs?, motherboard: ComponentSpecs?): String? {
        if (cpu == null || motherboard == null) return null

        val cpuSocket = cpu.specs[SpecKeys.Socket]
        val mbSocket = motherboard.specs[SpecKeys.Socket]
        if (cpuSocket != mbSocket)
            return context.getString(R.string.error_cpu_motherboard, cpuSocket, mbSocket)

        return null
    }

    private fun checkCpuCpuCooler(context: Context, cpu: ComponentSpecs?, cpu_cooler: ComponentSpecs?): String? {
        if (cpu == null || cpu_cooler == null) return null

        val cpuSocket = cpu.specs[SpecKeys.Socket]
        val supportedSockets = cpu_cooler.specs[SpecKeys.Socket]?.split(",")?.map { it.trim() } ?: emptyList()
        if (cpuSocket == null || cpuSocket !in supportedSockets)
            return context.getString(R.string.error_cpu_cpuCooler, cpuSocket, supportedSockets)

        return null
    }

    private fun checkGpuMotherboard(context: Context, gpu: ComponentSpecs?, motherboard: ComponentSpecs?): String? {
        if (gpu == null || motherboard == null) return null

        val gpuSlots = motherboard.specs[SpecKeys.PCIe_Slots]?.toIntOrNull() ?: 0
        if (gpuSlots <= 0)
            return context.getString(R.string.error_gpu_motherboard)

        return null
    }

    private fun checkRamMotherboard(context: Context, ram: ComponentSpecs?, motherboard: ComponentSpecs?): String? {
        if (ram == null || motherboard == null) return null

        val ramType = ram.specs[SpecKeys.Memory_Type]
        val mbRamType = motherboard.specs[SpecKeys.Memory_Type]
        if (ramType != mbRamType)
            return context.getString(R.string.error_ram_motherboard_1, ramType, mbRamType)

        val ramModules = ram.specs[SpecKeys.RAM_Module_Count]?.toIntOrNull() ?: 0
        val ramSlots = motherboard.specs[SpecKeys.RAM_Slots]?.toIntOrNull() ?: 0
        if (ramModules > ramSlots)
            return context.getString(R.string.error_ram_motherboard_2)

        val ramCapacity = ram.specs[SpecKeys.Ram_Capacity]?.toIntOrNull() ?: 0
        val mbMaxRamCapacity = motherboard.specs[SpecKeys.Motherboard_Max_Memory_Capacity]?.toIntOrNull() ?: 0
        if (ramCapacity > mbMaxRamCapacity)
            return context.getString(R.string.error_ram_motherboard_3, ramCapacity, mbMaxRamCapacity)

        return null
    }

    private fun checkStorageMotherboard(context: Context, storage: ComponentSpecs?, motherboard: ComponentSpecs?): String? {
        if (storage == null || motherboard == null) return null

        val storageFormFactor = storage.specs[SpecKeys.Form_Factor] ?: return null

        return when {
            storageFormFactor.contains("M.2",true) -> {
                val m2Slots = motherboard.specs[SpecKeys.M2_Slots]?.toIntOrNull() ?: 0
                if (m2Slots <= 0)
                    context.getString(R.string.error_storage_motherboard_1) else null
            }

            storageFormFactor.contains("2.5") || storageFormFactor.contains("3.5") -> {
                val sata = motherboard.specs[SpecKeys.SATA_Slots]?.toIntOrNull() ?: 0
                if (sata < 1)
                    context.getString(R.string.error_storage_motherboard_2) else null
            }

            else -> context.getString(R.string.error_storage_motherboard_3)
        }
    }

    private fun checkPsu(context: Context, psu: ComponentSpecs?, selectedComponentsTdp: Map<String, ComponentSpecs>): String? {
        if (psu == null) return null

        val totalTdp = selectedComponentsTdp.values.sumOf { it.tdp }
        val psuPower = psu.specs[SpecKeys.PSU_Power]?.toIntOrNull() ?: 0
        val requiredPower = (totalTdp * 1.3).toInt()
        if (psuPower < requiredPower)
            // required power at 30% higher than total tdp
            return context.getString(R.string.error_psu, psuPower, requiredPower, totalTdp)

        return null
    }

    private fun checkCaseMotherboard(context: Context, case: ComponentSpecs?, motherboard: ComponentSpecs?): String? {
        if (case == null || motherboard == null) return null

        val mbFormFactor = motherboard.specs[SpecKeys.Form_Factor]
        val supportedMb = case.specs[SpecKeys.Case_Motherboard_Form_Factor]?.split(",")?.map { it.trim() } ?: emptyList()
        if (mbFormFactor == null ||  mbFormFactor !in supportedMb)
            return context.getString(R.string.error_case_motherboard, mbFormFactor)

        return null
    }

    private fun checkCase(
        context: Context,
        case: ComponentSpecs?,
        gpu: ComponentSpecs?,
        cpu_cooler: ComponentSpecs?,
        psu: ComponentSpecs?,
        case_cooler: ComponentSpecs?): String? {

        if (case == null) return null

        gpu?.let {
            val gpuLength = it.specs[SpecKeys.GPU_Length]?.toIntOrNull() ?: 0
            val supportedLength = case.specs[SpecKeys.GPU_Length]?.toIntOrNull() ?: 0
            if (gpuLength > supportedLength)
                return context.getString(R.string.error_case_1, gpuLength, supportedLength)
        }

        cpu_cooler?.let {
            val cpuCoolerHeight = it.specs[SpecKeys.CPU_Cooler_Height]?.toIntOrNull() ?: 0
            val supportedHeight = case.specs[SpecKeys.CPU_Cooler_Height]?.toIntOrNull() ?: 0
            if (cpuCoolerHeight > supportedHeight)
                return context.getString(R.string.error_case_2, cpuCoolerHeight)

            val radiatorLength = it.specs[SpecKeys.Radiator_Length]?.toIntOrNull() ?: 0
            val supportedLength = case.specs[SpecKeys.Radiator_Length]?.toIntOrNull() ?: 0
            if (radiatorLength > supportedLength)
                return context.getString(R.string.error_case_3, radiatorLength, supportedLength)
        }

        psu?.let {
            val psuLength = it.specs[SpecKeys.PSU_Length]?.toIntOrNull() ?: 0
            val supportedLength = case.specs[SpecKeys.PSU_Length]?.toIntOrNull() ?: 0
            if (psuLength > supportedLength)
                return context.getString(R.string.error_case_4)
        }

        case_cooler?.let {
            val fanSize = it.specs[SpecKeys.Fan_Size]?. toIntOrNull() ?: 0
            val supportedFanSize = case.specs[SpecKeys.Fan_Size]?.split(",")?.mapNotNull { it.trim().toIntOrNull() } ?: emptyList()
            if (fanSize !in supportedFanSize)
                return context.getString(R.string.error_case_5)
        }

        return null
    }
}