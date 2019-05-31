package com.spark.newbitrade.activity.language;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.spark.newbitrade.R;
import com.spark.newbitrade.activity.main.MainActivity;
import com.spark.newbitrade.base.ActivityManage;
import com.spark.newbitrade.base.BaseActivity;
import com.spark.newbitrade.utils.SharedPreferenceInstance;

import butterknife.BindView;
import butterknife.OnClick;

public class LanguageActivity extends BaseActivity {
    @BindView(R.id.ivChinese)
    ImageView ivChinese;
    @BindView(R.id.llChinese)
    LinearLayout llChinese;
    @BindView(R.id.ivEnglish)
    ImageView ivEnglish;
    @BindView(R.id.llEnglish)
    LinearLayout llEnglish;
    @BindView(R.id.llTitle)
    LinearLayout llTitle;
    @BindView(R.id.ivJapanese)
    ImageView ivJapanese;
    @BindView(R.id.llJapanese)
    LinearLayout llJapanese;
    private int languageCode = 1;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_language;
    }

    @Override
    protected void initView() {
        setSetTitleAndBack(false, true);
    }


    @OnClick({R.id.llChinese, R.id.llEnglish, R.id.llJapanese})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.llChinese:
                if (languageCode != 1) language(1);
                break;
            case R.id.llEnglish:
                if (languageCode != 2) language(2);
                break;
            case R.id.llJapanese:
                if (languageCode != 3) language(3);
                break;

        }

    }

    private void language(int languageCode) {
        SharedPreferenceInstance.getInstance().saveLanguageCode(languageCode);
        Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
        //i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        /*ActivityManage.finishAll();
        Intent intent = new Intent(activity, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);*/
    }


    @Override
    protected void initData() {
        super.initData();
        setTitle(getString(R.string.language));
        languageCode = SharedPreferenceInstance.getInstance().getLanguageCode();
        if (languageCode == 1) ivChinese.setVisibility(View.VISIBLE);
        else if (languageCode == 2) ivEnglish.setVisibility(View.VISIBLE);
        else if (languageCode == 3) ivJapanese.setVisibility(View.VISIBLE);
    }

}
