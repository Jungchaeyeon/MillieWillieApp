package com.makeus.milliewillie.ui.routine

import androidx.lifecycle.MutableLiveData
import com.makeus.base.viewmodel.BaseViewModel
import com.makeus.milliewillie.MyApplication.Companion.isFirstExListSet
import com.makeus.milliewillie.R
import com.makeus.milliewillie.model.RoutineSelectedRecyclerItem
import com.makeus.milliewillie.model.RoutineWorkoutListItem
import com.makeus.milliewillie.model.WorkoutWeightRecordDate
import com.makeus.milliewillie.repository.ApiRepository
import com.makeus.milliewillie.util.Log
import com.makeus.milliewillie.util.SharedPreference


class MakeRoutineViewModel(val apiRepository: ApiRepository): BaseViewModel() {

    companion object {
        const val shoulderItemListKey = "shoulderItemListKey"
        const val chestItemListKey = "chestItemListKey"
        const val backItemListKey = "backItemListKey"
        const val absItemListKey = "absItemListKey"
        const val armItemListKey = "armItemListKey"
        const val legItemListKey = "legItemListKey"
        const val totalItemListKey = "totalItemListKey"

    }

    val liveDatePartOfEx = MutableLiveData<String>().apply { value = "" }
    val liveDataPartOfExTitle = MutableLiveData<String>().apply { value = "" }

    val liveDataExItemList = MutableLiveData<ArrayList<RoutineWorkoutListItem>>()
    val _exItemList = ArrayList<RoutineWorkoutListItem>()

    val liveDataSelectedItemList = MutableLiveData<ArrayList<RoutineSelectedRecyclerItem>>()
    val _selectedItemList = ArrayList<RoutineSelectedRecyclerItem>()

    var exerciseKind = ""

    var shoulderItemList = ArrayList<String>()
    var chestItemList = ArrayList<String>()
    var backItemList = ArrayList<String>()
    var absItemList = ArrayList<String>()
    var armItemList = ArrayList<String>()
    var legItemList = ArrayList<String>()
    var totalItemList = ArrayList<String>()


    init {
        defaultExItemList()
        if (!isFirstExListSet) {
            isFirstExListSet = true
            SharedPreference.putSettingBooleanItem("firstSet", isFirstExListSet)
            firstSetExList()
        } else {
            shoulderItemList = SharedPreference.getArrayStringItem(shoulderItemListKey)
            chestItemList = SharedPreference.getArrayStringItem(chestItemListKey)
            backItemList = SharedPreference.getArrayStringItem(backItemListKey)
            absItemList = SharedPreference.getArrayStringItem(absItemListKey)
            armItemList = SharedPreference.getArrayStringItem(armItemListKey)
            legItemList = SharedPreference.getArrayStringItem(legItemListKey)
            totalItemList = SharedPreference.getArrayStringItem(totalItemListKey)
        }
    }

