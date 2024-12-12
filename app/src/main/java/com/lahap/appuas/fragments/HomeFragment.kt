package com.lahap.appuas.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.lahap.appuas.R
import com.lahap.appuas.activities.MenuBottomSheetFragment
import com.lahap.appuas.adapter.ImageSliderAdapter
import com.lahap.appuas.adapter.MenuAdapter
import com.lahap.appuas.databinding.FragmentHomeBinding
import com.lahap.appuas.models.MenuItem

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var menuItems:MutableList<MenuItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.viewAllMenu.setOnClickListener {
            val bottomSheetDialog = MenuBottomSheetFragment()
            bottomSheetDialog.show(parentFragmentManager,"Test")
        }

        //retrieve and display popular item
        retrieveAndDisplayPopularItems()

        return binding.root
    }

    private fun retrieveAndDisplayPopularItems() {
        database = FirebaseDatabase.getInstance()
        val foodRef: DatabaseReference = database.reference.child("menu")
        menuItems = mutableListOf()

        //retrieve menu items from database
        foodRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //loop for through each food item
                for (foodSnapshot in snapshot.children){
                    val menuItem = foodSnapshot.getValue(MenuItem::class.java)
                    menuItem?.let {
                        menuItems.add(it)
                    }
                }
                randomPopularItems()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("DatabaseError", "Error: ${error.message}")
            }
        })
    }

    private fun randomPopularItems() {
        //create as shuffled list of menu items
        val index = menuItems.indices.toList().shuffled()
        val numItemToShow = 6
        val subsetMenuItems = index.take(numItemToShow).map { menuItems[it] }

        setPopularItemsAdapter(subsetMenuItems)
    }

    private fun setPopularItemsAdapter(subsetMenuItems: List<MenuItem>) {
        val adapter = MenuAdapter(subsetMenuItems,requireContext())
        binding.PopularRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.PopularRecyclerView.adapter = adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // List gambar untuk slider
        val images = listOf(
            R.drawable.banner1,
            R.drawable.banner2,
            R.drawable.banner3
        )

        // Atur RecyclerView sebagai slider
        val imageSlider = binding.imageSlider
        imageSlider.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        imageSlider.adapter = ImageSliderAdapter(images)

        // Auto-scroll dengan Handler
        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            var currentIndex = 0
            override fun run() {
                if (currentIndex == images.size) currentIndex = 0
                imageSlider.smoothScrollToPosition(currentIndex++) // Pindah ke item berikutnya
                handler.postDelayed(this, 3000) // Scroll setiap 3 detik
            }
        }
        handler.post(runnable)
    }




}