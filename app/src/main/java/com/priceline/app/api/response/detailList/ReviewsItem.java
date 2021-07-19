package com.priceline.app.api.response.detailList;

import com.google.gson.annotations.SerializedName;

public class ReviewsItem{

	@SerializedName("article_chapter_link")
	private String articleChapterLink;

	@SerializedName("book_review_link")
	private String bookReviewLink;

	@SerializedName("first_chapter_link")
	private String firstChapterLink;

	@SerializedName("sunday_review_link")
	private String sundayReviewLink;

	public String getArticleChapterLink(){
		return articleChapterLink;
	}

	public String getBookReviewLink(){
		return bookReviewLink;
	}

	public String getFirstChapterLink(){
		return firstChapterLink;
	}

	public String getSundayReviewLink(){
		return sundayReviewLink;
	}
}