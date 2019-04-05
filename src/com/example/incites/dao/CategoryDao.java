package com.example.incites.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jsoup.helper.StringUtil;

import com.example.incites.entity.Category;

public class CategoryDao extends BaseDao {

	public boolean addCategory(Category category) {
		try {
			if (!isExist(category.getYear(), category.getCategoryId(), category.getEdition())) {
				String sql = "insert into category (rank, year, category_id, category_name, edition, journals, total_cites, median_impact_factor, aggregate_impact_factor) values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
				Object[] params = { category.getRank(), category.getYear(), category.getCategoryId(),
						category.getCategoryName(), category.getEdition(), category.getNumberOfJournals(),
						category.getTotalCites(), category.getMedianImpactFactor(),
						category.getAggregateImpactFactor() };
				int i = this.executeUpdate(sql, params);
				if (i > 0) {
					return true;
				} else {
					return false;
				}
			} else {
				return true;
			}
		} finally {
			this.closeResource();
		}
	}

	public boolean isExist(String year, String categoryId, String edition) {
		String sql = "select * from category where year = ? and category_id = ? and edition = ?";
		Object[] params = { year, categoryId, edition };
		ResultSet rs = this.executeQuery(sql, params);
		try {
			if (rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeResource();
		}
		return false;
	}

	public List<Category> getCategories() {
		List<Category> categories = new ArrayList<Category>();
		String sql = "select * from category order by year,rank";
		Object[] params = {};
		ResultSet rs = this.executeQuery(sql, params);
		try {
			while (rs.next()) {
				Category category = new Category();
				category.setId(rs.getInt("id"));
				category.setRank(rs.getString("rank"));
				category.setYear(rs.getString("year"));
				category.setCategoryId(rs.getString("category_id"));
				category.setCategoryName(rs.getString("category_name"));
				category.setEdition(rs.getString("edition"));
				category.setNumberOfJournals(rs.getString("journals"));
				category.setTotalCites(rs.getString("total_cites"));
				category.setMedianImpactFactor(rs.getString("median_impact_factor"));
				category.setAggregateImpactFactor(rs.getString("aggregate_impact_factor"));
				categories.add(category);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeResource();
		}
		return categories;
	}
	
	public List<Category> getCategories(String[] years) {
		List<Category> categories = new ArrayList<Category>();
		if (years == null || years.length == 0) {
			return categories;
		}
		String[] querys = years.clone();
		Arrays.fill(querys, "?");
		String querySQl = StringUtil.join(querys, ",");
		StringBuffer sql = new StringBuffer("select * from category where year in (");
		sql.append(querySQl);
		sql.append(") order by year,rank");
		ResultSet rs = this.executeQuery(sql.toString(), years);
		try {
			while (rs.next()) {
				Category category = new Category();
				category.setId(rs.getInt("id"));
				category.setRank(rs.getString("rank"));
				category.setYear(rs.getString("year"));
				category.setCategoryId(rs.getString("category_id"));
				category.setCategoryName(rs.getString("category_name"));
				category.setEdition(rs.getString("edition"));
				category.setNumberOfJournals(rs.getString("journals"));
				category.setTotalCites(rs.getString("total_cites"));
				category.setMedianImpactFactor(rs.getString("median_impact_factor"));
				category.setAggregateImpactFactor(rs.getString("aggregate_impact_factor"));
				categories.add(category);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeResource();
		}
		return categories;
	}

	public boolean createTable() {
		try {
			String creatSql = "CREATE TABLE IF NOT EXISTS `category` ( `id` INT UNSIGNED NOT NULL AUTO_INCREMENT, `rank` INT UNSIGNED NOT NULL, `category_name` VARCHAR(100) NOT NULL, `category_id` VARCHAR(10) NOT NULL, `edition` VARCHAR(10) NOT NULL, `journals` VARCHAR(20) NOT NULL, `total_cites` VARCHAR(20), `median_impact_factor` VARCHAR(20), `aggregate_impact_factor` VARCHAR(20), `year` VARCHAR(10) NOT NULL,  PRIMARY KEY (`id`) ) CHARSET = utf8;";
			return super.createTable(creatSql);
		} finally {
			this.closeResource();
		}
	}

	public boolean isTableExist() {
		try {
			return super.isTableExist("category");
		} finally {
			this.closeResource();
		}
	}

}
