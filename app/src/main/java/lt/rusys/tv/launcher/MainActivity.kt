package lt.rusys.tv.launcher
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentActivity
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import androidx.core.content.edit

var lock_timeout: Long = 10
var lock_multiplier: Int = 0

var lock_texts: List<String> = listOf("Ups!")

class MainActivity : FragmentActivity() {
    val db = Firebase.firestore
    private val launchPinInputActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val mainContainer = findViewById<LinearLayout>(R.id.profile_container)

        val sharedPreferences = getSharedPreferences("pref", Context.MODE_PRIVATE)


        val last_profile = sharedPreferences.getString("last_profile", "1")

        val docRef = db.collection("lock").document("msg");
        docRef.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                lock_texts = documentSnapshot.get("text") as? List<String> ?: listOf("Ups!")
            } else {
                Log.w(TAG, "Document 'msg' does not exist.")
            }
        }.addOnFailureListener { exception ->
            Log.w(TAG, "Error getting documents.", exception)
        }


        db.collection("profiles")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {

                    val bbl = LayoutInflater.from(this).inflate(R.layout.profile_bubble, mainContainer, false)

                    bbl.findViewById<TextView>(R.id.profile_name).text = document.data["name"].toString();

                    val bbp = bbl.findViewById<ImageView>(R.id.profile_bubble);


                    bbp.setOnClickListener {
                        sharedPreferences.edit {
                            putString("last_profile", document.id)
                        }

                        val expectedPin = document.data["pin"].toString();
                        val intent = Intent(this, PinInputActivity::class.java).apply {
                            putExtra(PinInputActivity.Required_pin, expectedPin)
                            putExtra(PinInputActivity.Profile_name, document.data["name"].toString())
                        }
                        launchPinInputActivity.launch(intent)
                    }


                    // 4. Add the complete item view to the main container
                    mainContainer.addView(bbl)

                    if(last_profile==document.id) {
                        bbp.requestFocus();
                    }

                    Log.d(TAG, "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }

    }
}