package com.ceiba.padawan

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class UsersActivityTest {

    private lateinit var stringToBetyped: String

    @get:Rule
    val activityRule = ActivityScenarioRule(UsersActivity::class.java)

    @Before
    fun configure() {
        stringToBetyped = "Clementine"
    }

    @Test
    fun gotToDetail() {
        // Maybe the first time, API will haven't respond
        onView(withId(R.id.action_search)).perform(click())
        onView(withId(R.id.search_src_text)).perform(typeText(""))
        onView(withId(R.id.users_list))
            .perform(scrollToPosition<RecyclerView.ViewHolder>(4))
            .perform(click())
        onView(withId(R.id.user_card_layout)).check(
            matches(
                hasDescendant(
                    withText("Patricia Lebsack")
                )
            )
        )
    }

    @Test
    fun searchClementine() {
        // Maybe the first time, API will haven't respond
        onView(withId(R.id.action_search)).perform(click())
        onView(withId(R.id.search_src_text)).perform(typeText(stringToBetyped))
        onView(withId(R.id.users_list))
            .perform(scrollToPosition<RecyclerView.ViewHolder>(1))
            .check(matches(hasDescendant(withText("Clementine Bauch"))))
    }
}
