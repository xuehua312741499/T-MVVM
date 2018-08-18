package com.code.mvvm.core.view.course;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.basiclibrary.base.BaseFragment;
import com.code.mvvm.R;
import com.code.mvvm.base.BaseViewPagerFragment;
import com.code.mvvm.core.data.pojo.course.CourseTypeVo;
import com.code.mvvm.core.viewmodel.CourseViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author：tqzhang on 18/5/2 19:20
 */
public class VideoFragment extends BaseViewPagerFragment<CourseViewModel> {
    private List<CourseTypeVo.DataBean> titleName;

    public static VideoFragment newInstance() {
        return new VideoFragment();
    }

    @Override
    public void initView(Bundle state) {
        super.initView(state);
        setTitle(getResources().getString(R.string.video_title_name));
        titleName = new ArrayList<>();
        getTabData();
    }

    @Override
    protected void dataObserver() {
        mViewModel.getCourseType().observe(this, new Observer<CourseTypeVo>() {
            @Override
            public void onChanged(@Nullable CourseTypeVo courseTypeVo) {
                if (courseTypeVo == null) {
                    return;
                }
                setData(courseTypeVo);
            }
        });
    }

    @Override
    protected void onStateRefresh() {
        super.onStateRefresh();
        getTabData();
    }

    @Override
    protected CourseViewModel createViewModelProviders() {
        return VMProviders(this, CourseViewModel.class);
    }

    @Override
    protected String[] createPageTitle() {
        return mArrTitles;
    }

    @Override
    protected List<BaseFragment> createFragments() {
        return mFragments;
    }

    private void getTabData() {
        mViewModel.getCourseTypeData();
    }


    private void setData(CourseTypeVo courseTypeVo) {
        mArrTitles = new String[courseTypeVo.data.size() + 1];
        titleName.clear();
        CourseTypeVo.DataBean dataBean = new CourseTypeVo.DataBean();
        dataBean.name = getResources().getString(R.string.recommend_tab_name);
        mArrTitles[0] = getResources().getString(R.string.recommend_tab_name);
        titleName.add(dataBean);
        for (int j = 0; j < courseTypeVo.data.size(); j++) {
            titleName.add(courseTypeVo.data.get(j));
            mArrTitles[j + 1] = (courseTypeVo.data.get(j).name);
        }
        initFragment();
        setAdapter();
    }

    private void initFragment() {
        for (int i = 0; i < titleName.size(); i++) {
            if (i == 0) {
                CourseRecommendFragment courseRecommendFragment = CourseRecommendFragment.newInstance();
                mFragments.add(courseRecommendFragment);
            } else {
                CourseListFragment courseListFragment = CourseListFragment.newInstance();
                Bundle bundle = new Bundle();
                bundle.putString("f_catalog_id", titleName.get(i).id);
                bundle.putSerializable("s_catalog", titleName.get(i).s_catalog);
                courseListFragment.setArguments(bundle);
                mFragments.add(courseListFragment);
            }

        }
    }
}