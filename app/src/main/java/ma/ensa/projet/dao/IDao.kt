package ma.ensa.projet.dao

import ma.ensa.projet.beans.Livre

interface IDao {
    fun findAll(): List<Livre>
    fun findById(id: Int): Livre?
    fun create(livre: Livre)
    fun update(livre: Livre)
    fun delete(id: Int)
}
