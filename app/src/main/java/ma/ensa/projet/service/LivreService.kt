package ma.ensa.projet.service

import ma.ensa.projet.beans.Livre

object LivreService {
    private val livres = mutableListOf<Livre>()
    private var nextId = 1

    fun findAll(): List<Livre> {
        return livres
    }

    fun findById(id: Int): Livre? {
        return livres.find { it.id == id }
    }

    fun create(livre: Livre) {
        livre.id = nextId++
        livres.add(livre)
    }

    fun update(livre: Livre) {
        val index = livres.indexOfFirst { it.id == livre.id }
        if (index != -1) {
            livres[index] = livre
        }
    }

    fun delete(id: Int) {
        livres.removeIf { it.id == id }
    }
}
