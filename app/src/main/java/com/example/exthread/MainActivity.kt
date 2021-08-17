package com.example.exthread

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.MotionEvent
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
    val MESSAGE_COUNT:Int = 1
    lateinit var thread:Thread
    lateinit var handler:Handler
    var count:Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        handler = Handler(mainLooper)
        thread = Thread()
        btnPlus.setOnClickListener {
            plus()
            countToZero()
        }
        btnSub.setOnClickListener {
            subtract()
            countToZero()
        }

//        btnPlus.setOnTouchListener(object : View.OnTouchListener {
//            private var handle: Handler? = null
//            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
//                when (event?.action) {
//                    MotionEvent.ACTION_UP -> {
//                        if (handle != null) return true
//                        handle = Handler(Looper.getMainLooper())
//                        handle?.postDelayed(mAction, 50)
//                    }
//                    MotionEvent.ACTION_DOWN ->{
//
//                    }
//                    MotionEvent.ACTION_CANCEL ->{
//                        if (handle != null) return true
//                        handle?.removeCallbacks(mAction)
//                        handle = null
//                    }
//
//                }
//                return false
//
//            }
//
//            var mAction = Runnable{
//                plus()
//            }
//
//
//
//
//        })
    }
    fun plus(){
        count = tvNumber.text.toString().toInt()
        count++
        tvNumber.text = count.toString()
//        Thread.sleep(2000)
//        while (count!=0){
//            plus()
//            Thread.sleep(10)
//        }
    }
    private fun subtract(){
        count = tvNumber.text.toString().toInt()
        count--
        tvNumber.text = count.toString()
    }


    private fun countToZero(){
        count = tvNumber.text.toString().toInt()
        if (count>0){
            thread = Thread {
                Thread.sleep(3000)
                while (count!=0){
                    count--
                    handler.post {
                        tvNumber.text = count.toString()
                    }
                    Thread.sleep(100)
                }
            }
            if (!thread.isAlive)
            thread.start()
        }
        if (count<0){
            thread = Thread {
                Thread.sleep(3000)
                while (count!=0){
                    count++
                    handler.post {
                        tvNumber.text = count.toString()
                    }
                    Thread.sleep(100)
                }
                if (count==0) return@Thread
            }
            if (!thread.isAlive)
                thread.start()
        }

    }

}