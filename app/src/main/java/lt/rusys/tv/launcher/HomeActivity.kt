package lt.rusys.tv.launcher

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.widget.Toast
import android.content.Context
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.content.pm.ResolveInfo // This is the line that must be present
import android.graphics.drawable.AdaptiveIconDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import com.google.android.flexbox.FlexboxLayout

class HomeActivity: AppCompatActivity()  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_layout)

        val tvApps = getTvAppActivities(this)


        // Correct: Cast to FlexboxLayout
        val appsContainer = findViewById<FlexboxLayout>(R.id.apps_container_layout)

        // Loop through the list of TV apps and add them to the container
        tvApps.forEach { resolveInfo ->
            // Inflate the item layout
            val appView = LayoutInflater.from(this).inflate(R.layout.list_item_tv_app, appsContainer, false)


            val appInfo: ApplicationInfo = resolveInfo.activityInfo.applicationInfo

            // Find the views within the inflated item layout
            val appIcon: ImageView = appView.findViewById(R.id.app_icon)
            val appName: TextView = appView.findViewById(R.id.app_name)

            // Populate the views with data from the ApplicationInfo object
            appIcon.setImageDrawable(appInfo.loadIcon(packageManager))
        //    appIcon.background = getAppIconBackground(appInfo.loadIcon(packageManager))

            // Get the app's icon drawable from the package manager
            val appIconDrawable = appInfo.loadIcon(packageManager)

// Get the background layer (it could be a ColorDrawable or something else)
            val backgroundDrawable = getAppIconBackground(appIconDrawable)

// If a background drawable exists, set it as the background of your ImageView.
// This works for both ColorDrawables and other Drawable types.
            if (backgroundDrawable != null) {
                appIcon.background = backgroundDrawable

                // Now set the foreground on top of it.
                // The foreground is always the main icon image.
                if (appIconDrawable is AdaptiveIconDrawable) {
                    appIcon.setImageDrawable(appIconDrawable.foreground)
                } else {
                    // For older, non-adaptive icons, the whole image is the foreground.
                    appIcon.setImageDrawable(appIconDrawable)
                }

            } else {
                // If there's no adaptive icon background (e.g., an older app),
                // just set the entire icon as the image.
                appIcon.setImageDrawable(appIconDrawable)
            }


            appName.text = appInfo.loadLabel(packageManager)


            appIcon.setOnClickListener {

                // Use the ResolveInfo object to build a direct launch intent
                val launchIntent = Intent(Intent.ACTION_MAIN).apply {
                    addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER)
                    component = android.content.ComponentName(
                        resolveInfo.activityInfo.packageName,
                        resolveInfo.activityInfo.name
                    )
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }

                try {
                    startActivity(launchIntent)
                } catch (e: Exception) {
                    Log.e("LAUNCH_APP", "Failed to launch app: ${appInfo.packageName}", e)
                }

            }


            // Add the new view to the container LinearLayout
            appsContainer.addView(appView)
        }
    }

    // This function now returns a list of ResolveInfo objects
    private fun getTvAppActivities(context: Context): List<ResolveInfo> {
        val packageManager = context.packageManager

        val mainIntent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER)
        }

        return packageManager.queryIntentActivities(mainIntent, 0)
    }

    fun getAppIconBackground(drawable: Drawable): Drawable? {
        if (drawable is AdaptiveIconDrawable) {
            return drawable.background
        }
        return null
    }
}
