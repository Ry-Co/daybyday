package stick.compacto.daybyday.ui.main

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import stick.compacto.daybyday.EventDataObject
import stick.compacto.daybyday.MainActivity
import stick.compacto.daybyday.R
import stick.compacto.daybyday.ToolbarWorker
import java.util.*


class TextFragment : Fragment(), DatePickerDialog.OnDateSetListener {
    private lateinit var viewModel: MainViewModel
    private var mDatePickerDialog:DatePickerDialog? = null
    private val monthsArray = arrayOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
    private lateinit var tb: ToolbarWorker

    override fun onResume() {
        super.onResume()
        tb = ToolbarWorker(
            requireActivity(),
            viewModel.todayDateString
        )
        viewModel.toolbarMode.value = 1
        tb.setTitle(viewModel.currentDatestring)
        val a = requireActivity() as MainActivity
        a.refreshAd()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.currentEventList = viewModel.todayEventList
        viewModel.currentDatestring = viewModel.todayDateString
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_text, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val emptyLayout = view.findViewById<ConstraintLayout>(R.id.empty_layout)
        emptyLayout.visibility = View.VISIBLE
        tb = ToolbarWorker(
            requireActivity(),
            viewModel.todayDateString
        )
        tb.setTitle(viewModel.todayDateString)
        viewModel.toolbarMode.value = 1
        viewModel.updateList.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if(it == true){
                //update
                if(viewModel.currentEventList.isEmpty()){
                    clearView(view)
                    emptyLayout.visibility = View.VISIBLE
                    tb.setTitle(viewModel.currentDatestring)
                }else{
                    clearView(view)
                    setEventList(view, viewModel.currentEventList)
                    tb.setTitle(viewModel.currentDatestring)
                }

            }else{
                //don't update
            }
        })
        viewModel.textSize.observe(viewLifecycleOwner, Observer<Int>{
            setTextSize(view, it)
        })
        navigation()


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        viewModel.toolbarMode.value = 1
        val uCal = Calendar.getInstance()
        mDatePickerDialog = DatePickerDialog(requireContext(),AlertDialog.THEME_HOLO_LIGHT, this, uCal.get(Calendar.YEAR), uCal.get(Calendar.MONTH), uCal.get(Calendar.DAY_OF_MONTH))
        mDatePickerDialog!!.datePicker.findViewById<android.widget.NumberPicker>(resources.getIdentifier("year","id","android")).visibility = View.GONE
    }

    private fun navigation() {
        val textSizeButton = requireActivity().findViewById<ImageView>(R.id.right_button_iv)
        val backButton = requireActivity().findViewById<ImageView>(R.id.left_button_iv)
        val pickerButton = requireActivity().findViewById<ImageView>(R.id.middle_right_button_iv)
        textSizeButton.setOnClickListener {
            startActivity(Intent(requireActivity(), SettingsActivity::class.java))

        }
        backButton.setOnClickListener {
            findNavController().navigate(R.id.action_textFragment_to_mainFragment)
            viewModel.toolbarMode.value = 0
        }
        pickerButton.setOnClickListener {
            //val newFragment = DatePickerFragment()
            //newFragment.show(requireActivity().supportFragmentManager, "datePicker")
            mDatePickerDialog!!.show()
        }
    }

    private fun setEventList(view:View,eventList: MutableList<EventDataObject>){
        val yearRay = arrayOf("1939", "1940", "1941", "1942", "1943", "1944", "1945")
        val thisYearsList = mutableListOf<EventDataObject>()
        for(year in yearRay){
            thisYearsList.clear()
            for(event in eventList){
                if(event.year_string == year){
                    thisYearsList.add(event)
                }
            }
            when (year) {
                "1939" -> setYearValue(thisYearsList, "1939", view)
                "1940" -> setYearValue(thisYearsList, "1940", view)
                "1941" -> setYearValue(thisYearsList, "1941", view)
                "1942" -> setYearValue(thisYearsList, "1942", view)
                "1943" -> setYearValue(thisYearsList, "1943", view)
                "1944" -> setYearValue(thisYearsList, "1944", view)
                "1945" -> setYearValue(thisYearsList, "1945", view)
            }

        }


    }

    private fun clearView(view:View){
        val layoutThirtyNine = view.findViewById<ConstraintLayout>(R.id.l1939_layout)
        val layoutForty = view.findViewById<ConstraintLayout>(R.id.l1940_layout)
        val layoutFortyOne = view.findViewById<ConstraintLayout>(R.id.l1941_layout)
        val layoutFortyTwo = view.findViewById<ConstraintLayout>(R.id.l1942_layout)
        val layoutFortyThree = view.findViewById<ConstraintLayout>(R.id.l1943_layout)
        val layoutFortyFour = view.findViewById<ConstraintLayout>(R.id.l1944_layout)
        val layoutFortyFive = view.findViewById<ConstraintLayout>(R.id.l1945_layout)
        layoutThirtyNine.visibility =View.GONE
        layoutForty.visibility = View.GONE
        layoutFortyOne.visibility = View.GONE
        layoutFortyTwo.visibility = View.GONE
        layoutFortyThree.visibility = View.GONE
        layoutFortyFour.visibility = View.GONE
        layoutFortyFive.visibility = View.GONE

    }

    private fun setYearValue(eventList: List<EventDataObject>, year: String, view: View) {
        if(eventList.isEmpty()){
            return
        }

        val emptyLayout = view.findViewById<ConstraintLayout>(R.id.empty_layout)

        val layoutThirtyNine = view.findViewById<ConstraintLayout>(R.id.l1939_layout)
        val textThirtyNine = view.findViewById<TextView>(R.id.l1939_tv)
        val titleThirtyNine = view.findViewById<TextView>(R.id.title_tv)

        val layoutForty = view.findViewById<ConstraintLayout>(R.id.l1940_layout)
        val textForty = view.findViewById<TextView>(R.id.l1940_tv)
        val titleForty = view.findViewById<TextView>(R.id.title_tv_40)


        val layoutFortyOne = view.findViewById<ConstraintLayout>(R.id.l1941_layout)
        val textFortyOne = view.findViewById<TextView>(R.id.l1941_tv)
        val titleFortyOne = view.findViewById<TextView>(R.id.title_tv_41)


        val layoutFortyTwo = view.findViewById<ConstraintLayout>(R.id.l1942_layout)
        val textFortyTwo = view.findViewById<TextView>(R.id.l1942_tv)
        val titleFortyTwo = view.findViewById<TextView>(R.id.title_tv_42)


        val layoutFortyThree = view.findViewById<ConstraintLayout>(R.id.l1943_layout)
        val textFortyThree = view.findViewById<TextView>(R.id.l1943_tv)
        val titleFortyThree = view.findViewById<TextView>(R.id.title_tv_43)


        val layoutFortyFour = view.findViewById<ConstraintLayout>(R.id.l1944_layout)
        val textFortyFour = view.findViewById<TextView>(R.id.l1944_tv)
        val titleFortyFour = view.findViewById<TextView>(R.id.title_tv_44)


        val layoutFortyFive = view.findViewById<ConstraintLayout>(R.id.l1945_layout)
        val textFortyFive = view.findViewById<TextView>(R.id.l1945_tv)
        val titleFortyFive = view.findViewById<TextView>(R.id.title_tv_45)


        when (year) {
            "1939" -> {
                var html = ""
                for(item in eventList){
                    html += if(eventList.indexOf(item) == eventList.size-1){
                        item.html_text
                    }else{
                        item.html_text + "<br/><br/>"
                    }
                }
                textThirtyNine.text = fromHtmlCustom(html)
                textThirtyNine.movementMethod = LinkMovementMethod.getInstance()
                layoutThirtyNine.visibility = View.VISIBLE
                emptyLayout.visibility = View.GONE
            }
            "1940" -> {
                var html = ""
                for(item in eventList){
                    html += if(eventList.indexOf(item) == eventList.size-1){
                        item.html_text
                    }else{
                        item.html_text + "<br/><br/>"
                    }
                }
                textForty.text = fromHtmlCustom(html)
                textForty.movementMethod = LinkMovementMethod.getInstance()
                layoutForty.visibility = View.VISIBLE
                emptyLayout.visibility = View.GONE
            }
            "1941" -> {
                var html = ""
                for(item in eventList){
                    html += if(eventList.indexOf(item) == eventList.size-1){
                        item.html_text
                    }else{
                        item.html_text + "<br/><br/>"
                    }
                }
                textFortyOne.text = fromHtmlCustom(html)
                textFortyOne.movementMethod = LinkMovementMethod.getInstance()
                layoutFortyOne.visibility = View.VISIBLE
                emptyLayout.visibility = View.GONE
            }
            "1942" -> {
                var html = ""
                for(item in eventList){
                    html += if(eventList.indexOf(item) == eventList.size-1){
                        item.html_text
                    }else{
                        item.html_text + "<br/><br/>"
                    }
                }
                textFortyTwo.text = fromHtmlCustom(html)
                textFortyTwo.movementMethod = LinkMovementMethod.getInstance()
                layoutFortyTwo.visibility = View.VISIBLE
                emptyLayout.visibility = View.GONE
            }
            "1943" -> {
                var html = ""
                for(item in eventList){
                    html += if(eventList.indexOf(item) == eventList.size-1){
                        item.html_text
                    }else{
                        item.html_text + "<br/><br/>"
                    }
                }
                textFortyThree.text = fromHtmlCustom(html)
                textFortyThree.movementMethod = LinkMovementMethod.getInstance()
                layoutFortyThree.visibility = View.VISIBLE
                emptyLayout.visibility = View.GONE
            }
            "1944" -> {
                var html = ""
                for(item in eventList){
                    html += if(eventList.indexOf(item) == eventList.size-1){
                        item.html_text
                    }else{
                        item.html_text + "<br/><br/>"
                    }
                }
                textFortyFour.text = fromHtmlCustom(html)
                textFortyFour.movementMethod = LinkMovementMethod.getInstance()
                layoutFortyFour.visibility = View.VISIBLE
                emptyLayout.visibility = View.GONE
            }
            else -> {
                var html = ""
                for(item in eventList){
                    html += if(eventList.indexOf(item) == eventList.size-1){
                        item.html_text
                    }else{
                        item.html_text + "<br/><br/>"
                    }
                }
                textFortyFive.text = fromHtmlCustom(html)
                textFortyFive.movementMethod = LinkMovementMethod.getInstance()
                layoutFortyFive.visibility = View.VISIBLE
                emptyLayout.visibility = View.GONE
            }
        }
    }

    private fun setTextSize(view:View, textSize:Int){
        val emptyLayout = view.findViewById<ConstraintLayout>(R.id.empty_layout)

        val layoutThirtyNine = view.findViewById<ConstraintLayout>(R.id.l1939_layout)
        val textThirtyNine = view.findViewById<TextView>(R.id.l1939_tv)
        textThirtyNine.textSize = textSize.toFloat()
        val titleThirtyNine = view.findViewById<TextView>(R.id.title_tv)
        titleThirtyNine.textSize = textSize.toFloat() + 25

        val layoutForty = view.findViewById<ConstraintLayout>(R.id.l1940_layout)
        val textForty = view.findViewById<TextView>(R.id.l1940_tv)
        textForty.textSize = textSize.toFloat()
        val titleForty = view.findViewById<TextView>(R.id.title_tv_40)
        titleForty.textSize = textSize.toFloat() + 25


        val layoutFortyOne = view.findViewById<ConstraintLayout>(R.id.l1941_layout)
        val textFortyOne = view.findViewById<TextView>(R.id.l1941_tv)
        textFortyOne.textSize = textSize.toFloat()
        val titleFortyOne = view.findViewById<TextView>(R.id.title_tv_41)
        titleFortyOne.textSize = textSize.toFloat() + 25


        val layoutFortyTwo = view.findViewById<ConstraintLayout>(R.id.l1942_layout)
        val textFortyTwo = view.findViewById<TextView>(R.id.l1942_tv)
        textFortyTwo.textSize = textSize.toFloat()
        val titleFortyTwo = view.findViewById<TextView>(R.id.title_tv_42)
        titleFortyTwo.textSize = textSize.toFloat() + 25


        val layoutFortyThree = view.findViewById<ConstraintLayout>(R.id.l1943_layout)
        val textFortyThree = view.findViewById<TextView>(R.id.l1943_tv)
        textFortyThree.textSize = textSize.toFloat()
        val titleFortyThree = view.findViewById<TextView>(R.id.title_tv_43)
        titleFortyThree.textSize = textSize.toFloat() + 25


        val layoutFortyFour = view.findViewById<ConstraintLayout>(R.id.l1944_layout)
        val textFortyFour = view.findViewById<TextView>(R.id.l1944_tv)
        textFortyFour.textSize = textSize.toFloat()
        val titleFortyFour = view.findViewById<TextView>(R.id.title_tv_44)
        titleFortyFour.textSize = textSize.toFloat() + 25


        val layoutFortyFive = view.findViewById<ConstraintLayout>(R.id.l1945_layout)
        val textFortyFive = view.findViewById<TextView>(R.id.l1945_tv)
        textFortyFive.textSize = textSize.toFloat()
        val titleFortyFive = view.findViewById<TextView>(R.id.title_tv_45)
        titleFortyFive.textSize = textSize.toFloat() + 25
    }

    private fun fromHtmlCustom(html: String): Spanned {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // FROM_HTML_MODE_LEGACY is the behaviour that was used for versions below android N
            // we are using this flag to give a consistent behaviour
            Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            Html.fromHtml(html);
        }
    }

    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int) {
        mDatePickerDialog!!.updateDate(year, month, day)
        val dayString = day.toString()
        val monthString = monthsArray[month]

        val dateString = "$monthString $dayString"

        viewModel.currentDatestring = dateString
        viewModel.currentEventList = viewModel.getEventList(dateString)
        viewModel.updateList.value =true
    }

}

