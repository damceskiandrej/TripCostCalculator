package mk.ukim.finki.tripcostcalculator.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import mk.ukim.finki.tripcostcalculator.data.TripCost
import mk.ukim.finki.tripcostcalculator.databinding.ItemTripCostBinding

class TripCostAdapter(private val onLongClick: (TripCost) -> Unit) : ListAdapter<TripCost, TripCostAdapter.TripCostViewHolder>(
    DiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripCostViewHolder {
        val binding = ItemTripCostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TripCostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TripCostViewHolder, position: Int) {
        val tripCost = getItem(position)
        holder.bind(tripCost)
    }

    inner class TripCostViewHolder(private val binding: ItemTripCostBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(tripCost: TripCost) {
            binding.textViewDestination.text = tripCost.destination
            binding.textViewTotalCost.text = "Вкупни трошоци: ${tripCost.totalCost}"
            binding.root.setOnLongClickListener {
                onLongClick(tripCost)
                true
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<TripCost>() {
        override fun areItemsTheSame(oldItem: TripCost, newItem: TripCost): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: TripCost, newItem: TripCost): Boolean = oldItem == newItem
    }
}
