package app.mak.atmosense.feature.cities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.mak.atmosense.core.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

internal class CityManagementPresenter(
  private val weatherRepository: WeatherRepository
) : ViewModel() {
  val state = flow {
    emit(weatherRepository.getCurrentWeather())
  }.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = "Empty"
  )
}
