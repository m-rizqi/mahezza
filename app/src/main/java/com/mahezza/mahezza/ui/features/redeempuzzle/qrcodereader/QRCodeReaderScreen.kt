package com.mahezza.mahezza.ui.features.redeempuzzle.qrcodereader

import android.Manifest
import android.content.pm.PackageManager
import android.util.Size
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.core.resolutionselector.ResolutionStrategy
import androidx.camera.core.resolutionselector.ResolutionStrategy.FALLBACK_RULE_CLOSEST_HIGHER_THEN_LOWER
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.mahezza.mahezza.R
import com.mahezza.mahezza.ui.components.EmptyOrErrorScreen
import com.mahezza.mahezza.ui.components.FilledAccentYellowButton
import com.mahezza.mahezza.ui.components.LoadingScreen
import com.mahezza.mahezza.ui.ext.showToast
import com.mahezza.mahezza.ui.features.redeempuzzle.PuzzleRedeemedDialog
import com.mahezza.mahezza.ui.theme.Black
import com.mahezza.mahezza.ui.theme.GreyText
import com.mahezza.mahezza.ui.theme.PoppinsRegular12
import com.mahezza.mahezza.ui.theme.PoppinsRegular14
import com.mahezza.mahezza.ui.theme.White

@Composable
fun QRCodeReaderScreen(
    navController: NavController,
    viewModel: QRCodeReaderViewModel
) {
    val uiState = viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(key1 = uiState.value.generalMessage){
        uiState.value.generalMessage?.let { message ->
            showToast(context, message.asString(context))
            viewModel.onEvent(QRCodeReaderEvent.OnGeneralMessageShowed)
        }
    }

    QRCodeReaderContent(
        uiState = uiState.value,
        onEvent = viewModel::onEvent,
        onNavigateBack = {navController.popBackStack()}
    )

    LoadingScreen(isShowLoading = uiState.value.isShowLoading)

    uiState.value.puzzleRedeemedState?.let { puzzleRedeemedState ->
        PuzzleRedeemedDialog(
            puzzleName = puzzleRedeemedState.name,
            bannerUrl = puzzleRedeemedState.banner,
            onDismissRequest = {
                if (!it){
                    viewModel.onEvent(QRCodeReaderEvent.OnRedeemSuccessDialogShowed)
                }
            }
        )
    }

}

@Composable
fun QRCodeReaderContent(
    uiState: QRCodeReaderUiState,
    onEvent: (QRCodeReaderEvent) -> Unit,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember {
        ProcessCameraProvider.getInstance(context)
    }
    var hasCamPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            hasCamPermission = granted
        }
    )
    LaunchedEffect(key1 = hasCamPermission) {
        if (!hasCamPermission){
            launcher.launch(Manifest.permission.CAMERA)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ){
        Box(modifier = Modifier
            .fillMaxSize()
            .background(White)
        ){
            if (!hasCamPermission){
                EmptyOrErrorScreen(
                    message = stringResource(id = R.string.dont_have_camera_permission)
                )
            } else {
                AndroidView(
                    factory = { context ->
                        val previewView = PreviewView(context)
                        val preview = androidx.camera.core.Preview.Builder().build()
                        val selector = CameraSelector.Builder()
                            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                            .build()
                        preview.setSurfaceProvider(previewView.surfaceProvider)
                        val imageAnalysis = ImageAnalysis.Builder()
                            .setResolutionSelector(
                                ResolutionSelector.Builder()
                                    .setResolutionStrategy(
                                        ResolutionStrategy(
                                            Size(
                                                previewView.width,
                                                previewView.height
                                            ),
                                            FALLBACK_RULE_CLOSEST_HIGHER_THEN_LOWER
                                        )
                                    )
                                    .build()
                            )
                            .setBackpressureStrategy(STRATEGY_KEEP_ONLY_LATEST)
                            .build()
                        imageAnalysis.setAnalyzer(
                            ContextCompat.getMainExecutor(context),
                            QRCodeAnalyzer { result ->
                                onEvent(QRCodeReaderEvent.OnQRScanned(result))
                            }
                        )
                        try {
                            cameraProviderFuture.get().bindToLifecycle(
                                lifecycleOwner,
                                selector,
                                preview,
                                imageAnalysis
                            )
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        previewView
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        IconButton(
            modifier = Modifier
                .offset(16.dp, 16.dp)
                .background(White, RoundedCornerShape(8.dp)),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = White,
                contentColor = Black
            ),
            onClick = onNavigateBack
        ) {
            Icon(
                imageVector = Icons.Filled.ChevronLeft,
                contentDescription = stringResource(id = R.string.back)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ){

            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .background(White, RoundedCornerShape(4.dp))
                    .padding(8.dp)
            ){
                Text(
                    text = uiState.qrcode,
                    style = PoppinsRegular14,
                    color = GreyText
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(White, RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .padding(horizontal = 16.dp),
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = stringResource(id = R.string.point_camera_to_qrcode),
                    style = PoppinsRegular12,
                    color = Black
                )
                Spacer(modifier = Modifier.height(16.dp))
                FilledAccentYellowButton(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = stringResource(id = R.string.redeem_puzzle)
                ) {
                    onEvent(QRCodeReaderEvent.OnRedeemPuzzle)
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun QRCodeReaderContentPreview() {
    QRCodeReaderContent(
        QRCodeReaderUiState(),
        {}
    ) {}
}