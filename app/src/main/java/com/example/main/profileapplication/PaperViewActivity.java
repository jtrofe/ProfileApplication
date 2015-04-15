package com.example.main.profileapplication;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.ProgressCallback;


public class PaperViewActivity extends Activity{

    private String gameID;

    private boolean completed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paper_view);

        gameID = getIntent().getStringExtra("gameID");

        ParseQuery<ParseObject> gameObject = new ParseQuery<ParseObject>("ParsePaper");

        gameObject.getInBackground(gameID, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if(e == null){
                    OnPaperLoaded(parseObject);
                }
            }
        });
    }

    private void OnPaperLoaded(ParseObject paper){
        System.out.println(paper.getParseUser("user_0").getObjectId());

        String part_0_caption = paper.getString("part_0_caption");
        ParseFile part_0_drawing = paper.getParseFile("part_0_drawing");
        String part_1_caption = paper.getString("part_1_caption");
        ParseFile part_2_drawing = paper.getParseFile("part_2_drawing");
        String part_3_caption = paper.getString("part_3_caption");
        ParseFile part_4_drawing = paper.getParseFile("part_4_drawing");
        String part_5_caption = paper.getString("part_5_caption");
        ParseFile part_6_drawing = paper.getParseFile("part_6_drawing");

        TextView text0 = (TextView) findViewById(R.id.part_0_caption);
        ImageView draw0 = (ImageView) findViewById(R.id.part_0_drawing);
        TextView text1 = (TextView) findViewById(R.id.part_1_caption);
        ImageView draw2 = (ImageView) findViewById(R.id.part_2_drawing);
        TextView text3 = (TextView) findViewById(R.id.part_3_caption);
        ImageView draw4 = (ImageView) findViewById(R.id.part_4_drawing);
        TextView text5 = (TextView) findViewById(R.id.part_5_caption);
        ImageView draw6 = (ImageView) findViewById(R.id.part_6_drawing);

        LoadCaption(text0, part_0_caption);
        LoadDrawing(draw0, part_0_drawing);

        if(part_1_caption == null) return;
        LoadCaption(text1, part_1_caption);

        if(part_2_drawing == null) return;
        LoadDrawing(draw2, part_2_drawing);

        if(part_3_caption == null) return;
        LoadCaption(text3, part_3_caption);

        if(part_4_drawing == null) return;
        LoadDrawing(draw4, part_4_drawing);

        if(part_5_caption == null) return;
        LoadCaption(text5, part_5_caption);

        if(part_6_drawing == null) return;
        LoadDrawing(draw6, part_6_drawing);
    }

    private void LoadCaption(TextView v, String caption){
        v.setText(caption);
        v.setVisibility(View.VISIBLE);
    }

    private void LoadDrawing(ImageView v, ParseFile f){
        f.getDataInBackground(new ImageCallback(v));
    }

    private class ImageCallback implements GetDataCallback{
        private ImageView mView;

        public ImageCallback(ImageView imageView){
            mView = imageView;
        }

        @Override
        public void done(byte[] bytes, ParseException e) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            mView.setImageBitmap(bitmap);
            mView.setVisibility(View.VISIBLE);
        }
    }
}
