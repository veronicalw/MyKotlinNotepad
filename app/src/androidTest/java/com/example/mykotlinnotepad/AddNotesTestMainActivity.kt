package com.example.mykotlinnotepad

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.example.mykotlinnotepad.models.NotesAdapter
import junit.framework.TestCase
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.jvm.Throws

@RunWith(AndroidJUnit4::class)
class AddNotesTestMainActivity{
    @Rule @JvmField
    var mNotesActivityTestRule: ActivityTestRule<MainActivity> = ActivityTestRule<MainActivity>(MainActivity::class.java)

    @Test
    @Throws(Exception::class)
    fun testclickAddNoteButton_opensAddNote(){
        onView(withId(R.id.imgAddNotes))
            .perform(click())
//        onView(withId(R.id.nav_save))
//            .perform(click())
//        onView(withId(R.id.titleNote))
//            .check(matches(isDisplayed()))
//        onView(withId(R.id.txtNoteContent))
//            .check(matches(isDisplayed()))
//        onView(withId(R.id.rcViewStore))
//            .perform(RecyclerViewActions.actionOnItemAtPosition<NotesAdapter.ViewHolder>(0, ViewActions.click()))

    }
}