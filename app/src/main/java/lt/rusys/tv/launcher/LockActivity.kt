package lt.rusys.tv.launcher
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity


import android.view.View
import android.widget.TextView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

import lt.rusys.tv.launcher.MainActivity
import kotlin.random.Random

class LockActivity: AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lock_screen)

        GlobalScope.launch(Dispatchers.Main) {
            var remain = lock_timeout * lock_multiplier

            val randomString = lock_texts[Random.nextInt(lock_texts.size)]

            val bbl = findViewById<TextView>(R.id.lock_text)
            bbl.text = randomString;


            val bbt = findViewById<TextView>(R.id.lock_timer)

            while ( remain > 0){
                bbt.text = "(${remain} s)"
                delay(1000)
                remain--
            }
            finish()
        }

        // Set the activity to full-screen immersive mode.
        // This hides system UI (like navigation bars) and provides a more controlled environment.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
            window.insetsController?.let {
                it.hide(WindowInsets.Type.systemBars())
                it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_FULLSCREEN
                    ).toInt()
        }

        // Keep the screen on while this activity is active.
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    // This method is called for all key events (D-pad, Back, Volume, etc.).
    // By returning 'true', we consume the event, preventing it from being processed further
    // by the system or other views.
    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        // Return true to consume all key events.
        return true
    }

    // This method is called for all touch events.
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        // Return true to consume all touch events.
        return true
    }
}
