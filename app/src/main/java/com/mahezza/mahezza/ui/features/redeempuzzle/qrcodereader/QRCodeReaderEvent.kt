package com.mahezza.mahezza.ui.features.redeempuzzle.qrcodereader

sealed class QRCodeReaderEvent {
    object OnRedeemPuzzle : QRCodeReaderEvent()
    object OnRedeemSuccessDialogShowed : QRCodeReaderEvent()
    object OnGeneralMessageShowed : QRCodeReaderEvent()
    class OnQRScanned(val value : String) : QRCodeReaderEvent()
}