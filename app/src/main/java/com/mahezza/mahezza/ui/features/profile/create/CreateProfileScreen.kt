package com.mahezza.mahezza.ui.features.profile.create

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.request.RequestOptions
import com.mahezza.mahezza.R
import com.mahezza.mahezza.ui.components.AutoCompleteTextField
import com.mahezza.mahezza.ui.components.AutoCompleteTextFieldWithTitle
import com.mahezza.mahezza.ui.components.DefaultTextField
import com.mahezza.mahezza.ui.components.FilledAccentYellowButton
import com.mahezza.mahezza.ui.components.LoadingScreen
import com.mahezza.mahezza.ui.components.TextFieldWithTitle
import com.mahezza.mahezza.ui.ext.changeStatusBarColor
import com.mahezza.mahezza.ui.ext.showToast
import com.mahezza.mahezza.ui.nav.Routes
import com.mahezza.mahezza.ui.theme.AccentYellow
import com.mahezza.mahezza.ui.theme.Black
import com.mahezza.mahezza.ui.theme.GreyBorder
import com.mahezza.mahezza.ui.theme.GreyText
import com.mahezza.mahezza.ui.theme.PoppinsMedium14
import com.mahezza.mahezza.ui.theme.PoppinsMedium16
import com.mahezza.mahezza.ui.theme.PoppinsRegular12
import com.mahezza.mahezza.ui.theme.PoppinsRegular16
import com.mahezza.mahezza.ui.theme.PoppinsSemiBold14
import com.mahezza.mahezza.ui.theme.PoppinsSemiBold20
import com.mahezza.mahezza.ui.theme.White

