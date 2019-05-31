package com.spark.newbitrade.activity.switch_user;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.spark.newbitrade.MyApplication;
import com.spark.newbitrade.R;
import com.spark.newbitrade.activity.login.LoginActivity;
import com.spark.newbitrade.activity.main.MainActivity;
import com.spark.newbitrade.adapter.SwitchUserAdapter;
import com.spark.newbitrade.base.BaseActivity;
import com.spark.newbitrade.entity.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import config.Injection;

public class SwitchUserActivity extends BaseActivity implements SwitchUserContract.View {
    @BindView(R.id.llAddUser)
    LinearLayout llAddUser;
    @BindView(R.id.rvUser)
    RecyclerView rvUser;
    private User user;
    private SwitchUserContract.Presenter presenter;
    private List<User> users = new ArrayList<>();
    private SwitchUserAdapter adapter;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_switch_user;
    }

    @Override
    protected void initView() {
        setSetTitleAndBack(false, true);
    }

    @Override
    protected void initData() {
        super.initData();
        setTitle(getString(R.string.switch_account));
        new SwitchUserPresenter(Injection.provideTasksRepository(getApplicationContext()), this);
        user = MyApplication.getApp().getCurrentUser();
        initRv();
    }

    @Override
    protected void setListener() {
        super.setListener();
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                List<User> uses = (List<User>) (adapter.getData());
                User clickUser = (User) adapter.getData().get(position);
                if (clickUser.getUsername().equals(user.getUsername())) return;
                else {
                    for (User us : uses) us.setSelect(false);
                    clickUser.setSelect(true);
                }
                adapter.notifyDataSetChanged();
                MyApplication.getApp().deleteCurrentUser();
                MyApplication.getApp().setCurrentUser(clickUser);
                showActivity(MainActivity.class, null);
            }
        });
    }

    @OnClick(R.id.llAddUser)
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        if (v.getId() == R.id.llAddUser) {
            showActivity(LoginActivity.class, null);
        }
    }

    private void initRv() {
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvUser.setLayoutManager(manager);
        rvUser.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter = new SwitchUserAdapter(R.layout.item_user, users);
        rvUser.setAdapter(adapter);
    }

    @Override
    protected void loadData() {
        presenter.users();
    }

    @Override
    public void setPresenter(SwitchUserContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void userFail(Integer code, String toastMessage) {
    }

    @Override
    public void userSuccess(List<User> obj) {
        for (User us : obj) {
            if (user == null || us.getUsername().equals(user.getUsername())) {
                us.setSelect(true);
                break;
            }
        }
        adapter.setNewData(obj);
    }
}
