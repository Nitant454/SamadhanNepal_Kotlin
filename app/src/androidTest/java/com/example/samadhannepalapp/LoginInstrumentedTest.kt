package com.example.samadhannepalapp

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.samadhannepalapp.view.LoginActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginInstrumentedTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<LoginActivity>()

    @Test
    fun testLoginFieldsInput() {

        composeRule.onNodeWithTag("emailField")
            .performTextInput("test@gmail.com")

        composeRule.onNodeWithTag("passwordField")
            .performTextInput("123456")

        composeRule.onNodeWithTag("loginButton")
            .performClick()

    }
}