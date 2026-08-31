package com.example.pcbuilderapp

import android.content.Context

object InstructionData {
    fun getSteps(context: Context) = listOf(
        InstructionStep(
            title = context.getString(R.string.preparation),
            items = listOf(
                InstructionItem.Text(context.getString(R.string.preparation_step_1)),
                InstructionItem.Image(R.drawable.prepare_step_1),
                InstructionItem.Text(context.getString(R.string.preparation_step_2))
            )
        ),
        InstructionStep(
            title = context.getString(R.string.cpu_installation),
            items = listOf(
                InstructionItem.Text(context.getString(R.string.cpu_installation_step_1)),
                InstructionItem.Text(context.getString(R.string.cpu_installation_step_2)),
                InstructionItem.Text(context.getString(R.string.cpu_installation_step_3)),
                InstructionItem.Image(R.drawable.cpu_step_1),
                InstructionItem.Text(context.getString(R.string.cpu_installation_step_4)),
                InstructionItem.Image(R.drawable.cpu_step_2),
                InstructionItem.Text(context.getString(R.string.cpu_installation_step_5)),
                InstructionItem.Image(R.drawable.cpu_step_3),
                InstructionItem.Text(context.getString(R.string.cpu_installation_step_6))
            )
        ),
        InstructionStep(
            title = context.getString(R.string.ssdM2_installation),
            items = listOf(
                InstructionItem.Text(context.getString(R.string.ssdM2_installation_step_1)),
                InstructionItem.Text(context.getString(R.string.ssdM2_installation_step_2)),
                InstructionItem.Image(R.drawable.m2_step_1),
                InstructionItem.Image(R.drawable.m2_step_1_2),
                InstructionItem.Text(context.getString(R.string.ssdM2_installation_step_3)),
                InstructionItem.Image(R.drawable.m2_step_2),
                InstructionItem.Image(R.drawable.m2_step_3),
                InstructionItem.Text(context.getString(R.string.ssdM2_installation_step_4)),
                InstructionItem.Image(R.drawable.m2_step_4)
                )
        ),
        InstructionStep(
            title = context.getString(R.string.cpu_cooler_installation),
            items = listOf(
                InstructionItem.Text(context.getString(R.string.ssdM2_installation_step_1)),
                InstructionItem.Image(R.drawable.cpu_cooler_step_1),
                InstructionItem.Text(context.getString(R.string.ssdM2_installation_step_2)),
                InstructionItem.Image(R.drawable.cpu_cooler_step_2),
                InstructionItem.Text(context.getString(R.string.ssdM2_installation_step_3)),
                InstructionItem.Image(R.drawable.cpu_cooler_step_3),
                InstructionItem.Text(context.getString(R.string.ssdM2_installation_step_4)),
                InstructionItem.Image(R.drawable.cpu_cooler_step_4)
                )
        ),
        InstructionStep(
            title = context.getString(R.string.ram_installation),
            items = listOf(
                InstructionItem.Text(context.getString(R.string.ram_installation_step_1)),
                InstructionItem.Text(context.getString(R.string.ram_installation_step_2)),
                InstructionItem.Text(context.getString(R.string.ram_installation_step_3)),
                InstructionItem.Image(R.drawable.ram_step_1),
                InstructionItem.Text(context.getString(R.string.ram_installation_step_4)),
                InstructionItem.Image(R.drawable.ram_step_2)
            )
        ),
        InstructionStep(
            title = context.getString(R.string.motherboard_installation),
            items = listOf(
                InstructionItem.Text(context.getString(R.string.motherboard_installation_step_1)),
                InstructionItem.Image(R.drawable.motherboard_step_1),
                InstructionItem.Text(context.getString(R.string.motherboard_installation_step_2)),
                InstructionItem.Image(R.drawable.motherboard_step_2),
                InstructionItem.Text(context.getString(R.string.motherboard_installation_step_3)),
                InstructionItem.Image(R.drawable.motherboard_step_3),
                InstructionItem.Text(context.getString(R.string.motherboard_installation_step_3_1)),
                InstructionItem.Text(context.getString(R.string.motherboard_installation_step_4)),
                InstructionItem.Image(R.drawable.motherboard_step_4),
                InstructionItem.Text(context.getString(R.string.motherboard_installation_step_5)),
                InstructionItem.Image(R.drawable.motherboard_step_5),
                InstructionItem.Text(context.getString(R.string.motherboard_installation_step_6)),
                InstructionItem.Text(context.getString(R.string.motherboard_installation_step_6_1)),
                InstructionItem.Text(context.getString(R.string.motherboard_installation_step_6_2)),
                InstructionItem.Text(context.getString(R.string.motherboard_installation_step_6_3)),
                InstructionItem.Image(R.drawable.motherboard_step_6),
                InstructionItem.Image(R.drawable.motherboard_step_7)
            )
        ),
        InstructionStep(
            title = context.getString(R.string.ssd_hdd_installation),
            items = listOf(
                InstructionItem.Text(context.getString(R.string.ssd_hdd_installation_step_1)),
                InstructionItem.Image(R.drawable.storage_step_1),
                InstructionItem.Image(R.drawable.storage_step_2),
                InstructionItem.Text(context.getString(R.string.ssd_hdd_installation_step_2)),
                InstructionItem.Text(context.getString(R.string.ssd_hdd_installation_step_3)),
                InstructionItem.Image(R.drawable.storage_step_3),
                InstructionItem.Image(R.drawable.storage_step_4)
                )
        ),
        InstructionStep(
            title = context.getString(R.string.case_cooler_installation),
            items = listOf(
                InstructionItem.Text(context.getString(R.string.case_cooler_installation_step_1)),
                InstructionItem.Text(context.getString(R.string.case_cooler_installation_step_1)),
                InstructionItem.Image(R.drawable.case_cooler_step_1),
                InstructionItem.Image(R.drawable.case_cooler_step_2)
            )
        ),
        InstructionStep(
            title = context.getString(R.string.psu_installation),
            items = listOf(
                InstructionItem.Text(context.getString(R.string.psu_installation_step_1)),
                InstructionItem.Image(R.drawable.psu_step_1),
                InstructionItem.Text(context.getString(R.string.psu_installation_step_2))
            )
        ),
        InstructionStep(
            title = context.getString(R.string.cables_connection),
            items = listOf(
                InstructionItem.Text(context.getString(R.string.cables_connection_step_1)),
                InstructionItem.Image(R.drawable.psu_cables_step_1),
                InstructionItem.Text(context.getString(R.string.cables_connection_step_2)),
                InstructionItem.Image(R.drawable.psu_cables_step_2),
                InstructionItem.Text(context.getString(R.string.cables_connection_step_3)),
                InstructionItem.Image(R.drawable.psu_cables_step_3),
                InstructionItem.Text(context.getString(R.string.cables_connection_step_4))
            )
        ),
        InstructionStep(
            title = context.getString(R.string.gpu_installation),
            items = listOf(
                InstructionItem.Text(context.getString(R.string.gpu_installation_step_1)),
                InstructionItem.Image(R.drawable.gpu_step_1),
                InstructionItem.Image(R.drawable.gpu_step_2),
                InstructionItem.Text(context.getString(R.string.gpu_installation_step_2)),
                InstructionItem.Text(context.getString(R.string.gpu_installation_step_3)),
                InstructionItem.Image(R.drawable.gpu_step_3),
                InstructionItem.Image(R.drawable.gpu_step_4),
                InstructionItem.Text(context.getString(R.string.gpu_installation_step_4)),
                InstructionItem.Image(R.drawable.gpu_step_5),
            )
        ),
    )
}