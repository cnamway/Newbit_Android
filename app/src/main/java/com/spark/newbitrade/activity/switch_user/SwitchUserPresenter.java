package com.spark.newbitrade.activity.switch_user;


import com.spark.newbitrade.data.DataSource;
import com.spark.newbitrade.entity.User;

import java.util.List;

/**
 * Created by Administrator on 2017/9/25.
 */

public class SwitchUserPresenter implements SwitchUserContract.Presenter {
    private final DataSource dataRepository;
    private final SwitchUserContract.View view;

    public SwitchUserPresenter(DataSource dataRepository, SwitchUserContract.View view) {
        this.dataRepository = dataRepository;
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void users() {
        view.displayLoadingPopup();
        dataRepository.doStringPost("", new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                view.userSuccess((List<User>) obj);
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.userFail(code, toastMessage);
            }
        });
    }

}
