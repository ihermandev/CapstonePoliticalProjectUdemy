package com.example.android.politicalpreparedness

import android.app.Application
import com.example.android.politicalpreparedness.data.local.ElectionLocalDataSourceImpl
import com.example.android.politicalpreparedness.data.local.database.ElectionDao
import com.example.android.politicalpreparedness.data.local.database.ElectionDatabase
import com.example.android.politicalpreparedness.data.network.CivicsApi
import com.example.android.politicalpreparedness.data.network.CivicsApiService
import com.example.android.politicalpreparedness.data.network.ElectionNetworkDataSourceImpl
import com.example.android.politicalpreparedness.data.repository.ElectionRepository
import com.example.android.politicalpreparedness.election.ElectionsViewModel
import com.example.android.politicalpreparedness.election.VoterInfoViewModel
import com.example.android.politicalpreparedness.launch.LaunchViewModel
import com.example.android.politicalpreparedness.representative.RepresentativeViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin()
        initTimber()
    }

    private fun initKoin() {
        val mainModule = module {
            single { CivicsApi.createCivicRetrofitService() }
            single { ElectionDatabase.getInstance(context = this@App).electionDao }

            single(qualifier = named("database")) {
                ElectionLocalDataSourceImpl(get() as ElectionDao,
                    Dispatchers.IO)
            }
            single(qualifier = named("api")) {
                ElectionNetworkDataSourceImpl(get() as CivicsApiService,
                    Dispatchers.IO)
            }
            single {
                ElectionRepository(
                    database = get<ElectionLocalDataSourceImpl>(qualifier = named("database")),
                    api = get<ElectionNetworkDataSourceImpl>(qualifier = named("api"))
                )
            }
            viewModel { ElectionsViewModel(get(), get() as ElectionRepository) }
            viewModel { VoterInfoViewModel(get(), get() as ElectionRepository) }
            viewModel { RepresentativeViewModel(get(), get() as ElectionRepository) }
            viewModel { LaunchViewModel(get()) }
        }

        startKoin {
            androidContext(this@App)
            androidLogger()
            modules(listOf(mainModule))
        }
    }

    private fun initTimber() {
        Timber.plant(Timber.DebugTree())
    }
}
