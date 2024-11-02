package ma.ensa.projet.adapter
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ma.ensa.projet.R
import ma.ensa.projet.beans.Livre
import android.graphics.drawable.Drawable
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class LivreAdapter(
    private val context: Context,
    private val livres: List<Livre>,
    private val onItemClick: (Livre) -> Unit,
    private val onEditClick: (Livre) -> Unit,
    private val onDeleteClick: (Livre) -> Unit
) : RecyclerView.Adapter<LivreAdapter.LivreViewHolder>() {

    class LivreViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titreTv: TextView = view.findViewById(R.id.titreTv)
        val auteurTv: TextView = view.findViewById(R.id.auteurTv)
        val typeTv: TextView = view.findViewById(R.id.typeTv)
        val dateEditionTv: TextView = view.findViewById(R.id.dateEditionTv)
        val livreIv: ImageView = view.findViewById(R.id.livreIv)
        val editBtn: ImageView = view.findViewById(R.id.editBtn)
        val deleteBtn: ImageView = view.findViewById(R.id.deleteBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LivreViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_livre, parent, false)
        return LivreViewHolder(view)
    }

    override fun onBindViewHolder(holder: LivreViewHolder, position: Int) {
        val livre = livres[position]
        holder.titreTv.text = livre.titre
        holder.auteurTv.text = livre.auteur
        holder.typeTv.text = livre.type
        holder.dateEditionTv.text = livre.dateEdition

        if (livre.image.isNotEmpty()) {
            try {
                Glide.with(context)
                    .load(livre.image)
                    .timeout(60000) // Augmenter le timeout à 60 secondes
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.error_image)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            Log.e("GlideError", "Erreur de chargement: ${e?.message}", e)
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            return false
                        }
                    })
                    .into(holder.livreIv)
            } catch (e: Exception) {
                Log.e("GlideError", "Exception lors du chargement: ${e.message}", e)
                holder.livreIv.setImageResource(R.drawable.error_image)
            }
        } else {
            holder.livreIv.setImageResource(R.drawable.placeholder_image)
        }


        holder.editBtn.setOnClickListener { onEditClick(livre) }
        holder.deleteBtn.setOnClickListener { onDeleteClick(livre) }
        holder.itemView.setOnClickListener { onItemClick(livre) }
    }

    override fun getItemCount(): Int = livres.size
}
