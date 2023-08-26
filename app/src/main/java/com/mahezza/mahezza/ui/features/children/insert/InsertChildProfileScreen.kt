package com.mahezza.mahezza.ui.features.children.insert

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.request.RequestOptions
import com.mahezza.mahezza.R
import com.mahezza.mahezza.domain.common.FormatDateUseCase
import com.mahezza.mahezza.ui.components.FilledAccentYellowButton
import com.mahezza.mahezza.ui.components.LoadingScreen
import com.mahezza.mahezza.ui.components.OutlinedAccentYellowButton
import com.mahezza.mahezza.ui.components.TextFieldWithTitle
import com.mahezza.mahezza.ui.ext.changeStatusBarColor
import com.mahezza.mahezza.ui.ext.showToast
import com.mahezza.mahezza.ui.nav.Routes
import com.mahezza.mahezza.ui.theme.AccentYellow
import com.mahezza.mahezza.ui.theme.Black
import com.mahezza.mahezza.ui.theme.GreyBorder
import com.mahezza.mahezza.ui.theme.GreyText
import com.mahezza.mahezza.ui.theme.PoppinsFamily
import com.mahezza.mahezza.ui.theme.PoppinsMedium14
import com.mahezza.mahezza.ui.theme.PoppinsMedium16
import com.mahezza.mahezza.ui.theme.PoppinsRegular10
import com.mahezza.mahezza.ui.theme.PoppinsRegular16
import com.mahezza.mahezza.ui.theme.RedDanger
import com.mahezza.mahezza.ui.theme.White
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate

