package activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.mitali.bookhub.R

class SplashScreen : AppCompatActivity() {
    val splashTimeOut:Long = 2000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        Handler().postDelayed({
            // This method will be executed once the timer is over
            // Start your app main activity
            startActivity(Intent(this@SplashScreen,MainActivity::class.java))
            // close this activity
            finish()
        }, splashTimeOut)



    }
}