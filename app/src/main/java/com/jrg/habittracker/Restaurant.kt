package com.jrg.habittracker

import com.google.gson.annotations.SerializedName

data class Restaurant(
    var isLiked: Boolean,
    @SerializedName("r_id")
    val id: Int,
    @SerializedName("r_title")
    val title: String,
    @SerializedName("r_description")
    val description: String)

val dummyRestaurants = listOf(
    Restaurant(false, 0, "Alfredo's dishes", "At Alfredo's, we provide the best seafood dishes."),
    Restaurant(
        false,
        1,
        "Jamie's burgers",
        "At Jamie's, we serve the best meat and vegan burgers!"
    ),
    Restaurant(false, 2, "Pizza John", "Get the best pizza in town. We also serve vegan burgers!"),
    Restaurant(
        false,
        3,
        "Dinner in the clouds",
        "At DitC, you can experience the full immersive dining experience."
    ),
    Restaurant(
        false,
        4,
        "Eternity lunch",
        "At Eternity lunch, you will get the best American dishes."
    ),
    Restaurant(false, 5, "Food and coffee", "Drink your coffee and then have lunch at FaC!"),
    Restaurant(
        false,
        6,
        "Pizza and burgers Brazil",
        "Get your best burgers and pizza here in Brazil!"
    ),
    Restaurant(
        false,
        7,
        "Merry Dinner",
        "Get the Christmas experience at Merry Dinner with Santa!"
    ),
    Restaurant(
        false,
        8,
        "Uncle Ben's Pizza shop",
        "Relieve the childhood pizza experience at Uncle Ben's pizza shop, now!"
    ),
    Restaurant(false, 9, "Spicy Grill Toronto", "Get the best culinary experience in Toronto."),
    Restaurant(
        false,
        10,
        "Cheese Food shop",
        "Cheesy meals with cheesy ingredients, it's all about cheese!"
    ),
    Restaurant(
        false,
        11,
        "Mexican spicy Food in Atlanta",
        "Get your spicy food dose here in Atlanta at Mexican spicy Food!"
    ),
    Restaurant(
        false,
        12,
        "Spanish Kitchen reinvented",
        "Check out the true culinary experience with spanish dishes in NYC!"
    ),
    Restaurant(
        false,
        13,
        "Mike and Ben's food pub",
        "Come get the best food in New Jersey, now at Mike and Ben's!"
    ),
)