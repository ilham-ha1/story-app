package org.dicoding.storyapp.ui.home

import org.dicoding.storyapp.model.response.ListStoryItem

object DataDummy {
    fun dummyListStory(): List<ListStoryItem>{
        val list: MutableList<ListStoryItem> = arrayListOf()

        for (iterate in 0 .. 10){
            val dummy = ListStoryItem(
                "www $iterate",
                "2023-05-04",
                "ilham $iterate",
                "'testing $iterate",
                124.2,
                "$iterate",
                102.3
            )
            list.add(dummy)
        }
        return list
    }
}