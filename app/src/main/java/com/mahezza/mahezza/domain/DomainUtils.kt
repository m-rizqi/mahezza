package com.mahezza.mahezza.domain

import com.mahezza.mahezza.common.StringResource

fun anyOfValidatesIsFail(vararg validates : ResultWithKeyMessage<Boolean>): Boolean = validates.any {
    it is ResultWithKeyMessage.Fail
}

fun gatherErrorFromValidates(vararg validates : ResultWithKeyMessage<Boolean>): List<KeyValue<StringResource?>>{
    return validates
        .toList()
        .filter{
            it.message != null
        }
        .filterIsInstance(ResultWithKeyMessage.Fail::class.java)
        .map {
            it.message!!
        }
}