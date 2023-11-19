package com.github.rr97.emotionalintelligence

import net.minidev.json.JSONObject
import net.minidev.json.parser.JSONParser
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

fun getOpenAIRes(moodType: MoodType): JSONObject? {
    var endpoint = "newday"
    if (moodType == MoodType.BAD) {
        endpoint = "encouragement"
    }
    if (moodType == MoodType.EXCELLENT) {
        endpoint = "celebration"
    }


    val client = HttpClient.newBuilder().build();
    val request = HttpRequest.newBuilder()
            .uri(URI.create("http://127.0.0.1:5000/$endpoint"))
            .GET()
            .build()
    val response = client.send(request, HttpResponse.BodyHandlers.ofString());

    return JSONParser().parse(response.body()) as JSONObject
}