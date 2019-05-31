package com.spark.newbitrade.activity.wallet_coin;


/**
 * Created by Administrator on 2017/9/25.
 */

public class ExtractPresenter {
//    private final DataSource dataRepository;
//    private final ExtractContract.View view;
//
//    public ExtractPresenter(DataSource dataRepository, ExtractContract.View view) {
//        this.dataRepository = dataRepository;
//        this.view = view;
//        view.setPresenter(this);
//    }
//
//    @Override
//    public void getCode(HashMap<String, String> params) {
//        dataRepository.doStringPost(UrlFactory.getSendCodeAfterLoginUrl(), params, new DataSource.DataCallback() {
//            @Override
//            public void onDataLoaded(Object obj) {
//                String response = (String) obj;
//                try {
//                    JSONObject object = new JSONObject(response);
//                    if (object.optInt("code") == GlobalConstant.SUCCESS_CODE) {
//                        view.codeSuccess(object.optString("message"));
//                    } else {
//                        view.codeFail(object.getInt("code"), object.optString("message"));
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    view.codeFail(JSON_ERROR, null);
//                }
//            }
//
//            @Override
//            public void onDataNotAvailable(Integer code, String toastMessage) {
//                view.codeFail(code, toastMessage);
//            }
//        });
//    }
//
//    @Override
//    public void extractinfo(final HashMap<String, String> params) {
//        view.displayLoadingPopup();
//        dataRepository.doStringPost(UrlFactory.getExtractinfoUrl(), params, new DataSource.DataCallback() {
//            @Override
//            public void onDataLoaded(Object obj) {
//                view.hideLoadingPopup();
//                String response = (String) obj;
//                try {
//                    JSONObject object = new JSONObject(response);
//                    int code = object.optInt("code");
//                    if (code == GlobalConstant.SUCCESS_CODE) {
//                        ExtractInfo objs = new Gson().fromJson(object.getJSONObject("data").toString(), ExtractInfo.class);
//                        view.extractinfoSuccess(objs);
//                    } else if (code == GlobalConstant.SUCCESS_SEC_CODE) {
//                        extractinfo(params);
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
//    public void extract(final HashMap<String, String> params) {
//        view.displayLoadingPopup();
//        dataRepository.doStringPost(UrlFactory.getExtractUrl(), params, new DataSource.DataCallback() {
//            @Override
//            public void onDataLoaded(Object obj) {
//                view.hideLoadingPopup();
//                String response = (String) obj;
//                try {
//                    JSONObject object = new JSONObject(response);
//                    int code = object.optInt("code");
//                    if (code == GlobalConstant.SUCCESS_CODE) {
//                        view.extractSuccess(object.optString("message"));
//                    } else if (code == GlobalConstant.SUCCESS_SEC_CODE) {
//                        extract(params);
//                    } else {
//                        view.doPostFail(object.getInt("code"), object.optString("message"));
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    view.doPostFail(JSON_ERROR, null);
//                }
//
//            }
//
//            @Override
//            public void onDataNotAvailable(Integer code, String toastMessage) {
//                view.hideLoadingPopup();
//                view.doPostFail(code, toastMessage);
//
//            }
//        });
//    }


}
