package com.example.pcbuilderapp

object SpecsPriority {
    fun getPrioritySpecifications(type: String): List<String> {
        return when(type.lowercase()) {
            "cpu" -> listOf(
                "Brand",
                "Cores",
                "Threads",
                "Socket",
                "Base Clock (GHz)",
                "Boost Clock (GHz)",
                "L3 Cache (MB)",
                "TDP (W)"
            )
            "cpu_cooler" -> listOf(
                "Brand",
                "Cooling Type",
                "Socket",
                "Fan Count",
                "Fan Size (mm)",
                "CPU Cooler Height (mm)",
                "TDP (W)"
            )
            "gpu" -> listOf(
                "Brand",
                "GPU Clock (MHz)",
                "Memory Size (GB)",
                "Memory Type",
                "Memory Speed (MHz)",
                "Memory Bus (Bit)",
                "Fan Count",
                "GPU Length (mm)",
                "TDP (W)",
                "Color"
            )
            "motherboard" -> listOf(
                "Brand",
                "Socket",
                "Chipset",
                "Form Factor",
                "Memory Type",
                "Max Memory (GB)",
                "RAM Slots",
                "PCIe x16 Slots"
            )
            "ram" -> listOf(
                "Brand",
                "Capacity (GB)",
                "Memory Type",
                "Module Count",
                "Frequency",
                "Voltage (V)",
                "Color"
            )
            "storage" -> listOf(
                "Brand",
                "Form Factor",
                "Capacity (GB)",
                "Read Speed (MB/s)",
                "Write Speed (MB/s)",
                "Color"
            )
            "psu" -> listOf(
                "Brand",
                "Form Factor",
                "PSU Power (W)",
                "Modular",
                "Efficiency Rating",
                "Voltage (V)",
                "PSU Length (mm)",
                "Color"
            )
            "case" -> listOf(
                "Brand",
                "Form Factor",
                "CPU Cooler Height (mm)",
                "GPU Length (mm)",
                "Radiator Length (mm)",
                "PSU Length (mm)",
                "Included Fans",
                "Case Length (mm)",
                "Case Width (mm)",
                "Case Height (mm)",
                "Color"
            )
            "case_cooler" -> listOf(
                "Brand",
                "Fan Size (mm)",
                "Fan Speed (RPM)",
                "Fan Noise Level (dB)",
                "Color"
            )
            else -> emptyList()
        }
    }
}