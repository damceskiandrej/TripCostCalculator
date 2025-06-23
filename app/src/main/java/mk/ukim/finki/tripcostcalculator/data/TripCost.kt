package mk.ukim.finki.tripcostcalculator.data

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "trip_costs")
data class TripCost(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val destination: String,
    val distance: Double,
    val fuelPrice: Double,
    val additionalCosts: Double,
    val totalCost: Double
)
