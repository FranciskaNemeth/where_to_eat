package com.example.wheretoeat.model

data class City(
        val total_entries : Int,
        val per_page : Int,
        val page : Int,
        val restaurants : MutableList<Restaurant>
)