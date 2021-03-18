package com.github.shichuanyes.plugin.asoul

data class CardJson(
    val item: Item,
    val user: User
)

data class User(
    val uname: String
)


data class Item(
    val description: String,
    val content: String,
    val pictures: ArrayList<Picture>
)

data class Picture(
    val img_src: String
)
