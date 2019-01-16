package com.att.cppbook;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class settings extends AppCompatActivity
{
    private DatabaseHandler db;

    private ImageView sound , screen;
    private TextView txtSound , txtScreen , txtFont, txtAutoScroll;



    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        db = new DatabaseHandler( getBaseContext() );

        sound = (ImageView) findViewById(R.id.imgSoundState);
        screen = (ImageView) findViewById(R.id.imgScreenState);

        txtSound = (TextView) findViewById(R.id.txtSoundState);
        txtScreen = (TextView) findViewById(R.id.txtScreenState);
        txtFont = (TextView) findViewById(R.id.txtFontSize);


        db.open();

        if( db.getSoundState() == 1 ) {
            sound.setImageResource( R.drawable.sound_play );
            txtSound.setText( R.string.sound_settings_on );
        } else {
            sound.setImageResource( R.drawable.sound_stop );
            txtSound.setText( R.string.sound_settings_off );
        }

        if( db.getScreenState() == 1 ) {
            screen.setImageResource( R.drawable.screen_on );
            txtScreen.setText( R.string.screen_light_settings_on );
        } else {
            screen.setImageResource( R.drawable.screen_off );
            txtScreen.setText( R.string.screen_light_settings_general );
        }

        int font_size = db.getFontSize();
        txtFont.setText(
                String.valueOf(
                        font_size + " " +
                        getString(R.string.font_size_settings_sample)
                )
        );
        txtFont.setTextSize( font_size );

        db.close();
    }

    public void onImgSoundStateClick( View v )
    {
        db.open();

        if( db.getSoundState() == 1 )
        {
            if( db.setSoundState(0) ) {
                sound.setImageResource( R.drawable.sound_stop );
                txtSound.setText( R.string.sound_settings_off );
            }
        }
        else
        {
            if( db.setSoundState(1) ) {
                sound.setImageResource( R.drawable.sound_play );
                txtSound.setText( R.string.sound_settings_on );
            }
        }

        db.close();
    }

    public void onImgScreenStateClick( View v )
    {
        db.open();

        if( db.getScreenState() == 1 )
        {
            if( db.setScreenState(0) ) {
                screen.setImageResource( R.drawable.screen_off );
                txtScreen.setText( R.string.screen_light_settings_general );
            }
        }
        else
        {
            if( db.setScreenState(1) ) {
                screen.setImageResource( R.drawable.screen_on );
                txtScreen.setText( R.string.screen_light_settings_on );
            }
        }

        db.close();
    }

    public void onImgFontSizeUpClick( View v )
    {
        db.open();

        int size = db.getFontSize();

        if( db.setFontSize( size + 1 ) ) {
            size ++;
            txtFont.setText(
                    String.valueOf( size ) + " " +
                            getString(R.string.font_size_settings_sample)
            );
            txtFont.setTextSize( size );
        }

        db.close();
    }

    public void onImgFontSizeDownClick( View v )
    {
        db.open();

        int size = db.getFontSize();

        if( db.setFontSize( size - 1 ) ) {
            size --;
            txtFont.setText(
                    getString(R.string.font_size_settings_sample) + " " +
                            String.valueOf( size )
            );
            txtFont.setTextSize( size );
        }

        db.close();
    }

    public void onBtnBackClick( View v )
    {
        finish();

        overridePendingTransition(R.anim.fade_in , R.anim.fade_out);
    }
}
