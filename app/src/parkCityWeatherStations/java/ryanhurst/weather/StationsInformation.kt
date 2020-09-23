package ryanhurst.weather

/**
 * Station information for Park City
 * Created by ryan on 1/9/17.
 */
private val KIMBALL_JUNCTION = StationName("Kimball Junction", "KIJ")
private val CANYONS_LOOKOUT = StationName("Canyons Lookout", "CLK")
private val CANYONS_DAYBREAK = StationName("Canyons Daybreak", "CDYBK")
private val CANYONS_DREAMSCAPE = StationName("Canyons Dreamscape", "CDSUT")
private val PARK_CITY_MOUNTAIN = StationName("Park City Mountain", "PC018")

val STATIONS_LIST_DEFAULT = listOf(CANYONS_LOOKOUT, CANYONS_DAYBREAK, CANYONS_DREAMSCAPE, PARK_CITY_MOUNTAIN)
val STATIONS_LIST = listOf(KIMBALL_JUNCTION, CANYONS_LOOKOUT, CANYONS_DAYBREAK, CANYONS_DREAMSCAPE, PARK_CITY_MOUNTAIN)
val SHORT_STATIONS_LIST = listOf(CANYONS_LOOKOUT, PARK_CITY_MOUNTAIN)