package mk.ukim.finki.tripcostcalculator.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import mk.ukim.finki.tripcostcalculator.R
import mk.ukim.finki.tripcostcalculator.adapters.TripCostAdapter
import mk.ukim.finki.tripcostcalculator.data.TripCostDao
import mk.ukim.finki.tripcostcalculator.data.TripCostDatabase


class HistoryFragment : Fragment(R.layout.fragment_history) {

    private lateinit var tripCostDao: TripCostDao
    private lateinit var adapter: TripCostAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewHistory)
        adapter = TripCostAdapter(onLongClick = { trip ->
            CoroutineScope(Dispatchers.IO).launch {
                tripCostDao.deleteTripCost(trip)
                val updatedList = tripCostDao.getAllTripCosts()
                withContext(Dispatchers.Main) {
                    adapter.submitList(updatedList)
                }
            }
        })
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        val database = Room.databaseBuilder(requireContext(), TripCostDatabase::class.java, "trip_cost_db").build()
        tripCostDao = database.tripCostDao()

        CoroutineScope(Dispatchers.Main).launch {
            val tripCosts = withContext(Dispatchers.IO) {
                tripCostDao.getAllTripCosts()
            }
            adapter.submitList(tripCosts)
        }
    }
}

