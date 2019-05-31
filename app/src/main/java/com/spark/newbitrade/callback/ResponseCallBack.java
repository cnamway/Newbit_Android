/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.spark.newbitrade.callback;

import com.android.volley.VolleyError;
import com.spark.newbitrade.entity.HttpErrorEntity;

/**
 * 监听
 */
public class ResponseCallBack<T> {

    public interface SuccessListener<T> {
        public void onResponse(T response);
    }

    public interface ErrorListener {
        public void onErrorResponse(HttpErrorEntity httpErrorEntity);

        public void onErrorResponse(VolleyError volleyError);
    }
}
