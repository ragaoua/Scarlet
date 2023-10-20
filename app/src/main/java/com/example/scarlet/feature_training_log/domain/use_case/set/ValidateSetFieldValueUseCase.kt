package com.example.scarlet.feature_training_log.domain.use_case.set

import com.example.scarlet.feature_training_log.presentation.block.util.SetFieldType

class ValidateSetFieldValueUseCase {

    /**
     * Validates the value of a set field
     *
     * @param value value to be validated
     * @param setFieldType type of the set field
     *
     * @return true if the value is valid, false otherwise
     */
    operator fun invoke(value: String, setFieldType: SetFieldType): Boolean {
        return value.isBlank() ||
            when(setFieldType) {
                SetFieldType.REPS -> value.toIntOrNull()?.let { it < 1000 }
                SetFieldType.LOAD -> value.toFloatOrNull()?.let { it < 1000 }
                SetFieldType.RATING -> value.toFloatOrNull()?.let { it <= 10 }
            } ?: false
    }
}