package com.example.ae_chat_sdk.acti.home

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
<<<<<<< HEAD:app/src/main/java/com/example/ae_chat_sdk/acti/home/HomeFragment.kt
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
=======
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
>>>>>>> UI:app/src/main/java/com/example/ae_chat_sdk/acti/home/HomeActivity.kt
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ae_chat_sdk.MainActivity
import com.example.ae_chat_sdk.R
<<<<<<< HEAD:app/src/main/java/com/example/ae_chat_sdk/acti/home/HomeFragment.kt
import com.example.ae_chat_sdk.acti.Adapter.ContactAdapter
import com.example.ae_chat_sdk.acti.Adapter.OnstreamAdapter
import com.example.ae_chat_sdk.acti.Adapter.RecentAdapter
import com.example.ae_chat_sdk.acti.IClickListener
import com.example.ae_chat_sdk.data.api.ApiConstant
import com.example.ae_chat_sdk.data.api.reponsitory.RegisterRepository
import com.example.ae_chat_sdk.data.model.MyResponse
import com.example.ae_chat_sdk.data.model.User
import com.example.ae_chat_sdk.data.storage.AppStorage
=======
import com.example.ae_chat_sdk.acti.adapter.ContactAdapter
import com.example.ae_chat_sdk.acti.adapter.OnstreamAdapter
import com.example.ae_chat_sdk.acti.adapter.RecentAdapter
>>>>>>> UI:app/src/main/java/com/example/ae_chat_sdk/acti/home/HomeActivity.kt
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

<<<<<<< HEAD:app/src/main/java/com/example/ae_chat_sdk/acti/home/HomeFragment.kt
class HomeFragment : Fragment(), View.OnClickListener {
=======
class HomeActivity : AppCompatActivity() {
>>>>>>> UI:app/src/main/java/com/example/ae_chat_sdk/acti/home/HomeActivity.kt

    // Context
    lateinit var context: Context

    // RecyclerView Message Home
    lateinit var rvOnstream: RecyclerView
    lateinit var rvRecent: RecyclerView
    lateinit var rvListContact: RecyclerView

    lateinit var btnLogOut : Button


    lateinit var recent: ArrayList<String>
    lateinit var onstream: ArrayList<String>
    lateinit var contact: ArrayList<String>

    lateinit var rLayoutBottomSheetHome: RelativeLayout
    lateinit var rLayoutMessageHome: RelativeLayout
    lateinit var rLayoutBottomSheetListContact: RelativeLayout

    // BottomSheet
    lateinit var bottomSheetHomeBehavior: BottomSheetBehavior<View>
    lateinit var bottomSheetContactBehavior: BottomSheetBehavior<View>


    //
    lateinit var tvPagename: TextView
    lateinit var tvUserName : TextView

    lateinit var avatarUser : CircleImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // set data for list on Stream
        onstream = ArrayList()
        for (i in 1..20) {
            onstream.add("username # $i")
        }


        // set data for recent chat
        recent = ArrayList()
        for (i in 1..20) {
            recent.add("username # $i")
        }

        contact = ArrayList()
        for (i in 1..20) {
            contact.add("username # $i")
        }
        
        context=applicationContext;

        init()
        setButtonOnClickListener()
        renderDataRecyclerView()
        setBottomSheetBehaviorHome()
<<<<<<< HEAD:app/src/main/java/com/example/ae_chat_sdk/acti/home/HomeFragment.kt
        setData()
=======
    }
>>>>>>> UI:app/src/main/java/com/example/ae_chat_sdk/acti/home/HomeActivity.kt

    private fun init() {
        rLayoutBottomSheetHome = findViewById(R.id.rlBottomSheetHome)
        rLayoutMessageHome = findViewById(R.id.rlMessageHome)
        rLayoutBottomSheetListContact = findViewById(R.id.rlBottomSheetListContact)

        rvOnstream = findViewById(R.id.rvHorizonalOnstream)
        rvRecent = findViewById(R.id.rvVerticalRecent)
        rvListContact = findViewById(R.id.rvHorizonalContact)

        bottomSheetHomeBehavior = BottomSheetBehavior.from(rLayoutBottomSheetHome)
        bottomSheetContactBehavior = BottomSheetBehavior.from(rLayoutBottomSheetListContact)

        tvPagename = findViewById(R.id.tvPageName)
    }

    private fun setButtonOnClickListener() {
        findViewById<MaterialButton>(R.id.buttonListContact)
            .setOnClickListener(View.OnClickListener {
                // show Bottom Sheet Contact
                setBottomSheetBehaviorContact()
                findViewById<RelativeLayout>(R.id.rlMenuOption).animate().alpha(0F)
                    .setDuration(0).startDelay = 0
                tvPagename.text = "Danh sách liên hệ"
                tvPagename.setTextColor(Color.parseColor("#80FFFFFF"))
                rLayoutBottomSheetListContact.animate().alpha(1F).setDuration(0).startDelay = 0
                true
            })
    }

<<<<<<< HEAD:app/src/main/java/com/example/ae_chat_sdk/acti/home/HomeFragment.kt

    private fun init() {
        rLayoutBottomSheetHome = v.findViewById(R.id.rLayoutBottomSheetHome)
        rLayoutMessageHome = v.findViewById(R.id.rLayoutMessageHome)
        rLayoutBottomSheetListContact = v.findViewById(R.id.rLayoutBottomSheetListContact)

        rvOnstream = v.findViewById(R.id.rViewHorizonalOnstream)
        rvRecent = v.findViewById(R.id.rViewVerticalRecent)
        rvListContact = v.findViewById(R.id.rViewHorizonalContact)

        bottomSheetHomeBehavior = BottomSheetBehavior.from(rLayoutBottomSheetHome)
        bottomSheetContactBehavior = BottomSheetBehavior.from(rLayoutBottomSheetListContact)

        tvPagename = v.findViewById(R.id.tViewPagename)

        avatarUser  = v.findViewById(R.id.avatarUser)

        tvUserName = v.findViewById(R.id.tViewUsername)

        btnLogOut = v.findViewById(R.id.buttonLogOut)
        btnLogOut.setOnClickListener(this)
    }

