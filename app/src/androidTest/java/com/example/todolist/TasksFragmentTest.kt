package com.example.todolist

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.todolist.presentation.MainActivity
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class TasksFragmentTest {

    @Test
    fun testAddItem() {
        launchActivity()
        val taskTitle = "TEST TASK 1"
        onView(withId(R.id.taskNameET)).perform(typeText(taskTitle))
        onView(withId(R.id.addBtn)).perform(click())

        onView(withText(taskTitle))
            .check(matches(isDisplayed()))

    }

    @Test
    fun testRemoveItem() {
        launchActivity()
        val taskTitle = "TEST TASK 1"
        onView(withId(R.id.taskNameET)).perform(typeText(taskTitle))
        onView(withId(R.id.addBtn)).perform(click())
        onView(withText(taskTitle))
            .check(matches(isDisplayed()))
        onView(withText(taskTitle)).perform(swipeRight())
        onView(withText(taskTitle)).check(doesNotExist())
    }

    private fun launchActivity() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        activityScenario.onActivity { activity ->
            (activity.findViewById(R.id.tasksRV) as RecyclerView).itemAnimator = null
        }
    }
}