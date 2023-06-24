package com.example.scarlet.ui.events

sealed interface BlockEvent {
    object EndBlock: BlockEvent
    object AddSession: BlockEvent
}