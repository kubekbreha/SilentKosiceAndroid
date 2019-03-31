package sk.spacecode.silentkosice

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.android.synthetic.main.activity_profile.*


class ProfileActivity : AppCompatActivity() {

    var entries: List<Entry> = arrayListOf(
        Entry(1F, 1F), Entry(2F, 5F),
        Entry(3F, 10F), Entry(4F, 8F), Entry(5F, 1F),
        Entry(6F, 0F), Entry(7F, 8F)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        generateChart()

        back_icon.setOnClickListener {
            val myIntent = Intent(this@ProfileActivity, RecordActivity::class.java)
            this@ProfileActivity.startActivity(myIntent)
        }

        progress_bar_1.animateProgress(2000, 0, 80)
        progress_bar_2.animateProgress(2000, 0, 20)

    }

    private fun generateChart() {
        val dataSet = LineDataSet(entries, null)
        val lineData = LineData(dataSet)
        dataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        dataSet.setDrawFilled(true)
        dataSet.fillColor = ContextCompat.getColor(this, R.color.colorPrimary)
        dataSet.color = ContextCompat.getColor(this, R.color.colorPrimary)
        dataSet.valueTextSize = 10f
        dataSet.fillAlpha = 255
        dataSet.circleColors = arrayListOf(ContextCompat.getColor(this, R.color.gray))
        dataSet.valueFormatter = ChartFormatter()

        chart.data = lineData
        chart.setTouchEnabled(false)
        chart.axisRight.setDrawLabels(false)
        chart.xAxis.setDrawGridLines(false)
        chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        chart.axisLeft.setDrawGridLines(false)
        chart.axisLeft.isEnabled = false
        chart.axisRight.setDrawGridLines(false)
        chart.axisRight.isEnabled = false
        chart.legend.isEnabled = false
        chart.description.isEnabled = false
        chart.animateY(2000)
    }


}
