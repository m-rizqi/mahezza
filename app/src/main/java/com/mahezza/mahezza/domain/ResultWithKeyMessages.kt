package com.mahezza.mahezza.domain

import com.mahezza.mahezza.common.StringResource

sealed class ResultWithKeyMessages<T>(val data : T?, val messages : List<KeyValue<StringResource?>>?) {
    class Success<T>(data : T) : ResultWithKeyMessages<T>(data, null)
    class Fail<T>(messages : List<KeyValue<StringResource?>>?) : ResultWithKeyMessages<T>(null, messages)
}

sealed class ResultWithKeyMessage<T>(val data : T?, val message : KeyValue<StringResource?>?) {
    class Success<T>(data : T) : ResultWithKeyMessage<T>(data, null)
    class Fail<T>(message : KeyValue<StringResource?>?) : ResultWithKeyMessage<T>(null, message)
}