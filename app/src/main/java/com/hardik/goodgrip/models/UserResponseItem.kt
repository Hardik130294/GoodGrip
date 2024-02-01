package com.hardik.goodgrip.models

import androidx.annotation.Keep
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
@Entity(tableName = "user")
data class UserResponseItem(
    @PrimaryKey(autoGenerate = false)
    @SerializedName("id")
    @Expose
    val id: Int = 0, // 1

    @SerializedName("name")
    @Expose
    val name: String = "", // Leanne Graham

    @SerializedName("username")
    @Expose
    val username: String = "", // Bret

    @SerializedName("phone")
    @Expose
    val phone: String = "", // 1-770-736-8031 x56442

    @SerializedName("email")
    @Expose
    val email: String = "", // Sincere@april.biz

    @SerializedName("website")
    @Expose
    val website: String = "", // hildegard.org

    @Embedded(prefix = "company_")
    @SerializedName("company")
    @Expose
    val company: Company = Company(),

    @Embedded(prefix = "address_")
    @SerializedName("address")
    @Expose
    val address: Address = Address()

)

@Keep
data class Address(
    @SerializedName("city")
    @Expose
    val city: String = "", // Gwenborough
    @Embedded(prefix = "geo_")
    @SerializedName("geo")
    @Expose
    val geo: Geo = Geo(),
    @SerializedName("street")
    @Expose
    val street: String = "", // Kulas Light
    @SerializedName("suite")
    @Expose
    val suite: String = "", // Apt. 556
    @SerializedName("zipcode")
    @Expose
    val zipcode: String = "" // 92998-3874
)

@Keep
data class Geo(
    @SerializedName("lat")
    @Expose
    val lat: String = "", // -37.3159
    @SerializedName("lng")
    @Expose
    val lng: String = "" // 81.1496
)


@Keep
data class Company(
    @SerializedName("bs")
    @Expose
    val bs: String = "", // harness real-time e-markets
    @SerializedName("catchPhrase")
    @Expose
    val catchPhrase: String = "", // Multi-layered client-server neural-net
    @SerializedName("name")
    @Expose
    val name: String = "" // Romaguera-Crona
)

data class UserWithDetails(
    @Embedded
    val user: UserResponseItem,
    @Relation(parentColumn = "id", entityColumn = "user_id")
    val posts: List<PostResponseItem>, // Assuming a one-to-many relationship with posts

//    @Relation(parentColumn = "id", entityColumn = "user_id")
//    val comments: List<CommentResponseItem>, // Assuming a one-to-many relationship with comments

    @Relation(parentColumn = "id", entityColumn = "user_id")
    val albums: List<AlbumResponseItem>, // Assuming a one-to-many relationship with albums

//    @Relation(parentColumn = "id", entityColumn = "user_id")
//    val photos: List<PhotoResponseItem>, // Assuming a one-to-many relationship with photos

    @Relation(parentColumn = "id", entityColumn = "user_id")
    val todos: List<TodoResponseItem> // Assuming a one-to-many relationship with todos
)