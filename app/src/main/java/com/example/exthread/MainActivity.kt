package com.example.exthread

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    lateinit var threadPlus: Thread
    lateinit var threadSub: Thread
    var handler: Handler? = null
    var count: Int = 0
    var pointY: Int = 0;

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initThread()
        btnPlus.setOnClickListener {
            plus()
            countToZero()
        }
        btnSub.setOnClickListener {
            subtract()
            countToZero()
        }

        //tăng giảm khi vuốt màn hình
        layout_number.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> if (pointY == 0) {
                    pointY = event.y.toInt()
                    Log.d("pointY", "Click:y=$pointY")
                }
                MotionEvent.ACTION_MOVE -> if (event.y < pointY) {
                    plus()
                } else {
                    subtract()
                }
                MotionEvent.ACTION_UP -> {
                    pointY = 0
                    Log.d("pointY", "y=$pointY")
                    countToZero()
                }
            }
            true
        }

//        btnPlus.setOnTouchListener(object : View.OnTouchListener {
//            private var mHandler: Handler? = null
//            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
//                when (event?.action) {
//                    MotionEvent.ACTION_DOWN -> {
//                        if (mHandler != null) return true
//                        mHandler = Handler(mainLooper)
//                        mAction.run()
//                    }
//                    MotionEvent.ACTION_UP -> {
//                        if (handler == null) return true
//                        handler?.removeCallbacks(mAction)
//                        handler = null
//                    }
//                    else -> return false
//                }
//                return false
//            }
//
//            var mAction: Runnable = object : Runnable {
//                override fun run() {
//                    if (handler != null) {
//                        plus()
//                        mHandler!!.postDelayed(this, 100)
//                    }
//                }
//            }
//        })
    }

    fun plus() {
        count = tvNumber.text.toString().toInt()
        count++
        tvNumber.text = count.toString()
        if (count >= 100) {
            tvNumber.setTextColor(
                Color.rgb(
                    Random.nextInt(0, 255),
                    Random.nextInt(0, 255),
                    Random.nextInt(0, 255)
                )
            )
        }
    }

    private fun subtract() {
        count = tvNumber.text.toString().toInt()
        count--
        tvNumber.text = count.toString()
        if (count >= 100) {
            tvNumber.setTextColor(
                Color.rgb(
                    Random.nextInt(0, 255),
                    Random.nextInt(0, 255),
                    Random.nextInt(0, 255)
                )
            )
        }
    }

    fun initThread(){
        threadPlus = object : Thread() {
            override fun run() {
                sleep(2000)
                while (count != 0) {
                    count++
                    handler?.post {
                        tvNumber.text = count.toString()
                    }
                    sleep(100)
                }
                if (count==0){
                    handler = null
                    return
                }
            }
        }
        threadSub = object : Thread() {
            override fun run() {
                sleep(2000)
                while (count != 0) {
                    count--
                    handler?.post {
                        tvNumber.text = count.toString()
                    }
                    sleep(100)
                }
                if (count==0){
                    handler = null
                    return
                }
            }
        }
    }

    private fun countToZero() {
        count = tvNumber.text.toString().toInt()
        handler = Handler(mainLooper)
        if (count > 0 && !threadSub.isAlive) {
            threadSub.start()
        }
        if (count < 0 && !threadPlus.isAlive) {
                threadPlus.start()
        }

    }

}