package com.makeus.milliewillie.ui.home.tab3

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.listeners.OnCalendarPageChangeListener
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import com.google.android.material.snackbar.Snackbar
import com.makeus.base.disposeOnDestroy
import com.makeus.base.fragment.BaseDataBindingFragment
import com.makeus.base.recycler.BaseDataBindingRecyclerViewAdapter
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.FragmentEmotionBinding
import com.makeus.milliewillie.databinding.ItemEmoListBinding
import com.makeus.milliewillie.ext.setImage
import com.makeus.milliewillie.model.EmotionImg
import com.makeus.milliewillie.repository.local.LocalKey
import com.makeus.milliewillie.repository.local.RepositoryCached
import io.reactivex.Observable
import com.makeus.milliewillie.ui.SampleToast
import com.makeus.milliewillie.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_intro_setting_name.*
import kotlinx.android.synthetic.main.activity_make_plan.*
import kotlinx.android.synthetic.main.fragment_emotion.*
import kotlinx.android.synthetic.main.item_emo_calendar_day.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*


class EmotionFragment :
    BaseDataBindingFragment<FragmentEmotionBinding>(R.layout.fragment_emotion) {

    val viewModel by viewModel<EmoViewModel>()
    val repositoryCached by inject<RepositoryCached>()


    val df = SimpleDateFormat("M월 dd일")
    var dayFormat = SimpleDateFormat("yyyyMMdd")
    var monthFormat = SimpleDateFormat("yyyyMM")
    val daySFormat = SimpleDateFormat("yyyy-MM-dd")
    val cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"))
    val calendar1 = Calendar.getInstance()
    val events: MutableList<EventDay> = ArrayList()
    var day = df.format(Calendar.getInstance().time)
    var today = df.format(cal.time)
    var pickDay = dayFormat.format(cal.time)
    var pickSDay = daySFormat.format(cal.time)
    var emoId = 0
    var year = cal.get(Calendar.YEAR).toInt()
    var month = cal.get(Calendar.MONTH).plus(1)

    companion object {
        fun getInstance() = EmotionFragment()
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun FragmentEmotionBinding.onBind() {
        vi = this@EmotionFragment
        vm = viewModel
        //Today default설정
        viewModel.emotionsRecordRequest.date = pickSDay

        rv_emo.isNestedScrollingEnabled = false

        rv_emo.run {
            adapter = BaseDataBindingRecyclerViewAdapter<EmotionImg>()
                .addViewType(
                    BaseDataBindingRecyclerViewAdapter.MultiViewType<EmotionImg, ItemEmoListBinding>(
                        R.layout.item_emo_list
                    ) {
                        vi = this@EmotionFragment
                        item = it
                    })
            viewModel.liveEmoMemo.observe(this@EmotionFragment, androidx.lifecycle.Observer {
                if (it.equals("")) {
                    btn_check.visibility = View.GONE
                } else {
                    btn_check.visibility = View.VISIBLE
                }
            })
        }
        binding.editEmo.setOnTouchListener { v, p1 ->
            onClickEdit()
            false
        }


        //화살표
        calendarView.setCalendarDayLayout(R.layout.item_emo_calendar_day)
        context?.resources?.let {
            calendarView.setForwardButtonImage(it.getDrawable(R.drawable.icon_right, null))
            calendarView.setPreviousButtonImage(it.getDrawable(R.drawable.icon_left_reverse, null))

        }
        //캘린더 이전 월 리스너
        calendarView.setOnPreviousPageChangeListener(object : OnCalendarPageChangeListener {
            @RequiresApi(Build.VERSION_CODES.O)
            @SuppressLint("CheckResult")
            override fun onChange() {
                val cal = Calendar.getInstance()

                month--
                if (month == 0) month = 12
                else if (month == 13) month = 1
                var date = ""
                if (month < 10 && month >= 1) {
                    date = year.toString() + "0" + month.toString()
                } else {
                    date = year.toString() + month.toString()
                }
                viewModel.month = date
                Log.e(date.toString(), "DATE")
                viewModel.getEmotionsRecordMonth {
                    getEmo(it)
                }


            }
        })
        //캘린더 다음 월 리스너
        calendarView.setOnForwardPageChangeListener(object : OnCalendarPageChangeListener {
            @RequiresApi(Build.VERSION_CODES.O)
            @SuppressLint("CheckResult")
            override fun onChange() {
                month++
                if (month == 0) month = 12
                else if (month == 13) month = 1
                var date = ""
                if (month < 10 && month >= 1) {
                    date = year.toString() + "0" + month.toString()
                } else {
                    date = year.toString() + month.toString()
                }
                Log.e(date.toString(), "DATE")
                viewModel.month = date
                viewModel.getEmotionsRecordMonth {
                    getEmo(it)
                }
            }
        })

        //오늘 네모
        // val calendar = Calendar.getInstance()
        // events.add(EventDay(calendar, DrawableUtils.todayRect(context)))

        val calendar3 = Calendar.getInstance()
        calendarView.setEvents(events)


        calendarView.setOnDayClickListener(object : OnDayClickListener {
            override fun onDayClick(eventDay: EventDay) {

                txt_Date.setText(df.format(eventDay.calendar.time))
                Observable.timer(300, java.util.concurrent.TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread()).subscribe {
                        binding.sv.fullScroll(View.FOCUS_DOWN)
                    }.disposeOnDestroy(this@EmotionFragment)

                val calToday = Calendar.getInstance()
                calToday.add(Calendar.DATE, 0)


                if (eventDay.calendar.time > calToday.time) {
                    //  "미래 기록은 남길 수 없습니다,".showShortToastSafe()
//                    binding.txtDate.visibility = View.INVISIBLE
                         binding.rvEmo.visibility = View.GONE
                         binding.btnCheck.visibility = View.GONE
                         binding.layoutMakeTodayEmo.visibility = View.GONE // 메모 GONE
                         binding.txtPlzTodayEmo.visibility = View.GONE
                         txt_today.visibility = View.INVISIBLE //오늘 GONE
                         Snackbar.make(this@EmotionFragment.layout_emo,
                        "이전 날짜만 기록 가능합니다.",
                        Snackbar.LENGTH_SHORT).show()
                } else {

                          day = df.format(eventDay.calendar.time)
                          pickDay = dayFormat.format(eventDay.calendar.time)
                          pickSDay = daySFormat.format(eventDay.calendar.time)
                          //일별 기록 조회
                          repositoryCached.setValue(LocalKey.PICKDAY, pickDay)
                          Log.e(pickDay.toString(), "Pick Day")

                    viewModel.getEmotionsRecordDay {
                        if (it) {
                            binding.calendarView.hideKeyboard()
                            binding.trash.visibility = View.VISIBLE
                            Log.e("해당날짜에 내용이 있음")
                            emoId = viewModel.emotionsRecordResponse.result.emotion

                            if (today != day) { //오늘이 아니고 && 해당 날짜에 내용이 있으면

                                binding.imgEmo.setImage(viewModel.nextEmo(viewModel.emotionsRecordResponse.result.emotion))
                                binding.txtEmo.text = viewModel.emotionsRecordResponse.result.emotionText
                                viewModel.liveTodayData.postValue(day.toString())
                                txt_plz_today_emo.visibility = View.GONE // 오늘 감정을 남겨주세요 GONE
                                txt_today.visibility = View.GONE //오늘 GONE
                                //date response -> date request에 저장
                                viewModel.emotionsRecordRequest.date = viewModel.emotionsRecordResponse.result.date

                            } else { //오늘이고 && 해당 날짜에 내용이 있으면
                                txt_plz_today_emo.visibility = View.VISIBLE
                                txt_today.visibility = View.VISIBLE
                                binding.txtEmo.text =
                                    viewModel.emotionsRecordResponse.result.emotionText
                                binding.imgEmo.setImage(viewModel.nextEmo(viewModel.emotionsRecordResponse.result.emotion))

                            }
                                binding.rvEmo.visibility = View.GONE  // 리사이클러 GONE
                                binding.layoutMakeTodayEmo.visibility = View.VISIBLE //메모 VISIBLE
                        } else {
                            // 해당 날짜에 내용이 없으면
                                 Log.e("해당날짜에 내용이 없음")
                                 txt_plz_today_emo.visibility = View.GONE // 오늘 감정을 남겨주세요 GONE
                                 txt_today.visibility = View.GONE //오늘   GONE
                                 binding.btnCheck.visibility = View.INVISIBLE
                                 binding.trash.visibility = View.GONE
                                 binding.rvEmo.visibility = View.VISIBLE  // 리사이클러 VISIBLE
                                 binding.layoutMakeTodayEmo.visibility = View.GONE //메모 VISIBLE

                            viewModel.emotionsRecordRequest.date = pickSDay

                        }
                    }
                }

            }
        })


    }

    fun onClickEdit() {
        binding.editEmo.requestFocus()
    }

    fun View.openKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.editEmo, 0)
    }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onClickDelete() {
        viewModel.deleteEmotionsRecord {
            if (it) {
                // getEmo(it)
                val cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"))
                cal.set(Calendar.DAY_OF_MONTH,
                    calBetweenDayInput(viewModel.emotionsRecordResponse.result.date))
                events.remove(EventDay(cal,
                    viewModel.nextEmo(viewModel.emotionsRecordResponse.result.emotion)))
                binding.calendarView.setEvents(events)

                SampleToast.createToast(requireActivity(), "감정 기록 삭제 완료!")?.show()
                binding.rvEmo.visibility = View.VISIBLE
                binding.layoutMakeTodayEmo.visibility = View.GONE
            } else {
                Snackbar.make(this.layout_emo, "감정 기록 삭제에 실패하였습니다.", Snackbar.LENGTH_SHORT).show()
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun onClickCheck(v: View) {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(v.windowToken, 0)

//        cal.add(Calendar.DATE, calBetweenDay())
//        events.add(EventDay(cal, viewModel.nextEmo(emoId)))
//        binding.calendarView.setEvents(events)
        Log.e(Calendar.DAY_OF_MONTH.toString(),"DAM")
        Log.e(calBetweenDay().toString(),"between")

        if (binding.editEmo.text.isNullOrEmpty()) {
            Snackbar.make(this.layout_emo, "오늘의 감정을 기록해 주세요.", Snackbar.LENGTH_SHORT).show()
        } else {

            viewModel.emotionsRecordRequest.content = binding.editEmo.text.toString()
            if (!viewModel.emotionsRecordResponse.isSuccess) {
                viewModel.emotionsRecordRequest.emotion = emoId
                viewModel.postEmotionsRecord {
                    if (it) {
                        SampleToast.createToast(requireActivity(), "감정 기록 생성 완료!")?.show()
                        viewModel.getEmotionsRecordMonth {
                            getEmo(it)
                        }

                    } else {
                        Snackbar.make(this.layout_emo, "감정 기록  생성에 실패하였습니다.", Snackbar.LENGTH_SHORT)
                            .show()
                    }
                }
            } else {
                viewModel.emotionsRecordRequest.emotion = emoId
                viewModel.patchEmotionsRecord {
                    if (it) {
                        SampleToast.createToast(requireActivity(), "감정 기록 수정 완료!")?.show()
                    } else {
                        Snackbar.make(this.layout_emo, "감정 기록 수정에 실패하였습니다.", Snackbar.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }

    }

    fun onClickItem(img: Int, text: String, id: Int) {


        val changeImg = viewModel.nextEmo(id) //이미지 변환 텍스트 이미지로 -> id로

        rv_emo.visibility = View.GONE
        layout_make_today_emo.visibility = View.VISIBLE
        txt_plz_today_emo.visibility = View.INVISIBLE
        trash.visibility = View.VISIBLE
        btn_check.visibility = View.VISIBLE

//
//        calendar1.add(Calendar.DAY_OF_MONTH, calBetweenDay())
//        events.remove(EventDay(calendar1, img))
//        events.add(EventDay(calendar1, img))
//        calendarView.setEvents(events)
        Log.e(calBetweenDay().toString(), "이거 값 뭐들어갔어")
        Log.e(Calendar.DAY_OF_MONTH.toString(), "이거 값 뭐들어갔어")

        emoId = id

        binding.imgEmo.setImage(viewModel.nextEmo(emoId))
        viewModel.liveEmoMemo.postValue("")
        viewModel.livePickEmo.postValue(EmotionImg(changeImg, text, emoId))
        Log.e(emoId.toString(), "프래그먼트 감정 id")

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        viewModel.liveTodayData.postValue(df.format(cal.time))
        viewModel.requestEmo()
        viewModel.getEmotionsFirstMonth()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({

                viewModel.emotionMonthData = it.result.month

                if (it.result.today == null) {
                    getFirstEmo(it.isSuccess)
                    binding.layoutMakeTodayEmo.visibility = View.GONE
                    binding.rvEmo.visibility = View.VISIBLE
                } else {
                    viewModel.liveEmoMemo.value = it.result.today!!.content.toString()

                    binding.layoutMakeTodayEmo.visibility = View.VISIBLE
                    binding.rvEmo.visibility = View.GONE
                    binding.imgEmo.setImage(viewModel.nextEmo(it.result.today!!.emotion))
                    binding.txtEmo.text =it.result.today!!.emotionText
                    getFirstEmo(it.isSuccess)
                }


            }, {}).disposeOnDestroy(this)

        binding.calendarView.setEvents(events)
        btn_check.visibility = View.GONE
    }

    @RequiresApi(Build.VERSION_CODES.O)
     fun getEmo(data: Boolean) {

        if (data) {
            events.clear()
            Log.e(viewModel.emotionOnlyMonthData.toString(), "MONTHDATA")
            viewModel.emotionOnlyMonthData?.forEach {

                val cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"))
                cal.set(Calendar.DAY_OF_MONTH, calBetweenDayInput(it.date))
                events.add(EventDay(cal, viewModel.nextEmo(it.emotion)))
                Log.e(calBetweenDayInput(it.date).toString(), "CALBETWEENDAY")
                Log.e(it.date.toString(), "It Date")
                Log.e(it.emotion.toString(), "It Date")
                Log.e(Calendar.DATE.toString(), "It Date")

            }
            binding.calendarView.setEvents(events)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getFirstEmo(data: Boolean) {

        if (data) {
            events.clear()
            Log.e(viewModel.emotionMonthData.toString(), "MONTHDATA")

            viewModel.emotionMonthData?.forEach {

                val cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"))
                cal.set(Calendar.DAY_OF_MONTH, calBetweenDayInput(it.date))
                events.add(EventDay(cal, viewModel.nextEmo(it.emotion)))
                Log.e(calBetweenDayInput(it.date).toString(), "CALBETWEENDAY")
                Log.e(Calendar.DAY_OF_MONTH.toString(), "DOFMONTH")


            }
            binding.calendarView.setEvents(events)
        } else {
            binding.layoutMakeTodayEmo.visibility = View.GONE
            binding.rvEmo.visibility = View.VISIBLE
        }
    }
//    private fun getEmo(data: EmotionsRecordMonthResponse) {
//        data.result.month?.forEach {
//           // events.add(EventDay(calendar1, viewModel.nextEmo(it.emotion)))
//        }
//
//   //     calendarView.setEvents(events)
////            for (i in 0..viewModel.monthEmoSize) {
////                events.add(EventDay(calendar1,
////                    viewModel.nextEmo(data.result.month!![i].emotion)))
////            }
//
//    }

    fun calBetweenDay(): Int {

        val today = df.parse(today)
        val pickDay = df.parse(day)
        var calDate = pickDay.time - today.time
        val calDateDays = calDate / (24 * 60 * 60 * 1000)
        Log.e(calDateDays.toString(), "오늘 선택인데")
        return calDateDays.toInt()
    }

    fun calBetweenDayInput(dayInput: String): Int {
        val dff = SimpleDateFormat("yyyy-MM-dd")
        val cal = Calendar.getInstance(TimeZone.getDefault()).time

        val calPlus = Calendar.getInstance(TimeZone.getDefault())
        val today = calPlus.get(Calendar.DATE)

        val pickDay = dff.parse(dayInput)
        var calDate = pickDay.time - cal.time
        val calDateDays = calDate / (24 * 60 * 60 * 1000)
        Log.e(today.toString(),"doM")

        return calDateDays.toInt().plus(today)
    }

    fun calDay(): Int {
        val dateFormat = SimpleDateFormat("dd")
        val pickDay = dateFormat.format(day)
        return pickDay.toInt()
    }
}