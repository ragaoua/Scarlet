package com.example.scarlet.feature_training_log.presentation.core.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.scarlet.R

/**
 * Bottom sheet that allows the user to create or edit a block.
 *
 * @param sheetState the state of the sheet
 * @param onBlockNameValueChange callback that is called when the block name value changes
 * @param onMicroCycleSettingsToggle callback that is called when the user toggles the micro cycle
 * settings
 * @param onDaysPerMicroCycleValueChange callback that is called when the user changes the number
 * of days per micro cycle
 * @param onDismissRequest callback that is called when the user dismisses the sheet
 * @param onValidate callback that is called when the user clicks the validation button
 *
 * @see AddEditBlockSheetState
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditBlockSheet(
    sheetState: AddEditBlockSheetState,
    onBlockNameValueChange: (String) -> Unit,
    onMicroCycleSettingsToggle: () -> Unit,
    onDaysPerMicroCycleValueChange: (Int) -> Unit,
    onDismissRequest: () -> Unit,
    onValidate: (String) -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            /*************************************************************************
             * Sheet title
             *************************************************************************/
            Text(
                text = stringResource(R.string.new_block),
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            HorizontalDivider(modifier = Modifier.width(64.dp))

            Spacer(modifier = Modifier.height(32.dp))

            /*************************************************************************
             * Block name text field
             *************************************************************************/
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = sheetState.blockName,
                onValueChange = onBlockNameValueChange,
                label = {
                    Text(stringResource(R.string.block_name))
                    // TODO set the color to a lighter one
                },
                isError = sheetState.blockNameError != null,
                supportingText = sheetState.blockNameError?.let { error ->
                    { Text(error) }
                },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            /*************************************************************************
             * Advanced block micro cycle settings
             *************************************************************************/
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterStart
            ) {
                TextButton(onClick = onMicroCycleSettingsToggle) {
                    Text(
                        text = stringResource(
                            if (sheetState.areMicroCycleSettingsExpanded) {
                                R.string.hide_advanced_block_settings
                            } else R.string.advanced_block_settings
                        )
                    )
                }
            }
            AnimatedVisibility(sheetState.areMicroCycleSettingsExpanded) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(stringResource(R.string.training_days_per_micro_cycle))
                    Spacer(modifier = Modifier.width(8.dp))
                    NumberPicker(
                        value = sheetState.daysPerMicroCycle,
                        onValueChange = onDaysPerMicroCycleValueChange,
                        minValue = 1,
                        maxValue = 10
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            /*************************************************************************
             * Validation button
             *************************************************************************/
            Button(
                onClick = { onValidate(sheetState.blockName) },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(stringResource(R.string.validate))
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

data class AddEditBlockSheetState(
    val isSheetExpanded: Boolean = false,
    val blockName: String = "",
    val blockNameError: String? = null,
    val areMicroCycleSettingsExpanded: Boolean = false,
    val daysPerMicroCycle: Int = 3
)