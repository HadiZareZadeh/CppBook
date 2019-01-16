package com.att.cppbook;

import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import org.jsoup.Jsoup;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;

import java.io.File;
import java.util.HashMap;


public class book_content extends AppCompatActivity
{
    private DatabaseHandler db;

    private HashMap<String , Object> book;

    public TextView date;
    private WebView content;
    private ImageView fav , visit;

    private ImageView playingState;
    private boolean t1State = false;
    private TextToSpeech t1;


    private void start_voice()
    {
        try {
            t1.speak(gettext(getDetails("content")),TextToSpeech.QUEUE_FLUSH,null);
            t1State = true;
            String msg = "Voice Started";
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }
        catch (Exception ex){
            String msg = "Something's wrong";
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }
    }
    private void stop_voice()
    {
        String msg;
        if(t1 !=null) {
            t1.stop();
        }
        msg = "Voice has been Paused";
        Toast.makeText(getApplicationContext(), msg,Toast.LENGTH_SHORT).show();
        t1State = false;
    }

    private String getDetails(String detail){
       return book.get(detail).toString();
    }

    @Override
    public void onPause(){
        if(t1State)
            stop_voice();
        super.onPause();
    }
    @Override
    public void onResume(){
        if(t1State)
            start_voice();
        super.onPause();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_content);

        //getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        t1State= false;

        db = new DatabaseHandler( getBaseContext() );

        Bundle data = getIntent().getExtras();

        db.open();

        book = db.getBookContent( data.getString("id") );

        TextView title = (TextView) findViewById(R.id.txtBookTitle);
        TextView author = (TextView) findViewById(R.id.txtBookAuthor);
        date = (TextView) findViewById(R.id.txtBookDate);
        content = (WebView) findViewById(R.id.WebViewBookContent);
        fav = (ImageView) findViewById(R.id.imgFavorite);
        visit = (ImageView) findViewById(R.id.imgSee);
        playingState = (ImageView) findViewById(R.id.PlayingState);


        try
        {
            t1=new TextToSpeech(this, new TextToSpeech.OnInitListener() {

                @Override
                public void onInit(int status) {
                    // TODO Auto-generated method stub
                    if(status == TextToSpeech.SUCCESS){
                        t1.setLanguage(Locale.US);
                    }
                    else
                        Log.e("error", "Initilization Failed!");
                }
            });
        }
        catch (Exception e)
        {
            playingState.setEnabled(false);
            String msg = "Voice does not work on your phone";
            Toast.makeText(getApplicationContext(), msg,Toast.LENGTH_SHORT).show();
        }

        title.setText( getDetails("title") );
        title.setTypeface(Typeface.DEFAULT_BOLD);

        author.setText( getDetails("author") );
        title.setTypeface(Typeface.DEFAULT_BOLD);
        date.setText(getDetails("date") );
        title.setTypeface(Typeface.DEFAULT_BOLD);

        String font_size = String.valueOf( db.getFontSize() );

        String main_txt =
                "<html>" +
                    "<head></head>" +
                    "<body dir='ltr' style='font-size: " +
                        font_size + "px; text-align: justify;' >" +
                        "<span>" +
                        getDetails("content") +
                        "</span>" +
                    "</body>" +
                "</html>";

        content.loadDataWithBaseURL(
                null , main_txt , "text/html; charset=utf-8" , null , null
        );
        content.setBackgroundColor(0x00000000);

        fav.setImageResource(
                Integer.parseInt(getDetails("fav_flag"))
        );

        visit.setImageResource(
                Integer.parseInt( getDetails("see_flag") )
        );

        if( db.getScreenState() == 1 ) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }


        if( db.getSoundState() != 0 ) {
            t1State = true;
            start_voice();
            playingState.setImageResource(R.drawable.pause);
        }else{
            t1State = false;
            playingState.setImageResource(R.drawable.play);
        }

        db.close();
    }

    private String gettext(String text){
        text = Jsoup.parse(text).text();
        return  text;
    }

    public void onPlayingStateClick(View v)
    {
        if( t1State ){
            stop_voice();
            playingState.setImageResource(R.drawable.play);
        }
        else{
            start_voice();
            playingState.setImageResource(R.drawable.pause);
        }
    }

    public void onImgSeeClick( View v )
    {
        db.open();

        int id = Integer.parseInt( book.get("id").toString() );

        if( db.getBookVisitState(id) == 1 )
        {
            db.setBookVisitState( id , 0 );

            visit.setImageResource( R.drawable.not_see );
        }
        else
        {
            db.setBookVisitState( id , 1 );

            visit.setImageResource(R.drawable.see);
        }

        db.close();
    }

    public void onImgFavoriteClick( View v )
    {
        db.open();

        int id = Integer.parseInt( book.get("id").toString() );

        if( db.getBookFavoriteState(id) == 1 )
        {
            db.setBookFavoriteState( id , 0 );

            fav.setImageResource( R.drawable.not_favorite );
        }
        else
        {
            db.setBookFavoriteState( id , 1 );

            fav.setImageResource(R.drawable.is_favorite);
        }

        db.close();
    }

    public void onShareTextClick( View v )
    {
        Intent i = new Intent( Intent.ACTION_SEND );

        i.setType( "text/plain" );

        i.putExtra(Intent.EXTRA_SUBJECT, "subject");

        i.putExtra( Intent.EXTRA_TEXT , gettext(getDetails("content")));

        startActivity(Intent.createChooser(i, "sending"));

        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void onBtnBackClick( View v )
    {
        finish();

        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if(t1State)
            stop_voice();
    }
}
