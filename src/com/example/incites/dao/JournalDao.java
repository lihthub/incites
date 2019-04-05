package com.example.incites.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.incites.entity.Journal;

public class JournalDao extends BaseDao {

	public boolean addJournal(Journal journal) {
		try {
			if (!isExist(journal.getIssn(), journal.getJournalTitle(), journal.getCategoryId(), journal.getEdition(), journal.getYear())) {
				String sql = "insert into journal (rank, journal_title, abbr_journal, year, edition, issn, total_cites, journal_impact_factor, eigen_factor_score, category_name, category_id) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				Object[] params = { journal.getRank(), journal.getJournalTitle(), journal.getAbbrJournal(),
						journal.getYear(), journal.getEdition(), journal.getIssn(), journal.getTotalCites(),
						journal.getJournalImpactFactor(), journal.getEigenFactorScore(), journal.getCategoryName(),
						journal.getCategoryId() };
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
	
	public boolean deleteAll() {
		String sql = "delete from journal";
		Object[] params = { };
		try {
			return this.executeUpdate(sql, params) > 0 ? true : false;
		} finally {
			this.closeResource();
		}
	}

	public boolean isExist(String issn, String journalTitle, String categoryId, String edition, String year) {
		String sql = "select * from journal where issn = ? and journal_title = ? and category_id = ? and edition = ? and year = ?";
		Object[] params = { issn, journalTitle, categoryId, edition, year };
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

	public boolean createTable() {
		try {
			String creatSql = "CREATE TABLE IF NOT EXISTS `journal` ( `id` INT UNSIGNED NOT NULL AUTO_INCREMENT, `rank` INT UNSIGNED NOT NULL, `journal_title` VARCHAR(300) NOT NULL, `abbr_journal` VARCHAR(200) NOT NULL, `year` VARCHAR(10) NOT NULL, `edition` VARCHAR(10) NOT NULL, `issn` VARCHAR(10), `total_cites` VARCHAR(20), `journal_impact_factor` VARCHAR(20), `eigen_factor_score` VARCHAR(20), `category_name` VARCHAR(100) NOT NULL, `category_id` VARCHAR(10) NOT NULL, PRIMARY KEY (`id`) ) CHARSET = utf8;";
			return super.createTable(creatSql);
		} finally {
			this.closeResource();
		}
	}

	public boolean isTableExist() {
		try {
			return super.isTableExist("journal");
		} finally {
			this.closeResource();
		}
	}

	public int getTotalCount() {
		String sql = "select count(*) totalCount from journal";
		Object[] params = { };
		ResultSet rs = this.executeQuery(sql, params);
		try {
			if (rs.next()) {
				return rs.getInt("totalCount");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeResource();
		}
		return 0;
	}

}
