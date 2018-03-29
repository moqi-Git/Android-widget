package demo.moqi.sample.act

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import demo.moqi.sample.R
import kotlinx.android.synthetic.main.activity_button_demo.*

/**
 * Created by reol on 29/03/2018.
 */
class BetterButtonDemo: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_button_demo)

        button1.setOnClickListener {
            Toast.makeText(this, "示例按钮一", Toast.LENGTH_SHORT).show()
        }

        button2.setOnClickListener {
            Toast.makeText(this, "示例按钮一", Toast.LENGTH_SHORT).show()

        }

        button3.setOnClickListener {
            Toast.makeText(this, "示例按钮一", Toast.LENGTH_SHORT).show()
        }
    }
}