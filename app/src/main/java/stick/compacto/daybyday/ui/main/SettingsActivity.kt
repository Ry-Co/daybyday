package stick.compacto.daybyday.ui.main

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import stick.compacto.daybyday.MainActivity
import stick.compacto.daybyday.R

class SettingsActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {
    var testTV:TextView? = null
    private lateinit var viewModel: MainViewModel

    //we dont need the whole toolbar setup for this one activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        testTV =findViewById<TextView>(R.id.example_tv)
        supportFragmentManager.beginTransaction().replace(R.id.settings,
            SettingsFragment()
        ).commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Settings"
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        prefs.registerOnSharedPreferenceChangeListener(this)
    }


    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.app_prefs, rootKey)
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        startActivity(Intent(this, MainActivity::class.java))
        return true
    }

    override fun onSharedPreferenceChanged(p0: SharedPreferences?, p1: String?) {
        val textSize = p0!!.getString("text_size_select", null)
        when(textSize){
            "Small" -> testTV!!.textSize = viewModel.textSizeSmall.toFloat()
            "Medium" -> testTV!!.textSize = viewModel.textSizeMedium.toFloat()
            "Large" -> testTV!!.textSize = viewModel.textSizeLarge.toFloat()
        }


    }
}