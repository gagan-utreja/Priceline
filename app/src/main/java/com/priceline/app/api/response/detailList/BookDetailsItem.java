package com.priceline.app.api.response.detailList;

import com.google.gson.annotations.SerializedName;

public class BookDetailsItem{

	@SerializedName("contributor_note")
	private String contributorNote;

	@SerializedName("contributor")
	private String contributor;

	@SerializedName("author")
	private String author;

	@SerializedName("price")
	private Double price;

	@SerializedName("age_group")
	private String ageGroup;

	@SerializedName("description")
	private String description;

	@SerializedName("publisher")
	private String publisher;

	@SerializedName("primary_isbn10")
	private String primaryIsbn10;

	@SerializedName("title")
	private String title;

	@SerializedName("primary_isbn13")
	private String primaryIsbn13;

	public String getContributorNote(){
		return contributorNote;
	}

	public String getContributor(){
		return contributor;
	}

	public String getAuthor(){
		return author;
	}

	public Double getPrice(){
		return price;
	}

	public String getAgeGroup(){
		return ageGroup;
	}

	public String getDescription(){
		return description;
	}

	public String getPublisher(){
		return publisher;
	}

	public String getPrimaryIsbn10(){
		return primaryIsbn10;
	}

	public String getTitle(){
		return title;
	}

	public String getPrimaryIsbn13(){
		return primaryIsbn13;
	}
}