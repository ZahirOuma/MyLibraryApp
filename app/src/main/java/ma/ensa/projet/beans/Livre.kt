package ma.ensa.projet.beans

data class Livre(
    var id: Int,
    var titre: String,
    var auteur: String,
    var type: String,
    var dateEdition: String,
    var resume: String,
    var image: String
)
