package com.tutorial.runningapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.tutorial.runningapp.databinding.ActivityStopWatchBinding
import com.tutorial.runningapp.stopwatch.StopwatchListOrchestrator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class StopWatchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStopWatchBinding

    @Inject
    lateinit var stopWatchListOrchestrator: StopwatchListOrchestrator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStopWatchBinding.inflate(layoutInflater)
        setContentView(binding.root)


        lifecycleScope.launchWhenCreated {
            stopWatchListOrchestrator.tickerInMilli.collect {
                Timber.d("Stop Watch ticker: $it")
            }
        }

        binding.fab.setOnClickListener { view ->
            stopWatchListOrchestrator.start()
        }
    }

}