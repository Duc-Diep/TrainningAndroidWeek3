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
    var thread: Thread? = null

    //    var threadPlus: Thread? = null
//    var threadSub: Thread? = null
    var handler: Handler? = null
    var isRunning: Boolean = false
    var count: Int = 0
    var pointY: Int = 0

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        handler = Handler(mainLooper)
        initThread()
//        btnPlus.setOnClickListener {
//            isRunning = false
//            plus()
//            countToZero()
//        }
//        btnSub.setOnClickListener {
//            isRunning = false
//            subtract()
//            countToZero()
//        }

        //tăng giảm khi vuốt màn hình
        layout_number.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> if (pointY == 0) {
                    pointY = event.y.toInt()
                    isRunning = false
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

        //Giữ để tăng giảm số
        btnPlus.setOnTouchListener(object : View.OnTouchListener {
            private var mHandler: Handler? = null
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        isRunning = false
                        if (mHandler != null) return true
                        mHandler = Handler(mainLooper)
//                        mHandler?.postDelayed(mAction,200)
                        mAction.run()
                    }
                    MotionEvent.ACTION_UP -> {
                        if (mHandler == null) return true
                        mHandler?.removeCallbacks(mAction)
                        mHandler = null
                        countToZero()
                    }
                    else -> return false
                }
                return false
            }

            var mAction: Runnable = object : Runnable {
                override fun run() {
                    if (mHandler != null) {
                        plus()
                        mHandler!!.postDelayed(this, 100)
                    }
                }
            }
        })
        btnSub.setOnTouchListener(object : View.OnTouchListener {
            private var mHandler: Handler? = null
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        isRunning = false
                        if (mHandler != null) return true
                        mHandler = Handler(mainLooper)
//                        mHandler?.postDelayed(mAction,100)
                        mAction.run()
                    }
                    MotionEvent.ACTION_UP -> {
                        if (mHandler == null) return true
                        mHandler?.removeCallbacks(mAction)
                        mHandler = null
                        countToZero()
                    }
                    else -> return false
                }
                return false
            }

            var mAction: Runnable = object : Runnable {
                override fun run() {
                    if (mHandler != null) {
                        subtract()
                        mHandler!!.postDelayed(this, 100)
                    }
                }
            }
        })
    }

    fun plus() {
        count = tvNumber.text.toString().toInt()
        count++
        tvNumber.text = count.toString()
        randomColor()
    }

    private fun subtract() {
        count = tvNumber.text.toString().toInt()
        count--
        tvNumber.text = count.toString()
        randomColor()
    }

    private fun randomColor() {
        tvNumber.setTextColor(
            Color.rgb(
                Random.nextInt(0, 255),
                Random.nextInt(0, 255),
                Random.nextInt(0, 255)
            )
        )
        btnPlus.setTextColor(
            Color.rgb(
                Random.nextInt(0, 255),
                Random.nextInt(0, 255),
                Random.nextInt(0, 255)
            )
        )
        btnSub.setTextColor(
            Color.rgb(
                Random.nextInt(0, 255),
                Random.nextInt(0, 255),
                Random.nextInt(0, 255)
            )
        )

    }

    private fun initThread() {
        thread = object : Thread() {
            override fun run() {
                sleep(2500)
                while (isRunning) {
                    sleep(100)
                    if (count == 0) {
                        return
                    }
                    if (count < 0)
                        count++
                    if (count > 0)
                        count--
                    handler?.post {
                        tvNumber.text = count.toString()
                    }
                }
            }
        }
        Thread {
            while (true) {
                Thread.sleep(2000)
                handler?.post {
                    randomColor()
                }
            }
        }.start()
    }

    private fun countToZero() {
        isRunning = true
        count = tvNumber.text.toString().toInt()
        if (count != 0 && !thread?.isAlive!!) {
            thread!!.start()
        }

    }

}