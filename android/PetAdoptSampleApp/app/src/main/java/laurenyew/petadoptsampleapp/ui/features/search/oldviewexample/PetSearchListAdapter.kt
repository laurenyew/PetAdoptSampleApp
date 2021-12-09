package laurenyew.petadoptsampleapp.ui.features.search.oldviewexample

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import laurenyew.petadoptsampleapp.R
import laurenyew.petadoptsampleapp.db.animal.Animal
import laurenyew.petadoptsampleapp.databinding.AnimalPreviewBinding

class PetSearchListAdapter(private val animals: MutableList<Animal> = arrayListOf()) :
    RecyclerView.Adapter<PetSearchListAdapter.PetPreviewViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetPreviewViewHolder {
        val itemBinding =
            AnimalPreviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PetPreviewViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: PetPreviewViewHolder, position: Int) {
        val animal: Animal = animals[position]
        holder.bind(animal)
    }

    override fun getItemCount(): Int = animals.size

    //This could be made more performant. Leaving for now as this is just an example
    //of flows + traditional views
    fun setAnimals(newAnimals: List<Animal>) {
        animals.clear()
        animals.addAll(newAnimals)
    }

    class PetPreviewViewHolder(private val itemBinding: AnimalPreviewBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(animal: Animal) {
            itemBinding.nameTextView.text = animal.name
            itemBinding.infoTextView.text = animal.sex
            itemBinding.descriptionTextView.text = animal.description
            Glide.with(itemBinding.animalImageView)
                .load(animal.photoUrl)
                .placeholder(R.drawable.ic_baseline_image_24)
                .error(R.drawable.ic_baseline_broken_image_24)
                .into(itemBinding.animalImageView)
        }
    }
}