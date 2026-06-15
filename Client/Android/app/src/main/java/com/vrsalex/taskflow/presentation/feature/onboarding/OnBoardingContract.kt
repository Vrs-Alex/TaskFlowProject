package com.vrsalex.taskflow.presentation.feature.onboarding

import com.vrsalex.taskflow.R

object OnBoardingContract {

    data class Page(
        val imageId: Int,
        val titleId: Int,
        val descriptionId: Int
    )

    data class State(
        val pages: List<Page>
    )

    val state = State(
        listOf(
            Page(
                imageId = R.raw.onboarding_plan,
                titleId = R.string.onboarding_plan,
                descriptionId = R.string.onboarding_plan_description
            ),
            Page(
                imageId = R.raw.onboarding_goal,
                titleId = R.string.onboarding_goal,
                descriptionId = R.string.onboarding_goal_description
            ),
            Page(
                imageId = R.raw.onboarding_stat,
                titleId = R.string.onboarding_stat,
                descriptionId = R.string.onboarding_stat_description
            )
        )
    )

}