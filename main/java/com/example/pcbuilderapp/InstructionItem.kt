package com.example.pcbuilderapp

sealed class InstructionItem {
    data class Text(val text: String) : InstructionItem()
    data class Image(val image: Int) : InstructionItem()
}