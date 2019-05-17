package demo.moqi.sample

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import demo.moqi.sample.act.BetterButtonDemo
import demo.moqi.sample.act.TaggedSeekBarDemo
import demo.moqi.sample.act.WaveDemoAct
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnWaveView.setOnClickListener{
            startActivity(Intent(this, WaveDemoAct::class.java))
        }

        btnButtonDemo.setOnClickListener {
            startActivity(Intent(this, BetterButtonDemo::class.java))
        }

        btnTSBDemo.setOnClickListener {
            startActivity(Intent(this, TaggedSeekBarDemo::class.java))
        }
    }
}
