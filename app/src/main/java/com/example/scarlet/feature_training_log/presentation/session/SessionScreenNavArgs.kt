package com.example.scarlet.feature_training_log.presentation.session

import com.example.scarlet.feature_training_log.domain.model.Block
import com.example.scarlet.feature_training_log.domain.model.Session

data class SessionScreenNavArgs(
    val session: Session,
    val block: Block
)