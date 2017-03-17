package com.njit.student.yuqzy.njitstudent.net.http.api;


import com.njit.student.yuqzy.njitstudent.model.UpdateInfo;
import com.njit.student.yuqzy.njitstudent.net.http.BaseAppResponse;

import retrofit2.http.GET;
import rx.Observable;

public interface AppController {
    //https://github.com/yuqZY/MinXue/updateinfo.json
    @GET("https://raw.githubusercontent.com/yuqZY/NJITStudent/master/updateinfo.json")
    Observable<BaseAppResponse<UpdateInfo>> checkUpdate();
}
