package ryanhurst.weather.wiring

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ryanhurst.weather.data.WeatherService
import ryanhurst.weather.data.WeatherRepositoryImpl
import ryanhurst.weather.domain.WeatherRepository
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class WeatherModule {

    @Provides
    @Singleton
    fun provideWeatherService(): WeatherService {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.mesowest.net/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(WeatherService::class.java)
    }

    @Provides
    @Singleton
    fun provideWeatherRepository(weatherService: WeatherService): WeatherRepository = WeatherRepositoryImpl(weatherService)
}

