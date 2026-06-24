package com.example.pcbuilderapp

object SpecsPriority {

    fun getPrioritySpecifications(type: String): List<String> {
        return when(type.lowercase()) {
            "cpu" -> listOf(
                "Cores",
                "Threads",
                "Socket",
                "Base Clock (GHz)",
                "Boost Clock (GHz)",
                "L3 Cache (MB)",
                "TDP (W)"
            )
            "cpu_cooler" -> listOf(
                "Cooling Type",
                "Fan Count",
                "Fan Size (mm)",
                "Fan Speed (RPM)",
                "Fan Noise Level (dB)",
                "TDP (W)"
            )
            "gpu" -> listOf(
                "GPU Clock (MHz)",
                "Memory Size (GB)",
                "Memory Type",
                "Memory Speed (MHz)",
                "Memory Bus (Bit)",
                "GPU Length (mm)",
                "TDP (W)"
            )
            "motherboard" -> listOf(
                "Socket",
                "Chipset",
                "Form Factor",
                "Memory Type",
                "Max Memory (GB)",
                "RAM Slots",
                "PCIe x16 Slots"
            )
            "ram" -> listOf(
                "Capacity (GB)",
                "Module Count",
                "Module Size (GB)",
                "Type"
            )
            "storage" -> listOf(
                "Form Factor",
                "Capacity (GB)",
                "Read Speed (MB/s)",
                "Write Speed (MB/s)"
            )
            "psu" -> listOf(
                "Form Factor",
                "Power (W)",
                "Modular",
                "Efficiency Rating",
                "Voltage (V)"
            )
            "case" -> listOf(
                "Form Factor",
                "Max CPU cooler Height (mm)",
                "Max GPU Length (mm)",
                "Included Fans",
                "Case Length (mm)",
                "Case Width (mm)",
                "Case Height (mm)"
            )
            "case_cooler" -> listOf(
                "Fan Size (mm)",
                "Fan Speed (RPM)",
                "Fan Noise Level (dB)"
            )
            else -> emptyList()
        }
    }
}