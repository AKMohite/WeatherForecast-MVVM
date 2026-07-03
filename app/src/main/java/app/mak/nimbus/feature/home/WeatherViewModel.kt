package app.mak.nimbus.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.mak.nimbus.core.domain.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class WeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository
): ViewModel() {

    private val _uiState = MutableStateFlow<WeatherPagerUiState>(WeatherPagerUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        loadWeather()
    }

    private fun loadWeather() {
        viewModelScope.launch {
            weatherRepository.getCurrentWeather("", 19.888, 75.555)

        }
    }
}

sealed interface WeatherPagerUiState {
    data object Loading : WeatherPagerUiState
    data class Success(val cities: List<CityWeatherState>) : WeatherPagerUiState
    data class Error(val message: String) : WeatherPagerUiState
}

data class CityWeatherState(
    val cityId: String,
    val cityName: String,
//    val weatherState: WeatherUiState
)