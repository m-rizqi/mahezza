package com.mahezza.mahezza.app

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.core.app.ActivityCompat
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.ktx.isFlexibleUpdateAllowed
import com.google.android.play.core.ktx.isImmediateUpdateAllowed
import com.mahezza.mahezza.R
import com.mahezza.mahezza.data.source.datastore.MahezzaDataStore
import com.mahezza.mahezza.ui.nav.MainNavigation
import com.mahezza.mahezza.ui.theme.AccentYellow
import com.mahezza.mahezza.ui.theme.MahezzaTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var appUpdateManager: AppUpdateManager
    private val updateType = AppUpdateType.IMMEDIATE

    private val activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
    }
    private val installStateUpdatedListener = InstallStateUpdatedListener {state ->
        if (state.installStatus() == InstallStatus.DOWNLOADED){
            popupSnackbarForCompleteUpdate()
        }
    }

    @Inject
    lateinit var dataStore: MahezzaDataStore

    private val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ResourcesCompat.getColor(resources, R.color.accent_yellow, null)

        appUpdateManager = AppUpdateManagerFactory.create(applicationContext)
        if (updateType == AppUpdateType.FLEXIBLE){
            appUpdateManager.registerListener(installStateUpdatedListener)
        }
        checkForAppUpdates()
        checkAndGrantForPermissions()

        setContent {
            MahezzaTheme {
                Surface {
                    var isLogin by rememberSaveable {
                        mutableStateOf(false)
                    }
                    var isLoginFetched by rememberSaveable {
                        mutableStateOf(false)
                    }
                    LaunchedEffect(key1 = true){
                        isLogin = dataStore.isLoginPreference.first()
                        isLoginFetched = true
                    }
                    if (isLoginFetched){
                        MainNavigation(isLoggedIn = isLogin)
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(AccentYellow)
                        ){
                            Image(
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .scale(2f),
                                painter = painterResource(id = R.drawable.logo_with_name),
                                contentDescription = stringResource(id = R.string.app_name)
                            )
                        }
                    }
                }
            }
        }
    }

    private fun checkAndGrantForPermissions() {
        val unGrantedPermissions = mutableListOf<String>()
        if (isPermissionGranted(Manifest.permission.CAMERA)){
            unGrantedPermissions.add(Manifest.permission.CAMERA)
        }
        if (isPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            unGrantedPermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (
            isPermissionGranted(Manifest.permission.POST_NOTIFICATIONS) && isAndroidVersionGreaterThanTiramisu()
        ){
            unGrantedPermissions.add(Manifest.permission.POST_NOTIFICATIONS)
        }
        if (unGrantedPermissions.isNotEmpty()){
            permissionLauncher.launch(unGrantedPermissions.toTypedArray())
        }
    }

    private fun isPermissionGranted(permission : String) = ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED

    private fun isAndroidVersionGreaterThanTiramisu() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

    override fun onResume() {
        super.onResume()
        appUpdateManager.appUpdateInfo.addOnSuccessListener { info ->
            if (info.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS){
                appUpdateManager.startUpdateFlowForResult(
                    info,
                    activityResultLauncher,
                    AppUpdateOptions.newBuilder(updateType).build()
                )
            }
        }
    }

    private fun checkForAppUpdates(){
        appUpdateManager.appUpdateInfo.addOnSuccessListener { info ->
            val isUpdateAvailable = info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
            val isUpdateAllowed = when(updateType){
                AppUpdateType.FLEXIBLE -> info.isFlexibleUpdateAllowed
                AppUpdateType.IMMEDIATE -> info.isImmediateUpdateAllowed
                else -> false
            }
            if(isUpdateAvailable && isUpdateAllowed){
                appUpdateManager.startUpdateFlowForResult(
                    info,
                    activityResultLauncher,
                    AppUpdateOptions.newBuilder(updateType).build()
                )
            }
        }
    }

    fun popupSnackbarForCompleteUpdate() {
        Snackbar.make(
            findViewById(android.R.id.content),
            "An update has just been downloaded.",
            Snackbar.LENGTH_INDEFINITE
        ).apply {
            setAction("RESTART") { appUpdateManager.completeUpdate() }
            show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (updateType == AppUpdateType.FLEXIBLE){
            appUpdateManager.unregisterListener(installStateUpdatedListener)
        }
    }

}