    fun firstSetExList() {
            shoulderItemList = arrayListOf<String>("오버헤드 프레스", "덤벨 숄더 프레스", "덤벨 레터럴 레이즈",
                "덤벨 프론트 레이즈", "덤벨 슈러그", "비하인드 넥 프레스", "페이스 풀", "핸드 스탠드 푸시업",
                "케이블 리버스 플라이", "바벨 업 라이트 로우", "벤트오버 덤벨 레터럴 레이즈")
            chestItemList = arrayListOf<String>("벤치프레스", "인클라인 벤치 벤치프레스", "덤벨 벤치 벤치프레스",
                "덤벨 벤치프레스", "인클라인 덤벨 벤치프레스", "딥스", "덤벨 플라이", "케이블 크로스오버",
                "체스트 프레스 머신", "펙덱 플라이 머신", "푸시업")
            backItemList = arrayListOf<String>("풀업", "바벨 로우", "덤벨 로우", "펜들라이 로우",
                "시티드 로우 머신", "랫풀다운", "친업", "백 익스텐션", "굿모닝 엑서사이즈", "시티드 케이블 로우")
            absItemList = arrayListOf<String>("V업", "디클라인 싯업", "싯업", "레그 레이즈", "마운틴 클라이머",
                "러시안 트위스트", "플랭크", "덤벨 사이드 밴드", "복근 롤아웃", "복근 에어 바이크", "바이시클 크런치")
            armItemList = arrayListOf<String>("바벨컬", "덤벨컬", "덤벨 삼두 익스텐션", "덤벨 킥백",
                "덤벨 리스트 컬", "덤벨 해머 컬", "케이블 푸시 다운", "클로즈그립 푸시업")
            legItemList = arrayListOf<String>("스쿼트", "덤벨 스쿼트", "불가리안 스플릿 스쿼트", "바벨 스쿼트",
                "바벨 프론트 스쿼트", "바벨 백 스쿼트", "프론트 스쿼트", "에어 스쿼트", "내로우 스미스 머신 스쿼트",
                "스모 스쿼트", "스미스 머신 딥 스쿼트", "하프 스쿼트", "핵 스쿼트", "데드리프트",
                "컨벤셔널 데드리프트", "스모 데드리프트", "루마니안 데드리프트",
                "스티프 데드리프트", "스티프레드 데드리프트", "덤벨 데드리프트", "스미스 머신 루마니안 데드리프트",
                "런지", "백 런지", "리버스 런지", "프론트 런지", "워킹 런지",
                "사이드 런지", "덤벨 런지", "덤벨 스테핑 런지", "덤벨 크로스 런지", "래터럴런지",
                "케틀벨 워킹 런지", "스탠딩 카프레이즈", "시티드 카프레이즈", "플랭크 레그 리프트",
                "레그 프레스", "내로우 스탠스 레그프레스", "시티드 레그 프레스", "와이드 스탠스 레그프레스",
                "레그 컬", "레그 익스텐션", "스트레이트 레그 킥 백", "케이블 레그 익스텐션",
                "사이드킥", "스탠딩 사이드 킥", "스트레이트 레그 킥 백", "글루트 머신 킥백",
                "리버스 플랭크 브리지", "바벨 글루트 브리지", "어브덕션 글루트 브리지", "박스 점프",
                "버피 박스 점프 오버", "월 볼", "체어 스텝업", "크로스 잭", "프론 레그컬", "행 클린",
                "힙 레이즈", "힙 어덕션", "힙 어브덕션")
            totalItemList = arrayListOf<String>("오버헤드 프레스", "덤벨 숄더 프레스", "덤벨 레터럴 레이즈",
                "덤벨 프론트 레이즈", "덤벨 슈러그", "비하인드 넥 프레스", "페이스 풀", "핸드 스탠드 푸시업",
                "케이블 리버스 플라이", "바벨 업 라이트 로우", "벤트오버 덤벨 레터럴 레이즈" ,
                "벤치프레스", "인클라인 벤치 벤치프레스", "덤벨 벤치 벤치프레스",
                "덤벨 벤치프레스", "인클라인 덤벨 벤치프레스", "딥스", "덤벨 플라이", "케이블 크로스오버",
                "체스트 프레스 머신", "펙덱 플라이 머신", "푸시업", "풀업", "바벨 로우", "덤벨 로우", "펜들라이 로우",
                "시티드 로우 머신", "랫풀다운", "친업", "백 익스텐션", "굿모닝 엑서사이즈", "시티드 케이블 로우",
                "V업", "디클라인 싯업", "싯업", "레그 레이즈", "마운틴 클라이머",
                "러시안 트위스트", "플랭크", "덤벨 사이드 밴드", "복근 롤아웃", "복근 에어 바이크", "바이시클 크런치",
                "바벨컬", "덤벨컬", "덤벨 삼두 익스텐션", "덤벨 킥백", "덤벨 리스트 컬", "덤벨 해머 컬",
                "케이블 푸시 다운", "클로즈그립 푸시업", "스쿼트", "덤벨 스쿼트", "불가리안 스플릿 스쿼트", "바벨 스쿼트",
                "바벨 프론트 스쿼트", "바벨 백 스쿼트", "프론트 스쿼트", "에어 스쿼트", "내로우 스미스 머신 스쿼트",
                "스모 스쿼트", "스미스 머신 딥 스쿼트", "하프 스쿼트", "핵 스쿼트", "데드리프트",
                "컨벤셔널 데드리프트", "스모 데드리프트", "루마니안 데드리프트",
                "스티프 데드리프트", "스티프레드 데드리프트", "덤벨 데드리프트", "스미스 머신 루마니안 데드리프트",
                "런지", "백 런지", "리버스 런지", "프론트 런지", "워킹 런지",
                "사이드 런지", "덤벨 런지", "덤벨 스테핑 런지", "덤벨 크로스 런지", "래터럴런지",
                "케틀벨 워킹 런지", "스탠딩 카프레이즈", "시티드 카프레이즈", "플랭크 레그 리프트",
                "레그 프레스", "내로우 스탠스 레그프레스", "시티드 레그 프레스", "와이드 스탠스 레그프레스",
                "레그 컬", "레그 익스텐션", "스트레이트 레그 킥 백", "케이블 레그 익스텐션",
                "사이드킥", "스탠딩 사이드 킥", "스트레이트 레그 킥 백", "글루트 머신 킥백",
                "리버스 플랭크 브리지", "바벨 글루트 브리지", "어브덕션 글루트 브리지", "박스 점프",
                "버피 박스 점프 오버", "월 볼", "체어 스텝업", "크로스 잭", "프론 레그컬", "행 클린",
                "힙 레이즈", "힙 어덕션", "힙 어브덕션")
        SharedPreference.putArrayStringItem(shoulderItemListKey, shoulderItemList)
        SharedPreference.putArrayStringItem(chestItemListKey, chestItemList)
        SharedPreference.putArrayStringItem(backItemListKey, backItemList)
        SharedPreference.putArrayStringItem(absItemListKey, absItemList)
        SharedPreference.putArrayStringItem(armItemListKey, armItemList)
        SharedPreference.putArrayStringItem(legItemListKey, legItemList)
        SharedPreference.putArrayStringItem(totalItemListKey, totalItemList)
    }

