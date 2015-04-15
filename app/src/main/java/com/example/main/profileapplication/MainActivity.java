package com.example.main.profileapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.main.profileapplication.customViews.parallaxScrollView;
import com.parse.FunctionCallback;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends Activity{

    private parallaxScrollView mScrollView;
    private ProgressBar mCompLoader;
    private ProgressBar mProgLoader;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mScrollView = (parallaxScrollView) findViewById(R.id.scroll_view);
        mCompLoader = (ProgressBar) findViewById(R.id.list_profile_comp_loader);
        mProgLoader = (ProgressBar) findViewById(R.id.list_profile_prog_loader);

        

        InitProfile();
    }

    private void InitProfile(){
        RelativeLayout header = (RelativeLayout) mScrollView.findViewById(R.id.header);

        TextView nameText = (TextView) header.findViewById(R.id.txt_profile_header);
        final ImageView profileImage = (ImageView) header.findViewById(R.id.img_profile_header);

        String name = ParseUser.getCurrentUser().getUsername();
        name = name.substring(0, 1).toUpperCase() + name.substring(1);
        nameText.setText(name);

        ParseFile thumb = (ParseFile) ParseUser.getCurrentUser().get("thumbnail");

        if(thumb != null){
            thumb.getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] bytes, ParseException e) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    profileImage.setImageBitmap(bitmap);
                }
            });
        }

        //--Load the games
        HashMap<String, Object> params = new HashMap<>();
        params.put("userID", ParseUser.getCurrentUser().getObjectId());

        ParseCloud.callFunctionInBackground("GetProfile", params,
                new FunctionCallback<Map<String, Object>>() {
                    @Override
                    public void done(Map<String, Object> mapObject, ParseException e) {
                        if(e == null){
                            boolean isError = mapObject.get("error").toString().equals("true");

                            if(!isError) {
                                OnReceiveGames(mapObject);
                            }else{
                                //TODO handle errors
                            }
                        }else{
                            //TODO handle errors
                        }
                    }
                });
    }

    private void OnReceiveGames(Map<String, Object> mapObject){
        ArrayList<HashMap<String, Object>> completed = (ArrayList<HashMap<String, Object>>) mapObject.get("completed");
        ArrayList<HashMap<String, Object>> inProgress = (ArrayList<HashMap<String, Object>>) mapObject.get("inProgress");

        //--Get ListViews
        LinearLayout compList = (LinearLayout) findViewById(R.id.list_profile_completed);
        LinearLayout progList = (LinearLayout) findViewById(R.id.list_profile_progress);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mCompLoader.setVisibility(View.GONE);
        mProgLoader.setVisibility(View.GONE);
        InflateGameList(completed, compList, inflater);
        InflateGameList(inProgress, progList, inflater);
    }

    private void InflateGameList(ArrayList<HashMap<String, Object>> list, LinearLayout listView, LayoutInflater inflater){
        TextView v;
        if(list.size() == 0){
            v = (TextView) inflater.inflate(R.layout.profile_game_list, null);
            v.setText(getResources().getString(R.string.txt_no_games));

            listView.addView(v);
        }else{
            for(HashMap<String, Object> o:list){
                v = (TextView) inflater.inflate(R.layout.profile_game_list, null);

                SpannableString content = new SpannableString(o.get("caption").toString());
                content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                v.setText(content);
                v.setTag(o.get("id").toString());

                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OnGameClick(v.getTag().toString());
                    }
                });


                listView.addView(v);
            }
        }
    }

    private void OnGameClick(String id){
        Intent intent = new Intent(this, PaperViewActivity.class);
        intent.putExtra("gameID", id);

        startActivity(intent);
    }
}
