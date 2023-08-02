package com.example.scarlet.feature_training_log.domain.use_case.session

import com.example.scarlet.R
import com.example.scarlet.core.util.Resource
import com.example.scarlet.core.util.SimpleResource
import com.example.scarlet.feature_training_log.domain.model.Set
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository

class UpdateLoadBasedOnPreviousSetUseCase(
    private val repository: ScarletRepository
) {

    suspend operator fun invoke(
        set: Set,
        previousSet: Set?,
        loadPercentage: String
    ): SimpleResource {

        previousSet ?: return Resource.Error(R.string.error_no_previous_set)

        val percentageInFloat = try {
            loadPercentage.toFloat() / 100
        } catch(e: NumberFormatException) {
            return Resource.Error(R.string.error_invalid_percentage)
        }

        repository.updateSet(
            set.copy(
                weight = previousSet.weight?.times(percentageInFloat)
                // TODO Round to .#
                // TODO Check value doesn't exceed 1000
                // TODO check percentage value is not > 100
            )
        ) // TODO : check if the weight is not null before displaying the dialog

        return Resource.Success()
    }
}