package com.baba.lajursurveyor.notifikasi;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by root on 27/12/17.
 */

public class NotifId extends FirebaseInstanceIdService {
//    private SharedPreferences prefs;

    @Override
    public void onTokenRefresh() {
        String token = FirebaseInstanceId.getInstance().getToken();
        registerToken(token);
    }

    private void registerToken(String token) {
//        FungsiTerater ft = new FungsiTerater();
//        prefs = getSharedPreferences("AKUNNYA", MODE_PRIVATE);
//        HashMap map = new HashMap();
//        map.put("token",token);
//        map.put("email",prefs.getString("email_pengguna", ""));
//        task = new PostResponseAsyncTask(this, map, new AsyncResponse() {
//            @Override public void processFinish(String s) {
//
//            }
//        });
//        task.execute(ft.servernya+"_andro_token_user.php");
    }
}