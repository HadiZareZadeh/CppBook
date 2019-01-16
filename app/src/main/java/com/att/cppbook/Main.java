package com.att.cppbook;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class Main extends AppCompatActivity
{
    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        //getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        ImageButton ib = (ImageButton) findViewById(R.id.btnTblContent);
        ib.setMinimumWidth( getScreenWidth()/4 );
        ib = (ImageButton) findViewById(R.id.btnAboutMe);
        ib.setMinimumWidth( getScreenWidth()/4 );
        ib = (ImageButton) findViewById(R.id.btnContactMe);
        ib.setMinimumWidth( getScreenWidth()/4 );
        ib = (ImageButton) findViewById(R.id.btnExit);
        ib.setMinimumWidth( getScreenWidth()/4 );
        ib = (ImageButton) findViewById(R.id.btnFavorite);
        ib.setMinimumWidth( getScreenWidth()/4 );
        ib = (ImageButton) findViewById(R.id.btnSearch);
        ib.setMinimumWidth( getScreenWidth()/4 );
        ib = (ImageButton) findViewById(R.id.btnSettings);
        ib.setMinimumWidth( getScreenWidth()/4 );
        ib = (ImageButton) findViewById(R.id.btnQuestion);
        ib.setMinimumWidth( getScreenWidth()/4 );
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(getString(R.string.exit_title))
                .setMessage(getString(R.string.exit_message))
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

    public void onBtnTblContentClick( View v )
    {
        Intent i = new Intent(this , tblOfContent.class);
        startActivity(i);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void onBtnFavoriteClick( View v )
    {
        Intent i = new Intent(this , tblOfFavoriteBooks.class);
        startActivity(i);
        overridePendingTransition( R.anim.fade_in , R.anim.fade_out );
    }

    public void onBtnSearchClick( View v )
    {
        Intent i = new Intent(this , search.class);
        startActivity(i);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void onBtnSettingsClick( View v )
    {
        Intent i = new Intent(this , settings.class);
        startActivity(i);
        overridePendingTransition( R.anim.fade_in , R.anim.fade_out );
    }

    public void onBtnAboutMeClick( View v )
    {
        alert_me(
                getString(R.string.about_me_title) ,
                getString(R.string.about_me_message) ,
                true
        );
    }

    public void onBtnQuestionClick(View v)
    {
        Uri uri = Uri.parse( "http://www.instagram.com/_u/hadi.zare.zadeh/" );
        Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

        try {
            likeIng.setPackage("com.instagram.android");
            startActivity(likeIng);

        } catch (Exception e)
        {
            startActivity(likeIng);
        }
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void onBtnContactMeClick( View v )
    {
        alert_me(
                getString(R.string.contact_me_title) ,
                getString(R.string.contact_me_message) ,
                true
        );
    }

    public void onBtnExitClick( View v )
    {
        this.onBackPressed();
    }

    public void alert_me( String title , String message , boolean cancelable )
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setCancelable( cancelable );
        alert.setTitle(title);
        alert.setMessage(message);
        alert.create();
        alert.show();
    }
}
