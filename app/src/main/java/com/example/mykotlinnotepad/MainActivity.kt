package com.example.mykotlinnotepad

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.*
import com.example.mykotlinnotepad.models.Notes
import com.example.mykotlinnotepad.models.NotesAdapter
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit  var drawerLayout: DrawerLayout
    private lateinit  var navigationView: NavigationView
    private lateinit var menuDrawer: ImageView
    private lateinit var layoutDashboard : LinearLayout
    private lateinit var rvStore : RecyclerView
    private var endScale : Float = 1.8f
    private lateinit var adapter: NotesAdapter
    val titles: List<String> = listOf("First Note Title","Second Note Title","Third Note Title","Fourth Note Title")
    var content: List<String> = listOf("First Note Content Samples","Second Note Content Samples","Third Note Content Samples","Fourth Note Content Samples")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.layoutDrawer)
        navigationView = findViewById(R.id.navigation_view)
        menuDrawer = findViewById(R.id.imgDrawerMenu)
        layoutDashboard = findViewById(R.id.linearMainActivity)
        rvStore = findViewById(R.id.rcViewStore)

        rvStore.layoutManager = LinearLayoutManager(this)
        rvStore.layoutManager = GridLayoutManager(this,2)

        adapter = NotesAdapter(titles,content)
        rvStore.adapter = adapter
        rvStore.apply {
            rvStore.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
            rvStore.adapter = adapter
        }

        Toast.makeText(this,"Array : " + titles.toString(), Toast.LENGTH_LONG).show()
        navigationDrawerSetting()
    }

    private fun navigationDrawerSetting() {
        navigationView.bringToFront()
        navigationView.setNavigationItemSelectedListener(this)
        navigationView.setCheckedItem(R.id.nav_home)
        menuDrawer.setOnClickListener {
            if (drawerLayout.isDrawerVisible(GravityCompat.START))
                drawerLayout.closeDrawer(GravityCompat.START)
            else drawerLayout.openDrawer(GravityCompat.START)
        }
        navigationDrawerAnimation()
    }

    private fun navigationDrawerAnimation() {
        drawerLayout.setScrimColor(resources.getColor(R.color.chocolate))
        drawerLayout.addDrawerListener(object : DrawerLayout.SimpleDrawerListener(){
            override fun onDrawerSlide(@NonNull drawerView: View, slideOffSet: Float){
                val diffScaleOffSet = slideOffSet * (1 - endScale)
                val offSetScale = 1 - diffScaleOffSet
                layoutDashboard.setScaleX(offSetScale)
                layoutDashboard.setScaleY(offSetScale)
                //Translate View to scale width
                val xOffSet = drawerView.getWidth() * slideOffSet
                val xOffSetDiff = layoutDashboard.getWidth() * diffScaleOffSet / 2
                val xTransition = xOffSet - xOffSetDiff
                layoutDashboard.setTranslationX(xTransition)
            }
        })


    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return true
    }

    override fun onBackPressed() {
        if(drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else super.onBackPressed()
    }
}

