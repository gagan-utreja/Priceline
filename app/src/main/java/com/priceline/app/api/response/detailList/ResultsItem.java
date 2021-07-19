package com.priceline.app.api.response.detailList;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResultsItem{

	@SerializedName("isbns")
	private List<IsbnsItem> isbns;

	@SerializedName("dagger")
	private int dagger;

	@SerializedName("asterisk")
	private int asterisk;

	@SerializedName("book_details")
	private List<BookDetailsItem> bookDetails;

	@SerializedName("list_name")
	private String listName;

	@SerializedName("display_name")
	private String displayName;

	@SerializedName("weeks_on_list")
	private int weeksOnList;

	@SerializedName("bestsellers_date")
	private String bestsellersDate;

	@SerializedName("amazon_product_url")
	private String amazonProductUrl;

	@SerializedName("reviews")
	private List<ReviewsItem> reviews;

	@SerializedName("rank")
	private int rank;

	@SerializedName("published_date")
	private String publishedDate;

	@SerializedName("rank_last_week")
	private int rankLastWeek;

	public List<IsbnsItem> getIsbns(){
		return isbns;
	}

	public int getDagger(){
		return dagger;
	}

	public int getAsterisk(){
		return asterisk;
	}

	public List<BookDetailsItem> getBookDetails(){
		return bookDetails;
	}

	public String getListName(){
		return listName;
	}

	public String getDisplayName(){
		return displayName;
	}

	public int getWeeksOnList(){
		return weeksOnList;
	}

	public String getBestsellersDate(){
		return bestsellersDate;
	}

	public String getAmazonProductUrl(){
		return amazonProductUrl;
	}

	public List<ReviewsItem> getReviews(){
		return reviews;
	}

	public int getRank(){
		return rank;
	}

	public String getPublishedDate(){
		return publishedDate;
	}

	public int getRankLastWeek(){
		return rankLastWeek;
	}
}