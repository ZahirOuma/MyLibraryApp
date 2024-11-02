package ma.ensa.projet

import android.content.Intent
import android.view.View
import android.widget.ImageView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import ma.ensa.projet.beans.Livre
import ma.ensa.projet.service.LivreService
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LivreDetailActivityTest {

    private val testLivre = Livre(
        1,
        "Harry Potter",
        "J K Rolling",
        "Fantasy",
        "1997",
        "Harry Potter [ʔaʁi pɔtœʁ] est une série littéraire de low fantasy écrite par l'auteure britannique J. K. Rowling, dont la suite romanesque s'est achevée en 2007.",
        "https://cdn1.booknode.com/book_cover/835/full/harry-potter-tome-1-harry-potter-a-lecole-des-sorciers-835229.jpg"    )

    @Before
    fun setup() {
        // S'assurer que le livre de test existe dans le service
        if (LivreService.findById(testLivre.id) == null) {
            LivreService.create(testLivre)
        }
    }

    // Matcher personnalisé pour vérifier si une ImageView a un drawable
    private fun hasDrawable(): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("has drawable")
            }

            override fun matchesSafely(view: View): Boolean {
                return view is ImageView && view.drawable != null
            }
        }
    }

    @Test
    fun testLivreDetailsDisplayedCorrectly() {
        // Créer l'intent avec l'ID du livre
        val intent = Intent(ApplicationProvider.getApplicationContext(), LivreDetailActivity::class.java).apply {
            putExtra("livre_id", testLivre.id)
        }

        // Lancer l'activité avec l'intent
        ActivityScenario.launch<LivreDetailActivity>(intent).use {
            // Vérifier que tous les champs sont affichés correctement
            onView(withId(R.id.titreTvdetail))
                .check(matches(isDisplayed()))
                .check(matches(withText(testLivre.titre)))

            onView(withId(R.id.auteurTvdetail))
                .check(matches(isDisplayed()))
                .check(matches(withText(testLivre.auteur)))

            onView(withId(R.id.typeTvdetail))
                .check(matches(isDisplayed()))
                .check(matches(withText(testLivre.type)))

            onView(withId(R.id.dateEditionTvdetail))
                .check(matches(isDisplayed()))
                .check(matches(withText(testLivre.dateEdition)))

            onView(withId(R.id.resumeTvdetail))
                .check(matches(isDisplayed()))
                .check(matches(withText(testLivre.resume)))
           Thread.sleep(15000)
            // Vérifier que l'image est chargée
            onView(withId(R.id.imageViewdetail))
                .check(matches(isDisplayed()))
                .check(matches(hasDrawable()))
        }
    }








}