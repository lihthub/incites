package com.example.incites.entity;

public class Category {
	private Integer id;
	private String rank;
	private String year;
	private String categoryId;
	private String categoryName;
	private String edition;
	private String numberOfJournals;
	private String totalCites;
	private String medianImpactFactor;
	private String aggregateImpactFactor;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getEdition() {
		return edition;
	}

	public void setEdition(String edition) {
		this.edition = edition;
	}

	public String getNumberOfJournals() {
		return numberOfJournals;
	}

	public void setNumberOfJournals(String numberOfJournals) {
		this.numberOfJournals = numberOfJournals;
	}

	public String getTotalCites() {
		return totalCites;
	}

	public void setTotalCites(String totalCites) {
		this.totalCites = totalCites;
	}

	public String getMedianImpactFactor() {
		return medianImpactFactor;
	}

	public void setMedianImpactFactor(String medianImpactFactor) {
		this.medianImpactFactor = medianImpactFactor;
	}

	public String getAggregateImpactFactor() {
		return aggregateImpactFactor;
	}

	public void setAggregateImpactFactor(String aggregateImpactFactor) {
		this.aggregateImpactFactor = aggregateImpactFactor;
	}

}
