package com.ceiba.padawan

import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.ceiba.padawan.utils.Constants.USER_ID
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

// TODO How to controll secuential test
@RunWith(AndroidJUnit4::class)
@LargeTest
class UsersDetailActivityTest {

    private var userId: Long = 4

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule<UsersDetailActivity>(
        Intent(ApplicationProvider.getApplicationContext(), UsersDetailActivity::class.java).apply {
            putExtra(USER_ID, userId)
        }
    )

    @Test
    fun openUserDetail() {
        onView(withId(R.id.user_card_layout)).check(
            matches(
                hasDescendant(
                    withText("Patricia Lebsack")
                )
            )
        )
    }
}