    // 월별 체중 기록 리스트
    fun defaultExItemList() {
        liveDataExItemList.postValue(_exItemList)
    }

    fun createExItem(itemKind: String){
        _exItemList.clear()
        when (itemKind) {
            "전체" -> {
                exerciseKind = "전체"
                totalItemList.forEach { _exItemList.add(RoutineWorkoutListItem(it)) }
            }
            "하체" -> {
                exerciseKind = "하체"
                legItemList.forEach { _exItemList.add(RoutineWorkoutListItem(it)) }
            }
            "가슴" -> {
                exerciseKind = "가슴"
                chestItemList.forEach { _exItemList.add(RoutineWorkoutListItem(it)) }
            }
            "등" -> {
                exerciseKind = "등"
                backItemList.forEach { _exItemList.add(RoutineWorkoutListItem(it)) }
            }
            "어깨" -> {
                exerciseKind = "어깨"
                shoulderItemList.forEach { _exItemList.add(RoutineWorkoutListItem(it)) }
            }
            "팔" -> {
                exerciseKind = "팔"
                armItemList.forEach { _exItemList.add(RoutineWorkoutListItem(it)) }
            }
            "복근" -> {
                exerciseKind = "복근"
                absItemList.forEach { _exItemList.add(RoutineWorkoutListItem(it)) }
            }
        }

        defaultExItemList()
    }
    private fun defaultSelectedItemList() {
        liveDataSelectedItemList.postValue(_selectedItemList)
    }

    fun addSelectedItem(item: RoutineSelectedRecyclerItem) {
        var index = 1

        _selectedItemList.add(item)
        for (i in 0 until _selectedItemList.size) {
            _selectedItemList[i].index = "${index++}"
            Log.e("item = $_selectedItemList")
        }
        defaultSelectedItemList()
    }

    fun removeSelectedItem(position: Int) {
        var index = 1

        _selectedItemList.removeAt(position)
        for (i in 0 until _selectedItemList.size) {
            _selectedItemList[i].index = "${index++}"
            Log.e("item = $_selectedItemList")
        }
        Log.e("_selectedItemList = $_selectedItemList")
        defaultSelectedItemList()
    }




}