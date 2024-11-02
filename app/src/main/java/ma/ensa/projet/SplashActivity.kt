package ma.ensa.projet


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        // Délai de 2 secondes avant de passer à l'activité suivante
        Handler().postDelayed({
            val intent = Intent(this, LivreListActivity::class.java)
            startActivity(intent)
            finish() // Termine cette activité pour qu'elle ne soit pas dans la pile d'activités
        }, 2000) // 2000 millisecondes = 2 secondes
    }
}
