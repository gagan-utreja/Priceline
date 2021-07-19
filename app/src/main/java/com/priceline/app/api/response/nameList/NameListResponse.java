package com.priceline.app.api.response.nameList;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class NameListResponse{

	@SerializedName("copyright")
	private String copyright;

	@SerializedName("results")
	private List<ResultsItem> results;

	@SerializedName("num_results")
	private int numResults;

	@SerializedName("status")
	private String status;

	public String getCopyright(){
		return copyright;
	}

	public List<ResultsItem> getResults(){
		return results;
	}

	public int getNumResults(){
		return numResults;
	}

	public String getStatus(){
		return status;
	}
}