package com.hubahuma.mobile.profile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.androidannotations.annotations.AfterTextChange;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.NoTitle;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import com.hubahuma.mobile.R;
import com.hubahuma.mobile.UserType;
import com.hubahuma.mobile.R.layout;
import com.hubahuma.mobile.entity.ArticleEntity;
import com.hubahuma.mobile.entity.GroupEntity;
import com.hubahuma.mobile.entity.UserEntity;
import com.hubahuma.mobile.news.teachingdiary.TeachingDiaryViewAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

@SuppressWarnings("deprecation")
@NoTitle
@EActivity(R.layout.activity_published_article)
public class PublishedArticleActivity extends Activity {

	@ViewById
	ListView article_list;

	@ViewById
	ProgressBar progress_bar;

	@ViewById
	CheckBox btn_layout_switch;

	@ViewById
	EditText search_input;
	
	private List<ArticleEntity> dataList = new ArrayList<ArticleEntity>();
	private List<ArticleEntity> filteredList;

	private PublishedArticleDetailViewAdapter detailAdapter;

	private PublishedArticleOutlineViewAdapter outlineAdapter;

	@AfterViews
	void init() {
		loadData();
		btn_layout_switch
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							article_list.setAdapter(detailAdapter);
							detailAdapter.notifyDataSetChanged();
						} else {
							article_list.setAdapter(outlineAdapter);
							outlineAdapter.notifyDataSetChanged();
						}
						search_input.setText("");
					}
				});
	}

	@AfterTextChange(R.id.search_input)
	void afterTextChangedOnHelloTextView(Editable text) {

		String word = text.toString().trim();

		filteredList.clear();

		for (ArticleEntity entity : dataList) {
			if (entity.getTitle() != null && entity.getTitle().contains(word)) {
				filteredList.add(entity);
			}
		}

		if (btn_layout_switch.isChecked()) {
			detailAdapter.notifyDataSetChanged();
		} else {
			outlineAdapter.notifyDataSetChanged();
		}

	}

	@Background(delay = 2000)
	void loadData() {
		showProgressBar();
		dataList = getTestData();
		filteredList = new ArrayList<ArticleEntity>(dataList);
		hideProgressBar();
	}

	@UiThread
	void showProgressBar() {
		progress_bar.setVisibility(View.VISIBLE);
	}

	@UiThread
	void hideProgressBar() {
		detailAdapter = new PublishedArticleDetailViewAdapter(
				getApplicationContext(), filteredList);
		outlineAdapter = new PublishedArticleOutlineViewAdapter(
				getApplicationContext(), filteredList);
		article_list.setAdapter(detailAdapter);
		progress_bar.setVisibility(View.GONE);

	}

	@Click
	void btn_back() {
		Intent intent = getIntent();
		intent.putExtra("result", "returned from TeachingDiaryActivity");
		this.setResult(0, intent);
		this.finish();
	}

	@Override
	public void onBackPressed() {
		btn_back();
	}

	private List<ArticleEntity> getTestData() {

		List<ArticleEntity> articleList = new ArrayList<ArticleEntity>();
		Random rand = new Random();

		UserEntity user = new UserEntity();
		user.setId("#0001");
		user.setType(UserType.TEACHER);
		user.setUsername("姚燕芬老师");
		user.setRemark("北京市第二幼儿园");

		for (int i = 0; i < 4; i++) {
			ArticleEntity article = new ArticleEntity();
			article.setAuthor(user);
			article.setImg(null);
			article.setTitle("英孚青少儿英语建理想校园：校风师德受期待");
			article.setContent("英孚教育联手某知名门户网站针对展开调研，英孚教育联手某知名门户网站针对展开调研，英孚教育联手某知名门户网站针对展开调研，英孚教育联手某知名门户网站针对展开调研。");
			article.setCommentNum(rand.nextInt(50));
			article.setCollectNum(rand.nextInt(100));
			article.setDate(new Date());
			article.setLiked(rand.nextBoolean());
			articleList.add(article);
		}

		return articleList;
	}

}
