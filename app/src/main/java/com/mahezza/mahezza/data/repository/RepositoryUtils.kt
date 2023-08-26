package com.mahezza.mahezza.data.repository

import com.mahezza.mahezza.data.source.firebase.FirebaseResult

fun isFirebaseResultSuccess(firebaseResult: FirebaseResult<out Any>): Boolean = firebaseResult.isSuccess && firebaseResult.data != null
