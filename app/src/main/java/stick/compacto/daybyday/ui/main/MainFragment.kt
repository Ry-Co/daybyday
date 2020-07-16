package stick.compacto.daybyday.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import stick.compacto.daybyday.CampaignListAdapter
import stick.compacto.daybyday.R
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment() {

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel =  ViewModelProvider(this).get(MainViewModel::class.java)
    }

    private fun navigation(){
        val settingsButton = requireActivity().findViewById<ImageView>(R.id.right_button_iv)
        settingsButton.setOnClickListener {
            Toast.makeText(requireActivity(), "Settings Page", Toast.LENGTH_SHORT).show()
            startActivity(Intent(requireActivity(), SettingsActivity::class.java))
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        campaign_recycler_view.adapter =
            CampaignListAdapter(
                listOf(
                    "ww2",
                    "ww1",
                    "korea"
                ), findNavController()
            )
        campaign_recycler_view.layoutManager = LinearLayoutManager(requireContext())
        campaign_recycler_view.setHasFixedSize(true)
        navigation()
    }


}