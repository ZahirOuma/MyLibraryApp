 package ma.ensa.projet

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.RootMatchers.isPlatformPopup
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.util.HumanReadables
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ma.ensa.projet.adapter.LivreAdapter
import ma.ensa.projet.beans.Livre
import ma.ensa.projet.service.LivreService
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.any
import org.hamcrest.Matchers.instanceOf
import org.hamcrest.Matchers.`is`
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit
import android.widget.ImageView
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description
import org.hamcrest.Matchers.not


@RunWith(AndroidJUnit4::class)
class LivreListActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(LivreListActivity::class.java)

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








    // Vérification des champs du formulaire d'ajout
    private fun verifierChampsFormulaireAjout() {
        onView(withId(R.id.dialogAdd)).check(matches(isDisplayed()))
        onView(withId(R.id.titreEt)).check(matches(isDisplayed()))
        onView(withId(R.id.auteurEt)).check(matches(isDisplayed()))
        onView(withId(R.id.resumeEt)).check(matches(isDisplayed()))
        onView(withId(R.id.dateEditionEt)).check(matches(isDisplayed()))
        onView(withId(R.id.imageUrlEt)).check(matches(isDisplayed()))
        onView(withId(R.id.addButton)).check(matches(isDisplayed()))
        onView(withId(R.id.cancelButton)).check(matches(isDisplayed()))
    }
    // Vérification des champs du formulaire de modification
    private fun verifierChampsFormulaireModifier() {
        onView(withId(R.id.dialogUpdate)).check(matches(isDisplayed()))
        onView(withId(R.id.titreEt)).check(matches(isDisplayed()))
        onView(withId(R.id.auteurEt)).check(matches(isDisplayed()))
        onView(withId(R.id.resumeEt)).check(matches(isDisplayed()))
        onView(withId(R.id.dateEditionEt)).check(matches(isDisplayed()))
        onView(withId(R.id.imageUrlEt)).check(matches(isDisplayed()))
        onView(withId(R.id.editButton)).check(matches(isDisplayed()))
        onView(withId(R.id.cancelButtonEdit)).check(matches(isDisplayed()))
    }



    // Remplir les champs du ajout formulaire
    private fun remplirChampsFormulaireAjout(
        titre: String,
        auteur: String,
        resume: String,
        dateEdition: String,
        imageUrl: String,
        type: String
    ) {
        onView(withId(R.id.titreEt)).perform(replaceText(titre), closeSoftKeyboard())
        onView(withId(R.id.auteurEt)).perform(replaceText(auteur), closeSoftKeyboard())
        onView(withId(R.id.resumeEt)).perform(replaceText(resume), closeSoftKeyboard())
        onView(withId(R.id.dateEditionEt)).perform(replaceText(dateEdition), closeSoftKeyboard())
        onView(withId(R.id.imageUrlEt)).perform(replaceText(imageUrl), closeSoftKeyboard())

        onView(withId(R.id.typeSpinner)).perform(click())
        onData(allOf(instanceOf(String::class.java), `is`(type)))
            .inRoot(isPlatformPopup())
            .perform(click())
    }
    // Remplir les champs du modification formulaire
    private fun remplirChampsFormulaireModifier(
        titre: String,
        auteur: String,
        resume: String,
        dateEdition: String,
        imageUrl: String,
        type: String
    ) {
        onView(withId(R.id.titreEt)).perform(clearText(), replaceText(titre), closeSoftKeyboard())
        onView(withId(R.id.auteurEt)).perform(clearText(), replaceText(auteur), closeSoftKeyboard())
        onView(withId(R.id.resumeEt)).perform(clearText(), replaceText(resume), closeSoftKeyboard())
        onView(withId(R.id.dateEditionEt)).perform(clearText(), replaceText(dateEdition), closeSoftKeyboard())
        onView(withId(R.id.imageUrlEt)).perform(clearText(), replaceText(imageUrl), closeSoftKeyboard())

        onView(withId(R.id.typeSpinner)).perform(click())
        onData(allOf(instanceOf(String::class.java), `is`(type)))
            .inRoot(isPlatformPopup())
            .perform(click())
    }




    // Matcher personnalisé pour vérifier si une ImageView a un drawable
    fun hasDrawable(): Matcher<View> {
        return object : BoundedMatcher<View, ImageView>(ImageView::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("ImageView has drawable")
            }

            override fun matchesSafely(imageView: ImageView): Boolean {
                return imageView.drawable != null
            }
        }
    }

    // Matcher personnalisé pour vérifier la position dans le RecyclerView
    fun atPosition(position: Int, itemMatcher: Matcher<View>): Matcher<View> {
        return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("has item at position $position: ")
                itemMatcher.describeTo(description)
            }

            override fun matchesSafely(view: RecyclerView): Boolean {
                val viewHolder = view.findViewHolderForAdapterPosition(position)
                    ?: // Si le ViewHolder est null, l'item n'est pas affiché à l'écran
                    return false
                return itemMatcher.matches(viewHolder.itemView)
            }
        }
    }
    // Vérification de l'item à une position spécifique
    private fun verifierItemAtPosition(position: Int, expectedTitle: String, expectedAuthor: String, expectedDate: String,expectedType:String) {
        // Faire défiler jusqu'à la position de l'item
        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(position))

        // Vérifier les détails dans l'item à la position donnée
        onView(withId(R.id.recyclerView))
            .check(matches(atPosition(position, hasDescendant(withText(expectedTitle)))))
        onView(withId(R.id.recyclerView))
            .check(matches(atPosition(position, hasDescendant(withText(expectedAuthor)))))
        onView(withId(R.id.recyclerView))
            .check(matches(atPosition(position, hasDescendant(withText(expectedDate)))))
        onView(withId(R.id.recyclerView))
            .check(matches(atPosition(position, hasDescendant(withText(expectedType)))))

        // Vérifier que l'image est chargée dans l'ImageView
        onView(withId(R.id.recyclerView))
            .check(matches(atPosition(position, hasDescendant(hasDrawable()))))

        // Vérifier l'existence des boutons editBtn et deleteBtn
        onView(withId(R.id.recyclerView))
            .check(matches(atPosition(position, hasDescendant(withId(R.id.editBtn)))))
        onView(withId(R.id.recyclerView))
            .check(matches(atPosition(position, hasDescendant(withId(R.id.deleteBtn)))))
    }
    // Test principal pour ajouter un livre et vérifier les détails
    @Test
    fun testAddBook() {
        // Vérifier que le RecyclerView et le bouton Ajouter sont affichés
        onView(withId(R.id.recyclerView)).check(matches(isDisplayed()))
        onView(withId(R.id.addButton)).check(matches(isDisplayed()))

        // Cliquer sur le bouton pour ajouter un nouveau livre
        onView(withId(R.id.addButton)).perform(click())

        // Vérifier la présence des champs dans le formulaire
        verifierChampsFormulaireAjout()

        // Remplir les champs du formulaire
        remplirChampsFormulaireAjout(
            "The NoteBook",
            "Nicola Sparkle",
            "The NoteBook est le premier roman d'amour de l'auteur américain Nicholas Sparks...",
            "1968",
            "https://nicholassparks.com/wp-content/uploads/2022/08/TheNotebook.jpg",
            "Roman"
        )

        onView(withId(R.id.addButton)).perform(click())
        onView(withId(R.id.dialogAdd)).check(doesNotExist())


        Thread.sleep(10000)
        verifierItemAtPosition(
            1,
            "The NoteBook",
            "Nicola Sparkle",
            "1968",
            "Roman"
        )
    }



    @Test
    fun testEditButton() {
        // Cliquer sur le bouton d'édition du premier élément
        onView(withId(R.id.recyclerView))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<LivreAdapter.LivreViewHolder>(
                    0,
                    clickOnViewWithId(R.id.editBtn)
                )
            )

        verifierChampsFormulaireModifier()
        remplirChampsFormulaireModifier(
            "Titre Modifié",
            "Auteur Modifié",
            "Résumé Modifié",
            "2025",
            "https://cdn1.booknode.com/book_cover/835/full/harry-potter-tome-1-harry-potter-a-lecole-des-sorciers-835229.jpg",
            "Fantasy"
        )


        onView(withId(R.id.editButton)).perform(click())
        onView(withId(R.id.dialogUpdate)).check(doesNotExist())
        Thread.sleep(10000)
        verifierItemAtPosition(
            0,
            "Titre Modifié",
            "Auteur Modifié",
            "2025",
            "Fantasy"
        )

    }



    @Test
    fun testDeleteBook() {

        val initialCount = LivreService.findAll().size

        // Cliquer sur le bouton de suppression du premier élément
        onView(withId(R.id.recyclerView))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<LivreAdapter.LivreViewHolder>(
                    0,
                    clickOnViewWithId(R.id.deleteBtn)
                )
            )

        onView(withText("Supprimer le livre")).check(matches(isDisplayed()))
        onView(withText("Oui")).perform(click())
        onView(withText("Supprimer le livre")).check(doesNotExist())

        assert(LivreService.findAll().size == initialCount - 1)
        onView(withId(R.id.recyclerView)).check(matches(not(atPosition(0, hasDescendant(withText("Harry Potter"))))))


    }


    @Test
    fun testEditBookThenCancel() {
        // Garder une trace des valeurs initiales
        val initialTitle = "Harry Potter"
        val initialAuthor = "J K Rolling"
        val initialYear = "1997"
        val initialType = "Fantasy"

        // Vérifier l'état initial
        verifierItemAtPosition(
            0,
            initialTitle,
            initialAuthor,
            initialYear,
            initialType
        )

        // Cliquer sur le bouton d'édition du premier élément
        onView(withId(R.id.recyclerView))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<LivreAdapter.LivreViewHolder>(
                    0,
                    clickOnViewWithId(R.id.editBtn)
                )
            )

        // Vérifier que le formulaire de modification est affiché
        verifierChampsFormulaireModifier()

        // Remplir les champs avec de nouvelles valeurs
        remplirChampsFormulaireModifier(
            "Nouveau Titre",
            "Nouvel Auteur",
            "Nouveau Résumé",
            "2024",
            "https://nouvelle-image.jpg",
            "Roman"
        )

        // Cliquer sur le bouton Annuler
        onView(withId(R.id.cancelButtonEdit)).perform(click())
        onView(withId(R.id.dialogUpdate)).check(doesNotExist())



        // Attendre que l'interface se mette à jour
        Thread.sleep(2000)

        // Vérifier que les valeurs initiales sont conservées
        verifierItemAtPosition(
            0,
            initialTitle,
            initialAuthor,
            initialYear,
            initialType
        )
    }




    @Test
    fun testNavigateToDetails() {
        // Cliquer sur le premier élément de la liste
        onView(withId(R.id.recyclerView))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<LivreAdapter.LivreViewHolder>(
                    0,
                    click()
                )
            )

        // Vérifier que l'activité de détails est affichée
        onView(withId(R.id.detailLayout)).check(matches(isDisplayed()))
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
        Thread.sleep(20000)
        // Vérifier que l'image est chargée
        onView(withId(R.id.imageViewdetail))
            .check(matches(isDisplayed()))
            .check(matches(hasDrawable()))


        Thread.sleep(5000)
        Espresso.pressBack()
        Thread.sleep(5000)
        // Vérifier que LivresListActivity est affichée
        onView(withId(R.id.listlivre)) // Remplacer par l'ID du RecyclerView de LivresListActivity
            .check(matches(isDisplayed()))
    }

    // Fonction utilitaire corrigée pour cliquer sur une vue avec un ID spécifique
    private fun clickOnViewWithId(viewId: Int): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                // Retourner un matcher qui accepte n'importe quelle vue
                return any(View::class.java)
            }

            override fun getDescription(): String {
                return "Click on a child view with specified id."
            }

            override fun perform(uiController: UiController, view: View) {
                val v = view.findViewById<View>(viewId)
                v.performClick()
            }
        }
    }
}