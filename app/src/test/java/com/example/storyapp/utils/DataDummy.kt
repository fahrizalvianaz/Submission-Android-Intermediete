package com.example.storyapp.utils

import com.example.storyapp.data.remote.response.story.ListStoryItem
import com.example.storyapp.data.remote.response.story.StoryResponse

object DataDummy {
    fun generateDummyStories() : StoryResponse {
        val listStory = ArrayList<ListStoryItem>()
        for (i in 1..20 ) {
            val story = ListStoryItem(
                id = "id_$i",
                name = "Name $i",
                description = "Description $i",
                photoUrl = "https://i.pinimg.com/originals/9b/c4/5d/9bc45d1d9a5e435c83ffa33dd8826de7.jpg",
                createdAt = "02-05-2023",
                lat = i.toDouble() * 10,
                lon = i.toDouble() * 10,
            )
            listStory.add(story)
        }
        return StoryResponse(
            error = false,
            message = "Stories fetched successfully",
            listStory = listStory
        )
    }
}