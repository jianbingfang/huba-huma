package com.hubahuma.mobile.news;
import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;

import com.hubahuma.mobile.R;

import android.os.Bundle;  
import android.support.v4.app.Fragment;  
import android.view.LayoutInflater;  
import android.view.View;  
import android.view.ViewGroup;  

@EFragment(R.layout.fragment_group)
public class GroupFragment extends BaseFragment {
	
    private boolean isInit; // 是否可以开始加载数据  
  
//    @Override  
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {  
//        isInit = true;  
//        return inflater.inflate(R.layout.fragment_notice, container, false);  
//    }
  
//    @Override  
//    public void onViewCreated(View view, Bundle savedInstanceState) {  
//        super.onViewCreated(view, savedInstanceState);  
//        /* 初始化控件 */  
//    }  

    @AfterInject
    void init(){
    	isInit = true;
    }
    
    @Override  
    public void onResume() {  
        super.onResume();  
        // 判断当前fragment是否显示  
        if (getUserVisibleHint()) {  
            showData();  
        }  
    }  
  
    @Override  
    public void setUserVisibleHint(boolean isVisibleToUser) {  
        super.setUserVisibleHint(isVisibleToUser);  
        // 每次切换fragment时调用的方法  
        if (isVisibleToUser) {  
            showData();  
        }  
    }  
  
    private void showData() {  
        if (isInit) {  
            isInit = false;//加载数据完成  
            // 加载各种数据  
        }
    }

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}  
}  