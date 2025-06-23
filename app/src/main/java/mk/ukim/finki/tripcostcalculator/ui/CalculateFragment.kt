package mk.ukim.finki.tripcostcalculator.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mk.ukim.finki.tripcostcalculator.R
import mk.ukim.finki.tripcostcalculator.data.TripCost
import mk.ukim.finki.tripcostcalculator.data.TripCostDao
import mk.ukim.finki.tripcostcalculator.data.TripCostDatabase


class CalculateFragment : Fragment(R.layout.fragment_calculate) {

    private lateinit var tripCostDao: TripCostDao
    private lateinit var totalCostTextView: TextView
    private val CHANNEL_ID = "trip_notification_channel"

    private val notificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                showNotification("Пресметката е зачувана!")
            } else {
                Toast.makeText(requireContext(), "Дозволата за нотификации не е дадена", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val database = Room.databaseBuilder(
            requireContext(),
            TripCostDatabase::class.java,
            "trip_cost_db"
        ).build()
        tripCostDao = database.tripCostDao()

        val destinationEditText = view.findViewById<EditText>(R.id.editTextDestination)
        val distanceEditText = view.findViewById<EditText>(R.id.editTextDistance)
        val fuelPriceEditText = view.findViewById<EditText>(R.id.editTextFuelPrice)
        val additionalCostsEditText = view.findViewById<EditText>(R.id.editTextAdditionalCosts)
        totalCostTextView = view.findViewById(R.id.textViewTotalCost)

        view.findViewById<Button>(R.id.btnCalculateCost).setOnClickListener {
            val distance = distanceEditText.text.toString().toDoubleOrNull() ?: 0.0
            val fuelPrice = fuelPriceEditText.text.toString().toDoubleOrNull() ?: 0.0
            val additionalCosts = additionalCostsEditText.text.toString().toDoubleOrNull() ?: 0.0

            val totalCost = (distance / 10) * fuelPrice + additionalCosts
            totalCostTextView.text = "Вкупни трошоци: %.2f ден".format(totalCost)

            val tripCost = TripCost(
                destination = destinationEditText.text.toString(),
                distance = distance,
                fuelPrice = fuelPrice,
                additionalCosts = additionalCosts,
                totalCost = totalCost
            )

            CoroutineScope(Dispatchers.IO).launch {
                tripCostDao.insertTripCost(tripCost)
            }


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        android.Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    showNotification("Пресметката е зачувана!")
                } else {
                    notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                }
            } else {
                showNotification("Пресметката е зачувана!")
            }
        }

        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Trip Cost Channel"
            val descriptionText = "Канал за известување за патни трошоци"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

private fun showNotification(message: String) {
    if (ContextCompat.checkSelfPermission(
            requireContext(),
            android.Manifest.permission.POST_NOTIFICATIONS
        ) != PackageManager.PERMISSION_GRANTED
    ) {

        return
    }

    val builder = NotificationCompat.Builder(requireContext(), CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle("Trip Cost Calculator")
        .setContentText(message)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    with(NotificationManagerCompat.from(requireContext())) {
        notify(System.currentTimeMillis().toInt(), builder.build())
    }
}
}