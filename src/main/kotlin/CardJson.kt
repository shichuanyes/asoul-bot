package com.github.shichuanyes.plugin.asoul

//data class CardJson(
//    val item: Item?,
//
//    val desc: String?,
//    val pic: String?,
//    val title: String?
//)
//
//data class Item(
//    val description: String?,
//    val pictures: ArrayList<Picture>?,
//
//    val content: String?
//)
//
//data class Picture(
//    val img_src: String
//)

data class RepostDynamic(
    val user: User,
    val item: RepostItem,
    val origin: String

)

data class TextDynamic(
    val user: User,
    val item: TextItem
)

data class PictureDynamic(
    val item: PictureItem,
    val user: PictureUser
)

data class VideoDynamic(
    val desc: String,
    val owner: Owner,
    val pic: String,
    val short_link: String,
    val title: String
)

data class User(
    val uname: String
)

data class PictureUser(
    val name: String
)

data class Owner(
    val name: String
)

data class RepostItem(
    val content: String,
    val orig_type: Int
)

data class TextItem(
    val content: String
)

data class PictureItem(
    val description: String,
    val pictures: ArrayList<Picture>
)

data class Picture(
    val img_src: String
)