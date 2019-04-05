package com.example.incites.entity;

public class Journal {
	private Integer id;
	private String rank;
	private String journalTitle;
	private String abbrJournal;
	private String year;
	private String edition;
	private String issn;
	private String totalCites;
	private String journalImpactFactor;
	private String eigenFactorScore;
	private String categoryName;
	private String categoryId;

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

	public String getJournalTitle() {
		return journalTitle;
	}

	public void setJournalTitle(String journalTitle) {
		this.journalTitle = journalTitle;
	}

	public String getAbbrJournal() {
		return abbrJournal;
	}

	public void setAbbrJournal(String abbrJournal) {
		this.abbrJournal = abbrJournal;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getEdition() {
		return edition;
	}

	public void setEdition(String edition) {
		this.edition = edition;
	}

	public String getIssn() {
		return issn;
	}

	public void setIssn(String issn) {
		this.issn = issn;
	}

	public String getTotalCites() {
		return totalCites;
	}

	public void setTotalCites(String totalCites) {
		this.totalCites = totalCites;
	}

	public String getJournalImpactFactor() {
		return journalImpactFactor;
	}

	public void setJournalImpactFactor(String journalImpactFactor) {
		this.journalImpactFactor = journalImpactFactor;
	}

	public String getEigenFactorScore() {
		return eigenFactorScore;
	}

	public void setEigenFactorScore(String eigenFactorScore) {
		this.eigenFactorScore = eigenFactorScore;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

}
