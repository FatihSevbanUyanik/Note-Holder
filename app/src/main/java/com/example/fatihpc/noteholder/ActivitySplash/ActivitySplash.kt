package com.example.fatihpc.noteholder.ActivitySplash

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import com.example.fatihpc.noteholder.ActivityMain.ActivityMain
import com.example.fatihpc.noteholder.R

class ActivitySplash : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

    override fun onResume() {
        super.onResume()

        // starting count down timer and at the end directing to main activity.
        object: CountDownTimer(4000, 1000) {

            override fun onFinish() {
                startActivity( Intent(this@ActivitySplash, ActivityMain::class.java) )
            }

            override fun onTick(p0: Long) {
                // on tick
            }

        }.start()
    }

}
