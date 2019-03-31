package sk.spacecode.silentkosice

import com.github.mikephil.charting.formatter.ValueFormatter

class ChartFormatter : ValueFormatter() {

    override fun getFormattedValue(value: Float): String {
        return "" + value.toInt()
    }

}