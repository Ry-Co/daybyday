package stick.compacto.daybyday

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import stick.compacto.daybyday.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.campaign_card.view.*

class CampaignListAdapter (private val campaignList: List<String>, private val navController: NavController):
    RecyclerView.Adapter<CampaignListAdapter.CampaignViewHolder>() {

    class CampaignViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val campaignImage: ImageView = itemView.campaign_IV
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CampaignViewHolder {
        val campaignView = LayoutInflater.from(parent.context).inflate(R.layout.campaign_card, parent, false)
        return CampaignViewHolder(
            campaignView
        )
    }

    override fun getItemCount(): Int {
        return campaignList.size
    }

    override fun onBindViewHolder(holder: CampaignViewHolder, position: Int) {
        //ww2
        //https://i.imgur.com/KZbDOUA.png
        val ww2URL = "https://i.imgur.com/KZbDOUA.png"
        val ww1URL = "https://i.imgur.com/KJaaNCL.png"
        val koreaURL = "https://i.imgur.com/S1DDkK1.png"
        val campaignItem = campaignList[position]
        if(position == 0){
            Picasso.get().load(ww2URL).into(holder.campaignImage)
        }
        if(position == 1){
            Picasso.get().load(ww1URL).into(holder.campaignImage)
        }
        if(position == 2){
            Picasso.get().load(koreaURL).into(holder.campaignImage)
        }

        holder.campaignImage.setOnClickListener{
            //go to todays text
            if(position == 0){
                navController.navigate(R.id.action_mainFragment_to_textFragment)
            }

        }
        //holder.campaignImage.setImageBitmap(R.drawable.ic_launcher_background)

        //holder.imageView.setImageResource(campaignItem.imageResource)
    }
}
