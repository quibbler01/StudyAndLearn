package cn.quibbler.coroutine.ui.view.ui

import android.Manifest
import android.app.AppOpsManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.UserManager
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cn.quibbler.coroutine.R

@RequiresApi(Build.VERSION_CODES.Q)
class AppOpsManagerActivity : AppCompatActivity() {

    private lateinit var appOpsManager: AppOpsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_app_ops_manager)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        appOpsManager = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager

        appOpsManager.startWatchingMode("", packageName, wacth)

        ops()
    }

    override fun onDestroy() {
        super.onDestroy()
        appOpsManager.stopWatchingMode(wacth)

    }

    val wacth = object : AppOpsManager.OnOpChangedListener {
        override fun onOpChanged(op: String, packageName: String) {
            val uid: Int = packageManager.getPackageUid(packageName, 0)
            val result = appOpsManager.checkOpNoThrow(op, uid, packageName)
            Log.e("zh", "权限发生变更 op = ${op}  packageName = ${packageName} ${result}")
        }
    }

    private fun ops() {
        appOpsManager
    }

    fun checkPermission(pkg: String, op: String): Int {
        return try {
            val uid = packageManager.getPackageUid(pkg, 0)
            appOpsManager.checkOp(op, uid, pkg)
            appOpsManager.unsafeCheckOp(op, uid, pkg)
        } catch (_: Exception) {
            -1
        }
    }

    fun safeCheckPermission(pkg: String, op: String): Int {

        val uid = packageManager.getPackageUid(pkg, 0)
        appOpsManager.checkOpNoThrow(op, uid, pkg)
        return appOpsManager.unsafeCheckOpNoThrow(op, uid, pkg)
    }

    fun checkUid(pkg: String, uid: Int): Boolean {
        return runCatching {
            appOpsManager.checkPackage(uid, pkg)

            packageManager.getPackageUid(pkg, 0) == uid
        }.isSuccess

    }
    val sAppOpInfos: Array<AppOpInfo> = arrayOf<AppOpInfo>(
        Builder(AppOpsManager.OP_COARSE_LOCATION, AppOpsManager.OPSTR_COARSE_LOCATION, "COARSE_LOCATION")
            .setPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
            .setRestriction(UserManager.DISALLOW_SHARE_LOCATION)
            .setAllowSystemRestrictionBypass(AppOpsManager.RestrictionBypass(true, false, false))
            .setDefaultMode(AppOpsManager.MODE_ALLOWED).build(),
        Builder(AppOpsManager.OP_FINE_LOCATION, AppOpsManager.OPSTR_FINE_LOCATION, "FINE_LOCATION")
            .setPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            .setRestriction(UserManager.DISALLOW_SHARE_LOCATION)
            .setAllowSystemRestrictionBypass(AppOpsManager.RestrictionBypass(true, false, false))
            .setDefaultMode(AppOpsManager.MODE_ALLOWED).build(),
        Builder(AppOpsManager.OP_GPS, AppOpsManager.OPSTR_GPS, "GPS")
            .setSwitchCode(AppOpsManager.OP_COARSE_LOCATION)
            .setRestriction(UserManager.DISALLOW_SHARE_LOCATION)
            .setDefaultMode(AppOpsManager.MODE_ALLOWED).build(),
        Builder(AppOpsManager.OP_VIBRATE, AppOpsManager.OPSTR_VIBRATE, "VIBRATE")
            .setSwitchCode(AppOpsManager.OP_VIBRATE).setPermission(Manifest.permission.VIBRATE)
            .setDefaultMode(AppOpsManager.MODE_ALLOWED).build(),
        Builder(AppOpsManager.OP_READ_CONTACTS, AppOpsManager.OPSTR_READ_CONTACTS, "READ_CONTACTS")
            .setPermission(Manifest.permission.READ_CONTACTS)
            .setDefaultMode(AppOpsManager.MODE_ALLOWED).build(),
        Builder(AppOpsManager.OP_WRITE_CONTACTS, AppOpsManager.OPSTR_WRITE_CONTACTS, "WRITE_CONTACTS")
            .setPermission(Manifest.permission.WRITE_CONTACTS)
            .setDefaultMode(AppOpsManager.MODE_ALLOWED).build(),
        Builder(AppOpsManager.OP_READ_CALL_LOG, AppOpsManager.OPSTR_READ_CALL_LOG, "READ_CALL_LOG")
            .setPermission(Manifest.permission.READ_CALL_LOG)
            .setRestriction(UserManager.DISALLOW_OUTGOING_CALLS)
            .setDefaultMode(AppOpsManager.MODE_ALLOWED).build(),
        Builder(AppOpsManager.OP_WRITE_CALL_LOG, AppOpsManager.OPSTR_WRITE_CALL_LOG, "WRITE_CALL_LOG")
            .setPermission(Manifest.permission.WRITE_CALL_LOG)
            .setRestriction(UserManager.DISALLOW_OUTGOING_CALLS)
            .setDefaultMode(AppOpsManager.MODE_ALLOWED).build(),
        Builder(AppOpsManager.OP_READ_CALENDAR, AppOpsManager.OPSTR_READ_CALENDAR, "READ_CALENDAR")
            .setPermission(Manifest.permission.READ_CALENDAR)
            .setDefaultMode(AppOpsManager.MODE_ALLOWED).build(),
        Builder(AppOpsManager.OP_WRITE_CALENDAR, AppOpsManager.OPSTR_WRITE_CALENDAR, "WRITE_CALENDAR")
            .setPermission(Manifest.permission.WRITE_CALENDAR)
            .setDefaultMode(AppOpsManager.MODE_ALLOWED).build(),
        Builder(AppOpsManager.OP_WIFI_SCAN, AppOpsManager.OPSTR_WIFI_SCAN, "WIFI_SCAN")
            .setSwitchCode(AppOpsManager.OP_COARSE_LOCATION)
            .setPermission(Manifest.permission.ACCESS_WIFI_STATE)
            .setRestriction(UserManager.DISALLOW_SHARE_LOCATION)
            .setAllowSystemRestrictionBypass(AppOpsManager.RestrictionBypass(false, true, false))
            .setDefaultMode(AppOpsManager.MODE_ALLOWED).build(),
        Builder(AppOpsManager.OP_POST_NOTIFICATION, AppOpsManager.OPSTR_POST_NOTIFICATION, "POST_NOTIFICATION")
            .setPermission(Manifest.permission.POST_NOTIFICATIONS)
            .setDefaultMode(AppOpsManager.MODE_ALLOWED).build(),
        Builder(AppOpsManager.OP_NEIGHBORING_CELLS, AppOpsManager.OPSTR_NEIGHBORING_CELLS, "NEIGHBORING_CELLS")
            .setSwitchCode(AppOpsManager.OP_COARSE_LOCATION).setDefaultMode(AppOpsManager.MODE_ALLOWED).build(),
        Builder(AppOpsManager.OP_CALL_PHONE, AppOpsManager.OPSTR_CALL_PHONE, "CALL_PHONE")
            .setSwitchCode(AppOpsManager.OP_CALL_PHONE).setPermission(Manifest.permission.CALL_PHONE)
            .setDefaultMode(AppOpsManager.MODE_ALLOWED).build(),
        Builder(AppOpsManager.OP_READ_SMS, AppOpsManager.OPSTR_READ_SMS, "READ_SMS")
            .setPermission(Manifest.permission.READ_SMS)
            .setRestriction(UserManager.DISALLOW_SMS).setDefaultMode(AppOpsManager.MODE_ALLOWED)
            .setDisableReset(true).build(),
        Builder(AppOpsManager.OP_WRITE_SMS, AppOpsManager.OPSTR_WRITE_SMS, "WRITE_SMS")
            .setRestriction(UserManager.DISALLOW_SMS)
            .setDefaultMode(AppOpsManager.MODE_IGNORED).setDisableReset(true).build(),
        Builder(AppOpsManager.OP_RECEIVE_SMS, AppOpsManager.OPSTR_RECEIVE_SMS, "RECEIVE_SMS")
            .setPermission(Manifest.permission.RECEIVE_SMS)
            .setRestriction(UserManager.DISALLOW_SMS)
            .setDefaultMode(AppOpsManager.MODE_ALLOWED).setDisableReset(true).build(),
        Builder(
            AppOpsManager.OP_RECEIVE_EMERGECY_SMS, AppOpsManager.OPSTR_RECEIVE_EMERGENCY_BROADCAST,
            "RECEIVE_EMERGENCY_BROADCAST"
        ).setSwitchCode(AppOpsManager.OP_RECEIVE_SMS)
            .setPermission(Manifest.permission.RECEIVE_EMERGENCY_BROADCAST)
            .setDefaultMode(AppOpsManager.MODE_ALLOWED).build(),
        Builder(AppOpsManager.OP_RECEIVE_MMS, AppOpsManager.OPSTR_RECEIVE_MMS, "RECEIVE_MMS")
            .setPermission(Manifest.permission.RECEIVE_MMS)
            .setRestriction(UserManager.DISALLOW_SMS).setDefaultMode(AppOpsManager.MODE_ALLOWED)
            .build(),
        Builder(AppOpsManager.OP_RECEIVE_WAP_PUSH, AppOpsManager.OPSTR_RECEIVE_WAP_PUSH, "RECEIVE_WAP_PUSH")
            .setPermission(Manifest.permission.RECEIVE_WAP_PUSH)
            .setDefaultMode(AppOpsManager.MODE_ALLOWED).setDisableReset(true).build(),
        Builder(AppOpsManager.OP_SEND_SMS, AppOpsManager.OPSTR_SEND_SMS, "SEND_SMS")
            .setPermission(Manifest.permission.SEND_SMS)
            .setRestriction(UserManager.DISALLOW_SMS).setDefaultMode(AppOpsManager.MODE_ALLOWED)
            .setDisableReset(true).build(),
        Builder(AppOpsManager.OP_READ_ICC_SMS, AppOpsManager.OPSTR_READ_ICC_SMS, "READ_ICC_SMS")
            .setSwitchCode(AppOpsManager.OP_READ_SMS).setPermission(Manifest.permission.READ_SMS)
            .setRestriction(UserManager.DISALLOW_SMS).setDefaultMode(AppOpsManager.MODE_ALLOWED)
            .build(),
        Builder(AppOpsManager.OP_WRITE_ICC_SMS, AppOpsManager.OPSTR_WRITE_ICC_SMS, "WRITE_ICC_SMS")
            .setSwitchCode(AppOpsManager.OP_WRITE_SMS).setRestriction(UserManager.DISALLOW_SMS)
            .setDefaultMode(AppOpsManager.MODE_ALLOWED).build(),
        Builder(AppOpsManager.OP_WRITE_SETTINGS, AppOpsManager.OPSTR_WRITE_SETTINGS, "WRITE_SETTINGS")
            .setPermission(Manifest.permission.WRITE_SETTINGS).build(),
        Builder(
            AppOpsManager.OP_SYSTEM_ALERT_WINDOW, AppOpsManager.OPSTR_SYSTEM_ALERT_WINDOW,
            "SYSTEM_ALERT_WINDOW"
        )
            .setPermission(Manifest.permission.SYSTEM_ALERT_WINDOW)
            .setRestriction(UserManager.DISALLOW_CREATE_WINDOWS)
            .setAllowSystemRestrictionBypass(AppOpsManager.RestrictionBypass(false, true, false))
            .setDefaultMode(AppOpsManager.getSystemAlertWindowDefault()).build(),
        Builder(
            AppOpsManager.OP_ACCESS_NOTIFICATIONS, AppOpsManager.OPSTR_ACCESS_NOTIFICATIONS,
            "ACCESS_NOTIFICATIONS"
        )
            .setPermission(Manifest.permission.ACCESS_NOTIFICATIONS)
            .setDefaultMode(AppOpsManager.MODE_ALLOWED).build(),
        Builder(AppOpsManager.OP_CAMERA, AppOpsManager.OPSTR_CAMERA, "CAMERA")
            .setPermission(Manifest.permission.CAMERA)
            .setRestriction(UserManager.DISALLOW_CAMERA)
            .setDefaultMode(AppOpsManager.MODE_ALLOWED).build(),
        Builder(AppOpsManager.OP_RECORD_AUDIO, AppOpsManager.OPSTR_RECORD_AUDIO, "RECORD_AUDIO")
            .setPermission(Manifest.permission.RECORD_AUDIO)
            .setRestriction(UserManager.DISALLOW_RECORD_AUDIO)
            .setAllowSystemRestrictionBypass(AppOpsManager.RestrictionBypass(false, false, true))
            .setDefaultMode(AppOpsManager.MODE_ALLOWED).build(),
        Builder(AppOpsManager.OP_PLAY_AUDIO, AppOpsManager.OPSTR_PLAY_AUDIO, "PLAY_AUDIO")
            .setDefaultMode(AppOpsManager.MODE_ALLOWED).build(),
        Builder(AppOpsManager.OP_READ_CLIPBOARD, AppOpsManager.OPSTR_READ_CLIPBOARD, "READ_CLIPBOARD")
            .setDefaultMode(AppOpsManager.MODE_ALLOWED).build(),
        Builder(AppOpsManager.OP_WRITE_CLIPBOARD, AppOpsManager.OPSTR_WRITE_CLIPBOARD, "WRITE_CLIPBOARD")
            .setDefaultMode(AppOpsManager.MODE_ALLOWED).build(),
        Builder(AppOpsManager.OP_TAKE_MEDIA_BUTTONS, AppOpsManager.OPSTR_TAKE_MEDIA_BUTTONS, "TAKE_MEDIA_BUTTONS")
            .setDefaultMode(AppOpsManager.MODE_ALLOWED)
            .build(),
        Builder(AppOpsManager.OP_TAKE_AUDIO_FOCUS, AppOpsManager.OPSTR_TAKE_AUDIO_FOCUS, "TAKE_AUDIO_FOCUS")
            .setDefaultMode(AppOpsManager.MODE_ALLOWED).build(),
        Builder(
            AppOpsManager.OP_AUDIO_MASTER_VOLUME, AppOpsManager.OPSTR_AUDIO_MASTER_VOLUME,
            "AUDIO_MASTER_VOLUME"
        ).setSwitchCode(AppOpsManager.OP_AUDIO_MASTER_VOLUME)
            .setRestriction(UserManager.DISALLOW_ADJUST_VOLUME)
            .setDefaultMode(AppOpsManager.MODE_ALLOWED).build(),
        Builder(AppOpsManager.OP_AUDIO_VOICE_VOLUME, AppOpsManager.OPSTR_AUDIO_VOICE_VOLUME, "AUDIO_VOICE_VOLUME")
            .setRestriction(UserManager.DISALLOW_ADJUST_VOLUME)
            .setDefaultMode(AppOpsManager.MODE_ALLOWED).build(),
        Builder(AppOpsManager.OP_AUDIO_RING_VOLUME, AppOpsManager.OPSTR_AUDIO_RING_VOLUME, "AUDIO_RING_VOLUME")
            .setRestriction(UserManager.DISALLOW_ADJUST_VOLUME)
            .setDefaultMode(AppOpsManager.MODE_ALLOWED).build(),
        Builder(AppOpsManager.OP_AUDIO_MEDIA_VOLUME, AppOpsManager.OPSTR_AUDIO_MEDIA_VOLUME, "AUDIO_MEDIA_VOLUME")
            .setRestriction(UserManager.DISALLOW_ADJUST_VOLUME)
            .setDefaultMode(AppOpsManager.MODE_ALLOWED).build(),
        Builder(AppOpsManager.OP_AUDIO_ALARM_VOLUME, AppOpsManager.OPSTR_AUDIO_ALARM_VOLUME, "AUDIO_ALARM_VOLUME")
            .setRestriction(UserManager.DISALLOW_ADJUST_VOLUME)
            .setDefaultMode(AppOpsManager.MODE_ALLOWED).build(),
        Builder(
            AppOpsManager.OP_AUDIO_NOTIFICATION_VOLUME, AppOpsManager.OPSTR_AUDIO_NOTIFICATION_VOLUME,
            "AUDIO_NOTIFICATION_VOLUME"
        ).setSwitchCode(AppOpsManager.OP_AUDIO_NOTIFICATION_VOLUME)
            .setRestriction(UserManager.DISALLOW_ADJUST_VOLUME)
            .setDefaultMode(AppOpsManager.MODE_ALLOWED).build(),
        Builder(
            AppOpsManager.OP_AUDIO_BLUETOOTH_VOLUME, AppOpsManager.OPSTR_AUDIO_BLUETOOTH_VOLUME,
            "AUDIO_BLUETOOTH_VOLUME"
        ).setRestriction(UserManager.DISALLOW_ADJUST_VOLUME)
            .setDefaultMode(AppOpsManager.MODE_ALLOWED).build(),
        Builder(AppOpsManager.OP_WAKE_LOCK, AppOpsManager.OPSTR_WAKE_LOCK, "WAKE_LOCK")
            .setPermission(Manifest.permission.WAKE_LOCK)
            .setDefaultMode(AppOpsManager.MODE_ALLOWED).build(),
        Builder(AppOpsManager.OP_MONITOR_LOCATION, AppOpsManager.OPSTR_MONITOR_LOCATION, "MONITOR_LOCATION")
            .setSwitchCode(AppOpsManager.OP_COARSE_LOCATION)
            .setRestriction(UserManager.DISALLOW_SHARE_LOCATION)
            .setDefaultMode(AppOpsManager.MODE_ALLOWED).build(),
        Builder(
            AppOpsManager.OP_MONITOR_HIGH_POWER_LOCATION, AppOpsManager.OPSTR_MONITOR_HIGH_POWER_LOCATION,
            "MONITOR_HIGH_POWER_LOCATION"
        ).setSwitchCode(AppOpsManager.OP_COARSE_LOCATION)
            .setRestriction(UserManager.DISALLOW_SHARE_LOCATION)
            .setDefaultMode(AppOpsManager.MODE_ALLOWED).build(),
        Builder(AppOpsManager.OP_GET_USAGE_STATS, AppOpsManager.OPSTR_GET_USAGE_STATS, "GET_USAGE_STATS")
            .setPermission(Manifest.permission.PACKAGE_USAGE_STATS).build(),
        Builder(AppOpsManager.OP_MUTE_MICROPHONE, AppOpsManager.OPSTR_MUTE_MICROPHONE, "MUTE_MICROPHONE")
            .setRestriction(UserManager.DISALLOW_UNMUTE_MICROPHONE)
            .setDefaultMode(AppOpsManager.MODE_ALLOWED).build(),
        Builder(AppOpsManager.OP_TOAST_WINDOW, AppOpsManager.OPSTR_TOAST_WINDOW, "TOAST_WINDOW")
            .setRestriction(UserManager.DISALLOW_CREATE_WINDOWS)
            .setAllowSystemRestrictionBypass(AppOpsManager.RestrictionBypass(false, true, false))
            .setDefaultMode(AppOpsManager.MODE_ALLOWED).build(),
        Builder(AppOpsManager.OP_PROJECT_MEDIA, AppOpsManager.OPSTR_PROJECT_MEDIA, "PROJECT_MEDIA")
            .setDefaultMode(AppOpsManager.MODE_IGNORED).build(),
        Builder(AppOpsManager.OP_ACTIVATE_VPN, AppOpsManager.OPSTR_ACTIVATE_VPN, "ACTIVATE_VPN")
            .setDefaultMode(AppOpsManager.MODE_IGNORED).build(),
        Builder(AppOpsManager.OP_WRITE_WALLPAPER, AppOpsManager.OPSTR_WRITE_WALLPAPER, "WRITE_WALLPAPER")
            .setRestriction(UserManager.DISALLOW_WALLPAPER)
            .setDefaultMode(AppOpsManager.MODE_ALLOWED).build(),
        Builder(AppOpsManager.OP_ASSIST_STRUCTURE, AppOpsManager.OPSTR_ASSIST_STRUCTURE, "ASSIST_STRUCTURE")
            .setDefaultMode(AppOpsManager.MODE_ALLOWED).build(),
        Builder(AppOpsManager.OP_ASSIST_SCREENSHOT, AppOpsManager.OPSTR_ASSIST_SCREENSHOT, "ASSIST_SCREENSHOT")
            .setDefaultMode(AppOpsManager.MODE_ALLOWED)
            .build(),
        Builder(AppOpsManager.OP_READ_PHONE_STATE, AppOpsManager.OPSTR_READ_PHONE_STATE, "READ_PHONE_STATE")
            .setPermission(Manifest.permission.READ_PHONE_STATE)
            .setDefaultMode(AppOpsManager.MODE_ALLOWED).build(),
        Builder(AppOpsManager.OP_ADD_VOICEMAIL, AppOpsManager.OPSTR_ADD_VOICEMAIL, "ADD_VOICEMAIL")
            .setPermission(Manifest.permission.ADD_VOICEMAIL)
            .setDefaultMode(AppOpsManager.MODE_ALLOWED).build(),
        Builder(AppOpsManager.OP_USE_SIP, AppOpsManager.OPSTR_USE_SIP, "USE_SIP")
            .setPermission(Manifest.permission.USE_SIP)
            .setDefaultMode(AppOpsManager.MODE_ALLOWED).build(),
        Builder(
            AppOpsManager.OP_PROCESS_OUTGOING_CALLS, AppOpsManager.OPSTR_PROCESS_OUTGOING_CALLS,
            "PROCESS_OUTGOING_CALLS"
        ).setSwitchCode(AppOpsManager.OP_PROCESS_OUTGOING_CALLS)
            .setPermission(Manifest.permission.PROCESS_OUTGOING_CALLS)
            .setDefaultMode(AppOpsManager.MODE_ALLOWED).build(),
        Builder(AppOpsManager.OP_USE_FINGERPRINT, AppOpsManager.OPSTR_USE_FINGERPRINT, "USE_FINGERPRINT")
            .setPermission(Manifest.permission.USE_FINGERPRINT)
            .setDefaultMode(AppOpsManager.MODE_ALLOWED).build(),
        Builder(AppOpsManager.OP_BODY_SENSORS, AppOpsManager.OPSTR_BODY_SENSORS, "BODY_SENSORS")
            .setPermission(Manifest.permission.BODY_SENSORS)
            .setDefaultMode(AppOpsManager.MODE_ALLOWED).build(),
        Builder(
            AppOpsManager.OP_READ_CELL_BROADCASTS, AppOpsManager.OPSTR_READ_CELL_BROADCASTS,
            "READ_CELL_BROADCASTS"
        ).setPermission(Manifest.permission.READ_CELL_BROADCASTS)
            .setDefaultMode(AppOpsManager.MODE_ALLOWED).setDisableReset(true).build(),
        Builder(AppOpsManager.OP_MOCK_LOCATION, AppOpsManager.OPSTR_MOCK_LOCATION, "MOCK_LOCATION")
            .setDefaultMode(AppOpsManager.MODE_ERRORED).build(),
        Builder(
            AppOpsManager.OP_READ_EXTERNAL_STORAGE, AppOpsManager.OPSTR_READ_EXTERNAL_STORAGE,
            "READ_EXTERNAL_STORAGE"
        ).setPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            .setDefaultMode(AppOpsManager.MODE_ALLOWED).build(),
        Builder(
            AppOpsManager.OP_WRITE_EXTERNAL_STORAGE, AppOpsManager.OPSTR_WRITE_EXTERNAL_STORAGE,
            "WRITE_EXTERNAL_STORAGE"
        ).setPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .setDefaultMode(AppOpsManager.MODE_ALLOWED).build(),
        Builder(AppOpsManager.OP_TURN_SCREEN_ON, AppOpsManager.OPSTR_TURN_SCREEN_ON, "TURN_SCREEN_ON")
            .setPermission(Manifest.permission.TURN_SCREEN_ON)
            .setDefaultMode(AppOpsManager.MODE_DEFAULT).build(),
        Builder(AppOpsManager.OP_GET_ACCOUNTS, AppOpsManager.OPSTR_GET_ACCOUNTS, "GET_ACCOUNTS")
            .setPermission(Manifest.permission.GET_ACCOUNTS)
            .setDefaultMode(AppOpsManager.MODE_ALLOWED).build(),
        Builder(AppOpsManager.OP_RUN_IN_BACKGROUND, AppOpsManager.OPSTR_RUN_IN_BACKGROUND, "RUN_IN_BACKGROUND")
            .setDefaultMode(AppOpsManager.MODE_ALLOWED)
            .build(),
        Builder(
            AppOpsManager.OP_AUDIO_ACCESSIBILITY_VOLUME, AppOpsManager.OPSTR_AUDIO_ACCESSIBILITY_VOLUME,
            "AUDIO_ACCESSIBILITY_VOLUME"
        )
            .setRestriction(UserManager.DISALLOW_ADJUST_VOLUME)
            .setDefaultMode(AppOpsManager.MODE_ALLOWED).build(),
        Builder(AppOpsManager.OP_READ_PHONE_NUMBERS, AppOpsManager.OPSTR_READ_PHONE_NUMBERS, "READ_PHONE_NUMBERS")
            .setPermission(Manifest.permission.READ_PHONE_NUMBERS)
            .setDefaultMode(AppOpsManager.MODE_ALLOWED).build(),
        Builder(
            AppOpsManager.OP_REQUEST_INSTALL_PACKAGES, AppOpsManager.OPSTR_REQUEST_INSTALL_PACKAGES,
            "REQUEST_INSTALL_PACKAGES"
        ).setSwitchCode(AppOpsManager.OP_REQUEST_INSTALL_PACKAGES)
            .setPermission(Manifest.permission.REQUEST_INSTALL_PACKAGES).build(),
        Builder(AppOpsManager.OP_PICTURE_IN_PICTURE, AppOpsManager.OPSTR_PICTURE_IN_PICTURE, "PICTURE_IN_PICTURE")
            .setSwitchCode(AppOpsManager.OP_PICTURE_IN_PICTURE).setDefaultMode(AppOpsManager.MODE_ALLOWED)
            .build(),
        Builder(
            AppOpsManager.OP_INSTANT_APP_START_FOREGROUND, AppOpsManager.OPSTR_INSTANT_APP_START_FOREGROUND,
            "INSTANT_APP_START_FOREGROUND"
        )
            .setPermission(Manifest.permission.INSTANT_APP_FOREGROUND_SERVICE).build(),
        Builder(AppOpsManager.OP_ANSWER_PHONE_CALLS, AppOpsManager.OPSTR_ANSWER_PHONE_CALLS, "ANSWER_PHONE_CALLS")
            .setSwitchCode(AppOpsManager.OP_ANSWER_PHONE_CALLS)
            .setPermission(Manifest.permission.ANSWER_PHONE_CALLS)
            .setDefaultMode(AppOpsManager.MODE_ALLOWED).build(),
        Builder(
            AppOpsManager.OP_RUN_ANY_IN_BACKGROUND, AppOpsManager.OPSTR_RUN_ANY_IN_BACKGROUND,
            "RUN_ANY_IN_BACKGROUND"
        )
            .setDefaultMode(AppOpsManager.MODE_ALLOWED).build(),
        Builder(AppOpsManager.OP_CHANGE_WIFI_STATE, AppOpsManager.OPSTR_CHANGE_WIFI_STATE, "CHANGE_WIFI_STATE")
            .setSwitchCode(AppOpsManager.OP_CHANGE_WIFI_STATE)
            .setPermission(Manifest.permission.CHANGE_WIFI_STATE)
            .setDefaultMode(AppOpsManager.MODE_ALLOWED).build(),
        Builder(
            AppOpsManager.OP_REQUEST_DELETE_PACKAGES, AppOpsManager.OPSTR_REQUEST_DELETE_PACKAGES,
            "REQUEST_DELETE_PACKAGES"
        )
            .setPermission(Manifest.permission.REQUEST_DELETE_PACKAGES)
            .setDefaultMode(AppOpsManager.MODE_ALLOWED).build(),
        Builder(
            AppOpsManager.OP_BIND_ACCESSIBILITY_SERVICE, AppOpsManager.OPSTR_BIND_ACCESSIBILITY_SERVICE,
            "BIND_ACCESSIBILITY_SERVICE"
        )
            .setPermission(Manifest.permission.BIND_ACCESSIBILITY_SERVICE)
            .setDefaultMode(AppOpsManager.MODE_ALLOWED).build(),
        Builder(AppOpsManager.OP_ACCEPT_HANDOVER, AppOpsManager.OPSTR_ACCEPT_HANDOVER, "ACCEPT_HANDOVER")
            .setSwitchCode(AppOpsManager.OP_ACCEPT_HANDOVER)
            .setPermission(Manifest.permission.ACCEPT_HANDOVER)
            .setDefaultMode(AppOpsManager.MODE_ALLOWED).build(),
        Builder(
            AppOpsManager.OP_MANAGE_IPSEC_TUNNELS, AppOpsManager.OPSTR_MANAGE_IPSEC_TUNNELS,
            "MANAGE_IPSEC_TUNNELS"
        )
            .setPermission(Manifest.permission.MANAGE_IPSEC_TUNNELS)
            .setDefaultMode(AppOpsManager.MODE_ERRORED).build(),
        Builder(AppOpsManager.OP_START_FOREGROUND, AppOpsManager.OPSTR_START_FOREGROUND, "START_FOREGROUND")
            .setPermission(Manifest.permission.FOREGROUND_SERVICE)
            .setDefaultMode(AppOpsManager.MODE_ALLOWED).build(),
        Builder(AppOpsManager.OP_BLUETOOTH_SCAN, AppOpsManager.OPSTR_BLUETOOTH_SCAN, "BLUETOOTH_SCAN")
            .setPermission(Manifest.permission.BLUETOOTH_SCAN)
            .setAllowSystemRestrictionBypass(AppOpsManager.RestrictionBypass(false, true, false))
            .setDefaultMode(AppOpsManager.MODE_ALLOWED).build(),
        Builder(AppOpsManager.OP_USE_BIOMETRIC, AppOpsManager.OPSTR_USE_BIOMETRIC, "USE_BIOMETRIC")
            .setPermission(Manifest.permission.USE_BIOMETRIC)
            .setDefaultMode(AppOpsManager.MODE_ALLOWED).build(),
        Builder(
            AppOpsManager.OP_ACTIVITY_RECOGNITION, AppOpsManager.OPSTR_ACTIVITY_RECOGNITION,
            "ACTIVITY_RECOGNITION"
        )
            .setPermission(Manifest.permission.ACTIVITY_RECOGNITION)
            .setDefaultMode(AppOpsManager.MODE_ALLOWED).build(),
        Builder(
            AppOpsManager.OP_SMS_FINANCIAL_TRANSACTIONS, AppOpsManager.OPSTR_SMS_FINANCIAL_TRANSACTIONS,
            "SMS_FINANCIAL_TRANSACTIONS"
        )
            .setPermission(Manifest.permission.SMS_FINANCIAL_TRANSACTIONS)
            .setRestriction(UserManager.DISALLOW_SMS).build(),
        Builder(AppOpsManager.OP_READ_MEDIA_AUDIO, AppOpsManager.OPSTR_READ_MEDIA_AUDIO, "READ_MEDIA_AUDIO")
            .setPermission(Manifest.permission.READ_MEDIA_AUDIO)
            .setDefaultMode(AppOpsManager.MODE_ALLOWED).build(),
        Builder(AppOpsManager.OP_WRITE_MEDIA_AUDIO, AppOpsManager.OPSTR_WRITE_MEDIA_AUDIO, "WRITE_MEDIA_AUDIO")
            .setDefaultMode(AppOpsManager.MODE_ERRORED).build(),
        Builder(AppOpsManager.OP_READ_MEDIA_VIDEO, AppOpsManager.OPSTR_READ_MEDIA_VIDEO, "READ_MEDIA_VIDEO")
            .setPermission(Manifest.permission.READ_MEDIA_VIDEO)
            .setDefaultMode(AppOpsManager.MODE_ALLOWED).build(),
        Builder(AppOpsManager.OP_WRITE_MEDIA_VIDEO, AppOpsManager.OPSTR_WRITE_MEDIA_VIDEO, "WRITE_MEDIA_VIDEO")
            .setDefaultMode(AppOpsManager.MODE_ERRORED).setDisableReset(true).build(),
        Builder(AppOpsManager.OP_READ_MEDIA_IMAGES, AppOpsManager.OPSTR_READ_MEDIA_IMAGES, "READ_MEDIA_IMAGES")
            .setPermission(Manifest.permission.READ_MEDIA_IMAGES)
            .setDefaultMode(AppOpsManager.MODE_ALLOWED).build(),
        Builder(AppOpsManager.OP_WRITE_MEDIA_IMAGES, AppOpsManager.OPSTR_WRITE_MEDIA_IMAGES, "WRITE_MEDIA_IMAGES")
            .setDefaultMode(AppOpsManager.MODE_ERRORED).setDisableReset(true).build(),
        Builder(AppOpsManager.OP_LEGACY_STORAGE, AppOpsManager.OPSTR_LEGACY_STORAGE, "LEGACY_STORAGE")
            .setDisableReset(true).build(),
        Builder(
            AppOpsManager.OP_ACCESS_ACCESSIBILITY, AppOpsManager.OPSTR_ACCESS_ACCESSIBILITY,
            "ACCESS_ACCESSIBILITY"
        ).setDefaultMode(AppOpsManager.MODE_ALLOWED).build(),
        Builder(
            AppOpsManager.OP_READ_DEVICE_IDENTIFIERS, AppOpsManager.OPSTR_READ_DEVICE_IDENTIFIERS,
            "READ_DEVICE_IDENTIFIERS"
        ).setDefaultMode(AppOpsManager.MODE_ERRORED).build(),
        Builder(
            AppOpsManager.OP_ACCESS_MEDIA_LOCATION, AppOpsManager.OPSTR_ACCESS_MEDIA_LOCATION,
            "ACCESS_MEDIA_LOCATION"
        ).setPermission(Manifest.permission.ACCESS_MEDIA_LOCATION)
            .setDefaultMode(AppOpsManager.MODE_ALLOWED).build(),
        Builder(AppOpsManager.OP_QUERY_ALL_PACKAGES, AppOpsManager.OPSTR_QUERY_ALL_PACKAGES, "QUERY_ALL_PACKAGES")
            .build(),
        Builder(
            AppOpsManager.OP_MANAGE_EXTERNAL_STORAGE, AppOpsManager.OPSTR_MANAGE_EXTERNAL_STORAGE,
            "MANAGE_EXTERNAL_STORAGE"
        )
            .setPermission(Manifest.permission.MANAGE_EXTERNAL_STORAGE).build(),
        Builder(
            AppOpsManager.OP_INTERACT_ACROSS_PROFILES, AppOpsManager.OPSTR_INTERACT_ACROSS_PROFILES,
            "INTERACT_ACROSS_PROFILES"
        )
            .setPermission(Manifest.permission.INTERACT_ACROSS_PROFILES).build(),
        Builder(
            AppOpsManager.OP_ACTIVATE_PLATFORM_VPN, AppOpsManager.OPSTR_ACTIVATE_PLATFORM_VPN,
            "ACTIVATE_PLATFORM_VPN"
        ).setDefaultMode(AppOpsManager.MODE_IGNORED).build(),
        Builder(AppOpsManager.OP_LOADER_USAGE_STATS, AppOpsManager.OPSTR_LOADER_USAGE_STATS, "LOADER_USAGE_STATS")
            .setPermission(Manifest.permission.LOADER_USAGE_STATS).build(),
        Builder(AppOpsManager.OP_NONE, "", "").setDefaultMode(AppOpsManager.MODE_IGNORED).build(),
        Builder(
            AppOpsManager.OP_AUTO_REVOKE_PERMISSIONS_IF_UNUSED,
            AppOpsManager.OPSTR_AUTO_REVOKE_PERMISSIONS_IF_UNUSED, "AUTO_REVOKE_PERMISSIONS_IF_UNUSED"
        )
            .build(),
        Builder(
            AppOpsManager.OP_AUTO_REVOKE_MANAGED_BY_INSTALLER,
            AppOpsManager.OPSTR_AUTO_REVOKE_MANAGED_BY_INSTALLER, "AUTO_REVOKE_MANAGED_BY_INSTALLER"
        )
            .setDefaultMode(AppOpsManager.MODE_ALLOWED).build(),
        Builder(
            AppOpsManager.OP_NO_ISOLATED_STORAGE, AppOpsManager.OPSTR_NO_ISOLATED_STORAGE,
            "NO_ISOLATED_STORAGE"
        ).setDefaultMode(AppOpsManager.MODE_ERRORED)
            .setDisableReset(true).build(),
        Builder(
            AppOpsManager.OP_PHONE_CALL_MICROPHONE, AppOpsManager.OPSTR_PHONE_CALL_MICROPHONE,
            "PHONE_CALL_MICROPHONE"
        ).setDefaultMode(AppOpsManager.MODE_ALLOWED).build(),
        Builder(AppOpsManager.OP_PHONE_CALL_CAMERA, AppOpsManager.OPSTR_PHONE_CALL_CAMERA, "PHONE_CALL_CAMERA")
            .setDefaultMode(AppOpsManager.MODE_ALLOWED).build(),
        Builder(
            AppOpsManager.OP_RECORD_AUDIO_HOTWORD, AppOpsManager.OPSTR_RECORD_AUDIO_HOTWORD,
            "RECORD_AUDIO_HOTWORD"
        ).setDefaultMode(AppOpsManager.MODE_ALLOWED).build(),
        Builder(
            AppOpsManager.OP_MANAGE_ONGOING_CALLS, AppOpsManager.OPSTR_MANAGE_ONGOING_CALLS,
            "MANAGE_ONGOING_CALLS"
        ).setPermission(Manifest.permission.MANAGE_ONGOING_CALLS)
            .setDisableReset(true).build(),
        Builder(AppOpsManager.OP_MANAGE_CREDENTIALS, AppOpsManager.OPSTR_MANAGE_CREDENTIALS, "MANAGE_CREDENTIALS")
            .build(),
        Builder(
            AppOpsManager.OP_USE_ICC_AUTH_WITH_DEVICE_IDENTIFIER,
            AppOpsManager.OPSTR_USE_ICC_AUTH_WITH_DEVICE_IDENTIFIER, "USE_ICC_AUTH_WITH_DEVICE_IDENTIFIER"
        )
            .setPermission(Manifest.permission.USE_ICC_AUTH_WITH_DEVICE_IDENTIFIER)
            .setDisableReset(true).build(),
        Builder(
            AppOpsManager.OP_RECORD_AUDIO_OUTPUT, AppOpsManager.OPSTR_RECORD_AUDIO_OUTPUT,
            "RECORD_AUDIO_OUTPUT"
        ).setDefaultMode(AppOpsManager.MODE_ALLOWED).build(),
        Builder(
            AppOpsManager.OP_SCHEDULE_EXACT_ALARM, AppOpsManager.OPSTR_SCHEDULE_EXACT_ALARM,
            "SCHEDULE_EXACT_ALARM"
        ).setPermission(Manifest.permission.SCHEDULE_EXACT_ALARM)
            .build(),
        Builder(
            AppOpsManager.OP_FINE_LOCATION_SOURCE, AppOpsManager.OPSTR_FINE_LOCATION_SOURCE,
            "FINE_LOCATION_SOURCE"
        ).setSwitchCode(AppOpsManager.OP_FINE_LOCATION)
            .setDefaultMode(AppOpsManager.MODE_ALLOWED).build(),
        Builder(
            AppOpsManager.OP_COARSE_LOCATION_SOURCE, AppOpsManager.OPSTR_COARSE_LOCATION_SOURCE,
            "COARSE_LOCATION_SOURCE"
        ).setSwitchCode(AppOpsManager.OP_COARSE_LOCATION)
            .setDefaultMode(AppOpsManager.MODE_ALLOWED).build(),
        Builder(AppOpsManager.OP_MANAGE_MEDIA, AppOpsManager.OPSTR_MANAGE_MEDIA, "MANAGE_MEDIA")
            .setPermission(Manifest.permission.MANAGE_MEDIA).build(),
        Builder(AppOpsManager.OP_BLUETOOTH_CONNECT, AppOpsManager.OPSTR_BLUETOOTH_CONNECT, "BLUETOOTH_CONNECT")
            .setPermission(Manifest.permission.BLUETOOTH_CONNECT)
            .setDefaultMode(AppOpsManager.MODE_ALLOWED).build(),
        Builder(AppOpsManager.OP_UWB_RANGING, AppOpsManager.OPSTR_UWB_RANGING, "UWB_RANGING")
            .setPermission(Manifest.permission.UWB_RANGING)
            .setDefaultMode(AppOpsManager.MODE_ALLOWED).build(),
        Builder(
            AppOpsManager.OP_ACTIVITY_RECOGNITION_SOURCE, AppOpsManager.OPSTR_ACTIVITY_RECOGNITION_SOURCE,
            "ACTIVITY_RECOGNITION_SOURCE"
        )
            .setSwitchCode(AppOpsManager.OP_ACTIVITY_RECOGNITION).setDefaultMode(AppOpsManager.MODE_ALLOWED)
            .build(),
        Builder(
            AppOpsManager.OP_BLUETOOTH_ADVERTISE, AppOpsManager.OPSTR_BLUETOOTH_ADVERTISE,
            "BLUETOOTH_ADVERTISE"
        ).setPermission(Manifest.permission.BLUETOOTH_ADVERTISE)
            .setDefaultMode(AppOpsManager.MODE_ALLOWED).build(),
        Builder(
            AppOpsManager.OP_RECORD_INCOMING_PHONE_AUDIO, AppOpsManager.OPSTR_RECORD_INCOMING_PHONE_AUDIO,
            "RECORD_INCOMING_PHONE_AUDIO"
        ).setDefaultMode(AppOpsManager.MODE_ALLOWED).build(),
        Builder(
            AppOpsManager.OP_NEARBY_WIFI_DEVICES, AppOpsManager.OPSTR_NEARBY_WIFI_DEVICES,
            "NEARBY_WIFI_DEVICES"
        ).setPermission(Manifest.permission.NEARBY_WIFI_DEVICES)
            .setDefaultMode(AppOpsManager.MODE_ALLOWED).build(),
        Builder(
            AppOpsManager.OP_ESTABLISH_VPN_SERVICE, AppOpsManager.OPSTR_ESTABLISH_VPN_SERVICE,
            "ESTABLISH_VPN_SERVICE"
        ).setDefaultMode(AppOpsManager.MODE_ALLOWED).build(),
        Builder(
            AppOpsManager.OP_ESTABLISH_VPN_MANAGER, AppOpsManager.OPSTR_ESTABLISH_VPN_MANAGER,
            "ESTABLISH_VPN_MANAGER"
        ).setDefaultMode(AppOpsManager.MODE_ALLOWED).build(),
        Builder(
            AppOpsManager.OP_ACCESS_RESTRICTED_SETTINGS, AppOpsManager.OPSTR_ACCESS_RESTRICTED_SETTINGS,
            "ACCESS_RESTRICTED_SETTINGS"
        ).setDefaultMode(AppOpsManager.MODE_ALLOWED)
            .setDisableReset(true).setRestrictRead(true).build(),
        Builder(
            AppOpsManager.OP_RECEIVE_AMBIENT_TRIGGER_AUDIO, AppOpsManager.OPSTR_RECEIVE_AMBIENT_TRIGGER_AUDIO,
            "RECEIVE_SOUNDTRIGGER_AUDIO"
        ).setDefaultMode(AppOpsManager.MODE_ALLOWED)
            .setForceCollectNotes(true).build(),
        Builder(
            AppOpsManager.OP_RECEIVE_EXPLICIT_USER_INTERACTION_AUDIO,
            AppOpsManager.OPSTR_RECEIVE_EXPLICIT_USER_INTERACTION_AUDIO,
            "RECEIVE_EXPLICIT_USER_INTERACTION_AUDIO"
        ).setDefaultMode(
            AppOpsManager.MODE_ALLOWED
        ).build(),
        Builder(
            AppOpsManager.OP_RUN_USER_INITIATED_JOBS, AppOpsManager.OPSTR_RUN_USER_INITIATED_JOBS,
            "RUN_USER_INITIATED_JOBS"
        ).setDefaultMode(AppOpsManager.MODE_ALLOWED)
            .build(),
        Builder(
            AppOpsManager.OP_READ_MEDIA_VISUAL_USER_SELECTED,
            AppOpsManager.OPSTR_READ_MEDIA_VISUAL_USER_SELECTED, "READ_MEDIA_VISUAL_USER_SELECTED"
        )
            .setPermission(Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED)
            .setDefaultMode(AppOpsManager.MODE_ALLOWED).build(),
        Builder(
            AppOpsManager.OP_SYSTEM_EXEMPT_FROM_SUSPENSION,
            AppOpsManager.OPSTR_SYSTEM_EXEMPT_FROM_SUSPENSION,
            "SYSTEM_EXEMPT_FROM_SUSPENSION"
        )
            .setDisableReset(true).build(),
        Builder(
            AppOpsManager.OP_SYSTEM_EXEMPT_FROM_DISMISSIBLE_NOTIFICATIONS,
            AppOpsManager.OPSTR_SYSTEM_EXEMPT_FROM_DISMISSIBLE_NOTIFICATIONS,
            "SYSTEM_EXEMPT_FROM_DISMISSIBLE_NOTIFICATIONS"
        )
            .setDisableReset(true).build(),
        Builder(
            AppOpsManager.OP_READ_WRITE_HEALTH_DATA, AppOpsManager.OPSTR_READ_WRITE_HEALTH_DATA,
            "READ_WRITE_HEALTH_DATA"
        ).setDefaultMode(AppOpsManager.MODE_ALLOWED).build(),
        Builder(
            AppOpsManager.OP_FOREGROUND_SERVICE_SPECIAL_USE,
            AppOpsManager.OPSTR_FOREGROUND_SERVICE_SPECIAL_USE, "FOREGROUND_SERVICE_SPECIAL_USE"
        )
            .setPermission(Manifest.permission.FOREGROUND_SERVICE_SPECIAL_USE).build(),
        Builder(
            AppOpsManager.OP_SYSTEM_EXEMPT_FROM_POWER_RESTRICTIONS,
            AppOpsManager.OPSTR_SYSTEM_EXEMPT_FROM_POWER_RESTRICTIONS,
            "SYSTEM_EXEMPT_FROM_POWER_RESTRICTIONS"
        )
            .setDisableReset(true).build(),
        Builder(
            AppOpsManager.OP_SYSTEM_EXEMPT_FROM_HIBERNATION,
            AppOpsManager.OPSTR_SYSTEM_EXEMPT_FROM_HIBERNATION,
            "SYSTEM_EXEMPT_FROM_HIBERNATION"
        )
            .setDisableReset(true).build(),
        Builder(
            AppOpsManager.OP_SYSTEM_EXEMPT_FROM_ACTIVITY_BG_START_RESTRICTION,
            AppOpsManager.OPSTR_SYSTEM_EXEMPT_FROM_ACTIVITY_BG_START_RESTRICTION,
            "SYSTEM_EXEMPT_FROM_ACTIVITY_BG_START_RESTRICTION"
        )
            .setDisableReset(true).build(),
        Builder(
            AppOpsManager.OP_CAPTURE_CONSENTLESS_BUGREPORT_ON_USERDEBUG_BUILD,
            AppOpsManager.OPSTR_CAPTURE_CONSENTLESS_BUGREPORT_ON_USERDEBUG_BUILD,
            "CAPTURE_CONSENTLESS_BUGREPORT_ON_USERDEBUG_BUILD"
        )
            .setPermission(Manifest.permission.CAPTURE_CONSENTLESS_BUGREPORT_ON_USERDEBUG_BUILD)
            .build(),
        Builder(AppOpsManager.OP_DEPRECATED_2, AppOpsManager.OPSTR_DEPRECATED_2, "DEPRECATED_2")
            .setDefaultMode(AppOpsManager.MODE_IGNORED).build(),
        Builder(
            AppOpsManager.OP_USE_FULL_SCREEN_INTENT, AppOpsManager.OPSTR_USE_FULL_SCREEN_INTENT,
            "USE_FULL_SCREEN_INTENT"
        ).setPermission(Manifest.permission.USE_FULL_SCREEN_INTENT)
            .build(),
        Builder(
            AppOpsManager.OP_CAMERA_SANDBOXED, AppOpsManager.OPSTR_CAMERA_SANDBOXED,
            "CAMERA_SANDBOXED"
        ).setDefaultMode(AppOpsManager.MODE_ALLOWED).build(),
        Builder(
            AppOpsManager.OP_RECORD_AUDIO_SANDBOXED, AppOpsManager.OPSTR_RECORD_AUDIO_SANDBOXED,
            "RECORD_AUDIO_SANDBOXED"
        ).setDefaultMode(AppOpsManager.MODE_ALLOWED).build()
    )
}