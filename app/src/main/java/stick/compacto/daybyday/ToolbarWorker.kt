package stick.compacto.daybyday

import android.app.Activity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import stick.compacto.daybyday.R

class ToolbarWorker  constructor(activity:Activity, date:String){
    private val mActivity: Activity = activity
    private val toolbarTitleTV = mActivity.findViewById<TextView>(R.id.toolbar_title_tv)
    private val rightImageButton =mActivity.findViewById<ImageView>(R.id.right_button_iv)
    private val middleRightImageButton = mActivity.findViewById<ImageView>(R.id.middle_right_button_iv)
    private val leftImageButton =mActivity.findViewById<ImageView>(R.id.left_button_iv)
    private val currentDateString:String = date



    fun switchBox(int:Int){
        when(int){
            0 -> mainToolbar()
            1 -> textToolbar()
            2 -> settingsToolbar()
        }
    }

    fun setTitle(title:String){
        toolbarTitleTV.text = title
    }

    fun mainToolbar(){
        toolbarTitleTV.text ="Day by Day"
        rightImageButton.visibility = View.INVISIBLE
        //settings button
        leftImageButton.visibility = View.INVISIBLE
        middleRightImageButton.visibility = View.INVISIBLE
    }

    fun textToolbar(){
        toolbarTitleTV.text = ""

        //back button
        rightImageButton.visibility = View.VISIBLE
        //settings button
        leftImageButton.visibility = View.VISIBLE
        //date picker button
        middleRightImageButton.visibility = View.VISIBLE


    }

    fun settingsToolbar(){
        toolbarTitleTV.text = "Settings"
        rightImageButton.visibility = View.INVISIBLE
        middleRightImageButton.visibility = View.INVISIBLE
        leftImageButton.visibility = View.VISIBLE
    }
}