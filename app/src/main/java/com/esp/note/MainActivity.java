package com.esp.note;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public final  static int REQUEST_CODE = 1;

    private RecyclerView recyclerView;
    private ItemAdapter itemAdapter;
    private SearchView searchView;
    private Button addButton;

    Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hideStatusBar();

        init();
        initDb();


    }

    private void hideStatusBar() {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void initDb() {
        database = new Database(this,"abc",null,1);
        database.createTable();
        Cursor cursor = database.query();
        cursor.moveToFirst();
        String title = "";
        String content = "";
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            title = cursor.getString(1);
            content = cursor.getString(2);
            itemAdapter.add(new Item(id, title, content, R.drawable.bg_blue));
        }
    }

    private void init() {
        addButton = (Button) findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditActivity.class);

                database.insertRecord("New Note", "");
                Cursor cursor = database.query();
                cursor.moveToLast();

                int id = cursor.getInt(0);
                Item item = new Item(id, "New Note", "", R.drawable.bg_blue);
                Bundle bundle = new Bundle();
                bundle.putString("title", item.getTitle());
                bundle.putString("desc", item.getContent());
                bundle.putInt("bg",item.getBackgroundResId());
                bundle.putInt("id", item.getId());
                int position = itemAdapter.getItemCount();
                bundle.putInt("pos", position);
                intent.putExtra("item", bundle);
                startActivityForResult(intent, REQUEST_CODE);
                itemAdapter.add(item);

                itemAdapter.notifyDataSetChanged();
            }
        });
        searchView = (SearchView) findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        itemAdapter = new ItemAdapter(this);
        recyclerView.setAdapter(itemAdapter);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        overridePendingTransition(R.anim.anim_intent, R.anim.anim_intent_out);
    }


    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.anim_intent, R.anim.anim_intent_out);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (data != null) {
                Bundle bundle = data.getBundleExtra("data");
                String title = bundle.getString("title");
                String content = bundle.getString("desc");
                int backgroundResId = bundle.getInt("bg");
                int position = bundle.getInt("pos");
                Item item = itemAdapter.getItem(position);
                item.setTitle(title);
                item.setContent(content);
                item.setBackgroundResId(backgroundResId);
                int id = bundle.getInt("id");
                itemAdapter.notifyDataSetChanged();
                database.updateRecord(id+"", title, content);

            }
        }
    }
}
