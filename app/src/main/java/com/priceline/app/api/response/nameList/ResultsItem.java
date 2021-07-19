package com.priceline.app.api.response.nameList;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class ResultsItem{

	@SerializedName("newest_published_date")
	private Date newestPublishedDate;

	@SerializedName("oldest_published_date")
	private String oldestPublishedDate;

	@SerializedName("list_name")
	private String listName;

	@SerializedName("list_name_encoded")
	private String listNameEncoded;

	@SerializedName("display_name")
	private String displayName;

	@SerializedName("updated")
	private String updated;

	public Date getNewestPublishedDate(){
		return newestPublishedDate;
	}

	public String getOldestPublishedDate(){
		return oldestPublishedDate;
	}

	public String getListName(){
		return listName;
	}

	public String getListNameEncoded(){
		return listNameEncoded;
	}

	public String getDisplayName(){
		return displayName;
	}

	public String getUpdated(){
		return updated;
	}
}