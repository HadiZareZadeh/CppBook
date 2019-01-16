package com.att.cppbook;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.HashMap;
import java.util.List;

public class tblOfContent extends AppCompatActivity
{

    private List<HashMap<String , Object>> books_list;

    private DatabaseHandler db;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tbl_of_content);

        //getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        ListView contentListView = (ListView) findViewById(R.id.tblOfContentListView);

        db = new DatabaseHandler( getBaseContext() );

        db.open();

        books_list = db.getTableOfContent();

        String[] from = { "title" , "author" , "fav_flag" , "see_flag" };

        int[] to = { R.id.txtTitle , R.id.txtAuthorContent , R.id.setFav , R.id.setSee };

        SimpleAdapter adb = new SimpleAdapter(
            getBaseContext() , books_list , R.layout.tbl_content_list_row , from , to
        );

        contentListView.setAdapter( adb );

        db.close();

        contentListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id)
                    {
                        Intent i = new Intent( getBaseContext() , book_content.class );

                        String my_id = books_list.get( position ).get( "id" ).toString();

                        i.putExtra( "id" , my_id );

                        startActivity(i);

                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    }
                }
        );
    }



    public void onBtnBackClick( View v )
    {
        finish();

        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
