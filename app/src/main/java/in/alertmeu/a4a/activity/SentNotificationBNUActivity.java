package in.alertmeu.a4a.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.alertmeu.a4a.R;
import in.alertmeu.a4a.adapter.FCMSIDsListAdpter;
import in.alertmeu.a4a.jsonparser.JsonHelper;
import in.alertmeu.a4a.models.SendNotificationModelDAO;
import in.alertmeu.a4a.utils.AppStatus;
import in.alertmeu.a4a.utils.Config;
import in.alertmeu.a4a.utils.Constant;
import in.alertmeu.a4a.utils.WebClient;

public class SentNotificationBNUActivity extends AppCompatActivity {
    ImageView back_arrow1;
    JSONObject jsonLeadObj;
    String apptype = "", myPlaceListResponse = "";
    JSONArray jsonArray;
    List<SendNotificationModelDAO> data;
    FCMSIDsListAdpter fcmsiDsListAdpter;
    RecyclerView mainCatList;
    TextView title;
    CheckBox chkAll;
    LinearLayout btnNext;
    JSONObject jsonObjectSync, syncJsonObject;
    JSONArray jsonArraySync;
    String syncDataesponse = "", maincat_id = "", ntitle = "", nmessage = "";
    ArrayList<String> fcmIdsArrayList;
    AlertDialog dialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sent_notification_b_n_u);
        back_arrow1 = (ImageView) findViewById(R.id.back_arrow1);
        title = (TextView) findViewById(R.id.title);
        btnNext = (LinearLayout) findViewById(R.id.btnNext);
        data = new ArrayList<>();
        Intent intent = getIntent();
        apptype = intent.getStringExtra("apptype");
        if (apptype.equals("1")) {
            title.setText("Please select customers");
        } else {
            title.setText("Please select business");
        }
        mainCatList = (RecyclerView) findViewById(R.id.mainCatList);
        if (AppStatus.getInstance(SentNotificationBNUActivity.this).isOnline()) {
            new getSubCatList().execute();
        } else {

            Toast.makeText(SentNotificationBNUActivity.this, Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
        }
        back_arrow1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        chkAll = (CheckBox) findViewById(R.id.chkAllSelected);
        chkAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                if (cb.isChecked()) {
                    List<SendNotificationModelDAO> list = ((FCMSIDsListAdpter) fcmsiDsListAdpter).getSservicelist();
                    for (SendNotificationModelDAO workout : list) {
                        workout.setSelected(true);
                        workout.setChecked_status("1");
                    }

                    ((FCMSIDsListAdpter) mainCatList.getAdapter()).notifyDataSetChanged();
                } else {
                    List<SendNotificationModelDAO> list = ((FCMSIDsListAdpter) fcmsiDsListAdpter).getSservicelist();
                    for (SendNotificationModelDAO workout : list) {
                        workout.setSelected(false);
                        workout.setChecked_status("0");
                    }

                    ((FCMSIDsListAdpter) mainCatList.getAdapter()).notifyDataSetChanged();
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<SendNotificationModelDAO> stList = ((FCMSIDsListAdpter) fcmsiDsListAdpter).getSservicelist();

                String data1 = "";
                String data2 = "";
                String data3 = "";
                jsonArraySync = new JSONArray();
                fcmIdsArrayList = new ArrayList<>();
                for (int i = 0; i < stList.size(); i++) {
                    SendNotificationModelDAO serviceListDAO = stList.get(i);
                    if (serviceListDAO.isSelected() == true) {
                        fcmIdsArrayList.add(serviceListDAO.getId());
                    } else {
                        System.out.println("not selected");

                    }
                }


                if (fcmIdsArrayList.size() > 0) {
                    // Toast.makeText(SentNotificationBNUActivity.this, "" + syncJsonObject, Toast.LENGTH_SHORT).show();
                    if (AppStatus.getInstance(SentNotificationBNUActivity.this).isOnline()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(SentNotificationBNUActivity.this);
                        // set the custom layout
                        LayoutInflater li = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                        final View customLayout = li.inflate(R.layout.notify_custom_layout, null);
                        builder.setView(customLayout);
                        Button send = customLayout.findViewById(R.id.btnSend);
                        ImageView back_arrow1 = customLayout.findViewById(R.id.back_arrow1);
                        final EditText titleEdtTxt = customLayout.findViewById(R.id.titleEdtTxt);
                        final EditText editText = customLayout.findViewById(R.id.descEdtTxt);
                        final EditText urlEdtTxt = customLayout.findViewById(R.id.urlEdtTxt);
                        back_arrow1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.cancel();
                            }

                        });
                        send.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ntitle = titleEdtTxt.getText().toString();
                                nmessage = editText.getText().toString();
                                String url = urlEdtTxt.getText().toString();
                                if (url.equals("")) {
                                    url = "null";
                                }
                                if (validate(ntitle, nmessage)) {
                                    // Toast.makeText(getActivity(), "" + syncJsonObject, Toast.LENGTH_SHORT).show();
                                    List<SendNotificationModelDAO> stList = ((FCMSIDsListAdpter) fcmsiDsListAdpter).getSservicelist();
                                    for (int i = 0; i < stList.size(); i++) {
                                        SendNotificationModelDAO serviceListDAO = stList.get(i);
                                        if (serviceListDAO.isSelected() == true) {
                                            jsonObjectSync = new JSONObject();
                                            try {
                                                jsonObjectSync.put("fcm_id", serviceListDAO.getFcm_id());
                                                jsonObjectSync.put("title", ntitle);
                                                jsonObjectSync.put("message", nmessage);
                                                jsonObjectSync.put("url", url);
                                                jsonArraySync.put(jsonObjectSync);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        } else {
                                            System.out.println("not selected");

                                        }
                                    }
                                    try {
                                        syncJsonObject = new JSONObject();
                                        syncJsonObject.put("preData", jsonArraySync);
                                        Log.d("preData", "" + syncJsonObject);
                                    } catch (JSONException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                    Thread objectThread = new Thread(new Runnable() {
                                        public void run() {
                                            // TODO Auto-generated method stub
                                            final WebClient serviceAccess = new WebClient();
                                            if (apptype.equals("1")) {
                                                syncDataesponse = serviceAccess.SendHttpPost(Config.URL_SENDCUSTUMNOTIFICATIONUSERDATA, syncJsonObject);
                                            } else {
                                                syncDataesponse = serviceAccess.SendHttpPost(Config.URL_SENDCUSTUMNOTIFICATIONBUSINESSDATA, syncJsonObject);
                                            }
                                            Log.i("resp", "syncDataesponse" + syncDataesponse);
                                            final Handler handler = new Handler(Looper.getMainLooper());
                                            Runnable runnable = new Runnable() {
                                                @Override
                                                public void run() {
                                                    handler.post(new Runnable() { // This thread runs in the UI
                                                        @Override
                                                        public void run() {
                                                            dialog.cancel();
                                                            if (AppStatus.getInstance(SentNotificationBNUActivity.this).isOnline()) {
                                                                new getSubCatList().execute();
                                                            } else {

                                                                Toast.makeText(SentNotificationBNUActivity.this, Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                                }
                                            };

                                            new Thread(runnable).start();
                                        }
                                    });
                                    objectThread.start();

                                }
                            }
                        });

                        // create and show
                        // the alert dialog
                        dialog = builder.create();
                        dialog.show();

                    } else {

                        Toast.makeText(SentNotificationBNUActivity.this, Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(SentNotificationBNUActivity.this, "Please Select Data", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public boolean validate(String ntitle, String nmessage) {
        boolean isValidate = false;
        if (ntitle.equals("")) {
            Toast.makeText(SentNotificationBNUActivity.this, "Please Enter Title", Toast.LENGTH_LONG).show();
        } else if (nmessage.equals("")) {
            Toast.makeText(SentNotificationBNUActivity.this, "Please Select Description", Toast.LENGTH_LONG).show();
        } else {
            isValidate = true;
        }
        return isValidate;
    }

    private class getSubCatList extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            //   mProgressDialog = new ProgressDialog(getActivity());
            // Set progressdialog title
            //    mProgressDialog.setTitle(res.getString(R.string.jpw));
            // Set progressdialog message
            //   mProgressDialog.setMessage(res.getString(R.string.jsql));
            //mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            //    mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            jsonLeadObj = new JSONObject() {
                {
                    try {
                        put("apptype", apptype);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("json exception", "json exception" + e);
                    }
                }
            };
            WebClient serviceAccess = new WebClient();

            Log.i("json", "json" + jsonLeadObj);
            myPlaceListResponse = serviceAccess.SendHttpPost(Config.URL_GETALLUBFCMIDBYADMIN, jsonLeadObj);
            Log.i("resp", "myPlaceListResponse" + myPlaceListResponse);
            if (myPlaceListResponse.compareTo("") != 0) {
                if (isJSONValid(myPlaceListResponse)) {
                    try {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                try {


                                    JsonHelper jsonHelper = new JsonHelper();
                                    data = jsonHelper.parseFCMIdsList(myPlaceListResponse);
                                    jsonArray = new JSONArray(myPlaceListResponse);

                                } catch (JSONException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                        });
                    } catch (Exception e) {
                    }
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(SentNotificationBNUActivity.this, Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                        }
                    });

                    return null;
                }
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SentNotificationBNUActivity.this, Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                    }
                });

                return null;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            // Close the progressdialog

            //mProgressDialog.dismiss();
            if (data.size() > 0) {

                fcmsiDsListAdpter = new FCMSIDsListAdpter(SentNotificationBNUActivity.this, data);
                mainCatList.setAdapter(fcmsiDsListAdpter);
                mainCatList.setLayoutManager(new LinearLayoutManager(SentNotificationBNUActivity.this));
                fcmsiDsListAdpter.notifyDataSetChanged();

            }
        }
    }

    protected boolean isJSONValid(String callReoprtResponse2) {
        // TODO Auto-generated method stub
        try {
            new JSONObject(callReoprtResponse2);
        } catch (JSONException ex) {
            // edited, to include @Arthur's comment
            // e.g. in case JSONArray is valid as well...
            try {
                new JSONArray(callReoprtResponse2);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }

}