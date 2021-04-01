package com.makeus.milliewillie.ui.home.tab3

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import com.makeus.base.activity.BaseActivity
import com.makeus.base.fragment.BaseDataBindingFragment
import com.makeus.base.recycler.BaseDataBindingRecyclerViewAdapter
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.FragmentEmotionBinding
import com.makeus.milliewillie.databinding.ItemEmoListBinding
import com.makeus.milliewillie.ext.showShortToastSafe
import com.makeus.milliewillie.model.EmotionImg
import com.makeus.milliewillie.ui.SampleToast
import com.makeus.milliewillie.util.Log
import kotlinx.android.synthetic.main.activity_intro_setting_name.*
import kotlinx.android.synthetic.main.activity_make_plan.*
import kotlinx.android.synthetic.main.fragment_emotion.*
import kotlinx.android.synthetic.main.item_emo_calendar_day.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*


class EmotionFragment :
    BaseDataBindingFragment<FragmentEmotionBinding>(R.layout.fragment_emotion) {

    val viewModel by viewModel<EmoViewModel>()

    val df = SimpleDateFormat("M월 dd일")
    val cal = Calendar.getInstance()
    val events: MutableList<EventDay> = ArrayList()
    var day = df.format(Calendar.getInstance().time)
    var today = df.format(cal.time)
    var emoId = 0
   // val context = requireActivity()

    companion object {
        fun getInstance() = EmotionFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
    fun onClickEditTxt(view: View){
        view.isFocusable = true
        view.requestFocus()
    }

    fun onChangeFocus(hasFocus: Boolean) {
        if (hasFocus) {
//            Observable.timer(300, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe {
//                binding.sv.fullScroll(View.FOCUS_DOWN)
//            }.disposeOnDestroy(this)
        } else {
                Log.e("hideKeyboard")
            (activity as BaseActivity).hideKeyboard()
        }
    }

    override fun FragmentEmotionBinding.onBind() {
        vi = this@EmotionFragment
        vm = viewModel

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


        //화살표
        calendarView.setCalendarDayLayout(R.layout.item_emo_calendar_day)
        context?.resources?.let {
            calendarView.setForwardButtonImage(it?.getDrawable(R.drawable.icon_right, null))
            calendarView.setPreviousButtonImage(it?.getDrawable(R.drawable.icon_left, null))

        }


        //오늘 네모
        // val calendar = Calendar.getInstance()
        // events.add(EventDay(calendar, DrawableUtils.todayRect(context)))

        val calendar3 = Calendar.getInstance()
        calendarView.setEvents(events)


        calendarView.setOnDayClickListener(object : OnDayClickListener {
            override fun onDayClick(eventDay: EventDay) {
                if(true){ //내용이 있으면
                    binding.trash.visibility = View.VISIBLE
                }

                day = df.format(eventDay.calendar.time)
                binding.calendarView.hideKeyboard()
                if (today != day) { //오늘이 아니면
                    //Log.e(day.toString(), "오늘날짜바뀜")
                    viewModel.liveTodayData.postValue(day.toString())
                    txt_plz_today_emo.visibility = View.GONE // 오늘 감정을 남겨주세요 GONE
                    txt_today.visibility = View.GONE //오늘 GONE
                    rv_emo.visibility = View.VISIBLE //이모지 rv
                    layout_make_today_emo.visibility = View.GONE
                } else {
                    txt_plz_today_emo.visibility = View.VISIBLE
                    txt_today.visibility = View.VISIBLE  // 오늘 감정을 남겨주세요
                    rv_emo.visibility = View.VISIBLE //오늘 활성화
                    layout_make_today_emo.visibility = View.GONE
//                    if(false){ //해당 날짜에 내용이 없으면
//                        rv_emo.visibility = View.VISIBLE //rv리사이클러 VISIBLE
//                        layout_make_today_emo.visibility = View.GONE // 메모 GONE
//                    }
//                    else{                                 //해당 날짜에 내용이 있으면
//                        rv_emo.visibility = View.GONE  // 리사이클러 GONE
//                        layout_make_today_emo.visibility = View.VISIBLE //메모 VISIBLE
//                    }
                }

            }
        })


    }
    fun onClickDelete(){
        viewModel.deleteEmotionsRecord(){
            if(it){
             //   SampleToast.createToast(context,"삭제 완료")?.show()
                "삭제 완료".showShortToastSafe()
            }
            else{
                "삭제 실패".showShortToastSafe()
            }
        }
    }

    fun onClickCheck(v: View) {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(v.windowToken, 0)
        if(binding.editEmo.text.isNullOrEmpty()) viewModel.emotionsRecordRequest.content = ""
        else viewModel.emotionsRecordRequest.content = binding.editEmo.text.toString()
        viewModel.emotionsRecordRequest.emotion = emoId
        //서버에 쏘기
        viewModel.postEmotionsRecord()
       // viewModel.patchEmotionsRecord()
    }

    fun onClickItem(img: Int, text: String, id : Int) {
        rv_emo.visibility = View.GONE
        layout_make_today_emo.visibility = View.VISIBLE
        txt_plz_today_emo.visibility = View.INVISIBLE

        val changeImg = viewModel.nextEmo(img) //이미지 변환 텍스트 이미지로
        val calendar1 = Calendar.getInstance()

        calendar1.add(Calendar.DAY_OF_MONTH, calBetweenDay())
        events.remove(EventDay(calendar1, img))
        events.add(EventDay(calendar1, img))
        calendarView.setEvents(events)
        viewModel.liveEmoMemo.postValue("")
        viewModel.livePickEmo.postValue(EmotionImg(changeImg, text, id))
        emoId = id
        Log.e(id.toString(),"뭐임")
        Log.e(emoId.toString(),"프래그먼트 감정 id")
    }

    override fun onResume() {
        super.onResume()
        viewModel.liveTodayData.postValue(df.format(cal.time))
        viewModel.requestEmo()
//        viewModel.getEmotionsRecordDay(){
//            if(it){
//                "조회 성공".showShortToastSafe()
//            }else{
//                "조회 실패".showShortToastSafe()
//            }
//        }
        btn_check.visibility = View.GONE
    }

    fun calBetweenDay(): Int {

        val today = df.parse(today)
        val pickDay = df.parse(day)
        var calDate = pickDay.time - today.time
        val calDateDays = calDate / (24 * 60 * 60 * 1000)

        return calDateDays.toInt()
    }

    fun calDay(): Int {
        val dateFormat = SimpleDateFormat("dd")
        val pickDay = dateFormat.format(day)
        return pickDay.toInt()
    }
}