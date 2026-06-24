package com.example.pcbuilderapp

import kotlin.text.toIntOrNull

object CompatibilityCheck {
    fun checkAll(selected: Map<String, ComponentSpecs>): List<String> {
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
            checkCpuMotherboard(cpu, motherboard),
            checkRamMotherboard(ram, motherboard),
            checkCpuCpuCooler(cpu, cpu_cooler),
            checkGpuMotherboard(gpu, motherboard),
            checkStorageMotherboard(storage, motherboard),
            checkCaseMotherboard(case, motherboard),
            checkPsu(psu, selected),
            checkCase(case, gpu, cpu_cooler, psu, case_cooler)
        )
    }

    private fun checkCpuMotherboard(cpu: ComponentSpecs?, motherboard: ComponentSpecs?): String? {
        if (cpu == null || motherboard == null) return null

        val cpuSocket = cpu.specs[SpecKeys.Socket]
        val mbSocket = motherboard.specs[SpecKeys.Socket]
        if (cpuSocket != mbSocket)
            return "CPU ligzda $cpuSocket nav savietojama ar mātesplates ligzdu $mbSocket."

        return null
    }

    private fun checkRamMotherboard(ram: ComponentSpecs?, motherboard: ComponentSpecs?): String? {
        if (ram == null || motherboard == null) return null

        val ramType = ram.specs[SpecKeys.Memory_type]
        val mbRamType = motherboard.specs[SpecKeys.Memory_type]
        if (ramType != mbRamType)
            return "RAM tips $ramType nav savietojams ar mātesplates atbalstīto atmiņas tipu $mbRamType."

        val ramModules = ram.specs[SpecKeys.RAM_Module_Count]?.toIntOrNull() ?: 0
        val ramSlots = motherboard.specs[SpecKeys.RAM_Slots]?.toIntOrNull() ?: 0
        if (ramModules > ramSlots)
            return "Mātesplatē nav pietiekami daudz RAM ligzdas skaits."

        return null
    }

    private fun checkCpuCpuCooler(cpu: ComponentSpecs?, cpu_cooler: ComponentSpecs?): String? {
        if (cpu == null || cpu_cooler == null) return null

        val cpuSocket = cpu.specs[SpecKeys.Socket]
        val supportedSockets = cpu_cooler.specs[SpecKeys.Socket]?.split(",")?.map { it.trim() } ?: emptyList()
        if (cpuSocket == null || cpuSocket !in supportedSockets)
            return "CPU ligzda $cpuSocket nav savietojama ar dzesētāja ligzdu $supportedSockets."

        return null
    }

    private fun checkGpuMotherboard(gpu: ComponentSpecs?, motherboard: ComponentSpecs?): String? {
        if (gpu == null || motherboard == null) return null

        val gpuSlots = motherboard.specs[SpecKeys.PCIe_Slots]?.toIntOrNull() ?: 0
        if (gpuSlots <= 0)
            return "Mātesplatē nav nodrošināts PCIe x16 savienojums."

        return null
    }

    private fun checkStorageMotherboard(storage: ComponentSpecs?, motherboard: ComponentSpecs?): String? {
        if (storage == null || motherboard == null) return null

        val storageFormFactor = storage.specs[SpecKeys.Form_Factor] ?: return null

        return when {
            storageFormFactor.contains("M.2",true) -> {
                val m2Slots = motherboard.specs[SpecKeys.M2_Slots]?.toIntOrNull() ?: 0
                if (m2Slots <= 0)
                    "Mātesplate neatbalsta SSD M.2." else null
            }

            storageFormFactor.contains("2.5") || storageFormFactor.contains("3.5") -> {
                val sata = motherboard.specs[SpecKeys.SATA_Slots]?.toIntOrNull() ?: 0
                if (sata < 1)
                    "Mātesplatē SATA savienojums nav pieejams." else null
            }

            else -> "Nepiedāvātais SSD/HDD formfaktors."
        }
    }

    private fun checkCaseMotherboard(case: ComponentSpecs?, motherboard: ComponentSpecs?): String? {
        if (case == null || motherboard == null) return null

        val mbFormFactor = motherboard.specs[SpecKeys.Form_Factor]
        val supportedMb = case.specs[SpecKeys.Case_Motherboard_Form_Factor]?.split(",")?.map { it.trim() } ?: emptyList()
        if (mbFormFactor == null ||  mbFormFactor !in supportedMb)
            return "Mātesplates formfaktors $mbFormFactor nav savietojamas ar datora korpusa formfaktoru."

        return null
    }

    private fun checkPsu(psu: ComponentSpecs?, selectedComponentsTdp: Map<String, ComponentSpecs>): String? {
        if (psu == null) return null

        val totalTdp = selectedComponentsTdp.values.sumOf { it.tdp }
        val psuPower = psu.specs[SpecKeys.PSU_Power]?.toIntOrNull() ?: 0
        if (psuPower < totalTdp * 1.3)
            return "PSU jauda $psuPower W nav pietiekama, komponentu kopējo enerģijas patēriņš ir $totalTdp W." // required power at 30% higher than total tdp

        return null
    }

    private fun checkCase(
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
                return "GPU garums $gpuLength mm neatbilst korpusa izmēriem, maksimālais GPU garums $supportedLength mm."
        }

        cpu_cooler?.let {
            val cpuCoolerHeight = it.specs[SpecKeys.CPU_Cooler_Height]?.toIntOrNull() ?: 0
            val supportedHeight = case.specs[SpecKeys.CPU_Cooler_Height]?.toIntOrNull() ?: 0
            if (cpuCoolerHeight > supportedHeight)
                return "CPU dzesētāja augstums $cpuCoolerHeight mm pārsniedz korpusam paredzētos maksimālos dzesētāja izmērus."

            val radiatorLength = it.specs[SpecKeys.Radiator_Length]?.toIntOrNull() ?: 0
            val supportedLength = case.specs[SpecKeys.Radiator_Length]?.toIntOrNull() ?: 0
            if (radiatorLength > supportedLength)
                return "CPU radiators $radiatorLength mm pārsniedz korpusam paredzētos maksimālos radiatoru izmērus $supportedLength."
        }

        psu?.let {
            val psuLength = it.specs[SpecKeys.PSU_Length]?.toIntOrNull() ?: 0
            val supportedLength = case.specs[SpecKeys.PSU_Length]?.toIntOrNull() ?: 0
            if (psuLength > supportedLength)
                return "PSU garums ir ļoti liels."
        }

        case_cooler?.let {
            val fanSize = it.specs[SpecKeys.Fan_Size]?. toIntOrNull() ?: 0
            val supportedFanSize = case.specs[SpecKeys.Fan_Size]?.split(",")?.mapNotNull { it.trim().toIntOrNull() } ?: emptyList()
            if (fanSize !in supportedFanSize)
                return "Dzesētāja izmērs neatbilst korpusa pieļaujamajiem izmēriem."
        }

        return null
    }
}