package io.ssafy.mogeun.ui.screens.routine.execution

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.yml.charts.common.extensions.isNotNull
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent
import io.ssafy.mogeun.data.BleRepository
import io.ssafy.mogeun.data.DataLayerRepository
import io.ssafy.mogeun.data.Emg
import io.ssafy.mogeun.data.EmgRepository
import io.ssafy.mogeun.data.ExecutionRepository
import io.ssafy.mogeun.data.KeyRepository
import io.ssafy.mogeun.data.RoutineRepository
import io.ssafy.mogeun.data.UserRepository
import io.ssafy.mogeun.model.SensorData
import io.ssafy.mogeun.model.SetExecutionRequest
import io.ssafy.mogeun.model.SetInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jtransforms.fft.DoubleFFT_1D
import java.text.SimpleDateFormat
import kotlin.math.abs
import kotlin.math.pow


class ExecutionViewModel(
    private val executionRepository: ExecutionRepository,
    private val emgRepository: EmgRepository,
    private val routineRepository: RoutineRepository,
    private val bleRepository: BleRepository,
    private val keyRepository: KeyRepository,
    private val dataLayerRepository: DataLayerRepository,
    private val userRepository: UserRepository
): ViewModel(),
    MessageClient.OnMessageReceivedListener {

    var userKey by mutableStateOf<Int?>(null)
    var fatigueList: MutableList<List<Double>> = mutableListOf()
    var fatigueWarning by mutableStateOf(false)
    var fatigueVal by mutableFloatStateOf(0f)

    fun getUserKey() {
        viewModelScope.launch(Dispatchers.IO) {
            val key = keyRepository.getKey()
            Log.d("execution", "사용자 키: $userKey")
            launch(Dispatchers.Main) {
                userKey = key?.userKey
            }
            dataLayerRepository.launchMogeun()
        }
    }

    private val _routineState = MutableStateFlow(RoutineState(null, showBottomSheet = false))
    val routineState = _routineState.asStateFlow()


    suspend fun getPlanList(routineKey: Int) {
        val ret = routineRepository.listMyExercise(routineKey)
        _routineState.update { routineState -> routineState.copy(planList = ret) }
    }

    fun getSetOfRoutine() {
        viewModelScope.launch {
            routineState.value.planList!!.data.forEach { plan ->
                val planKey = plan.planKey

                val ret = executionRepository.getSetOfRoutine(planKey)
                if(ret.data == null)
                {
                    _routineState.update { routineState -> routineState.copy(planDetails = routineState.planDetails + SetOfPlan(planKey, true, listOf(
                        SetProgress(1, 55, 10),
                        SetProgress(2, 60, 8),
                        SetProgress(3, 65, 6)
                    )
                        )) }
                } else {
                    _routineState.update { routineState -> routineState.copy(planDetails = routineState.planDetails + SetOfPlan(planKey, false, ret.data.setDetails.map { SetProgress(it.setNumber, it.weight, it.targetRep) }) ) }
                }
            }
        }
    }

    fun addSet(planKey: Int) {
        _routineState.update { routineState ->
            val changedPlanDetails = routineState.planDetails.map { setOfPlan ->
                if (setOfPlan.planKey == planKey) {
                    setOfPlan.copy(
                        valueChanged = true,
                        setOfRoutineDetail = setOfPlan.setOfRoutineDetail + setOfPlan.setOfRoutineDetail.last().copy(
                            setNumber = setOfPlan.setOfRoutineDetail.size + 1,
                            successRep = 0,
                            sensorData = listOf(MuscleSensorValue(), MuscleSensorValue())
                        )
                    )
                } else {
                    setOfPlan
                }
            }

            routineState.copy(planDetails = changedPlanDetails)
        }
    }

    fun removeSet(planKey: Int, setIdx: Int) {
        _routineState.update { routineState ->
            var newIdx = 1
            val changedPlanDetails = routineState.planDetails.map { setOfPlan ->
                if (setOfPlan.planKey == planKey && setOfPlan.setOfRoutineDetail.size > 1) {
                    setOfPlan.copy(valueChanged = true, setOfRoutineDetail = setOfPlan.setOfRoutineDetail.filter { setOfRoutineDetail -> setOfRoutineDetail.setNumber != setIdx }.map { setOfRoutineDetail -> setOfRoutineDetail.copy(setNumber = newIdx++) })
                } else {
                    setOfPlan
                }
            }

            routineState.copy(planDetails = changedPlanDetails)
        }
    }

    fun setWeight(planKey: Int, setIdx: Int, weight: Int) {
        _routineState.update { routineState ->
            val changedPlanDetails = routineState.planDetails.map { setOfPlan ->
                if(setOfPlan.planKey == planKey) {
                    setOfPlan.copy(valueChanged = true, setOfRoutineDetail = setOfPlan.setOfRoutineDetail.map { setOfRoutineDetail -> if(setOfRoutineDetail.setNumber == setIdx) setOfRoutineDetail.copy(targetWeight = weight) else setOfRoutineDetail } )
                } else {
                    setOfPlan
                }
            }

            routineState.copy(planDetails = changedPlanDetails)
        }
    }

    fun setRep(planKey: Int, setIdx: Int, rep: Int) {
        _routineState.update { routineState ->
            val changedPlanDetails = routineState.planDetails.map { setOfPlan ->
                if(setOfPlan.planKey == planKey) {
                    setOfPlan.copy(valueChanged = true, setOfRoutineDetail = setOfPlan.setOfRoutineDetail.map { setOfRoutineDetail -> if(setOfRoutineDetail.setNumber == setIdx) setOfRoutineDetail.copy(targetRep = rep) else setOfRoutineDetail } )
                } else {
                    setOfPlan
                }
            }

            routineState.copy(planDetails = changedPlanDetails)
        }
    }

    fun startSet(planKey: Int, setIdx: Int) {
        if(routineState.value.setInProgress) return

        _routineState.update { routineState -> routineState.copy(setInProgress = true, planDetails = routineState.planDetails.map { setOfPlan ->
            if(setOfPlan.planKey == planKey) {
                setOfPlan.copy(setOfRoutineDetail = setOfPlan.setOfRoutineDetail.map { routineDetail ->
                    if(routineDetail.setNumber == setIdx) {
                        routineDetail.copy(startTime = System.currentTimeMillis())
                    } else {
                        routineDetail
                    }
                })
            } else {
                setOfPlan
            }
        }) }
    }

    fun addCnt(planKey: Int, setIdx: Int) {
        _routineState.update { routineState -> routineState.copy(hasValidSet = true, planDetails = routineState.planDetails.map { setOfPlan ->
            if(setOfPlan.planKey == planKey) {
                setOfPlan.copy(setOfRoutineDetail = setOfPlan.setOfRoutineDetail.map { routineDetail ->
                    if(routineDetail.setNumber == setIdx) {
                        routineDetail.copy(successRep = routineDetail.successRep + 1)
                    } else {
                        routineDetail
                    }
                })
            } else {
                setOfPlan
            }
        }) }

        Log.d("set", "cnt+ : ${routineState.value}")
    }

    fun FFT_ready(emgList: List<Int>): Double {//N은 신호의 갯수
        val N = emgList.size

        val a = DoubleArray(2 * N) //fft 수행할 배열 사이즈 2N

        for (k in 0 until N) {
            a[2 * k] = emgList[k].toDouble() //Re
            a[2 * k + 1] = 0.0 //Im
        }

        val fft = DoubleFFT_1D(N.toLong()) //1차원의 fft 수행

        fft.complexForward(a) //a 배열에 output overwrite


        val mag = DoubleArray(N / 2)
        var sum = 0.0
        for (k in 0 until N / 2) {
            mag[k] = Math.sqrt(Math.pow(a[2 * k], 2.0) + Math.pow(a[2 * k + 1], 2.0))
            sum += mag[k]
        }
        var average = 0.0
        var nowSum = 0.0
        var fatigue = 0
        for (k in 0 until N / 2) {
            nowSum += mag[k];
            average = nowSum / sum

            if (average >= 0.5) {
                fatigue = k
                break
            }
        }


        return fatigue.toDouble() * 1000 / N
    }

    fun endSet(planKey: Int, setIdx: Int) {
        if(!routineState.value.setInProgress) return

        _routineState.update { routineState -> routineState.copy(setInProgress = false) }

        val reportKey = routineState.value.reportKey
        val plan = routineState.value.planDetails.find { setOfPlan -> setOfPlan.planKey == planKey }

        if (!plan.isNotNull()) return

        val set = plan!!.setOfRoutineDetail.find { setProgress -> setProgress.setNumber == setIdx }

        if (!set.isNotNull()) return

        val targetWeight = set!!.targetWeight
        val targetRep = set!!.targetRep
        val successRep = set!!.successRep

        val startTime: Long = set!!.startTime!!
        val endTime: Long = System.currentTimeMillis()

        val dateFormat = "yyyy-MM-dd"
        val timeFormat = "HH:mm:ss"
        val dateFormatter = SimpleDateFormat(dateFormat)
        val timeFormatter = SimpleDateFormat(timeFormat)

        val formmattedStartDate: String = dateFormatter.format(startTime)
        val formmattedStartTime: String = timeFormatter.format(startTime)
        val formmattedEndDate: String = dateFormatter.format(endTime)
        val formmattedEndTime: String = timeFormatter.format(endTime)

        viewModelScope.launch {
            CoroutineScope(Dispatchers.IO).launch {
                val entireEmgList = emgRepository.getEmgData(startTime, endTime)

                var fatigues = mutableListOf(0.0, 0.0)
                var averages = mutableListOf(0.0, 0.0)

                for (i in 0..1) {
                    val setEmgList = entireEmgList.filter { it.deviceId == i }.map { it.value }

                    if (setEmgList.isEmpty()) break

                    fatigues[i] = FFT_ready(setEmgList)

                    val setEmgListAbs = setEmgList.map { abs(it) }
                    averages[i] = setEmgListAbs.average()
                }

                _routineState.update { routineState ->
                    routineState.copy(planDetails = routineState.planDetails.map {setOfPlan ->
                        if (setOfPlan.planKey == planKey) {
                            setOfPlan.copy(
                                setOfRoutineDetail = setOfPlan.setOfRoutineDetail.map {setProgress ->
                                    if(setProgress.setNumber == setIdx) {
                                        setProgress.copy(sensorData = listOf(MuscleSensorValue(averages[0], fatigues[0]),MuscleSensorValue(averages[1], fatigues[1])))
                                    } else {
                                        setProgress
                                    }
                                }
                            )
                        } else {
                            setOfPlan
                        }
                    })
                }

                if (plan.valueChanged) {
                    val ret1 = async {
                        executionRepository.clearPlan(planKey)
                    }.await()

                    Log.d("report", "clear plan : $ret1")

                    val ret2 = async {
                        executionRepository.setPlan(planKey, plan.setOfRoutineDetail.map { setProgress ->
                            SetInfo(setProgress.setNumber, setProgress.targetWeight, setProgress.targetRep)
                        })
                    }.await()

                    Log.d("report", "set plan : $ret2")

                    val ret3 = executionRepository.reportSet(
                        SetExecutionRequest(
                            routineReportKey = reportKey!!,
                            planKey = planKey,
                            setNumber = setIdx,
                            weight = targetWeight,
                            targetRep = targetRep,
                            successRep = successRep,
                            startTime = "${formmattedStartDate}T${formmattedStartTime}",
                            endTime = "${formmattedEndDate}T${formmattedEndTime}",
                            muscleActs = listOf(
                                SensorData(1,averages[0],fatigues[0]),
                                SensorData(2,averages[1],fatigues[1])
                            )
                        )
                    )

                    Log.d("report", "report set : $ret3")

                    fatigueList.add(fatigues)
                    Log.d("fatigueList", fatigueList.toString())
                    if (fatigueList.size > 2)
                        calFatigueSlope()
                }
                dataLayerRepository.noticeEndOfSet()
            }
        }
    }

    var terminateTimer by mutableStateOf(true)

    suspend fun startRoutine(routineKey: Int, isAttached: String = "Y") {
        if(isAttached == "N") {
            _routineState.update { routineState -> routineState.copy(hasValidSet = true) }
        }

        terminateTimer = false
        viewModelScope.launch {
            runTimer()
        }
        _routineState.update { routineState -> routineState.copy(routineInProgress = true) }

        viewModelScope.launch {
            val ret = executionRepository.startRoutine(userKey!!, routineKey, isAttached)
            Log.d("report", "routine started = $ret")
            _routineState.update { routineState -> routineState.copy(reportKey = ret.data!!.reportKey) }
        }
    }

    fun endRoutine() {
        val reportKey = routineState.value.reportKey
        terminateTimer = true

        _routineState.update { routineState -> routineState.copy(routineInProgress = false) }

        if(routineState.value.hasValidSet) {
            viewModelScope.launch {
                val ret1 = executionRepository.endRoutine(userKey!!, reportKey!!)
                Log.d("report", "routine report = $ret1")

                val ret2 = executionRepository.reportCalorie(reportKey, 0.0)
                Log.d("report", "calorie report = $ret2")
            }
        }
    }

    fun resetRoutine() {
        _routineState.update { routineState -> routineState.copy(
            routineInProgress = false,
            setInProgress = false,
            hasValidSet = false,
            planDetails = routineState.planDetails.map { setOfPlan ->
                setOfPlan.copy(setOfRoutineDetail = setOfPlan.setOfRoutineDetail.map { setProgress ->
                    setProgress.copy(successRep = 0, sensorData = listOf(MuscleSensorValue(), MuscleSensorValue()), startTime = null)
                })
            }) }
    }

    val _elaspedTime = MutableStateFlow(ElapsedTime(System.currentTimeMillis(), 0, 0))
    val elaspedTime = _elaspedTime.asStateFlow()

    suspend fun runTimer() {
        _elaspedTime.update { elapsedTime -> elapsedTime.copy(startTime = System.currentTimeMillis()) }
        while (!terminateTimer) {
            delay(1000)
            val now = System.currentTimeMillis()
            val offset = (now - elaspedTime.value.startTime).toInt() / 1000
            _elaspedTime.update { elapsedTime -> elapsedTime.copy(minute = offset / 60, second = offset % 60) }
            noticeTimer("%02d:%02d".format(offset / 60, offset % 60))
        }
    }

    private val _inbodyInfo = MutableStateFlow(InbodyInfo())
    val inbodyInfo = _inbodyInfo.asStateFlow()

    fun getInbodyInfo() {
        viewModelScope.launch {
            val ret = userRepository.getInbody(userKey.toString())

            if(ret.data.height != 0.0 && ret.data.weight != 0.0 && ret.data.muscleMass != 0.0) {

                val height = ret.data.height / 100
                val weight = ret.data.weight
                val muscleMass = ret.data.muscleMass

                Log.d("smi", "height : ${height}, weight: ${weight}, muscle: ${muscleMass}")

                val powHeight = height.pow(2)

                Log.d("smi", "pow : $powHeight")

                val bmi = weight / powHeight
                val smi = muscleMass / powHeight

                val normalSmi = 0.175 * bmi + 3.176
                val offset = smi - normalSmi

                Log.d("smi", "bmi : ${bmi}, smi: $smi")
                Log.d("smi", "offset : $offset")

                _inbodyInfo.update {
                    it.copy(
                        hasData = true,
                        height = height,
                        weight = weight,
                        muscleMass = muscleMass,
                        offset = offset
                    )
                }
            }
        }
    }

    private val _emgState = MutableStateFlow(EmgUiState())
    val emgState = _emgState.asStateFlow()

    private val _sensorState = MutableStateFlow(SensorState())
    val sensorState: StateFlow<SensorState> = combine(
        bleRepository.connectedDevices,
        _sensorState
    ) { connectedDevices, state ->
        state.copy(
            connectedDevices = connectedDevices,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(TIMEOUT_MILLIS), _sensorState.value)

    fun getSensorVal() {
        viewModelScope.launch {
            bleRepository.sensorVal.collect { sensorVal ->
                for (i in 0..1) {
                    if(!bleRepository.connectedDevices.value[i].isNotNull())
                    {
                        _emgState.update {emgUiState ->
                            var bufList = emgUiState.emgList.toMutableList()
                            bufList[i] = listOf()
                            emgUiState.copy(
                                emgList = bufList
                            )
                        }
                    }
                    val bufValue = abs(sensorVal[i])
                    _emgState.update { emgUiState ->
                        val calcList = if(bufValue > 1500) emgUiState.emgList[i] else {if(emgUiState.emgList[i].size < 80) emgUiState.emgList[i] + bufValue else emgUiState.emgList[i].subList(1, 80) + bufValue}
                        var avg = calcList.average()
                        if(avg.isNaN()) {
                            avg = 0.0
                        }

                        var bufEmg: MutableList<Emg?> = emgUiState.emg.toMutableList()
                        bufEmg[i] = Emg(0, i, "unknown", sensorVal[i], System.currentTimeMillis())

                        var bufList = emgUiState.emgList.toMutableList()
                        bufList[i] = calcList

                        var bufAvg = emgUiState.emgAvg.toMutableList()
                        bufAvg[i] = avg

                        var bufMax = emgUiState.emgMax.toMutableList()
                        bufMax[i] = if(emgUiState.emgMax[i] < avg) avg.toInt() else emgUiState.emgMax[i]


                        emgUiState.copy(
                            emg = bufEmg,
                            emgList = bufList,
                            emgAvg = bufAvg,
                            emgMax = bufMax
                        )
                    }
                    if(bufValue <= 1500) {
                        viewModelScope.launch {
                            saveData(i, sensorVal[i])
                        }
                    }
                }
            }
        }
    }

    suspend fun saveData(idx: Int, value: Int) {
        val emgInput = Emg(0, idx, "unknown", value, System.currentTimeMillis())
        emgRepository.insertEmg(emgInput)
    }

    suspend fun deleteEmgData() {
        emgRepository.deleteEmgData()
    }

    fun showBottomSheet() {
        _routineState.update { routineState -> routineState.copy(showBottomSheet = true) }
    }
    fun hideBottomSheet() {
        _routineState.update { routineState -> routineState.copy(showBottomSheet = false) }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.launch {
            emgRepository.deleteEmgData()
        }
    }

    fun launchWearApp() {
        viewModelScope.launch(Dispatchers.IO) {
            dataLayerRepository.launchMogeun()
        }
    }

    fun noticeExerciseName(execName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dataLayerRepository.noticeExerciseName(execName)
        }
    }

    fun noticeTimer(elapsedTime: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dataLayerRepository.noticeTimer(elapsedTime)
        }
    }

    fun noticeStartOfRoutine() {
        viewModelScope.launch(Dispatchers.IO) {
            dataLayerRepository.noticeStartOfRoutine()
        }
    }

    fun noticeEndOfRoutine() {
        viewModelScope.launch(Dispatchers.IO) {
            dataLayerRepository.noticeEndOfRoutine()
        }
    }

    fun sendMessage(message: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dataLayerRepository.sendMessage(message)
        }
    }

    fun noticeProgress(current: Int, total: Int) {
        val progressFloat: Float = current.toFloat() / total.toFloat()
        viewModelScope.launch(Dispatchers.IO) {
            dataLayerRepository.noticeProgress("$progressFloat")
        }
    }

    private val _setControl = MutableStateFlow(0)
    val setControl = _setControl.asStateFlow()

    override fun onMessageReceived(messageEvent: MessageEvent) {
        if (messageEvent.path == MOGEUN_ROUTINE_START_SET_PATH) {
            _setControl.update {
                1
            }
        } else if (messageEvent.path == MOGEUN_ROUTINE_END_SET_PATH) {
            _setControl.update {
                2
            }
        }
    }

    private fun calFatigueSlope() {
        var trendLineVal1: MutableList<Float> = mutableListOf()
        var trendLineVal2 = 0f
        var trendLineVal3: MutableList<Float> = mutableListOf()
        var trendLineVal4 = 0f
        trendLineVal1.add(0f)
        trendLineVal1.add(0f)
        trendLineVal3.add(0f)
        trendLineVal3.add(0f)
        for (i in 1 .. fatigueList.size) {
            trendLineVal1[0] += i * fatigueList[i - 1][0].toFloat()
            trendLineVal1[1] += i * fatigueList[i - 1][1].toFloat()
            trendLineVal2 += i
            trendLineVal3[0] += fatigueList[i - 1][0].toFloat()
            trendLineVal3[1] += fatigueList[i - 1][1].toFloat()
            trendLineVal4 += i * i
        }
        val a1 = fatigueList.size * trendLineVal1[0]
        val a2 = fatigueList.size * trendLineVal1[1]
        val b1 = trendLineVal2 * trendLineVal3[0]
        val b2 = trendLineVal2 * trendLineVal3[1]
        val c = fatigueList.size * trendLineVal4
        val d  = trendLineVal2 * trendLineVal2
        val m1 = (a1 - b1) / (c - d)
        val m2 = (a2 - b2) / (c - d)
        Log.d("Left Slope", m1.toString())
        Log.d("Right Slope", m2.toString())
        fatigueVal = if (m1 > m2) m1 else m2
        if (fatigueVal > 7)
            fatigueWarning = true
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
        private const val TAG = "datalayer"

        private const val WEAR_CAPABILITY = "mogeun_transcription"
        private const val MOGEUN_SERVICE_START_PATH = "/mogeun_start"
        private const val MOGEUN_EXERCISE_NAME_MESSAGE_PATH = "/mogeun_routine_name"
        private const val MOGEUN_ROUTINE_TIMER_MESSAGE_PATH = "/mogeun_routine_timer"
        private const val MOGEUN_SET_ENDED_PATH = "/mogeun_set_ended"
        private const val MOGEUN_ROUTINE_STARTED_PATH = "/mogeun_routine_started"
        private const val MOGEUN_ROUTINE_ENDED_PATH = "/mogeun_routine_ended"
        private const val MOGEUN_SEND_MESSAGE_PATH = "/mogeun_send_message"
        private const val MOGEUN_SEND_Progress_PATH = "/mogeun_send_progress"
        private const val MOGEUN_ROUTINE_START_SET_PATH = "/mogeun_start_set"
        private const val MOGEUN_ROUTINE_END_SET_PATH = "/mogeun_end_set"
    }
}