@Composable
fun CreateProfileScreen(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    navController: NavController,
    userId: String,
    viewModel: CreateProfileViewModel
) {
    changeStatusBarColor(color = White)
    val uiState = viewModel.uiState.collectAsState()
    val context = LocalContext.current
    DisposableEffect(key1 = userId, key2 = lifecycleOwner){
        val observer = LifecycleEventObserver {_, event ->
            if (event == Lifecycle.Event.ON_CREATE){
                viewModel.onEvent(CreateProfileEvent.SetUserId(userId))
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    LaunchedEffect(key1 = uiState.value.generalMessage){
        if (uiState.value.generalMessage != null){
            showToast(context, uiState.value.generalMessage?.asString(context))
            viewModel.onEvent(CreateProfileEvent.OnGeneralMessageShowed)
        }
    }
    LaunchedEffect(key1 = uiState.value.shouldStartAddChildProfile){
        if (uiState.value.shouldStartAddChildProfile){
            showToast(context, context.getString(R.string.profile_created_next_add_children))
            navController.navigate(Routes.InsertChildProfile)
            viewModel.onEvent(CreateProfileEvent.OnStartAddChildrenProfileScreen)
        }
    }

    CreateProfileContent(
        uiState = uiState.value,
        onEvent = viewModel::onEvent
    )

    LoadingScreen(uiState.value.isShowLoading)
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CreateProfileContent(
    uiState: CreateProfileUiState,
    onEvent: (CreateProfileEvent) -> Unit
) {

    val galleryLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()){uri: Uri? ->
        uri?.let { onEvent(CreateProfileEvent.SetPhotoProfileUri(it)) }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ){
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                modifier = Modifier
                    .align(CenterHorizontally),
                text = stringResource(id = R.string.parent_profile),
                style = PoppinsMedium16
            )
            Spacer(modifier = Modifier.height(32.dp))
            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .align(CenterHorizontally)
            ){
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
                    model = uiState.newPhotoUri ?: uiState.photoUrl,
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
                        .align(BottomCenter)
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
                    onEvent(CreateProfileEvent.OnNameValueChanged(it))
                }
            )
            Spacer(modifier = Modifier.height(24.dp))
            AutoCompleteTextFieldWithTitle(
                title = stringResource(id = R.string.job),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                errorText = uiState.jobErrorMessage?.asString(),
                suggestions = stringArrayResource(id = R.array.default_job_list).toList().sorted(),
                onValueChange = {
                    onEvent(CreateProfileEvent.OnJobValueChanged(it))
                }
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = stringResource(id = R.string.relationship_with_children),
                style = PoppinsMedium14,
                color = Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            RelationshipWithChildrenCardGroup(
                selectedIndex = uiState.selectedRelationshipWithChildrenIndex,
                relationshipWithChildrenList = uiState.relationshipWithChildrenData,
                errorText = uiState.relationshipWithChildrenErrorMessage?.asString(),
                onCardSelected = {
                    onEvent(CreateProfileEvent.OnRelationshipWithChildrenIndexChanged(it))
                },
                onTextFieldValueChanged = {
                    onEvent(CreateProfileEvent.OnRelationshipWithChildrenValueChanged(it))
                }
            )
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.time_with_children),
                    style = PoppinsMedium14,
                    color = Black
                )
                Spacer(modifier = Modifier.width(4.dp))
                var isShowTimeSpendWithChildrenDialog by remember { mutableStateOf(false) }
                if (isShowTimeSpendWithChildrenDialog) {
                    AlertDialog(
                        onDismissRequest = { isShowTimeSpendWithChildrenDialog = false },
                        title = { 
                                    Text(
                                        text = stringResource(id = R.string.time_with_children),
                                        style = PoppinsSemiBold20,
                                        color = Black
                                    ) 
                                },
                        text = {
                                    Text(
                                        text = stringResource(id = R.string.time_spend_is_how_much_to_play_with_children),
                                        style = PoppinsRegular16,
                                        color = Black
                                    )
                               },
                        confirmButton = {
                            FilledAccentYellowButton(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                text = stringResource(R.string.close),
                                onClick = { isShowTimeSpendWithChildrenDialog = false },
                            )
                        }
                    )
                }
                Image(
                    modifier = Modifier
                        .clickable {
                            isShowTimeSpendWithChildrenDialog = true
                        }
                        .size(14.dp),
                    painter = painterResource(id = R.drawable.ic_question_mark),
                    contentDescription = stringResource(id = R.string.time_with_children)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            TimeSpendWithChildrenViewGroup(
                selectedIndex = uiState.selectedTimeSpendWithChildrenIndex,
                timeSpendWithChildrenList = uiState.timeSpendWithChildrenData,
                errorText = uiState.timeSpendWithChildrenErrorMessage?.asString(),
                onViewSelected = {
                    onEvent(CreateProfileEvent.OnTimeSpendWithChildrenChanged(it))
                },
                timeSpendTextFieldValue = uiState.timeSpendWithChildrenValue,
                onTextFieldValueChanged = {
                    onEvent(CreateProfileEvent.OnTimeSpendWithChildrenValueChanged(it))
                }
            )
            Spacer(modifier = Modifier.height(104.dp))
        }

        Box(modifier = Modifier
            .fillMaxWidth()
            .align(BottomCenter)
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
        ){
            FilledAccentYellowButton(
                modifier = Modifier
                    .fillMaxWidth(),
                text = stringResource(id = R.string.save_and_next)
            ) {
                onEvent(CreateProfileEvent.OnSaveAndNextButtonClicked)
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun CreateProfileContentPreview() {
    CreateProfileContent(
        uiState = CreateProfileUiState(),
        onEvent = {event ->

        }
    )
}

@Composable
fun RelationshipWithChildrenCardGroup(
    selectedIndex: Int,
    relationshipWithChildrenList: List<CreateProfileUiState.RelationshipWithChildren>,
    onCardSelected: (Int) -> Unit,
    onTextFieldValueChanged: (String) -> Unit,
    errorText: String? = null
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement
                .spacedBy(8.dp)
        ) {
            relationshipWithChildrenList.forEachIndexed { index, relationshipWithChildren ->
                RelationshipWithChildrenCard(
                    modifier = Modifier
                        .weight(1f),
                    isSelected = index == selectedIndex,
                    relationshipWithChildren = relationshipWithChildren,
                    contentDescription = stringResource(id = relationshipWithChildren.stringResId)
                ) {
                    onCardSelected(index)
                }

            }
        }
        if (selectedIndex == relationshipWithChildrenList.lastIndex){
            Spacer(modifier = Modifier.height(8.dp))
            val relationshipSuggestions = stringArrayResource(id = R.array.other_relationships).toList().sorted()
            AutoCompleteTextField(
                suggestions = relationshipSuggestions ,
                onValueChange = onTextFieldValueChanged,
                errorText = errorText,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                placeholder = relationshipSuggestions.first()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RelationshipWithChildrenCard(
    modifier: Modifier = Modifier,
    isSelected : Boolean,
    relationshipWithChildren: CreateProfileUiState.RelationshipWithChildren,
    contentDescription : String? = null,
    onClick: () -> Unit
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
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Image(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .align(CenterHorizontally),
                painter = painterResource(id = relationshipWithChildren.drawableResId),
                contentDescription = contentDescription
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = stringResource(id = relationshipWithChildren.stringResId),
                style = PoppinsMedium14,
                color = Black
            )
        }
    }
}

@Preview
@Composable
fun RelationshipWithChildrenCardPreview() {
    Column {
        RelationshipWithChildrenCard(
            isSelected = false,
            relationshipWithChildren = CreateProfileUiState.RelationshipWithChildren(
                R.string.father,
                R.drawable.ic_father
            ),
            onClick = {}
        )
        RelationshipWithChildrenCard(
            isSelected = true,
            relationshipWithChildren = CreateProfileUiState.RelationshipWithChildren(
                R.string.father,
                R.drawable.ic_father
            ),
            onClick = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeSpendWithChildrenViewGroup(
    selectedIndex: Int,
    timeSpendWithChildrenList: List<CreateProfileUiState.TimeSpendWithChildren>,
    onViewSelected: (Int) -> Unit,
    timeSpendTextFieldValue: String,
    onTextFieldValueChanged: (String) -> Unit,
    errorText: String? = null,
) {
    Column {
        val scrollState = rememberScrollState()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .border(1.dp, AccentYellow, RoundedCornerShape(8.dp))
                .horizontalScroll(scrollState)
        ) {
            timeSpendWithChildrenList.forEachIndexed { index, timeSpendWithChildren ->
                TimeSpendWithChildrenView(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f),
                    isSelected = index == selectedIndex,
                    timeSpendWithChildren = timeSpendWithChildren,
                    onClick = {onViewSelected(index)}
                )
            }
        }
        if (selectedIndex == timeSpendWithChildrenList.lastIndex){
            Spacer(modifier = Modifier.height(8.dp))
            DefaultTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = timeSpendTextFieldValue,
                errorText = errorText,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                placeholder = stringResource(id = R.string.six_hour),
                onValueChange = onTextFieldValueChanged
            )
        }
    }
}

@Composable
fun TimeSpendWithChildrenView(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    timeSpendWithChildren: CreateProfileUiState.TimeSpendWithChildren,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(
                if (isSelected) AccentYellow else White
            )
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        timeSpendWithChildren.value?.let { value ->
            Text(
                text = "$value",
                style = PoppinsSemiBold14,
                color = Black
            )
        }
        Text(
            text = stringResource(id = timeSpendWithChildren.unitStringResId),
            style = PoppinsRegular12,
            color = if (isSelected) Black else GreyText
        )
    }
}

@Preview
@Composable
fun TimeSpendWithChildrenViewPreview() {
    Column {
        TimeSpendWithChildrenView(isSelected = true, timeSpendWithChildren = CreateProfileUiState.TimeSpendWithChildren(30, R.string.hour)) {

        }
        TimeSpendWithChildrenView(isSelected = false, timeSpendWithChildren = CreateProfileUiState.TimeSpendWithChildren(30, R.string.hour)) {

        }
    }
}