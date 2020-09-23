package ryanhurst.weather

/**
 * Station information for Snowbasin
 * Created by ryan on 2/20/17.
 */
private val STRAWBERRY_TOP = StationName("Strawberry Top","SB2")
private val BASE = StationName("Base","SBE")
private val OGDEN_PEAK = StationName("Ogden Peak","OGP")
private val BOARDWALK = StationName("Boardwalk","SBBWK")
private val WILDCAT = StationName("Wildcat","SWI")
private val MIDDLE_BOWL = StationName("Middle Bowl","SNI")
private val STRAWBERRY_TOWER_15 = StationName("Strawberry Tower 15","SBT15")
private val TRAPPERS = StationName("Trapper's Loop","TPR")

val STATIONS_LIST_DEFAULT = listOf(STRAWBERRY_TOP, BASE, OGDEN_PEAK, MIDDLE_BOWL)
val STATIONS_LIST = listOf(STRAWBERRY_TOP, BASE, OGDEN_PEAK, BOARDWALK, MIDDLE_BOWL, STRAWBERRY_TOWER_15, TRAPPERS, WILDCAT)
val SHORT_STATIONS_LIST = listOf(STRAWBERRY_TOP, BASE, BOARDWALK)