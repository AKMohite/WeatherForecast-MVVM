package app.mak.atmosense.core.data.testing

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeDataStore<T>(initialValue: T) : DataStore<T> {
  private val _data = MutableStateFlow(initialValue)
  override val data: Flow<T> = _data

  override suspend fun updateData(transform: suspend (t: T) -> T): T {
    val currentData = _data.value
    val newData = transform(currentData)
    _data.value = newData
    return newData
  }
}
