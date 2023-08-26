package com.mahezza.mahezza.ui.features.redeempuzzle.redeem

sealed class RedeemPuzzleEvent {
    object OnRedeemPuzzle : RedeemPuzzleEvent()
    object OnRedeemSuccessDialogShowed : RedeemPuzzleEvent()
    object OnGeneralMessageShowed : RedeemPuzzleEvent()
    class OnCodeValueChanged(val value : String) : RedeemPuzzleEvent()
}
