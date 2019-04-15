package com.example.rcjoshi.listviewandcheckbox;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> selItems = new ArrayList<>();
    ListView mListV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListV = (ListView) findViewById(R.id.checkable_list);
        mListV.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        ArrayList<String> mItemsA = new ArrayList<String>();
        mItemsA.add("English");
        mItemsA.add("French");
        mItemsA.add("Arabic");
        mItemsA.add("Hindi");
        //String[] mItems = {"English","French","Arabic","Hindi"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), R.layout.rowlayout, R.id.txt_lan,mItemsA);
        mListV.setAdapter(adapter);
        mListV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String mSelItems = ((TextView)view).getText().toString();
                if (selItems.contains(mSelItems))
                {
                    selItems.remove(mSelItems);
                }
                else
                {
                    selItems.add(mSelItems);
                }
            }
        });
    }

    public void showSelectedItems (View view)
    {
        String mitems = "";
        for (String mEachitem:selItems)
        {
            mitems+="-"+mEachitem+"\n";
        }
        Toast.makeText(this, "You have selected \n"+mitems, Toast.LENGTH_SHORT).show();
    }
}
