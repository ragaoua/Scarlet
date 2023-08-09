package com.example.scarlet.feature_training_log.domain.use_case.session

import com.example.scarlet.R
import com.example.scarlet.core.util.Resource
import com.example.scarlet.core.util.SimpleResource
import com.example.scarlet.core.util.StringResource
import com.example.scarlet.feature_training_log.domain.model.Set
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository

class UpdateLoadBasedOnPreviousSetUseCase(
    private val repository: ScarletRepository
) {

    /**
     * Update a set's load based on a percentage of the preceding set.
     * Checks that the preceding set exists and that the percentage format is correct
     *
     * @param set the set to be updated
     * @param precedingSet the preceding set
     * @param loadPercentage percentage to apply to the preceding set's load
     *
     * @return a resource with an error, or a simple resource with no data
     */
    suspend operator fun invoke(
        set: Set,
        precedingSet: Set?,
        loadPercentage: String
    ): SimpleResource {

        precedingSet ?: return Resource.Error(StringResource(R.string.error_no_previous_set))

        val percentageInFloat = try {
            loadPercentage.toFloat() / 100
        } catch(e: NumberFormatException) {
            return Resource.Error(StringResource(R.string.error_invalid_percentage))
        }

        repository.updateSet(
            set.copy(
                weight = precedingSet.weight?.times(percentageInFloat)
                // TODO Round to .#
                // TODO Check value doesn't exceed 1000
                // TODO check percentage value is not > 100
            )
        ) // TODO : check if the weight is not null before displaying the dialog

        return Resource.Success()
    }
}