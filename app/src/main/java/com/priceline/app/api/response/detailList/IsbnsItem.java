package com.priceline.app.api.response.detailList;

import com.google.gson.annotations.SerializedName;

public class IsbnsItem{

	@SerializedName("isbn13")
	private String isbn13;

	@SerializedName("isbn10")
	private String isbn10;

	public String getIsbn13(){
		return isbn13;
	}

	public String getIsbn10(){
		return isbn10;
	}
}