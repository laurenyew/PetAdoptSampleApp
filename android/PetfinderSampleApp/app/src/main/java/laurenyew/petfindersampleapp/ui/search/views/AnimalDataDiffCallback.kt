package laurenyew.petfindersampleapp.ui.search.views

import androidx.recyclerview.widget.DiffUtil
import laurenyew.petfindersampleapp.repository.models.AnimalModel

/**
 * @author Lauren Yew
 *
 * DiffUtil.Callback that compares data wrappers
 */
open class AnimalDataDiffCallback(
    private val oldData: List<AnimalModel>?,
    private val newData: List<AnimalModel>?
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldData?.size ?: 0

    override fun getNewListSize(): Int = newData?.size ?: 0

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldData?.get(oldItemPosition)
        val newItem = newData?.get(newItemPosition)

        return oldItem?.id == newItem?.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldData?.get(oldItemPosition)
        val newItem = newData?.get(newItemPosition)

        return oldItem?.id == newItem?.id && oldItem?.name == newItem?.name
    }
}