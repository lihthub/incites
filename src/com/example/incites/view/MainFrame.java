package com.example.incites.view;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.incites.dao.CategoryDao;
import com.example.incites.dao.JournalDao;
import com.example.incites.entity.Category;
import com.example.incites.entity.Journal;
import com.example.incites.util.HttpResult;
import com.example.incites.util.HttpUtils;
import com.example.incites.util.PropertiesUtils;

public class MainFrame extends JFrame {
	private static final long serialVersionUID = 6270372566138112989L;
	public static Log log = LogFactory.getLog(HttpUtils.class);
	private JPanel contentPanel = new JPanel();
	private JProgressBar progressBar = new JProgressBar();
	private JTextPane textPane = new JTextPane();
	private Map<String, String> cookies;
	private int numberOfCategories; // 类别导入条数
	private int numberOfJournals; // 期刊导入条数
	private int errorsOfCategories; // 类别导入失败条数
	private int errorsOfJournals; // 期刊导入失败条数
	private int failuresOfCategories; // 类别查询失败次数
	private int failuresOfJournals; // 期刊查询失败次数

	public MainFrame() {
		try {
			// 设置界面风格为系统默认风格
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		Toolkit kit = Toolkit.getDefaultToolkit(); // 定义工具包
		int screenWidth = kit.getScreenSize().width; // 屏幕宽度
		int screenHeight = kit.getScreenSize().height; // 屏幕高度
		int frameWidth = 400; // 主窗口宽度
		int frameHeight = 165; // 主窗口高度
		setBounds(screenWidth / 2 - frameWidth / 2, screenHeight / 2 - frameHeight / 2, frameWidth, frameHeight);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPanel.setBorder(new EmptyBorder(20, 20, 0, 20));
		contentPanel.setLayout(new GridLayout(3, 1));
		
	    progressBar.setStringPainted(true);
	    progressBar.setSize(frameWidth, 26);
		progressBar.setFont(new Font("Menlo", Font.PLAIN, 12));
		progressBar.setDoubleBuffered(true);
	    progressBar.setVisible(true);
		
		textPane.setBackground(getBackground());
		textPane.setSize(frameWidth, 40);
		textPane.setEditable(false);
		textPane.setFont(new Font("Menlo", Font.PLAIN, 12));
		textPane.setText("loading...");
		
		contentPanel.add(progressBar);
		contentPanel.add(textPane);
		setContentPane(contentPanel);
		
		new Thread() {
			@Override
			public void run() {
				try {
					loadCategories();
					loadJournals();
					textPane.setText("SUCCESS!");
					JOptionPane.showMessageDialog(getRootPane(), "Categories : 成功 " + numberOfCategories + " 条，导入失败 " + errorsOfCategories
							+ " 条，查询失败 " + failuresOfCategories + " 次\nJournals : 成功 " + numberOfJournals + " 条，导入失败 " + errorsOfJournals
							+ " 条，查询失败 " + failuresOfJournals + " 次", "导入成功！", JOptionPane.INFORMATION_MESSAGE);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(getRootPane(), e.getStackTrace(), "Error!", JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
					log.error("导入数据时发生错误！", e);
					System.exit(0);
				}
			}
		}.start();
	}
	
	private void loadCategories() {
		CategoryDao categoryDao = new CategoryDao();
		if (!categoryDao.isTableExist()) {
			categoryDao.createTable();
		}
		String yearStr = PropertiesUtils.getInstance().getProperty("year");
		String[] years = yearStr.split(",");
		for (int i = 0; i < years.length; i++) {
			String year = years[i];
			int page = 0;
			int total = 0;
			int limit = 500;
			int count = 0;
			do {
				String url = "https://jcr.incites.thomsonreuters.com/CategoriesDataJson.action?start=" + (limit * page) + "&limit=" + limit + "&jcrYear=" + year;
				String categoriesDataJson = httpGet(url);
				JSONObject jsonObject = JSON.parseObject(categoriesDataJson);
				String status = jsonObject.getString("status");
				if ("SUCCESS".equals(status)) {
					String totalCount = jsonObject.getString("totalCount");
					if (totalCount != null & !totalCount.isEmpty()) {
						total = Integer.parseInt(totalCount);
					}
					JSONArray jsonArray = jsonObject.getJSONArray("data");
					if (jsonArray != null) {
						List<Category> categories = jsonArray.toJavaList(Category.class);
						for (Category category : categories) {
							boolean success = categoryDao.addCategory(category);
							if (success) {
								numberOfCategories++;
							} else {
								errorsOfCategories++;
								log.error("插入类别失败！Category Name : " + category.getCategoryName() + " (" + category.getYear() + ") ");
							}
							count++;
							int n = Double.valueOf(Math.ceil(100.0 / total * count)).intValue();
							progressBar.setValue(n);
							progressBar.setString(n + "%");
							textPane.setText("Categories (" + count + "/" + total + ") in " + year);
						}
					}
				} else {
					failuresOfCategories++;
					log.error("查询类别失败！url = " + url + ", result = " + categoriesDataJson);
				}
				page++;
			} while (page < (total % limit > 0 ? (total / limit + 1) : (total / limit)));
		}
	}
	
	private void loadJournals() {
		CategoryDao categoryDao = new CategoryDao();
		JournalDao journalDao = new JournalDao();
		if (!journalDao.isTableExist()) {
			journalDao.createTable();
		}
		String yearStr = PropertiesUtils.getInstance().getProperty("year");
		String[] years = yearStr.split(",");
		List<Category> categories = categoryDao.getCategories(years);
		int categoryCount = categories.size();
		for (int i = 0; i < categoryCount; i++) {
			Category category = categories.get(i);
			int page = 0;
			int total = 0;
			int limit = 500;
			int count = 0;
			do {
				String url = "https://jcr.incites.thomsonreuters.com/JournalHomeGridJson.action?start=" + (limit * page) + "&limit=" + limit + "&jcrYear=" + category.getYear() + "&edition=" + category.getEdition() + "&categoryIds=" + category.getCategoryId();
				String journalHomeGridJson = httpGet(url);
				JSONObject jsonObject = JSON.parseObject(journalHomeGridJson);
				String status = jsonObject.getString("status");
				if ("SUCCESS".equals(status)) {
					String totalCount = jsonObject.getString("totalCount");
					if (totalCount != null & !totalCount.isEmpty()) {
						total = Integer.parseInt(totalCount);
					}
					JSONArray jsonArray = jsonObject.getJSONArray("data");
					if (jsonArray != null) {
						List<Journal> journals = jsonArray.toJavaList(Journal.class);
						for (Journal journal : journals) {
							journal.setCategoryId(category.getCategoryId());
							journal.setCategoryName(category.getCategoryName());
							boolean success = journalDao.addJournal(journal);
							if (success) {
								numberOfJournals++;
							} else {
								errorsOfJournals++;
								log.error("插入期刊失败！Journal Name : " + journal.getJournalTitle() + ", ISSN : " + journal.getIssn() + ", Year : " + journal.getYear() + ", Category Name : " + journal.getCategoryName());
							}
							count++;
							int n = Double.valueOf(Math.ceil(100.0 / categoryCount * (i + 1))).intValue();
							progressBar.setValue(n);
							progressBar.setString(n + "%");
							textPane.setText("Journals (" + count + "/" + total + ") in " + category.getCategoryName() + " (" + category.getYear() + ")");
						}
					}
				} else {
					failuresOfJournals++;
					log.error("查询期刊失败！url = " + url + ", result = " + journalHomeGridJson);
				}
				page++;
			} while (page < (total % limit > 0 ? (total / limit + 1) : (total / limit)));
		}
	}
	
	private String httpGet(String url) {
		int connectTimes = 0; // 连接次数
		return connect(url, connectTimes);
	}
	
	private String connect(String url, int connectTimes) {
		HttpResult result = null;
		try {
			result = HttpUtils.connect(url, cookies);
			cookies = result.getCookies();
			return result.getBody();
		} catch (Exception e) {
			connectTimes++;
			e.printStackTrace();
			log.error("网络连接错误：" + e.getMessage() + "！已尝试连接 " + connectTimes + " 次！url = " + url);
			if (connectTimes < 6) {
				return connect(url, connectTimes);
			} else {
				return "{\"status\":\"FAILURE\",\"message\":\"" + e.getMessage() + "\"}";
			}
		}
	}
	
}
