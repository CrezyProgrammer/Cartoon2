package com.cartoon.funnyvideos.ui.dashboard

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cartoon.funnyvideos.R
import com.cartoon.funnyvideos.adapter.ItemAdapter
import com.cartoon.funnyvideos.databinding.FragmentHomeBinding
import com.cartoon.funnyvideos.model.Item
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.imaginativeworld.oopsnointernet.callbacks.ConnectionCallback
import org.imaginativeworld.oopsnointernet.dialogs.signal.NoInternetDialogSignal

class DashboardFragment : Fragment()  {
    var item: ArrayList<Item>? = null

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =  FragmentHomeBinding.inflate(layoutInflater)
        binding.progressBar.visibility=View.GONE
        val root = binding.root
        item = ArrayList()




        activity?.let {
            NoInternetDialogSignal.Builder(
                it,
                lifecycle
            ).apply {
                dialogProperties.apply {
                    connectionCallback = object : ConnectionCallback { // Optional
                        override fun hasActiveConnection(hasActiveConnection: Boolean) {
                            if (hasActiveConnection)
                            {showRecycler() }
                            // ...
                        }
                    }

                    cancelable = true// Optional
                    noInternetConnectionTitle = "No Internet" // Optional
                    noInternetConnectionMessage =
                        "Check your Internet connection and try again." // Optional
                    showInternetOnButtons = true // Optional
                    pleaseTurnOnText = "Please turn on" // Optional
                    wifiOnButtonText = "Wifi" // Optional
                    mobileDataOnButtonText = "Mobile data" // Optional

                    onAirplaneModeTitle = "No Internet" // Optional
                    onAirplaneModeMessage = "You have turned on the airplane mode." // Optional
                    pleaseTurnOffText = "Please turn off" // Optional
                    airplaneModeOffButtonText = "Airplane mode" // Optional
                    showAirplaneModeOffButtons = true // Optional
                }
            }.build()
        }








        return root
    }

    private fun showRecycler() {
        Handler(Looper.getMainLooper()).post {
            binding.progressBar.visibility = View.VISIBLE
        }


        val ref = FirebaseDatabase.getInstance().getReference("popular")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {


                if (p0.exists()) {
                    item?.clear()
                    for (v in p0.children) {

                        val bal = Item(
                            v.child("title").value.toString(),
                            v.child("id").value.toString()
                        )
                        item?.add(bal)


                    }
                    binding.progressBar.visibility = View.GONE

                    binding.recyclerview.layoutManager = LinearLayoutManager(activity)
                    binding.recyclerview.adapter = ItemAdapter(item!!)

                }

            }
        })

    }
}