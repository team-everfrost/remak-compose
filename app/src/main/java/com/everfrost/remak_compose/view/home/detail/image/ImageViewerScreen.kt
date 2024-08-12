package com.everfrost.remak_compose.view.home.detail.image

import android.util.Log
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.everfrost.remak_compose.viewModel.home.detail.image.ImageDetailViewModel
import com.skydoves.landscapist.glide.GlideImage
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

            ZoomableGlideImage(model = thumbnailUrl, contentDescription = null)

        }

    }

}