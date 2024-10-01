package com.example.myweatherapp.presentation.ui.components.states

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.myweatherapp.R
import com.example.myweatherapp.utils.connectivity.NetworkUtil

@Composable
fun LoadingState() {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .align(Alignment.Center)
        )
    }

}


@Composable
fun ErrorState() {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (NetworkUtil.isInternetAvailable(context).not())
            Toast.makeText(
                context,
                stringResource(id = R.string.label_no_internet_connection),
                Toast.LENGTH_SHORT
            ).show()
        else
            Toast.makeText(
                context, stringResource(id = R.string.label_unknown_error),
                Toast.LENGTH_SHORT
            ).show()
    }
}