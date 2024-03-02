package io.ssafy.mogeun.data

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import io.ssafy.mogeun.network.MogeunApiService
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer {
    val userDataRepository: UserRepository
    val emgDataRepository: EmgRepository
    val recordRepository: RecordRepository
    val keyRepository: KeyRepository
    val routineRepository: RoutineRepository
    val setRepository: SetRepository
    val summaryRepository: SummaryRepository
    val executionRepository: ExecutionRepository
    val bleRepository: BleRepository
    val dataLayerRepository: DataLayerRepository
}

class DefaultAppContainer(private val context: Context): AppContainer {
    private val baseUrl = "https://k9c104.p.ssafy.io:8080/API/"

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .build()

    private val retrofitService: MogeunApiService by lazy {
        retrofit.create(MogeunApiService::class.java)
    }

    override val userDataRepository: UserRepository by lazy {
        NetworkUserRepository(retrofitService)
    }

    override val emgDataRepository: EmgRepository by lazy {
        OfflineEmgRepository(EmgDatabase.getDatabase(context).emgDao())
    }
    
    override val recordRepository: RecordRepository by lazy {
        NetworkRecordRepository(retrofitService)
    }

    override val routineRepository: RoutineRepository by lazy {
        NetworkRoutineRepository(retrofitService)
    }

    override val keyRepository: KeyRepository by lazy {
        OfflineKeyRepository(KeyDatabase.getDatabase(context).keyDao())
    }

    override val setRepository: SetRepository by lazy {
        NetworkSetRepository(retrofitService)
    }

    override val summaryRepository: SummaryRepository by lazy {
        NetworkSummaryRepository(retrofitService)
    }
    override val executionRepository: ExecutionRepository by lazy {
        NetworkExecutionRepository(retrofitService)
    }
    override val bleRepository: BleRepository by lazy {
        AndroidBleRepository(context)
    }
    override val dataLayerRepository: DataLayerRepository by lazy {
        AndroidDataLayerRepository(context)
    }
}