package com.example.myarcore

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.MotionEvent
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.core.view.GravityCompat
import com.google.ar.core.HitResult
import com.google.ar.core.Plane
import com.google.ar.sceneform.AnchorNode

import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

   lateinit private var arFragment: ArFragment
   lateinit private var andyRenderable: ModelRenderable





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//region Ustawienie Toolbara jako ActionBar + Obsluga klikniecia menu, ktore rozwija NavigationView
        setSupportActionBar(toolbar)
        val actionbar: ActionBar? = supportActionBar
        actionbar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu)
        }
        //endregion
//region Ustawienie Click Listenera w menu Navigation View
        nav_view.setNavigationItemSelectedListener { menuItem ->

            when (menuItem.itemId) {
                R.id.menu1 -> {
                    Toast.makeText(this, "menu1", Toast.LENGTH_SHORT).show()
                    menuItem.isChecked = true
                    drawer_layout.closeDrawers()
                    true
                }
                R.id.menu2 -> {
                    Toast.makeText(this, "menu2", Toast.LENGTH_SHORT).show()
                    menuItem.isChecked = true
                    drawer_layout.closeDrawers()
                    true
                }
                R.id.menu3 -> {
                    Toast.makeText(this, "menu3", Toast.LENGTH_SHORT).show()
                    menuItem.isChecked = true
                    drawer_layout.closeDrawers()
                    true
                }
                R.id.menu4 -> {
                    Toast.makeText(this, "menu4", Toast.LENGTH_SHORT).show()
                    menuItem.isChecked = true
                    drawer_layout.closeDrawers()
                    true
                }
                else -> false
            }
        }
//endregion

arFragment =supportFragmentManager.findFragmentById(R.id.ux_fragment) as ArFragment

         ModelRenderable.builder()
        .setSource(this, R.raw.andy)
        .build()
        .thenAccept({ renderable -> andyRenderable = renderable })
        .exceptionally(
        { throwable ->
        val toast = Toast.makeText(this, "Unable to load andy renderable", Toast.LENGTH_LONG)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
        null
        })


        arFragment.setOnTapArPlaneListener { hitResult, plane, motionEvent ->
            // Create the Anchor.
            val anchor = hitResult.createAnchor()
            val anchorNode = AnchorNode(anchor)
            anchorNode.setParent(arFragment.arSceneView.scene)

            // Create the transformable andy and add it to the anchor.
            val andy = TransformableNode(arFragment.transformationSystem)
            andy.setParent(anchorNode)
            andy.renderable = andyRenderable
            andy.select()



        }








    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //Rozwiniecie NavigationView po kliknieciu na home item w Toolbarze
        return when (item.itemId) {
            android.R.id.home -> {
                drawer_layout.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else
            super.onBackPressed()
    }
}
