package com.salmanqureshi.i170282_170019;

import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

public class SendNotif {
    public SendNotif(String msg,String heading,String notifKey){
        notifKey = "e085c92c-f3c3-4ced-b786-de43d9cfa77e";
         try {
            JSONObject notifContent = new JSONObject("{'contents':{'en':'" +msg + "'},"+
                    "'include_player_ids':['"+notifKey
                    +"'],"
                    + "'headings':{'en':"+heading+"'}}" );
            OneSignal.postNotification(notifContent,null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
