package mk.ukim.finki.tripcostcalculator.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [TripCost::class], version = 1)
abstract class TripCostDatabase : RoomDatabase() {
    abstract fun tripCostDao(): TripCostDao
}
