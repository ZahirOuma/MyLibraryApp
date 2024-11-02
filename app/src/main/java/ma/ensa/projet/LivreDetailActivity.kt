package ma.ensa.projet

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import ma.ensa.projet.R
import ma.ensa.projet.beans.Livre
import ma.ensa.projet.service.LivreService

class LivreDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_livre_detail)

        val livreId = intent.getIntExtra("livre_id", -1)
        Log.d("LivreDetailActivity", "Received livre ID: $livreId")

        // Utiliser l'instance singleton de LivreService
        val livre = LivreService.findById(livreId)

        if (livre == null) {
            Log.e("LivreDetailActivity", "Livre non trouvé pour ID: $livreId")
            // Afficher un message ou une vue par défaut si le livre n'est pas trouvé
            return // Sortir si le livre n'est pas trouvé
        }

        // Mettre à jour les vues avec les données du livre
        findViewById<TextView>(R.id.titreTvdetail).text = livre.titre ?: "Titre par défaut"
        findViewById<TextView>(R.id.auteurTvdetail).text = livre.auteur ?: "Auteur par défaut"
        findViewById<TextView>(R.id.typeTvdetail).text = livre.type ?: "Type par défaut"
        findViewById<TextView>(R.id.dateEditionTvdetail).text = livre.dateEdition ?: "Date par défaut"
        findViewById<TextView>(R.id.resumeTvdetail).text = livre.resume ?: "Résumé par défaut"

        // Charger l'image avec Glide (optionnel)
        val imageView = findViewById<ImageView>(R.id.imageViewdetail)
        if (livre.image.isNotEmpty()) {
            Glide.with(this)
                .load(livre.image)
                .into(imageView)
        } else {
            Glide.with(this)
                .load("https://via.placeholder.com/150") // URL d'une image par défaut
                .into(imageView)
        }
    }
}
