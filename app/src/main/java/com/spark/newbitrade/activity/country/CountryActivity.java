package com.spark.newbitrade.activity.country;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.spark.newbitrade.R;
import com.spark.newbitrade.adapter.CountryAdapter;
import com.spark.newbitrade.base.BaseActivity;
import com.spark.newbitrade.entity.Country;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 国家选择
 */
public class CountryActivity extends BaseActivity implements CountryContract.View {
    public static final int RETURN_COUNTRY = 0;
    @BindView(R.id.rvCountry)
    RecyclerView rvCountry;
    private List<Country> countries = new ArrayList<>();
    private CountryAdapter adapter;
    private CountryPresenterImpl presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_country;
    }

    @Override
    protected void initView() {
        setSetTitleAndBack(false, true);
        presenter = new CountryPresenterImpl(this);
    }

    @Override
    protected void initData() {
        super.initData();
        initRvCountry();
        setTitle(getString(R.string.egional_selection));
    }


    private void initRvCountry() {
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvCountry.setLayoutManager(manager);
        adapter = new CountryAdapter(R.layout.item_country, countries);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent();
                intent.putExtra("country", countries.get(position));
                CountryActivity.this.setResult(RESULT_OK, intent);
                finish();
            }
        });
        rvCountry.setAdapter(adapter);
    }

    @Override
    protected void loadData() {
        presenter.country();
    }

    @Override
    public void countrySuccess(List<Country> obj) {
        if (obj == null) return;
        this.countries.clear();
        this.countries.addAll(obj);
        adapter.notifyDataSetChanged();
    }


}
