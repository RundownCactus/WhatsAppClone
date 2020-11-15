package com.salmanqureshi.i170282_170019;

import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

public class SendNotif {
    public SendNotif(String msg,String heading,String notifKey){
         try {
             JSONObject notificationContent = new JSONObject(
                     "{'contents':{'en':'" + msg + "'},"+
                             "'include_player_ids':['" + notifKey + "']," +
                             "'headings':{'en': '" + heading + "'}}");
            OneSignal.postNotification(notificationContent,null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
