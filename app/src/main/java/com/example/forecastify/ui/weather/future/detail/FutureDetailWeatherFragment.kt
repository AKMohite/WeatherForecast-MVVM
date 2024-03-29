package com.example.forecastify.ui.weather.future.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.forecastify.R
import com.example.forecastify.internal.DateNotFoundException
import com.example.forecastify.internal.glide.GlideApp
import com.example.forecastify.ui.base.ScopedFragment
import com.example.forecastify.utils.LocalDateConverter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.future_detail_weather_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle

@AndroidEntryPoint
class FutureDetailWeatherFragment : ScopedFragment() {

    private val viewModel: FutureDetailWeatherViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.future_detail_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val safeArgs = arguments?.let { FutureDetailWeatherFragmentArgs.fromBundle(it) }
        val date = LocalDateConverter.stringToDate(safeArgs?.dateString) ?: throw DateNotFoundException()

//        todo call detail api
        viewModel.getDetail(date)
        bindUI()
    }

    private fun bindUI() = launch(Dispatchers.Main) {
        val weatherLocation = viewModel.weatherLocation.await()

        weatherLocation.observe(viewLifecycleOwner, Observer { location ->
            if (location == null) return@Observer
            updateLocation(location.name)
        })

        viewModel.weather.observe(viewLifecycleOwner, Observer { weatherEntry ->
            if (weatherEntry == null) return@Observer

            updateDate(weatherEntry.date)
            updateTemperatures(weatherEntry.avgtemp,
                weatherEntry.mintemp, weatherEntry.maxtemp)
            updateCondition(weatherEntry.conditionText)
            updatePrecipitation(weatherEntry.precip)
            updateWindSpeed(weatherEntry.maxWindSpeed)
            updateVisibility(weatherEntry.avgVisibilityDistance)
            updateUv(weatherEntry.uvIndex)

            GlideApp.with(this@FutureDetailWeatherFragment)
                .load(weatherEntry.conditionIconUrl)
                .into(imageView_condition_icon)
        })
    }

    private fun chooseLocalizedUnitAbbreviation(metric: String, imperial: String): String {
        return if (viewModel.isMetricUnit) metric else imperial
    }

    private fun updateLocation(location: String) {
        (activity as? AppCompatActivity)?.supportActionBar?.title = location
    }

    private fun updateDate(date: LocalDate) {
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle =
            date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
    }

    private fun updateTemperatures(temperature: Double, min: Double, max: Double) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("°C", "°F")
        textView_temperature.text = "$temperature$unitAbbreviation"
        textView_min_max_temperature.text = "Min: $min$unitAbbreviation, Max: $max$unitAbbreviation"
    }

    private fun updateCondition(condition: String) {
        textView_condition.text = condition
    }

    private fun updatePrecipitation(precipitationVolume: Double) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("mm", "in")
        textView_precipitation.text = "Precipitation: $precipitationVolume $unitAbbreviation"
    }

    private fun updateWindSpeed(windSpeed: Double) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("kph", "mph")
        textView_wind.text = "Wind speed: $windSpeed $unitAbbreviation"
    }

    private fun updateVisibility(visibilityDistance: Double) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("km", "mi.")
        textView_visibility.text = "Visibility: $visibilityDistance $unitAbbreviation"
    }

    private fun updateUv(uv: Double) {
        textView_uv.text = "UV: $uv"
    }

}
