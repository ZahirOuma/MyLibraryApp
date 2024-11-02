package ma.ensa.projet

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents // Bon import pour Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SplashActivityTest {

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(SplashActivity::class.java)

    @Before
    fun setUp() {
        // Initialiser Intents avant chaque test
        Intents.init() // Utilisation de la bonne classe Intents
    }

    @After
    fun tearDown() {
        // Libérer les ressources après chaque test
        Intents.release() // Utilisation de la bonne classe Intents
    }

    @Test
    fun splashScreenRedirectsToLivreListActivity() {
        // Vérifier que l'activité SplashActivity est affichée
        onView(withId(R.id.splash_layout)).check(matches(isDisplayed()))

        // Attendre que la redirection se fasse
        // Le délai doit correspondre au délai défini dans votre SplashActivity
        // Si dans votre SplashActivity vous utilisez un délai de 3000ms, utilisez :
        Thread.sleep(2500) // Ajustez cette valeur selon votre délai dans SplashActivity

        // Vérifier que l'activité LivreListActivity est lancée
        intended(hasComponent(LivreListActivity::class.java.name))
    }
}