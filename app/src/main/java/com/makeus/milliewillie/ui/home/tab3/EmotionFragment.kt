package com.makeus.milliewillie.ui.home.tab3

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.content.ContextCompat.getSystemService
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import com.makeus.base.fragment.BaseDataBindingFragment
import com.makeus.base.recycler.BaseDataBindingRecyclerViewAdapter
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.FragmentEmotionBinding
import com.makeus.milliewillie.databinding.ItemEmoListBinding
import com.makeus.milliewillie.model.EmotionImg
import com.makeus.milliewillie.ui.utils.DrawableUtils
import com.makeus.milliewillie.util.Log
import kotlinx.android.synthetic.main.activity_make_plan.*
import kotlinx.android.synthetic.main.fragment_emotion.*
import kotlinx.android.synthetic.main.item_emo_calendar_day.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import kotlin.math.abs


class EmotionFragment :
    BaseDataBindingFragment<FragmentEmotionBinding>(R.layout.fragment_emotion) {

    val viewModel by viewModel<EmoViewModel>()

    val df = SimpleDateFormat("M월 dd일")
    val cal = Calendar.getInstance()
    val events: MutableList<EventDay> = ArrayList()
    var day = df.format(Calendar.getInstance().time)
    var today = df.format(cal.time)

    companion object {
        fun getInstance() = EmotionFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


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

                day = df.format(eventDay.calendar.time)
                if (today != day) {
                    Log.e(day.toString(), "오늘날짜바뀜")
                    viewModel.liveTodayData.postValue(day.toString())
                    txt_plz_today_emo.visibility = View.GONE
                    txt_today.visibility = View.GONE
                    rv_emo.visibility = View.VISIBLE
                    layout_make_today_emo.visibility = View.GONE
                } else {
                    txt_plz_today_emo.visibility = View.VISIBLE
                    txt_today.visibility = View.VISIBLE
                    rv_emo.visibility = View.VISIBLE
                    layout_make_today_emo.visibility = View.GONE
//                    if(viewModel.liveEmoMemo.value?.isEmpty()==true){
//                        rv_emo.visibility = View.VISIBLE
//                        layout_make_today_emo.visibility = View.GONE
//                    }
//                    else{
//                        rv_emo.visibility = View.GONE
//                        layout_make_today_emo.visibility = View.VISIBLE
//                    }
                }

            }
        })


    }

    fun onClickCheck(v: View) {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(v.windowToken, 0)
        //서버에 쏘기
    }

    fun onClickItem(img: Int, text: String) {
        rv_emo.visibility = View.GONE
        layout_make_today_emo.visibility = View.VISIBLE
        txt_plz_today_emo.visibility = View.INVISIBLE

        val changeImg = viewModel.nextEmo(img)
        val calendar1 = Calendar.getInstance()

        calendar1.add(Calendar.DAY_OF_MONTH, calBetweenDay())
        events.remove(EventDay(calendar1, img))
        events.add(EventDay(calendar1, img))
        calendarView.setEvents(events)
        viewModel.liveEmoMemo.postValue("")
        viewModel.livePickEmo.postValue(EmotionImg(changeImg, text, id))
    }

    override fun onResume() {
        super.onResume()
        viewModel.liveTodayData.postValue(df.format(cal.time))
        viewModel.requestEmo()
        btn_check.visibility = View.GONE
    }

    fun calBetweenDay(): Int {

        val today = df.parse(today)
        val pickDay = df.parse(day)
        var calDate = pickDay.time - today.time
        val calDateDays = calDate / (24 * 60 * 60 * 1000)

        return calDateDays.toInt()
    }
    fun calDay():Int{
        val dateFormat = SimpleDateFormat("dd")
        val pickDay =dateFormat.format(day)
        return pickDay.toInt()
    }
}
