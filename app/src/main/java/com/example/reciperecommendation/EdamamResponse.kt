package com.example.reciperecommendation

import EdamamHit

data class EdamamResponse(
    val from: Int,
    val to: Int,
    val count: Int,
    val hits: List<EdamamHit>
)



