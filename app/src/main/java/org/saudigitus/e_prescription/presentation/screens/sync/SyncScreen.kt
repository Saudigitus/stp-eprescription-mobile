package org.saudigitus.e_prescription.presentation.screens.sync


import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.RenderMode
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import org.saudigitus.e_prescription.R

@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
fun SyncScreen(
    viewModel: SyncViewModel,
    navigateToHome: () -> Unit = {}
) {

    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.lottie_world),
    )
    val progress by animateLottieCompositionAsState(
        composition,
        isPlaying = true,
        restartOnPlay = true,
        speed = 0.9F,
        iterations = LottieConstants.IterateForever,
    )

    val state by viewModel.syncUiState.collectAsStateWithLifecycle()

    val syncMetadataLogo = AnimatedImageVector.animatedVectorResource(state.metadataLogo)
    val syncDataLogo = AnimatedImageVector.animatedVectorResource(state.dataLogo)

    if (state.dataSyncStep == SyncStep.SUCCESS) {
        navigateToHome()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            modifier = Modifier.height(48.dp),
            alignment = Alignment.TopCenter,
            painter = painterResource(R.drawable.ic_sync),
            contentDescription = null,
            colorFilter = ColorFilter.tint(Color.White)
        )

        LottieAnimation(
            composition = composition,
            progress = { progress },
            alignment = Alignment.Center,
            renderMode = RenderMode.AUTOMATIC
        )

        Column(
            modifier = Modifier.fillMaxWidth(1f),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .height(48.dp)
                    .padding(5.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SyncStateLabel(value = state.metadataSyncMsg.toString())

                Image(
                    painter = if (state.dataSyncStep != SyncStep.FAILED) {
                        rememberAnimatedVectorPainter(
                            syncMetadataLogo,
                            state.metadataSyncStep == SyncStep.RUNNING ||
                                state.metadataSyncStep == SyncStep.SUCCESS ||
                                state.metadataSyncStep == SyncStep.FAILED
                        )
                    } else painterResource(state.errorLogo!!),
                    contentDescription = state.metadataSyncMsg,
                    contentScale = ContentScale.Crop
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .height(48.dp)
                    .padding(5.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SyncStateLabel(
                    value = state.dataSyncMsg.toString(),
                    alpha = if (state.dataSyncStep == SyncStep.RUNNING) 1f else .3f
                )

                if (state.dataSyncStep != null) {
                    Image(
                        painter = if (state.dataSyncStep != SyncStep.FAILED) {
                            rememberAnimatedVectorPainter(
                                syncDataLogo,
                                state.dataSyncStep == SyncStep.RUNNING ||
                                    state.dataSyncStep == SyncStep.SUCCESS
                            )
                        } else painterResource(state.errorLogo!!),
                        contentDescription = state.dataSyncMsg,
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}

@Composable
private fun SyncStateLabel(
    value: String,
    alpha: Float = 1f
) {
    Text(
        text = value,
        color = Color.White.copy(alpha = alpha),
        maxLines = 1,
        textAlign = TextAlign.Start,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp
    )
}