package com.example.android.inventory;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.inventory.data.InventoryContract.ProductEntry;
import com.example.android.inventory.data.InventoryDbHelper;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>  {
    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final int PRODUCT_LOADER = 0;
    private InventoryDbHelper mDbHelper;
    private InventoryCursorAdapter mCursorAdapter;
    private FloatingActionButton mAddButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAddButton = (FloatingActionButton) findViewById(R.id.fab);
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        mDbHelper = new InventoryDbHelper(this);

        mCursorAdapter = new InventoryCursorAdapter(this, null);
        ListView listView = (ListView) findViewById(R.id.list);
        View emptyView = findViewById(R.id.empty_view);
        listView.setEmptyView(emptyView);
        listView.setAdapter(mCursorAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                intent.setData(ContentUris.withAppendedId(ProductEntry.CONTENT_URI, id));
                startActivity(intent);

            }
        });
        getLoaderManager().initLoader(PRODUCT_LOADER, null, this);


    }

    private Cursor queryData() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = {
                ProductEntry._ID,
                ProductEntry.COLUMN_PROD_NAME,
                ProductEntry.COLUMN_PROD_PRICE,
                ProductEntry.COLUMN_PROD_NUM,
                ProductEntry.COLUMN_PROD_SUPPLIER
        };

        Cursor cursor = db.query(ProductEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null);
        Log.i(LOG_TAG, "There are " +cursor.getCount()+" data in database");
        return cursor;
    }

    private void insertData() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(ProductEntry.COLUMN_PROD_NAME, "七龙珠");
        values.put(ProductEntry.COLUMN_PROD_PRICE, 25);
        values.put(ProductEntry.COLUMN_PROD_NUM, 100);
        values.put(ProductEntry.COLUMN_PROD_SUPPLIER, "集英社");
        values.put(ProductEntry.COLUMN_PROD_SUPPLIER_EMAIL, "info@mangacapsule.jp");

        db.insert(ProductEntry.TABLE_NAME, null, values);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = new String[]{
                ProductEntry._ID,
                ProductEntry.COLUMN_PROD_NAME,
                ProductEntry.COLUMN_PROD_SUPPLIER,
                ProductEntry.COLUMN_PROD_NUM
        };
        CursorLoader loader = new CursorLoader(this,
                ProductEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.i(LOG_TAG, "There are "+ (data == null) + " in db.");
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_delete_all:
                deleteAll();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteAll() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Dialog：");
        builder.setMessage(getString(R.string.confirm_to_delete_all));
        builder.setIcon(R.mipmap.ic_launcher_round);
        builder.setCancelable(false);
        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getContentResolver().delete(ProductEntry.CONTENT_URI, null, null);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