    private fun renderDataRecyclerView() {
        rvOnstream.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        rvOnstream.adapter = OnstreamAdapter(onstream, object : IClickListener {
            override fun clickItem(itemObject: String) {
                Toast.makeText(context, itemObject, Toast.LENGTH_SHORT).show()
            }
        })

        rvRecent.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvRecent.adapter = RecentAdapter(recent, object : IClickListener {
            override fun clickItem(itemObject: String) {
                Toast.makeText(context, itemObject, Toast.LENGTH_SHORT).show()
            }
        })

        rvListContact.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvListContact.adapter = ContactAdapter(contact, object : IClickListener {
            override fun clickItem(itemObject: String) {
                Toast.makeText(context, itemObject, Toast.LENGTH_SHORT).show()
            }
        })
        rvListContact.addItemDecoration(
            DividerItemDecoration(
                context, DividerItemDecoration.VERTICAL
            )
        )
    }

=======
>>>>>>> UI:app/src/main/java/com/example/ae_chat_sdk/acti/home/HomeActivity.kt
    private fun setBottomSheetBehaviorHome() {
        bottomSheetHomeBehavior.apply {
            this.state = BottomSheetBehavior.STATE_EXPANDED
        }
        bottomSheetHomeBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        findViewById<RelativeLayout>(R.id.rlMessageHome).animate().alpha(1F)
                            .setDuration(100).startDelay = 0
                        findViewById<RelativeLayout>(R.id.rlMenuOption).animate().alpha(0F)
                            .setDuration(500).startDelay = 0

                    }
                    else -> {
                        findViewById<RelativeLayout>(R.id.rlMessageHome).animate().alpha(0F)
                            .setDuration(100).startDelay = 0
                        findViewById<RelativeLayout>(R.id.rlMenuOption).animate().alpha(1F)
                            .setDuration(500).startDelay = 0
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })
    }

    private fun setBottomSheetBehaviorContact() {
        bottomSheetContactBehavior.apply {
            this.state = BottomSheetBehavior.STATE_EXPANDED
        }
        bottomSheetContactBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        rLayoutBottomSheetListContact.animate().alpha(1F)
                            .setDuration(0).startDelay = 0
                        findViewById<RelativeLayout>(R.id.rlMenuOption).animate().alpha(0F)
                            .setDuration(0).startDelay = 0
                        tvPagename.text = "Danh sách liên hệ"
                        tvPagename.setTextColor(Color.parseColor("#80FFFFFF"))
                    }
                    else -> {
                        tvPagename.text = "Username"
                        findViewById<RelativeLayout>(R.id.rlMenuOption).animate().alpha(1F)
                            .setDuration(0).startDelay = 0
                        tvPagename.setTextColor(Color.parseColor("#FFFFFF"))
                        rLayoutBottomSheetListContact.animate().alpha(0F)
                            .setDuration(1000).startDelay = 0
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })
    }

<<<<<<< HEAD:app/src/main/java/com/example/ae_chat_sdk/acti/home/HomeFragment.kt
    private fun setData() {
        val gson = Gson()
        val type = object : TypeToken<User>() {}.type
        val appStorage = AppStorage.getInstance(requireContext())
        val userString: String = appStorage.getData("User", "").toString()
        val user = gson.fromJson<User>(userString, type)
        tvUserName.text = user.userName.toString()
        Log.d("Notify", user.userName.toString())

        if(user.avatar == null)
        {
            val imageUrl = "https://3.bp.blogspot.com/-SMNLs_5XfVo/VHvNUx8dWZI/AAAAAAAAQnY/NWdkO4JPE_M/s1600/Avatar-Facebook-an-danh-trang-4.jpg"
            Glide.with(this)
                .load(imageUrl)
                .into(avatarUser)
        }
        else
        {
            val imageUrl = ApiConstant.URL_AVATAR +user.avatar
            Log.d("link",imageUrl.toString())
            Glide.with(this)
                .load(imageUrl)
                .into(avatarUser)
        }


    }
    override fun onClick(view: View)
    {
        when (view?.id) {
            R.id.buttonLogOut -> {
                Handler(Looper.getMainLooper()).postDelayed({
                    v.findNavController().navigate(R.id.nav_graph)
                }, 1000)
                val appStorage = context?.let { AppStorage.getInstance(it) }
                val userString = appStorage?.clearData()
                Log.d("SHOW DATAAAAAA",userString.toString())
            }
        }
    }

=======
    private fun renderDataRecyclerView() {
        rvOnstream.layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        rvOnstream.adapter = OnstreamAdapter(onstream, context)
>>>>>>> UI:app/src/main/java/com/example/ae_chat_sdk/acti/home/HomeActivity.kt

        rvRecent.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvRecent.adapter = RecentAdapter(recent,context)

        rvListContact.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvListContact.adapter = ContactAdapter(contact, context)
        rvListContact.addItemDecoration(
            DividerItemDecoration(
                context, DividerItemDecoration.VERTICAL
            )
        )
    }


<<<<<<< HEAD:app/src/main/java/com/example/ae_chat_sdk/acti/home/HomeFragment.kt



=======
}
>>>>>>> UI:app/src/main/java/com/example/ae_chat_sdk/acti/home/HomeActivity.kt
