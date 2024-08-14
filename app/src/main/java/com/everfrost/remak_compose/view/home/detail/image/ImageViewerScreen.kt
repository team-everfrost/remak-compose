package com.everfrost.remak_compose.view.home.detail.image

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.everfrost.remak_compose.viewModel.home.detail.image.ImageDetailViewModel
import me.saket.telephoto.zoomable.glide.ZoomableGlideImage
import me.saket.telephoto.zoomable.rememberZoomableState


@Composable
fun ImageViewerScreen(
    viewModel: ImageDetailViewModel,
    navController: NavController
) {
    val state = rememberZoomableState()
    val thumbnailUrl by viewModel.thumbnailUrl.collectAsState()

    Scaffold { innerPadding ->
        Log.d("ImageViewerScreen", "thumbnailUrl: $thumbnailUrl")
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            ZoomableGlideImage(
                model = thumbnailUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center)
            )

        }

    }

}