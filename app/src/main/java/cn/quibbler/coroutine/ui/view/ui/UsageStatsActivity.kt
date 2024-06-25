package cn.quibbler.coroutine.ui.view.ui

import android.app.AppOpsManager
import android.app.usage.ConfigurationStats
import android.app.usage.EventStats
import android.app.usage.UsageEvents
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Process
import android.provider.Settings
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cn.quibbler.coroutine.R
import cn.quibbler.coroutine.databinding.ActivityUsageStatsBinding
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import java.util.Calendar


class UsageStatsActivity : AppCompatActivity() {

    companion object {
        const val TAG = "TAG_UsageStatsActivity"
    }

    private lateinit var binding: ActivityUsageStatsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUsageStatsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (checkUsageAccessPermission()) {
            aqueryUsage()
        } else {
            val intent: Intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
            startActivity(intent)
        }


        window.decorView.viewTreeObserver.isAlive
    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun aqueryUsage() {
        val usageStateManager: UsageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

        usageStateManager.apply {
            val calendar = Calendar.getInstance()
            val endTime = calendar.timeInMillis
            calendar.add(Calendar.DAY_OF_WEEK, -1)
            val startTime = calendar.timeInMillis

            val packageCntSet = HashSet<String>()

            val entries = ArrayList<PieEntry>()
            val packageManager = packageManager

            val usage: List<UsageStats> = queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime)
            Log.d(TAG, "usage size : ${usage.size} , package set : ${packageCntSet.size}")
            for (u in usage) {
                packageCntSet.add(u.packageName)
                if (u.totalTimeInForeground > 0L) {
                    val name = packageManager.getApplicationLabel(packageManager.getApplicationInfo(u.packageName, 0)).toString()
                    if (entries.size < 6) {
                        entries.add(PieEntry(u.totalTimeInForeground.toFloat(), name))
                    }
                    Log.d(
                        TAG,
                        "name $name package:${u.packageName} lastTimeUsed:${u.lastTimeUsed} lastTimeStamp:${u.lastTimeStamp}" +
                                " lastTimeVisible:${u.lastTimeVisible} totalTimeVisible:${u.totalTimeVisible} totalTimeInForeground:${u.totalTimeInForeground}"
                    )
                }
            }
            Log.d(TAG, "usage size : ${usage.size} , package set : ${packageCntSet.size}")


            //https://github.com/PhilJay/MPAndroidChart/blob/master/MPChartExample/src/main/java/com/xxmassdeveloper/mpchartexample/PieChartActivity.java
            val pieSet = PieDataSet(entries, "queryUsageStats")

            // add a lot of colors
            val colors = ArrayList<Int>()
            for (c in ColorTemplate.VORDIPLOM_COLORS) colors.add(c)
            for (c in ColorTemplate.JOYFUL_COLORS) colors.add(c)
            for (c in ColorTemplate.COLORFUL_COLORS) colors.add(c)
            for (c in ColorTemplate.LIBERTY_COLORS) colors.add(c)
            for (c in ColorTemplate.PASTEL_COLORS) colors.add(c)
            pieSet.colors = colors

            val data = PieData(pieSet)
            data.setValueFormatter(PercentFormatter())

            binding.pieChart.data = data
            binding.pieChart.setEntryLabelColor(Color.BLUE)
            binding.pieChart.description.text = "应用使用情况"
            binding.pieChart.centerText = "queryUsageStats"
            binding.pieChart.highlightValue(null)
            binding.pieChart.invalidate()

            val aggregateUsage: Map<String, UsageStats> = queryAndAggregateUsageStats(startTime, endTime)
            val aggregateList = ArrayList<UsageStats>()
            //https://github.com/PhilJay/MPAndroidChart/blob/master/MPChartExample/src/main/java/com/xxmassdeveloper/mpchartexample/HorizontalBarChartActivity.java
            val listBarEntry = ArrayList<BarEntry>()
            var cnt = 1
            for (aU in aggregateUsage) {
                if (aU.value.totalTimeInForeground > 0) {
                    aggregateList.add(aU.value)
                }
            }
//            aggregateList.sortByDescending {
//                it.lastTimeStamp
//            }
            for (u in aggregateList) {
                Log.d("$TAG aggregateList", "${u.packageName} ${u.totalTimeInForeground}")
                if (u.totalTimeInForeground > 0) {
                    val icon = packageManager.getApplicationIcon(packageManager.getApplicationInfo(u.packageName, 0))
                    listBarEntry.add(BarEntry((cnt++).toFloat(), u.totalTimeInForeground.toFloat() / 1000 / 60 / 60, icon))
                    Log.d("$TAG aggregateList", " -------- ${u.packageName} ${u.totalTimeInForeground} icon:${icon}")
                }
                if (listBarEntry.size > 6) {
                    break
                }
            }
            val barDataSet = BarDataSet(listBarEntry, "queryAndAggregateUsageStats")
            val barData = BarData(barDataSet)
            barData.dataSetLabels
            binding.barChart.apply {
                this.data = barData
                description.isEnabled = false
                invalidate()
            }


            val event: UsageEvents = queryEvents(startTime, endTime)
            Log.d("${TAG}_UsageEvents", "event :${event}")
            while (event.hasNextEvent()) {
                val e = UsageEvents.Event()
                event.getNextEvent(e)
                Log.d("${TAG}_UsageEvents", "e :${e.eventType} ${e.packageName} ${e.className}")
            }

            val selfEvent: UsageEvents = queryEventsForSelf(startTime, endTime)
            while (selfEvent.hasNextEvent()) {
                val e = UsageEvents.Event()
                selfEvent.getNextEvent(e)
                Log.d("${TAG}_selfEvent", "e :${e.eventType} ${e.packageName} ${e.className}")
            }


            /**
             *      * <ul>
             *      *     <li>{@link UsageEvents.Event#SCREEN_INTERACTIVE}</li>
             *      *     <li>{@link UsageEvents.Event#SCREEN_NON_INTERACTIVE}</li>
             *      *     <li>{@link UsageEvents.Event#KEYGUARD_SHOWN}</li>
             *      *     <li>{@link UsageEvents.Event#KEYGUARD_HIDDEN}</li>
             */
            val eventStatus: List<EventStats> = queryEventStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime)
            for (e in eventStatus) {

                Log.d("${TAG}_EventStats", "e :${e.eventType} ${e.lastEventTime} ${e.lastTimeStamp}")
            }


            val config: List<ConfigurationStats> = queryConfigurations(UsageStatsManager.INTERVAL_DAILY, startTime, endTime)
            Log.d("${TAG}_Config", "config :${config.size}")
            val setOfConfig = HashSet<String>()
            for (c in config) {
                setOfConfig.add(c.configuration.toString())
                Log.d("${TAG}_Config", "c ${c.configuration} ${c.activationCount}")
            }
            Log.d("${TAG}_Config", "config :${config.size} ${setOfConfig.size}")

        }
        Log.d(TAG, "aqueryUsage---")
    }

    //https://www.cnblogs.com/baiqiantao/p/12758115.html
    fun checkUsageAccessPermission(): Boolean {
        val appOpsManager: AppOpsManager = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, Process.myUid(), packageName)
        Log.d(TAG, "checkUsageAccessPermission mode $mode")
        return when (mode) {
            AppOpsManager.MODE_ALLOWED -> true
            AppOpsManager.MODE_DEFAULT -> checkCallingPermission(android.Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED
            else -> false
        }
    }

}