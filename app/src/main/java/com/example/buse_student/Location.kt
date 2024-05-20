package com.example.buse_student

data class Location(
    val latitude: Double,
    val longitude: Double,
    val timestamp: Long)
{
    // Default constructor required for Firebase deserialization
    constructor() : this(0.0, 0.0, 0L)
}