package  util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

class ConnectionManager {
    fun checkConnectivity(context: Context): Boolean {

        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo

        if (activeNetwork?.isConnected != null){
            return activeNetwork.isConnected
        } else {
            return false
        }
    }
}

//isconnected is a method of networkinfo
// we use it to check whether the device is connected to the internet or not
// this can return to values
// true - if the network has internet
// false - if the network doesn't have internet
// null - if the network is broken/inactive