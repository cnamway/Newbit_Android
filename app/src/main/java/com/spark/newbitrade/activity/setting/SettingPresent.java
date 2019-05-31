package com.spark.newbitrade.activity.setting;

/**
 * Created by Administrator on 2018/4/24 0024.
 */

public class SettingPresent {

//    private final DataSource dataRepository;
//    private final SettingContact.View view;
//
//    public SettingPresent(DataSource dataRepository, SettingContact.View view) {
//        this.dataRepository = dataRepository;
//        this.view = view;
//        view.setPresenter(this);
//    }
//
//    @Override
//    public void doUcLoginOut() {
//        view.displayLoadingPopup();
//        dataRepository.doStringPost(UrlFactory.getUcLoginOutUrl(), new DataSource.DataCallback() {
//            @Override
//            public void onDataLoaded(Object obj) {
//                view.hideLoadingPopup();
//                try {
//                    String response = (String) obj;
//                    JSONObject object = new JSONObject(response);
//                    if (object.optInt("code") == GlobalConstant.SUCCESS_SEC_CODE) {
//                        view.doLoginOutSuccess(object.optString("message"));
//                    } else {
//                        view.doPostFail(object.getInt("code"), object.optString("message"));
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    view.doPostFail(JSON_ERROR, null);
//                }
//            }
//
//            @Override
//            public void onDataNotAvailable(Integer code, String toastMessage) {
//                view.hideLoadingPopup();
//                view.doPostFail(code, toastMessage);
//            }
//        });
//    }
//
//    @Override
//    public void doAcLoginOut() {
//    //    view.displayLoadingPopup();
//        dataRepository.doStringPost(UrlFactory.getAcLoginOutUrl(), new DataSource.DataCallback() {
//            @Override
//            public void onDataLoaded(Object obj) {
//                /*view.hideLoadingPopup();
//                try {
//                    String response = (String) obj;
//                    JSONObject object = new JSONObject(response);
//                    if (object.optInt("code") == GlobalConstant.SUCCESS_SEC_CODE) {
//                        view.doLoginOutSuccess(object.optString("message"));
//                    } else {
//                        view.doPostFail(object.getInt("code"), object.optString("message"));
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    view.doPostFail(JSON_ERROR, null);
//                }*/
//            }
//
//            @Override
//            public void onDataNotAvailable(Integer code, String toastMessage) {
//                /*view.hideLoadingPopup();
//                view.doPostFail(code, toastMessage);*/
//            }
//        });
//    }
//
//    @Override
//    public void doOtcLoginOut() {
//     //   view.displayLoadingPopup();
//        dataRepository.doStringPost(UrlFactory.getOtcLoginOutUrl(), new DataSource.DataCallback() {
//            @Override
//            public void onDataLoaded(Object obj) {
//                /*view.hideLoadingPopup();
//                try {
//                    String response = (String) obj;
//                    JSONObject object = new JSONObject(response);
//                    if (object.optInt("code") == GlobalConstant.SUCCESS_SEC_CODE) {
//                        view.doLoginOutSuccess(object.optString("message"));
//                    } else {
//                        view.doPostFail(object.getInt("code"), object.optString("message"));
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    view.doPostFail(JSON_ERROR, null);
//                }*/
//            }
//
//            @Override
//            public void onDataNotAvailable(Integer code, String toastMessage) {
//                /*view.hideLoadingPopup();
//                view.doPostFail(code, toastMessage);*/
//            }
//        });
//    }
}
