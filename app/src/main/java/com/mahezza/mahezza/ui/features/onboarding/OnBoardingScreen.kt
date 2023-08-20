package com.mahezza.mahezza.ui.features.onboarding

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.mahezza.mahezza.R
import com.mahezza.mahezza.ui.ext.makeStatusBarTransparent
import com.mahezza.mahezza.ui.theme.AccentYellow
import com.mahezza.mahezza.ui.theme.Black
import com.mahezza.mahezza.ui.theme.Grey
import com.mahezza.mahezza.ui.theme.GreyText
import com.mahezza.mahezza.ui.theme.PoppinsMedium16
import com.mahezza.mahezza.ui.theme.PoppinsRegular16
import com.mahezza.mahezza.ui.theme.PoppinsSemiBold20
import com.mahezza.mahezza.ui.theme.White
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnBoardingScreen() {
    val viewModel : OnBoardingViewModel = viewModel()
    val uiState = viewModel.uiState.collectAsState()
    (LocalContext.current as Activity).makeStatusBarTransparent()
    Box(
        modifier = Modifier
            .fillMaxSize()
    ){
        val pagerState = rememberPagerState()
        val coroutineScope = rememberCoroutineScope()
        HorizontalPager(
            count = uiState.value.onBoardingResources.size,
            key = {
                uiState.value.onBoardingResources[it].imageResId
            },
            state = pagerState,
        ) {index ->
            val onBoardingResource = uiState.value.onBoardingResources[index]
            Image(
                painter = painterResource(id = onBoardingResource.imageResId),
                contentDescription = stringResource(id = onBoardingResource.titleResId),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        OnBoardingContent(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            onBoardingResource = uiState.value.onBoardingResources[pagerState.currentPage],
            currentPage = pagerState.currentPage,
            pageCount = pagerState.pageCount,
            onNextClickListener = {
                if (viewModel.isLastPage(pagerState.currentPage, pagerState.pageCount)){
                    // Go To Login
                }else{
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(
                            pagerState.currentPage + 1
                        )
                    }
                }
            }
        )
    }
}

@Composable
fun OnBoardingContent(
    modifier: Modifier = Modifier,
    onBoardingResource: OnBoardingUiState.OnBoardingResource,
    currentPage: Int,
    pageCount: Int,
    onNextClickListener : () -> Unit
) {
    Column(
        modifier = modifier
            .background(White)
            .padding(start = 16.dp, top = 32.dp, end = 16.dp, bottom = 16.dp)
    ){
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = onBoardingResource.titleResId),
            style = PoppinsSemiBold20,
            color = Black,
        )
        Spacer(Modifier.height(16.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = onBoardingResource.subTitleResId),
            style = PoppinsRegular16,
            color = GreyText
        )
        Spacer(Modifier.height(56.dp))
        Row(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
            ,
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OnBoardingPageIndicator(currentPage = currentPage, pageCount = pageCount)
            val isLastPage = remember(currentPage, pageCount) {
                currentPage == (pageCount - 1)
            }
            if (isLastPage){
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AccentYellow,
                        contentColor = Black
                    ),
                    onClick = onNextClickListener
                ) {
                    Text(
                        text = stringResource(id = R.string.start),
                        style = PoppinsMedium16,
                        textAlign = TextAlign.Center,
                    )
                }
            } else {
                FilledIconButton(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                    ,
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = AccentYellow,
                        contentColor = Black
                    ),
                    onClick = onNextClickListener
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_right),
                        contentDescription = stringResource(id = R.string.next),
                    )
                }
            }
        }
    }
}

@Composable
fun OnBoardingPageIndicator(
    currentPage : Int,
    pageCount : Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .wrapContentSize()
    ) {
        (0 until pageCount).forEach {pageIndex ->
            val isActive by remember(currentPage, pageIndex) {
                mutableStateOf(pageIndex == currentPage)
            }
            val indicatorColor by remember(isActive) {
                mutableStateOf(if (isActive) AccentYellow else Grey)
            }
            val xOffset = (-6.1 * pageIndex).dp
            Image(
                modifier = Modifier
                    .size(24.dp)
                    .offset(x = xOffset)
                ,
                painter = painterResource(id = R.drawable.ic_puzzle),
                contentDescription = stringResource(id = R.string.page_indicator),
                colorFilter = ColorFilter.tint(indicatorColor)
            )
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun OnBoardingScreenPreview() {
    OnBoardingScreen()
}

@Preview
@Composable
fun OnBoardingContentPreview() {
    Box(modifier = Modifier.wrapContentSize()) {
        OnBoardingContent(
            modifier = Modifier
                .align(Alignment.BottomCenter),
            onBoardingResource = OnBoardingUiState.OnBoardingResource(
                R.drawable.onboarding2_image,
                R.string.lorem_ipsum_dolor_si_amet,
                R.string.lorem_ipsum_dolor_si_amet_consectur
            ),
            currentPage = 2,
            pageCount = 3,
            onNextClickListener = {}
        )
    }
}

@Preview
@Composable
fun OnBoardingPageIndicatorPreview() {
    OnBoardingPageIndicator(currentPage = 0, pageCount = 3)
}