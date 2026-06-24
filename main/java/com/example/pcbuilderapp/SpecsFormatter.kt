package com.example.pcbuilderapp

object SpecsFormatter {
    fun formatKey(key: String): String {
        return when (key) {
            "Base Clock (GHz)" -> "Base Clock"
            "Boost Clock (GHz)" -> "Boost Clock"
            "L2 Cache (MB)" -> "L2 Cache"
            "L3 Cache (MB)" -> "L3 Cache"
            "Process Node (nm)" -> "Process Node"
            "Max Memory Speed (MHz)" -> "Max Memory Speed"
            "TDP (W)" -> "TDP"
            "GPU Clock (MHz)" -> "GPU Clock"
            "Memory Size (GB)" -> "Memory Size"
            "Memory Bus (Bit)" -> "Memory Bus"
            "Memory Speed (MHz)" -> "Memory Speed"
            "GPU Length (mm)" -> "GPU Length"
            "Max Memory (GB)" -> "Max Memory"
            "LAN Speed (Gbps)" -> "LAN Speed"
            "Capacity (GB)" -> "Capacity"
            "Module Size (GB)" -> "Capacity"
            "Voltage (V)" -> "Voltage"
            "Read Speed (MB/s)" -> "Read Speed"
            "Write Speed (MB/s)" -> "Write Speed"
            "Power (W)" -> "Power"
            "Length (mm)" -> "Length"
            "Max GPU Length (mm)" -> "Max GPU Length"
            "Max Radiator Size (mm)" -> "Max Radiator Size"
            "Fan Support (mm)" -> "Fan Support"
            "Case Length (mm)" -> "Case Length"
            "Case Width (mm)" -> "Case Width"
            "Case Height (mm)" -> "Case Height"
            "Fan Size (mm)" -> "Fan Size"
            "Fan Speed (RPM)" -> "Fan Speed"
            "Air Flow (CFM)" -> "Air Flow"
            "Fan Noise Level (dB)" -> "Fan Noise Level"
            "Fan Connector (Pin)" -> "Fan Connector"
            "Pump Speed (RPM)" -> "Pump Speed"
            "Radiator Length (mm)" -> "Radiator Length"
            "Radiator Width (mm)" -> "Radiator Width"
            "Tube Length (mm)" -> "Tube Length"
            "Block Length (mm)" -> "Block Length"
            "Block Width (mm)" -> "Block Width"
            "Block Height (mm)" -> "Block Height"
            "Cooler Height (mm)" -> "Cooler Height"
            "Fan Airflow (CFM)" -> "Fan Airflow"
            "Max CPU cooler Height (mm)" -> "Max CPU cooler Height"
            else -> key
        }
    }
    fun formatValue(key: String, value: String): String {
        return when {
            key.contains("(GHz)") -> "$value GHz"
            key.contains("(MB)") -> "$value MB"
            key.contains("(nm)") -> "$value nm"
            key.contains("(MHz)") -> "$value MHz"
            key.contains("(W)") -> "$value W"
            key.contains("(GB)") -> "$value GB"
            key.contains("(Bit)") -> "$value Bit"
            key.contains("(mm)") -> "$value mm"
            key.contains("(Gbps)") -> "$value Gbps"
            key.contains("(V)") -> "$value V"
            key.contains("(MB/s)") -> "$value MB/s"
            key.contains("(RPM)") -> "$value RPM"
            key.contains("(CFM)") -> "$value CFM"
            key.contains("(dB)") -> "$value dB"
            else -> value
        }
    }
}