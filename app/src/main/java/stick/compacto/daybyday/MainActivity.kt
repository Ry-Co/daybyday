package stick.compacto.daybyday

import android.app.*
import android.content.*
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import stick.compacto.daybyday.R
import stick.compacto.daybyday.ui.main.MainViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var tb: ToolbarWorker
    lateinit var mAdView : AdView

    override fun onResume() {
        super.onResume()
        viewModel.toolbarMode.value = 0
        MobileAds.initialize(this){}
        mAdView = findViewById(R.id.adView)
        refreshAd()
        if(intent.hasExtra("notification")){
            viewModel.todayDateString = viewModel.getCurrentDateString()
            viewModel.todayEventList = viewModel.getEventList(viewModel.todayDateString)
            viewModel.currentDatestring = viewModel.todayDateString
            viewModel.currentEventList = viewModel.todayEventList
        }else{
        }
    }

    fun refreshAd(){
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //AppRater.app_launched(this);
        //views

        setContentView(R.layout.main_activity)
        setSupportActionBar(findViewById(R.id.toolbar_main))
        mAdView = findViewById(R.id.adView)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.activity = this

        //viewmodel vals
        viewModel.todayDateString = viewModel.getCurrentDateString()
        viewModel.todayEventList = viewModel.getEventList(viewModel.todayDateString)
        viewModel.currentDatestring = viewModel.todayDateString
        viewModel.currentEventList = viewModel.todayEventList
        viewModel.updateList.value = true

        //ads
        MobileAds.initialize(this){}

        //toolbar
        tb = ToolbarWorker(
            this,
            viewModel.todayDateString
        )
        tb.switchBox(0)

        //prefs
        val prefs = PreferenceManager.getDefaultSharedPreferences(baseContext)
        when(prefs.getString("text_size_select", "Small")){
            "Small" -> viewModel.textSize.value = viewModel.textSizeSmall
            "Medium" -> viewModel.textSize.value = viewModel.textSizeMedium
            "Large" -> viewModel.textSize.value = viewModel.textSizeLarge
        }
        val alertsRay = prefs.getStringSet("campaignAlerts",null)
        if(alertsRay != null){
            setNotification()
        }

        //misc
        viewModel.toolbarMode.observe(this, Observer {
            tb.switchBox(it)
        })
    }


    private fun setNotification(){
        val intent = Intent(this, AlarmBroadcastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)
        val am:AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 8)
        cal.set(Calendar.MINUTE, 30)
        cal.set(Calendar.SECOND, 0)
        //if the time has already passed today, set it for tomorrow
        if(System.currentTimeMillis()>cal.timeInMillis){
            cal.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR)+1)
        }
        am.cancel(pendingIntent)
        am.set(AlarmManager.RTC_WAKEUP, cal.timeInMillis, pendingIntent)
    }

}

class AlarmBroadcastReceiver() : BroadcastReceiver() {
    private val monthsArray = arrayOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")

    override fun onReceive(context: Context?, intent: Intent?) {
        val mNotificationManager = context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel("daybyday", "Day By Day Alerts Channel", NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = "Channel for day by day app to send daily updates to user"
            mNotificationManager.createNotificationChannel(channel)
        }
        val dateString = getCurrentDateString()
        val eventList = getEventList(context, dateString)

        val mBuilder = NotificationCompat.Builder(context, "daybyday")
            .setSmallIcon(R.mipmap.logo) // notification icon
            .setContentTitle(dateString+", WW2") // title for notification
            .setContentText(eventList.random().plain_text)// message for notification
            .setAutoCancel(true) // clear notification after click
        val intent = Intent(context, MainActivity::class.java)
        intent.putExtra("notification", true)
        val pi = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        mBuilder.setContentIntent(pi)
        mNotificationManager.notify(0, mBuilder.build())
    }

    private fun getCurrentDateString():String{
        val sdf = SimpleDateFormat("d/M/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())
        val dateRay = currentDate.split("/")
        val dayString = dateRay[0]
        val monthString = monthsArray[dateRay[1].toInt()-1]
        return "$monthString $dayString"
    }

    fun getEventList(context:Context?, dateString:String):List<EventDataObject>{
        val eventList = mutableListOf<EventDataObject>()
        val lineList = mutableListOf<String>()
        val inputStream: InputStream = context!!.resources.openRawResource(context!!.resources.getIdentifier("output_main", "raw", context!!.packageName))
        inputStream.bufferedReader().forEachLine { lineList.add(it) }
        var yearString = ""
        for(line in lineList) {
            if (line.contains(dateString)) {
                val event = EventDataObject()
                val idx = lineList.indexOf(line)
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


object AppRater {
    //todo improve this look
    private const val APP_TITLE = "DayByDay" // App Name
    private const val APP_PNAME = "com.example.daybyday" // Package Name
    private const val DAYS_UNTIL_PROMPT = 0 //Min number of days
    private const val LAUNCHES_UNTIL_PROMPT = 1 //Min number of launches
    fun app_launched(mContext: Context) {
        val prefs = mContext.getSharedPreferences("apprater", 0)
        if (prefs.getBoolean("dontshowagain", false)) {
            return
        }
        val editor = prefs.edit()

        // Increment launch counter
        val launch_count = prefs.getLong("launch_count", 0) + 1
        editor.putLong("launch_count", launch_count)

        // Get date of first launch
        var date_firstLaunch = prefs.getLong("date_firstlaunch", 0)
        if (date_firstLaunch == 0L) {
            date_firstLaunch = System.currentTimeMillis()
            editor.putLong("date_firstlaunch", date_firstLaunch)
        }

        // Wait at least n days before opening
        if (launch_count >= LAUNCHES_UNTIL_PROMPT) {
            if (System.currentTimeMillis() >= date_firstLaunch +
                DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000
            ) {
                showRateDialog(mContext, editor)
            }
        }
        editor.commit()
    }

    fun showRateDialog(
        mContext: Context,
        editor: SharedPreferences.Editor?
    ) {
        val dialog = Dialog(mContext)
        dialog.setTitle("Rate $APP_TITLE")
        val ll = LinearLayout(mContext)
        ll.orientation = LinearLayout.VERTICAL
        val tv = TextView(mContext)
        tv.text =
            "If you enjoy using $APP_TITLE, please take a moment to rate it. Thanks for your support!"
        tv.width = 240
        tv.setPadding(4, 0, 4, 10)
        ll.addView(tv)
        val b1 = Button(mContext)
        b1.setText("Rate $APP_TITLE")
        b1.setOnClickListener{
            fun onClick(v: View?) {
                mContext.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=$APP_PNAME")
                    )
                )
                dialog.dismiss()
            }
        }
        ll.addView(b1)
        val b2 = Button(mContext)
        b2.setText("Remind me later")
        b2.setOnClickListener{
            fun onClick(v: View?) {
                dialog.dismiss()
            }
        }
        ll.addView(b2)
        val b3 = Button(mContext)
        b3.setText("No, thanks")
        b3.setOnClickListener {  }
        b3.setOnClickListener{
            fun onClick(v: View?) {
                if (editor != null) {
                    editor.putBoolean("dontshowagain", true)
                    editor.commit()
                }
                dialog.dismiss()
            }
        }
        ll.addView(b3)
        dialog.setContentView(ll)
        dialog.show()
    }
}

