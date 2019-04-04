package com.example.myarcore

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.core.view.GravityCompat
import com.google.ar.core.*
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.FrameTime
import com.google.ar.sceneform.Scene
import com.google.ar.sceneform.math.Vector3

import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.BaseArFragment
import com.google.ar.sceneform.ux.TransformableNode
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), View.OnClickListener {

    lateinit private var arFragment: ArFragment

    lateinit var arrayView: Array<View>

    lateinit var andyRenderable: ModelRenderable
    lateinit var uhRenderable: ModelRenderable
    lateinit var tankRenderable: ModelRenderable
    lateinit var soldierRenderable: ModelRenderable

    internal var selected = 1 //default 1 is andy

    lateinit var mAnchorNode: AnchorNode
    lateinit var btnLine: Button


    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.andy_image -> {
                selected = 1
                mySetBackground(view!!.id)
            }
            R.id.uh_image -> {
                selected = 2
                mySetBackground(view!!.id)
            }
            R.id.soldier_image -> {
                selected = 3
                mySetBackground(view!!.id)
            }
            R.id.tank_image -> {
                selected = 4
                mySetBackground(view!!.id)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnLine = findViewById(R.id.btnLine)
        setSupportActionBar(toolbar)
        setupArray()
        setupClickListener()
        setupModel()
        arFragment = supportFragmentManager.findFragmentById(R.id.ux_fragment) as ArFragment

        btnLine.setOnClickListener({ v -> onUpdate() })

/*
        arFragment.setOnTapArPlaneListener { hitResult, plane, motionEvent ->
            // Create the Anchor.
          //  val anchor = hitResult.createAnchor()
         val anchor=Session(this).createAnchor(Session(this).update().camera.pose.compose(Pose.makeTranslation(0f,0f,-1f)).extractTranslation())
         // val anchor = hitResult.trackable.createAnchor(hitResult.hitPose.compose(Pose.makeTranslation(0f,0.2f,0f)))//20cm nad hit pointem
            val anchorNode = AnchorNode(anchor)
            anchorNode.setParent(arFragment.arSceneView.scene)
            createModel(anchorNode,selected)
        }
*/

    }
/*
    private fun onUpdateRender() {
        arFragment.arSceneView.scene.addOnUpdateListener({ this.onUpdate() })
    } */

    private fun onUpdate() {

        if ((arFragment.getArSceneView().getArFrame()!!.getCamera().getTrackingState() !== TrackingState.TRACKING))
        {
           Toast.makeText(this,"Tracking not working",Toast.LENGTH_SHORT).show()
            return
        }



            val session = arFragment.getArSceneView().getSession()
            val cameraPos = arFragment.getArSceneView().getScene().getCamera().getWorldPosition()
            val cameraForward = arFragment.getArSceneView().getScene().getCamera().getForward()
            val position = Vector3.add(cameraPos, cameraForward.scaled(1.0f))

            val pose = Pose.makeTranslation(position.x, position.y, position.z)
            val anchor = arFragment.arSceneView.session!!.createAnchor(pose)
            mAnchorNode = AnchorNode(anchor)
            mAnchorNode.setParent(arFragment.getArSceneView().getScene())
            createModel(mAnchorNode, selected)

    }

    private fun createModel(anchorNode: AnchorNode, selected: Int) {
        when (selected) {
            1 -> {
                val andy = TransformableNode(arFragment.transformationSystem)
                andy.setParent(anchorNode)
                andy.renderable = andyRenderable
               andy.select()

            }
            2 -> {
                val uh = TransformableNode(arFragment.transformationSystem)
                uh.setParent(anchorNode)
                uh.renderable = uhRenderable
                uh.select()

            }
            3 -> {
                val soldier = TransformableNode(arFragment.transformationSystem)
                soldier.setParent(anchorNode)
                soldier.renderable = soldierRenderable
                soldier.rotationController.rotationRateDegrees = 90f
                soldier.select()

            }
            4 -> {
                val tank = TransformableNode(arFragment.transformationSystem)
                tank.setParent(anchorNode)
                tank.renderable = tankRenderable

                tank.select()
            }
            else -> {
                val toast = Toast.makeText(this, "Nie udało się pobrać modelu", Toast.LENGTH_LONG)
                toast.setGravity(Gravity.CENTER, 0, 0)
                toast.show()
            }
        }
    }

    private fun mySetBackground(id: Int) {

        for (i in arrayView.indices) {

            if (arrayView[i].id == id)
                arrayView[i].setBackgroundColor(Color.parseColor("#80333639"))
            else
                arrayView[i].setBackgroundColor(Color.TRANSPARENT)

        }


    }

    private fun setupModel() {
        ModelRenderable.builder()
            .setSource(this, R.raw.uh60)
            .build()
            .thenAccept({ renderable -> uhRenderable = renderable })
            .exceptionally(
                { throwable ->
                    val toast = Toast.makeText(this, "Unable to load andy renderable", Toast.LENGTH_LONG)
                    toast.setGravity(Gravity.CENTER, 0, 0)
                    toast.show()
                    null
                })
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
        ModelRenderable.builder()
            .setSource(this, R.raw.leo)
            .build()
            .thenAccept({ renderable -> tankRenderable = renderable })
            .exceptionally(
                { throwable ->
                    val toast = Toast.makeText(this, "Unable to load andy renderable", Toast.LENGTH_LONG)
                    toast.setGravity(Gravity.CENTER, 0, 0)
                    toast.show()
                    null
                })
        ModelRenderable.builder()
            .setSource(this, R.raw.sold)
            .build()
            .thenAccept({ renderable -> soldierRenderable = renderable })
            .exceptionally(
                { throwable ->
                    val toast = Toast.makeText(this, "Unable to load andy renderable", Toast.LENGTH_LONG)
                    toast.setGravity(Gravity.CENTER, 0, 0)
                    toast.show()
                    null
                })
    }

    private fun setupClickListener() {
        for (i in arrayView.indices) {

            arrayView[i].setOnClickListener(this)
        }
    }

    private fun setupArray() {
        arrayView = arrayOf(
            soldier_image,
            andy_image,
            tank_image,
            uh_image
        )
    }

    //region USTAWIENIA MENU W TOOLBARZE
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_navigation, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.menu1 -> {
                Toast.makeText(this, "menu1", Toast.LENGTH_SHORT).show()
                item?.isChecked = true

                true
            }
            R.id.menu2 -> {
                Toast.makeText(this, "menu2", Toast.LENGTH_SHORT).show()
                item?.isChecked = true

                true
            }
            R.id.menu3 -> {
                Toast.makeText(this, "menu3", Toast.LENGTH_SHORT).show()
                item?.isChecked = true

                true
            }
            R.id.menu4 -> {
                Toast.makeText(this, "menu4", Toast.LENGTH_SHORT).show()
                item?.isChecked = true

                true
            }
            else -> false
        }


    }
//endregion

}