@Composable
fun InsertChildProfileScreen(
    navController: NavController,
    viewModel: InsertChildProfileViewModel
) {
    changeStatusBarColor(color = White)
    val uiState = viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(key1 = uiState.value.generalErrorMessage){
        uiState.value.generalErrorMessage?.let { message ->
            showToast(context, message.asString(context))
            viewModel.onEvent(InsertChildProfileEvent.OnGeneralMessageShowed)
        }
    }
    LaunchedEffect(key1 = uiState.value.shouldStartDashboardScreen){
        if (uiState.value.shouldStartDashboardScreen){
            navController.navigate(Routes.Dashboard)
            viewModel.onEvent(InsertChildProfileEvent.OnDashboardStarted)
        }
    }

    InsertChildProfileContent(
        uiState = uiState.value,
        onEvent = viewModel::onEvent
    )

    LoadingScreen(isShowLoading = uiState.value.isShowLoading)
}

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
fun InsertChildProfileContent(
    uiState: InsertChildProfileUiState,
    onEvent : (InsertChildProfileEvent) -> Unit
) {
    val galleryLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()){ uri: Uri? ->
        uri?.let {
            onEvent(InsertChildProfileEvent.OnImagePicked(uri))
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
            ) {
                Text(
                    modifier = Modifier
                        .align(Alignment.Center),
                    text = stringResource(id = R.string.child_profile),
                    style = PoppinsMedium16
                )
                StackedPhotoProfile(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    photoProfiles = uiState.savedChildPhotoUrls
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .align(Alignment.CenterHorizontally)
            ) {
                val circleImageModifier = remember {
                    Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .border(
                            width = 1.dp,
                            color = AccentYellow,
                            shape = CircleShape
                        )
                        .clickable {
                            galleryLauncher.launch("image/*")
                        }
                }
                GlideImage(
                    modifier = circleImageModifier,
                    model = uiState.newPhotoUri,
                    contentDescription = stringResource(id = R.string.photo_profile),
                    contentScale = ContentScale.Crop,
                    requestBuilderTransform = { request ->
                        val requestOptions = RequestOptions()
                            .placeholder(R.drawable.ic_loading_placeholder)
                            .error(R.drawable.ic_error_placeholder)
                        request.apply(requestOptions)
                    }
                )

                FilledIconButton(
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.BottomCenter)
                        .offset(y = 8.dp),
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = AccentYellow,
                        contentColor = Black
                    ),
                    onClick = {
                        galleryLauncher.launch("image/*")
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_camera),
                        contentDescription = stringResource(id = R.string.take_image_or_photo)
                    )
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            TextFieldWithTitle(
                title = stringResource(id = R.string.name),
                value = uiState.name,
                errorText = uiState.nameErrorMessage?.asString(),
                placeholder = stringResource(id = R.string.fullname),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                onValueChange = {
                   onEvent(InsertChildProfileEvent.OnNameChanged(it))
                }
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = stringResource(id = R.string.gender),
                style = PoppinsMedium14,
                color = Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            GenderCardGroup(
                uiState.selectedGenderIndex
            ){index, value ->
                onEvent(InsertChildProfileEvent.OnGenderChanged(index, value))
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = stringResource(id = R.string.birthday),
                style = PoppinsMedium14,
                color = Black
            )

            val pickBirthDateDialogState = rememberMaterialDialogState()
            val birthDateTextFieldFocusRequester = remember { FocusRequester() }
            val focusManager = LocalFocusManager.current

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(birthDateTextFieldFocusRequester)
                    .onFocusChanged {
                        if (it.isFocused) {
                            pickBirthDateDialogState.show()
                        }
                    },
                value = uiState.birthDate,
                readOnly = true,
                shape = RoundedCornerShape(8.dp),
                textStyle = PoppinsRegular16,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = Black,
                    unfocusedBorderColor = GreyBorder,
                    focusedBorderColor = AccentYellow
                ),
                trailingIcon = {
                    IconButton(
                        onClick = {
                            pickBirthDateDialogState.show()
                        }
                    ) {
                        Icon(imageVector = Icons.Filled.CalendarMonth, contentDescription = stringResource(id = R.string.birthday))
                    }
                },
                supportingText = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(id = R.string.current_year_is, uiState.age),
                        style = PoppinsRegular10,
                        color = GreyText
                    )
                },
                onValueChange = {},

            )
            MaterialDialog(
                dialogState = pickBirthDateDialogState,
                buttons = {
                    positiveButton(
                        text = stringResource(id = R.string.save),
                        textStyle = TextStyle(
                            fontFamily = PoppinsFamily,
                            fontWeight = FontWeight.Medium,
                            color = Black,
                            fontSize = 14.sp,
                        )
                    )
                    negativeButton(
                        text = stringResource(id = R.string.cancel),
                        textStyle = TextStyle(
                            fontFamily = PoppinsFamily,
                            fontWeight = FontWeight.Medium,
                            color = RedDanger,
                            fontSize = 14.sp
                        )
                    ){
                        focusManager.clearFocus()
                    }
                },
                onCloseRequest = {
                    focusManager.clearFocus()
                    birthDateTextFieldFocusRequester.freeFocus()
                }
            ) {
                this.datepicker(
                    initialDate = uiState.pickedBirthDate,
                    colors = DatePickerDefaults.colors(
                        headerBackgroundColor = AccentYellow,
                        headerTextColor = Black,
                        calendarHeaderTextColor = Black,
                        dateActiveBackgroundColor = AccentYellow,
                        dateActiveTextColor = Black,
                        dateInactiveTextColor = Black
                    ),
                    allowedDateValidator = {date ->
                        date.isBefore(LocalDate.now().plusDays(1))
                    },
                    title = stringResource(id = R.string.select_child_birthday),
                ){
                    onEvent(InsertChildProfileEvent.OnBirthDateChanged(it))
                }
            }
            Spacer(modifier = Modifier.height(144.dp))
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
        ) {
            OutlinedAccentYellowButton(
                modifier = Modifier
                    .fillMaxWidth(),
                text = stringResource(id = R.string.save_and_add_profile)
            ) {
                onEvent(InsertChildProfileEvent.OnSaveAndInsertMore)
            }
            Spacer(modifier = Modifier.height(8.dp))
            FilledAccentYellowButton(
                modifier = Modifier
                    .fillMaxWidth(),
                text = stringResource(id = R.string.save_and_next)
            ) {
                onEvent(InsertChildProfileEvent.OnSaveAndNext)
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun AddChildProfileContentPreview() {
    InsertChildProfileContent(
        InsertChildProfileUiState()
    ){}
}

@Composable
fun GenderCardGroup(
    selectedIndex : Int,
    onGenderChanged: (Int, String) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        GenderCard(
            modifier = Modifier
                .weight(1f)
                .wrapContentHeight(),
            isSelected = selectedIndex == 0, 
            genderText = stringResource(id = R.string.boy), 
            genderDrawableId = R.drawable.ic_boy, 
            onClick = {
                onGenderChanged(0, it)
            }
        )
        Spacer(modifier = Modifier.width(16.dp))
        GenderCard(
            modifier = Modifier
                .weight(1f)
                .wrapContentHeight(),
            isSelected = selectedIndex == 1,
            genderText = stringResource(id = R.string.girl),
            genderDrawableId = R.drawable.ic_girl,
            onClick = {
                onGenderChanged(1, it)
            }
        )
    }
}

@Preview
@Composable
fun GenderCardGroupPreview() {
    GenderCardGroup(0){_, _ -> }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenderCard(
    modifier: Modifier = Modifier,
    isSelected : Boolean,
    genderText : String,
    @DrawableRes
    genderDrawableId : Int,
    onClick: (String) -> Unit
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) AccentYellow else White
        ),
        border = BorderStroke(
            width = 1.dp,
            color = if (isSelected) AccentYellow else GreyBorder
        ),
        onClick = {onClick(genderText)}
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .align(Alignment.CenterHorizontally),
                painter = painterResource(id = genderDrawableId),
                contentDescription = genderText
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = genderText,
                style = PoppinsMedium14,
                color = Black
            )
        }
    }
}


