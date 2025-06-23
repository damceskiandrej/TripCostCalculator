package mk.ukim.finki.tripcostcalculator.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TripCostDao {
    @Insert
    suspend fun insertTripCost(tripCost: TripCost)

    @Query("SELECT * FROM trip_costs")
    suspend fun getAllTripCosts(): List<TripCost>

    @Delete
    suspend fun deleteTripCost(tripCost: TripCost)

    @Query("DELETE FROM trip_costs")
    suspend fun deleteAll()
}
