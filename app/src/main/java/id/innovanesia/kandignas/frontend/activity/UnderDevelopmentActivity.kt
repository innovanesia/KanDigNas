package id.innovanesia.kandignas.frontend.activity

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import id.innovanesia.kandignas.databinding.ActivityUnderDevelopmentBinding

class UnderDevelopmentActivity : AppCompatActivity()
{
    private lateinit var binds: ActivityUnderDevelopmentBinding

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binds = ActivityUnderDevelopmentBinding.inflate(layoutInflater)
        setContentView(binds.root)

        binds.apply {
            setSupportActionBar(toolbar)

            toolbar.setNavigationOnClickListener {
                finish()
            }
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true)
        {
            override fun handleOnBackPressed()
            {
                finish()
            }
        })
    }
}