@Preview
@Composable
fun GenderCardPreview() {
    Row {
        GenderCard(
            modifier = Modifier
                .weight(1f)
                .wrapContentHeight(),
            isSelected = true,
            genderText = stringResource(id = R.string.boy),
            genderDrawableId = R.drawable.ic_boy
        ){}
        GenderCard(
            modifier = Modifier
                .weight(1f)
                .wrapContentHeight(),
            isSelected = false,
            genderText = stringResource(id = R.string.girl),
            genderDrawableId = R.drawable.ic_girl
        ){}
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun StackedPhotoProfile(
    modifier : Modifier = Modifier,
    photoProfiles : List<String>,
    maxPhotoShowed : Int = 3
) {
    val circleImageModifier = remember {
        Modifier
            .size(32.dp)
            .clip(CircleShape)
            .border(
                width = 1.dp,
                color = AccentYellow,
                shape = CircleShape
            )
    }
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy((-12).dp)
    ){
        photoProfiles.take(3).forEach {photoUrl ->
            GlideImage(
                modifier = circleImageModifier,
                model = photoUrl,
                contentDescription = stringResource(id = R.string.saved_child_photo_profile),
                contentScale = ContentScale.Crop,
                requestBuilderTransform = { request ->
                    val requestOptions = RequestOptions()
                        .placeholder(R.drawable.ic_loading_placeholder)
                        .error(R.drawable.ic_error_placeholder)
                    request.apply(requestOptions)
                }
            )
        }
        val offsetSize = remember {
            photoProfiles.size - maxPhotoShowed
        }
        if (offsetSize > 0){
            Box(
                modifier = circleImageModifier
                    .background(AccentYellow)
            ){
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(id = R.string.plus_param, offsetSize),
                    style = PoppinsMedium14,
                    color = Black
                )
            }
        }
    }
}

@Preview
@Composable
fun StackedPhotoProfilePreview() {
    StackedPhotoProfile(
        photoProfiles = listOf(
            "https://img.freepik.com/free-vector/businessman-character-avatar-isolated_24877-60111.jpg?w=2000",
            "https://img.freepik.com/free-vector/businessman-character-avatar-isolated_24877-60111.jpg?w=2000",
            "https://img.freepik.com/free-vector/businessman-character-avatar-isolated_24877-60111.jpg?w=2000",
            "https://img.freepik.com/free-vector/businessman-character-avatar-isolated_24877-60111.jpg?w=2000",
            "https://img.freepik.com/free-vector/businessman-character-avatar-isolated_24877-60111.jpg?w=2000",
        )
    )
}