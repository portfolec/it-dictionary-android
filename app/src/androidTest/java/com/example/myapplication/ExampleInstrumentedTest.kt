package com.example.myapplication

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.myapplication.ui.theme.ITDictionaryTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ITDictionaryUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun splashScreen_displaysAppName() {
        composeTestRule.setContent {
            ITDictionaryTheme {
                com.example.myapplication.presentation.splash.SplashScreen(onComplete = {})
            }
        }
        composeTestRule.onNodeWithText("IT Справочник").assertIsDisplayed()
    }

    @Test
    fun splashScreen_displaysTagline() {
        composeTestRule.setContent {
            ITDictionaryTheme {
                com.example.myapplication.presentation.splash.SplashScreen(onComplete = {})
            }
        }
        composeTestRule.onNodeWithText("Весь мир IT у тебя в кармане").assertIsDisplayed()
    }
}
