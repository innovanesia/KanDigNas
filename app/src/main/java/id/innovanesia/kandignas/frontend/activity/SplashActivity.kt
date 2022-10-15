package id.innovanesia.kandignas.frontend.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import id.innovanesia.kandignas.databinding.ActivitySplashBinding

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity()
{
    private lateinit var binds: ActivitySplashBinding

    companion object
    {
        const val timeDel: Long = 1500
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binds = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binds.root)

        Handler(Looper.getMainLooper())
            .postDelayed(
                {
                    startActivity(
                        Intent(
                            this, AuthActivity::class.java
                        )
                    )
                    finish()
                },
                timeDel
            )
    }
}