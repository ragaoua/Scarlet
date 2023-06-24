package com.example.scarlet.events

sealed interface BlockEvent {
    object EndBlock: BlockEvent
    object AddSession: BlockEvent
}