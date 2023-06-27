package com.example.scarlet.feature_training_log.presentation.session.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.scarlet.feature_training_log.domain.model.Session

@Composable
fun SessionHeader(
    session: Session
) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = session.date,
        fontSize = 20.sp,
        textAlign = TextAlign.Center
    )
}


@Preview
@Composable
fun HeaderPreview() {
    SessionHeader(
        session = Session(date = "2021-09-01")
    )
}