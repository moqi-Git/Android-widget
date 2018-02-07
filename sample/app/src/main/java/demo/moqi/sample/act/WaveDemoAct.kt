package demo.moqi.sample.act

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import demo.moqi.sample.R
import kotlinx.android.synthetic.main.activity_wave_demo.*

class WaveDemoAct : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wave_demo)

        waveview.mBgColor = Color.parseColor("#66ccff")
        waveview.mRadiusMin = 120f


        waveview.setOnClickListener {
            waveview.wave()
        }
    }
}
