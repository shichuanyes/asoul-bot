package com.github.shichuanyes.plugin.asoul

class APIException internal constructor(
    private val code: Int,
    override val message: String
) : Exception(message) {
    override fun toString(): String {
        return "$code: $message"
    }
}