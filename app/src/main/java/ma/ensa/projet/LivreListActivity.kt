package ma.ensa.projet

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ma.ensa.projet.R
import ma.ensa.projet.adapter.LivreAdapter
import ma.ensa.projet.beans.Livre
import ma.ensa.projet.service.LivreService
import java.net.URL

class LivreListActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: LivreAdapter
    private val types = arrayOf("Roman",
        "Science-fiction",
        "Fantasy",
        "Non-fiction",
        "Biographie",
        "Essai",
        "Historique",
        "Poésie",
        "Thriller",
        "Crime",
        "Horreur",
        "Développement personnel")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_livre_list)

        recyclerView = findViewById(R.id.recyclerView)
        setupRecyclerView()
        setupAddButton()
    }

    private fun setupRecyclerView() {
        adapter = LivreAdapter(
            this,
            LivreService.findAll(),  // Accéder directement à l'objet singleton
            onItemClick = { livre -> navigateToDetails(livre) },
            onEditClick = { livre -> showEditDialog(livre) },
            onDeleteClick = { livre -> deleteLivre(livre) }
        )
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun setupAddButton() {
        findViewById<android.widget.Button>(R.id.addButton).setOnClickListener {
            showAddDialog()
        }
    }

    private fun showAddDialog() {
        val dialogBinding = layoutInflater.inflate(R.layout.dialog_add_livre, null)

        // Initialize the spinner and adapter
        val spinner = dialogBinding.findViewById<Spinner>(R.id.typeSpinner)
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, types)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Set the adapter to the spinner
        spinner.adapter = spinnerAdapter

        val dialog = AlertDialog.Builder(this)
            .setTitle("Ajouter un livre")
            .setView(dialogBinding)
            .create()

        // Set up the buttons
        val addButton = dialogBinding.findViewById<Button>(R.id.addButton)
        val cancelButton = dialogBinding.findViewById<Button>(R.id.cancelButton)

        addButton.setOnClickListener {
            val titre = dialogBinding.findViewById<EditText>(R.id.titreEt).text.toString()
            val auteur = dialogBinding.findViewById<EditText>(R.id.auteurEt).text.toString()
            val type = spinner.selectedItem.toString()
            val dateEdition = dialogBinding.findViewById<EditText>(R.id.dateEditionEt).text.toString()
            val resume = dialogBinding.findViewById<EditText>(R.id.resumeEt).text.toString()
            val imageUrl = dialogBinding.findViewById<EditText>(R.id.imageUrlEt).text.toString()

            // Validation de l'URL
            if (imageUrl.isNotEmpty()) {
                try {
                    val url = URL(imageUrl)
                    if (!url.protocol.startsWith("http")) {
                        Toast.makeText(this, "L'URL de l'image doit commencer par http:// ou https://", Toast.LENGTH_LONG).show()
                        return@setOnClickListener
                    }
                } catch (e: Exception) {
                    Toast.makeText(this, "URL de l'image invalide", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }
            }

            if (titre.isNotEmpty() && auteur.isNotEmpty()) {
                val livre = Livre(0, titre, auteur, type, dateEdition, resume, imageUrl)
                LivreService.create(livre)
                updateRecyclerView()
                dialog.dismiss() // Fermez le dialogue après ajout
            } else {
                Toast.makeText(this, "Veuillez remplir tous les champs obligatoires", Toast.LENGTH_SHORT).show()
            }
        }

        cancelButton.setOnClickListener {
            dialog.dismiss() // Fermez le dialogue sans rien faire
        }

        dialog.show()
    }


    private fun showEditDialog(livre: Livre) {
        val dialogBinding = layoutInflater.inflate(R.layout.dialog_update_livre, null)

        // Initialize the spinner and adapter
        val spinner = dialogBinding.findViewById<Spinner>(R.id.typeSpinner)
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, types)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Set the adapter to the spinner
        spinner.adapter = spinnerAdapter
        spinner.setSelection(types.indexOf(livre.type)) // Preselect the book type

        // Prefill the fields with the book values
        dialogBinding.findViewById<EditText>(R.id.titreEt).setText(livre.titre)
        dialogBinding.findViewById<EditText>(R.id.auteurEt).setText(livre.auteur)
        dialogBinding.findViewById<EditText>(R.id.dateEditionEt).setText(livre.dateEdition)
        dialogBinding.findViewById<EditText>(R.id.resumeEt).setText(livre.resume)
        dialogBinding.findViewById<EditText>(R.id.imageUrlEt).setText(livre.image)

        val dialog = AlertDialog.Builder(this)
            .setTitle("Modifier le livre")
            .setView(dialogBinding)
            .create()

        // Edit button
        val editButton = dialogBinding.findViewById<Button>(R.id.editButton)
        editButton.setOnClickListener {
            val titre = dialogBinding.findViewById<EditText>(R.id.titreEt).text.toString()
            val auteur = dialogBinding.findViewById<EditText>(R.id.auteurEt).text.toString()
            val type = spinner.selectedItem.toString()
            val dateEdition = dialogBinding.findViewById<EditText>(R.id.dateEditionEt).text.toString()
            val resume = dialogBinding.findViewById<EditText>(R.id.resumeEt).text.toString()
            val imageUrl = dialogBinding.findViewById<EditText>(R.id.imageUrlEt).text.toString()

            // URL validation
            if (imageUrl.isNotEmpty()) {
                try {
                    val url = URL(imageUrl)
                    if (!url.protocol.startsWith("http")) {
                        Toast.makeText(this, "L'URL de l'image doit commencer par http:// ou https://", Toast.LENGTH_LONG).show()
                        return@setOnClickListener
                    }
                } catch (e: Exception) {
                    Toast.makeText(this, "URL de l'image invalide", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }
            }

            // Required field validation
            if (titre.isNotEmpty() && auteur.isNotEmpty()) {
                livre.titre = titre
                livre.auteur = auteur
                livre.type = type
                livre.dateEdition = dateEdition
                livre.resume = resume
                livre.image = imageUrl

                LivreService.update(livre) // Call update method
                updateRecyclerView()
                dialog.dismiss() // Close the dialog after modification
            } else {
                Toast.makeText(this, "Veuillez remplir tous les champs obligatoires", Toast.LENGTH_SHORT).show()
            }
        }

        // Cancel button
        val cancelButton = dialogBinding.findViewById<Button>(R.id.cancelButtonEdit) // Updated ID
        cancelButton.setOnClickListener {
            dialog.dismiss() // Close the dialog without doing anything
        }

        dialog.show()
    }






    private fun deleteLivre(livre: Livre) {
        val dialog = AlertDialog.Builder(this)
            .setTitle("Supprimer le livre")
            .setMessage("Voulez-vous vraiment supprimer ce livre ?")
            .setPositiveButton("Oui") { _, _ ->
                LivreService.delete(livre.id)  // Appeler la méthode de l'objet singleton
                updateRecyclerView()
            }
            .setNegativeButton("Non", null)
            .create()

        dialog.show()
    }

    private fun navigateToDetails(livre: Livre) {
        val intent = Intent(this, LivreDetailActivity::class.java)
        intent.putExtra("livre_id", livre.id)
        startActivity(intent)
    }

    private fun updateRecyclerView() {
        adapter.notifyDataSetChanged()
    }
}
