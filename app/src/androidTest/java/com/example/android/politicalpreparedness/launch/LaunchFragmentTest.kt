package com.example.android.politicalpreparedness.launch

import android.app.Application
import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.android.politicalpreparedness.BaseTest
import com.example.android.politicalpreparedness.R
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.mockito.Mockito


@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
@MediumTest
class LaunchFragmentTest : BaseTest() {

    private lateinit var appContext: Application
    private lateinit var viewModel: LaunchViewModel

    @Before
    fun init() {
        stopKoin()

        appContext = getApplicationContext()
        viewModel = LaunchViewModel(appContext)

        val module = module {
            viewModel {
                viewModel
            }
        }
        startKoin {
            modules(listOf(module))
        }
    }

    @Test
    fun navigateToElectionScreenWhenButtonClicked() = runBlockingTest {
        val scenario = launchFragmentInContainer<LaunchFragment>(Bundle(), R.style.AppTheme)

        val navController = Mockito.mock(NavController::class.java)

        scenario.onFragment {
            it.view?.let { v -> Navigation.setViewNavController(v, navController) }
        }

        onView(withId(R.id.btnUpcoming)).perform(ViewActions.click())

        Mockito.verify(navController).navigate(
            LaunchFragmentDirections.actionLaunchFragmentToElectionsFragment()
        )
    }

    @Test
    fun navigateToRepresentativeScreenWhenButtonClicked() = runBlockingTest {
        val scenario = launchFragmentInContainer<LaunchFragment>(Bundle(), R.style.AppTheme)

        val navController = Mockito.mock(NavController::class.java)

        scenario.onFragment {
            it.view?.let { v -> Navigation.setViewNavController(v, navController) }
        }

        onView(withId(R.id.btnRepresentative)).perform(ViewActions.click())

        Mockito.verify(navController).navigate(
            LaunchFragmentDirections.actionLaunchFragmentToRepresentativeFragment()
        )
    }

}
