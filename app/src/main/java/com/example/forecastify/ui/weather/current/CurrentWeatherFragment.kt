package com.example.forecastify.ui.weather.current

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.forecastify.R
import com.example.forecastify.internal.glide.GlideApp
import com.example.forecastify.ui.base.ScopedFragment
import kotlinx.android.synthetic.main.current_weather_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.support.closestKodein
import org.kodein.di.generic.instance

class CurrentWeatherFragment : ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()

    private val viewModelFactory: CurrentWeatherViewModelFactory by instance()

    private lateinit var viewModel: CurrentWeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.current_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(CurrentWeatherViewModel::class.java)
        bindUI()
    }

    private fun bindUI()= launch{
        val currentWeather = viewModel.weather.await()

        val weatherLocation = viewModel.weatherLocation.await()

        weatherLocation.observe(this@CurrentWeatherFragment, Observer { location ->
            if(location == null) return@Observer
            updateLocation(location.name)
        })

        currentWeather.observe(this@CurrentWeatherFragment, Observer {
            if (it == null) return@Observer

            group_loading.visibility = View.GONE
            updateDateToday()
            it.apply {
                updateTemperatures(temperature, feelslike)
                updateCondition(weatherDescriptions.joinToString(" ")) //todo hande description
                updatePrecipitation(precip)
                updateWind(windDir, windSpeed)
                updateVisibility(visibility)
                GlideApp.with(this@CurrentWeatherFragment)
                    .load(weatherIcons?.get(0))
                    .into(imageView_condition_icon)
            }
        })
    }

    private fun chooseLocalisedUnitAbbreviation(metric: String, imperial: String): String{
        return if (viewModel.isMetricUnit) metric else imperial
    }

    private fun updateLocation(location: String){
        (activity as? AppCompatActivity)?.supportActionBar?.title = location
    }

    private fun updateDateToday(){
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = "Today"
    }

    private fun updateCondition(condition: String){
        textView_condition.text = condition

    }

    private fun updateTemperatures(temperature: Double, feelsLike: Double){
        val unitAbbreviation = chooseLocalisedUnitAbbreviation("°C", "°F") // todo not converted and saved
        textView_temperature.text = "$temperature$unitAbbreviation"
        textView_feels_like_temperature.text = "Feels like $feelsLike$unitAbbreviation"
    }

    private fun updatePrecipitation(precipitation: Double){
        val unitAbbreviation = chooseLocalisedUnitAbbreviation("mm", "in") // todo not converted and saved
        textView_precipitation.text = "Precipitation: $precipitation $unitAbbreviation"

    }

    private fun updateWind(windDirection: String, windSpeed: Double){
        val unitAbbreviation = chooseLocalisedUnitAbbreviation("kmph", "mph") // todo not converted and saved
        textView_wind.text = "Wind: $windDirection $windSpeed $unitAbbreviation"

    }

    private fun updateVisibility(visibilityDistance: Double){
        val unitAbbreviation = chooseLocalisedUnitAbbreviation("km", "mi") // todo not converted and saved
        textView_visibility.text = "Visibility: $visibilityDistance $unitAbbreviation"

    }

}
