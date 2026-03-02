package com.example.samadhannepalapp

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.samadhannepalapp.view.RegisterActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RegisterInstrumentedTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<RegisterActivity>()

    @Test
    fun testRegisterInputFields() {

        composeRule.onNodeWithTag("fullNameField")
            .performTextInput("Nitant")

        composeRule.onNodeWithTag("registerEmailField")
            .performTextInput("test@gmail.com")

        composeRule.onNodeWithTag("registerPasswordField")
            .performTextInput("123456")

        composeRule.onNodeWithTag("confirmPasswordField")
            .performTextInput("123456")

        composeRule.onNodeWithTag("registerButton")
            .performClick()
    }
}