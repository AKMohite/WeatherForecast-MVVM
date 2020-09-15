# WeatherForecast-MVVM

Weather Playlist on youtube: [Forecast App - Android Kotlin MVVM Tutorial Course](https://www.youtube.com/playlist?list=PLB6lc7nQ1n4jTLDyU2muTBo8xk0dg0D_w)  
:roller_coaster: This repo is under development :roller_coaster:  
### API key  
[weatherstack](https://weatherstack.com/documentation)


API key need to be set in your `.gradle/gradle.properties`:

```
# Get it from weatherstack and set in variable
WEATHER_API_KEY=<insert>
```  
### Note
[Weather forecast](https://weatherstack.com/documentation#weather_forecast) has been mocked and passed as string response since it is not free [[source](/app/src/main/java/com/example/forecastify/data/network/WeatherNetworkDataSourceImpl.kt#L41)]
