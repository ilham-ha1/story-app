package org.dicoding.storyapp.model.response

import com.google.gson.annotations.SerializedName

data class AddNewStoryGuestResponse(

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)
