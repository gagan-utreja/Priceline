package com.priceline.app.api.response.detailList;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class DetailListResponse{

	@SerializedName("copyright")
	private String copyright;

	@SerializedName("last_modified")
	private String lastModified;

	@SerializedName("results")
	private List<ResultsItem> results;

	@SerializedName("num_results")
	private int numResults;

	@SerializedName("status")
	private String status;

	public String getCopyright(){
		return copyright;
	}

	public String getLastModified(){
		return lastModified;
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