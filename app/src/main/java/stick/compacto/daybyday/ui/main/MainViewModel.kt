package stick.compacto.daybyday.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import stick.compacto.daybyday.EventDataObject
import stick.compacto.daybyday.MainActivity
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*

class MainViewModel() : ViewModel() {
    private val monthsArray = arrayOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
    var activity: MainActivity? = null
    val updateList = MutableLiveData<Boolean>()
    var currentEventList = mutableListOf<EventDataObject>()
    var todayEventList = mutableListOf<EventDataObject>()
    var todayDateString:String = ""
    var currentDatestring:String = ""
    var toolbarMode = MutableLiveData<Int>()
    var textSize=MutableLiveData<Int>()
    var textSizeSmall = 18
    var textSizeMedium = 30
    var textSizeLarge = 45

    fun getCurrentDateString():String{
        val sdf = SimpleDateFormat("d/M/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())
        val dateRay = currentDate.split("/")
        val dayString = dateRay[0]
        val monthString = monthsArray[dateRay[1].toInt()-1]
        return "$monthString $dayString"
    }

    fun getEventList(dateString:String):MutableList<EventDataObject>{
        val eventList = mutableListOf<EventDataObject>()
        val lineList = mutableListOf<String>()
        val inputStream: InputStream = activity!!.resources.openRawResource(activity!!.resources.getIdentifier("output_main", "raw", activity!!.packageName))
        inputStream.bufferedReader().forEachLine { lineList.add(it) }
        var yearString = ""
        for((idx,line) in lineList.withIndex()) {
            if (line.split(",")[0] == dateString) {
                val event = EventDataObject()
                //val idx = lineList.indexOf(line)
                val splitLine = line.split(',')
                if(splitLine.size > 1){
                    yearString = splitLine[1].trim()
                }
                event.date_text = line
                event.plain_text = lineList[idx + 1]
                event.html_text = lineList[idx + 2]
                event.year_string = yearString
                eventList.add(event)
            }
        }
        return eventList
    }


}