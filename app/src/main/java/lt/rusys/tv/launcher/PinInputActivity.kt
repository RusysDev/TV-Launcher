package lt.rusys.tv.launcher

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import android.util.Log
import android.widget.TextView

class PinInputActivity : AppCompatActivity() {

    // Keys for Intent extras
    companion object {
        const val Required_pin = "extra_expected_pin"
        const val Profile_name = "profile_name"
        const val Result_pin = "result_pin_validated"
    }
    private var profileName: String = ""
    private var pinCode: String = ""
    private var pinLen: Int = 0
    private var currPin: String = ""
    private var retry: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_pin)

        // Retrieve the expected PIN from the Intent
        pinCode = intent.getStringExtra(Required_pin)?:"6"
        pinLen = pinCode.length
        profileName = intent.getStringExtra(Profile_name)?:""

        retry = 0
        Toast.makeText(this, "Expected PIN: $pinCode", Toast.LENGTH_SHORT).show();

        // Find the views
      //  val linearLayout: LinearLayout = findViewById(R.id.my_linear_layout)
       // val imageView: ImageView = findViewById(R.id.profile_bubble)
        val bbt = findViewById<TextView>(R.id.profile_name)
        bbt.text = profileName

    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            currPin="";
        } else {
            Log.d(TAG, "Activity has lost window focus.")
        }
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        val keyName = KeyEvent.keyCodeToString(keyCode)
        val action = "KEY_DOWN"

        // Log the key press to Logcat
        Log.d(TAG, "Key Down: $keyName (Code: $keyCode)")

        // Show a Toast message on the TV screen
       // Toast.makeText(this, "$action: $keyName", Toast.LENGTH_SHORT).show()

        return super.onKeyDown(keyCode, event)
    }



    @SuppressLint("GestureBackNavigation")
    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_DPAD_UP -> { currPin+=1 }
            KeyEvent.KEYCODE_DPAD_RIGHT -> { currPin+=2 }
            KeyEvent.KEYCODE_DPAD_DOWN -> { currPin+=3 }
            KeyEvent.KEYCODE_DPAD_LEFT -> { currPin+=4 }
            KeyEvent.KEYCODE_DPAD_CENTER -> { currPin+=5 }
            KeyEvent.KEYCODE_BACK ->{ return super.onKeyUp(keyCode, event) }
        }
        checkPin()
        return true
        //return super.onKeyUp(keyCode, event)
    }

    private fun checkPin() {
        if(currPin.endsWith(pinCode)) {
            lock_multiplier=0
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
        else if (currPin.length >= 4) {
            Toast.makeText(this, "Neteisingas kodas", Toast.LENGTH_SHORT).show()
            retry++
            currPin = ""
            //TODO: log retry
            if(retry>=3){
                retry=0
                lock_multiplier+=1
                startActivity(Intent(this, LockActivity::class.java))
                //TODO: log locks
                finish()
            }
        }
    }
}
