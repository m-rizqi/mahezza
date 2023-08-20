package com.mahezza.mahezza.ui.theme

import androidx.compose.ui.res.fontResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.mahezza.mahezza.R

val PoppinsBlack = Font(R.font.poppins_black, FontWeight.Black)
val PoppinsBold = Font(R.font.poppins_bold, FontWeight.Bold)
val PoppinsExtraBold = Font(R.font.poppins_extrabold, FontWeight.ExtraBold)
val PoppinsExtraLight = Font(R.font.poppins_extralight, FontWeight.ExtraLight)
val PoppinsLight = Font(R.font.poppins_light, FontWeight.Light)
val PoppinsMedium = Font(R.font.poppins_medium, FontWeight.Medium)
val PoppinsRegular = Font(R.font.poppins_regular, FontWeight.Normal)
val PoppinsSemiBold = Font(R.font.poppins_semibold, FontWeight.SemiBold)
val PoppinsThin = Font(R.font.poppins_thin, FontWeight.Thin)

val PoppinsFamily = FontFamily(
    PoppinsBlack,
    PoppinsBold,
    PoppinsExtraBold,
    PoppinsExtraBold,
    PoppinsExtraBold,
    PoppinsExtraLight,
    PoppinsLight,
    PoppinsMedium,
    PoppinsRegular,
    PoppinsSemiBold,
    PoppinsThin
)

val PoppinsSemiBold20 = TextStyle(
    fontFamily = PoppinsFamily,
    fontWeight = FontWeight.SemiBold,
    fontSize = 20.sp
)
val PoppinsRegular10 = TextStyle(
    fontFamily = PoppinsFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 10.sp
)
val PoppinsRegular14 = TextStyle(
    fontFamily = PoppinsFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 14.sp
)
val PoppinsRegular16 = TextStyle(
    fontFamily = PoppinsFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 16.sp
)
val PoppinsMedium14 = TextStyle(
    fontFamily = PoppinsFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 14.sp
)
val PoppinsMedium16 = TextStyle(
    fontFamily = PoppinsFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 16.sp
)
val PoppinsBold32 = TextStyle(
    fontFamily = PoppinsFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 32.sp
)