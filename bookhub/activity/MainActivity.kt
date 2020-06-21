package com.internshala.bookhub.activity
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.ActionMode
import android.view.MenuItem
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.internshala.bookhub.R
import com.internshala.bookhub.adapter.Global
import com.internshala.bookhub.util.CheckNetwork
import com.internshala.bookhub.util.GlobalVars

class MainActivity : AppCompatActivity() {
    lateinit var drawerlayout: DrawerLayout

    lateinit var toolbar: Toolbar
    lateinit var framelayout: FrameLayout
    lateinit var navigationview: NavigationView
    lateinit var appBarConfiguration: AppBarConfiguration

    var previousMenuItem:MenuItem?=null
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val network: CheckNetwork = CheckNetwork()
        network.registerDefaultNetworkCallback(this as Context)

        drawerlayout=findViewById(R.id.drawerlayout)

        toolbar=findViewById(R.id.toolbar)
        navigationview=findViewById(R.id.navigationview)
        val navController=findNavController(R.id.nav_host_fragment)
        setSupportActionBar(toolbar)
        appBarConfiguration= AppBarConfiguration(setOf(R.id.dashboard,R.id.favourite,R.id.profile),drawerlayout)
        setupActionBarWithNavController(navController,appBarConfiguration)
        navigationview.setupWithNavController(navController)




    }

    override fun onSupportNavigateUp(): Boolean {
        val navController=findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }










}









