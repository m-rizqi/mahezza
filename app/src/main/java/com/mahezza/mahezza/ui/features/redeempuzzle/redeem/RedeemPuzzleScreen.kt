package com.mahezza.mahezza.ui.features.redeempuzzle.redeem

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mahezza.mahezza.R
import com.mahezza.mahezza.ui.components.AccentYellowTextButton
import com.mahezza.mahezza.ui.components.DefaultTextField
import com.mahezza.mahezza.ui.components.FilledAccentYellowButton
import com.mahezza.mahezza.ui.components.FilledAccentYellowExtendedButton
import com.mahezza.mahezza.ui.components.LoadingScreen
import com.mahezza.mahezza.ui.ext.changeStatusBarColor
import com.mahezza.mahezza.ui.ext.showToast
import com.mahezza.mahezza.ui.features.redeempuzzle.PuzzleRedeemedDialog
import com.mahezza.mahezza.ui.nav.Routes
import com.mahezza.mahezza.ui.theme.AccentYellowDark
import com.mahezza.mahezza.ui.theme.Black
import com.mahezza.mahezza.ui.theme.PoppinsMedium14
import com.mahezza.mahezza.ui.theme.PoppinsMedium16
import com.mahezza.mahezza.ui.theme.PoppinsRegular12
import com.mahezza.mahezza.ui.theme.PoppinsRegular14
import com.mahezza.mahezza.ui.theme.PoppinsRegular16
import com.mahezza.mahezza.ui.theme.PoppinsSemiBold20
import com.mahezza.mahezza.ui.theme.PoppinsSemiBold24
import com.mahezza.mahezza.ui.theme.White
import timber.log.Timber

const val STORE_URL = "https://www.instagram.com/pkmk_mahezza.puzzle/"

@Composable
fun RedeemPuzzleScreen(
    navController: NavController,
    nextRoute: String? = null,
    viewModel: RedeemPuzzleViewModel
) {
    changeStatusBarColor(color = White)
    val uiState = viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(key1 = uiState.value.generalMessage){
        uiState.value.generalMessage?.let { message ->
            showToast(context, message.asString(context))
            viewModel.onEvent(RedeemPuzzleEvent.OnGeneralMessageShowed)
        }
    }

    BackHandler {
        if (nextRoute.isNullOrBlank()){
            navController.popBackStack()
        } else {
            navController.navigate(nextRoute)
        }
    }

    RedeemPuzzleContent(
        uiState = uiState.value,
        onEvent = viewModel::onEvent,
        onNavigateToQRCodeReader = {
            navController.navigate(Routes.QRCodeReader)
        },
        onNavigateBack = {
            if (nextRoute.isNullOrBlank()){
                navController.popBackStack()
            } else {
                navController.navigate(nextRoute)
            }
        }
    )

    LoadingScreen(isShowLoading = uiState.value.isShowLoading)

    uiState.value.puzzleRedeemedState?.let { puzzleRedeemedState ->
        PuzzleRedeemedDialog(
            puzzleName = puzzleRedeemedState.name,
            bannerUrl = puzzleRedeemedState.banner,
            onDismissRequest = {
                if (!it){
                    viewModel.onEvent(RedeemPuzzleEvent.OnRedeemSuccessDialogShowed)
                }
            }
        )
    }
}

@Composable
fun RedeemPuzzleContent(
    uiState: RedeemPuzzleUiState,
    onEvent : (RedeemPuzzleEvent) -> Unit,
    onNavigateToQRCodeReader: () -> Unit,
    onNavigateBack : () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ){
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(256.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop,
                painter = painterResource(id = R.drawable.scan_qr_illustration),
                contentDescription = stringResource(id = R.string.scan_qr)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = stringResource(id = R.string.redeem),
                style = PoppinsMedium16,
                color = Black
            )
            Text(
                text = stringResource(id = R.string.mahezza_puzzle),
                style = PoppinsSemiBold24,
                color = AccentYellowDark
            )
            Spacer(modifier = Modifier.height(32.dp))
            FilledAccentYellowExtendedButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.scan_qr),
                painter = painterResource(id = R.drawable.ic_qrcode)
            ) {
                onNavigateToQRCodeReader()
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                modifier = Modifier
                    .align(CenterHorizontally),
                text = stringResource(id = R.string.or_enter_code),
                style = PoppinsRegular14,
                color = Black
            )
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.code),
                    style = PoppinsMedium14,
                    color = Black
                )
                Spacer(modifier = Modifier.width(4.dp))
                var isShowCodeHelpDialog by remember { mutableStateOf(false) }
                if (isShowCodeHelpDialog) {
                    AlertDialog(
                        onDismissRequest = { isShowCodeHelpDialog = false },
                        title = {
                            Text(
                                text = stringResource(id = R.string.puzzle_code),
                                style = PoppinsSemiBold20,
                                color = Black
                            )
                        },
                        text = {
                            Text(
                                text = stringResource(id = R.string.puzzle_code_can_be_find_in_packaging),
                                style = PoppinsRegular16,
                                color = Black
                            )
                        },
                        confirmButton = {
                            FilledAccentYellowButton(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                text = stringResource(R.string.close),
                                onClick = { isShowCodeHelpDialog = false },
                            )
                        }
                    )
                }
                Image(
                    modifier = Modifier
                        .clickable {
                            isShowCodeHelpDialog = true
                        }
                        .size(14.dp),
                    painter = painterResource(id = R.drawable.ic_question_mark),
                    contentDescription = stringResource(id = R.string.time_with_children)
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                DefaultTextField(
                    modifier = Modifier
                        .weight(5f),
                    value = uiState.code,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
                    keyboardActions = KeyboardActions(
                        onGo = {
                            onEvent(RedeemPuzzleEvent.OnRedeemPuzzle)
                        }
                    ),
                    onValueChange = {onEvent(RedeemPuzzleEvent.OnCodeValueChanged(it))}
                )
                FilledAccentYellowButton(
                    modifier = Modifier
                        .weight(2f)
                        .fillMaxHeight(),
                    verticalPadding = 16.dp,
                    text = stringResource(id = R.string.redeem)
                ) {
                    onEvent(RedeemPuzzleEvent.OnRedeemPuzzle)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            AccentYellowTextButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.close_this_page)
            ) {
                onNavigateBack()
            }
            Spacer(modifier = Modifier.height(104.dp))
        }

        Column(
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.dont_have_puzzle),
                    style = PoppinsRegular12,
                    color = Black
                )
                Spacer(modifier = Modifier.width(4.dp))
                val uriHandler = LocalUriHandler.current
                Text(
                    modifier = Modifier
                        .clickable {
                            uriHandler.openUri(STORE_URL)
                        },
                    text = stringResource(id = R.string.get_here),
                    style = PoppinsMedium14,
                    color = AccentYellowDark
                )

            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun RedeemPuzzleContentPreview() {
    RedeemPuzzleContent(
        RedeemPuzzleUiState(),
        {},
        {},
        {}
    )
}