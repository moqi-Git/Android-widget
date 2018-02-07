import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.CountDownTimer
import android.util.AttributeSet
import android.view.View


class WaveView@JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {


    private val paint:Paint = Paint()
    public var mBgColor = Color.TRANSPARENT
    public var mWaveColor = Color.WHITE

    public var mRadiusMin = 90f
    public var mRadiusMax = 1080f
    public var mWaveInterval = 240f
    //用每次扩散半径增加的值作为速度参数
    public var speed = 14f

    private val mWaveList = ArrayList<Float>()

    private val timeline = object:CountDownTimer(300000,16){

        override fun onTick(millisUntilFinished: Long) {

            // 每个时间间隔都把正在扩散的波纹半径增加
            for (i in 0 until mWaveList.size){
                mWaveList[i] = mWaveList[i] + speed
                if (mWaveList[i] < mRadiusMin + mWaveInterval){
                    break
                }
            }

            //最外层波纹超过最大值时，重新把它添加到波纹队列末尾
            if (mWaveList[0] > mRadiusMax){
                mWaveList[0] = mRadiusMin
                val newList = transList(mWaveList)
                mWaveList.clear()
                mWaveList.addAll(newList)
            }

            invalidate()
        }

        override fun onFinish() {
            //尽量保证手动调用waveStop，就不会执行到这里
            reset()
        }
    }

    init {
        initWave(mWaveList)
        paint.color = mWaveColor
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas!!.drawColor(mBgColor)
        val centerX = canvas.width.div(2).toFloat()
        val centerY = canvas.height.div(2).toFloat()

        for (i in 0 until mWaveList.size){
            paint.alpha = calcAlpha(mWaveList[i])
            canvas.drawCircle(centerX, centerY, mWaveList[i], paint)
        }
    }

    fun wave(){
        timeline.start()
    }

    fun stopWave(){
        timeline.cancel()
    }

    fun reset(){
        timeline.cancel()
        mWaveList.clear()
        initWave(mWaveList)
    }

    private fun initWave(waveList: ArrayList<Float>){
        val waveNum = ((mRadiusMax-mRadiusMin)/mWaveInterval).toInt() + 2
        for (i in 1..waveNum){
            waveList.add(mRadiusMin)
        }
    }

    private fun transList(list: ArrayList<Float>):ArrayList<Float>{
        val newList = ArrayList<Float>()

        (1 until list.size).mapTo(newList) { list[it] }
        newList.add(list[0])
        return newList
    }

    // 通过半径计算透明度，趋势是半径越大越透明，直到看不见
    private fun calcAlpha(r:Float):Int = ((mRadiusMax - r)/ mRadiusMax * 120).toInt()
}