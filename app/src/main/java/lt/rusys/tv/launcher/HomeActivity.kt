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
import android.util.Log

class HomeActivity: AppCompatActivity()  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_layout)
/*
        // Get the list of all TV launcher activities

        tvAppActivities.forEach { resolveInfo ->
            val appView = LayoutInflater.from(this).inflate(R.layout.list_item_tv_app, appsContainer, false)

            val appIcon: ImageView = appView.findViewById(R.id.app_icon)
            val appName: TextView = appView.findViewById(R.id.app_name)

            val appInfo: ApplicationInfo = resolveInfo.activityInfo.applicationInfo

            // Populate the views
            appIcon.setImageDrawable(appInfo.loadIcon(packageManager))
            appName.text = appInfo.loadLabel(packageManager)

            // Set the click listener on the entire appView
            appView.setOnClickListener {
                Log.d("LAUNCH_APP", "Attempting to launch app: ${appInfo.loadLabel(packageManager)}")

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

            // Add the new view to the container
            appsContainer.addView(appView)
        }
    }


}
    */

        val tvApps = getTvAppActivities(this)


        // Reference the container LinearLayout from your main layout
        val appsContainer = findViewById<LinearLayout>(R.id.apps_container_layout)

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

    private fun getTvApps(context: Context): List<ApplicationInfo> {
        val packageManager = context.packageManager

        val mainIntent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER)
        }

        return packageManager.queryIntentActivities(mainIntent, 0)
            .map { resolveInfo ->
                packageManager.getApplicationInfo(resolveInfo.activityInfo.packageName, PackageManager.GET_META_DATA)
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
}
