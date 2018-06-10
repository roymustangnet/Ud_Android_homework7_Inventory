package com.example.android.inventory;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.android.inventory.data.InventoryContract.ProductEntry;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>  {
    private final int EDITOR_LOADER_ID = 0;

    private EditText mProductName;
    private EditText mProductPrice;
    private EditText mProductCount;
    private EditText mProductSupplier;
    private EditText mProductSupplierEmail;
    private EditText mProductSupplierTel;

    private TextView mMinusButton;
    private TextView mPlusButton;


    private Uri mCurrentProductUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        mProductName = (EditText) findViewById(R.id.edit_product_name);
        mProductPrice = (EditText) findViewById(R.id.edit_product_price);
        mProductCount = (EditText) findViewById(R.id.edit_product_count);
        mProductSupplier = (EditText) findViewById(R.id.edit_produce_supplier);
        mProductSupplierEmail = (EditText) findViewById(R.id.edit_produce_supplier_email);
        mProductSupplierTel = (EditText) findViewById(R.id.edit_produce_supplier_tel);

        mMinusButton = (TextView) findViewById(R.id.minus_count);
        mMinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String strCurrentCount = mProductCount.getText().toString();
            try {
                int intCurrentCount = Integer.parseInt(strCurrentCount);
                if(intCurrentCount > 0) {
                    intCurrentCount--;
                }
                mProductCount.setText(String.valueOf(intCurrentCount));
            } catch (Exception e) {
                mProductCount.setText("0");
            }
            }
        });

        mPlusButton = (TextView) findViewById(R.id.plus_count);
        mPlusButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
            String strCurrentCount = mProductCount.getText().toString();
            try {
                int intCurrentCount = Integer.parseInt(strCurrentCount);
                intCurrentCount ++;
                mProductCount.setText(String.valueOf(intCurrentCount));
            } catch (Exception e) {
                mProductCount.setText("0");
            }

            }
        });


        mCurrentProductUri = getIntent().getData();
        if(mCurrentProductUri != null){
            setTitle(getString(R.string.editor_activity_title_edit));
            getLoaderManager().initLoader(EDITOR_LOADER_ID, null, this);
        } else {
            setTitle(getString(R.string.editor_activity_title_add));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editor_menu, menu);
        if(mCurrentProductUri == null){
            menu.findItem(R.id.action_delete).setVisible(false);
            menu.findItem(R.id.action_order).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_save:
                if(insertProduct()) {
                    finish();
                }
                return true;
            case R.id.action_delete:
                deleteProduct();
                finish();
                return true;
            case R.id.action_order:
                contractSupplier();
                return true;
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // contract supplier with email or tel
    private void contractSupplier() {
        String reciver = mProductSupplierEmail.getText().toString().trim();
        if(!reciver.isEmpty()){
            Uri uri=Uri.parse("mailto:"+reciver);
            Intent MymailIntent=new Intent(Intent.ACTION_SENDTO,uri);
            startActivity(MymailIntent);
            return;
        }
        String phone = mProductSupplierTel.getText().toString().trim();
        if(!phone.isEmpty()){
            Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+phone));
            startActivity(intent);
            return;
        }
        Toast.makeText(this, getString(R.string.cannot_contract_supplier), Toast.LENGTH_SHORT).show();
    }

    private void deleteProduct() {

        int rowsAffected = getContentResolver().delete(mCurrentProductUri,null, null);
        if (rowsAffected == 1){
            Toast.makeText(this, getString(R.string.delete_success), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.delete_error), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean insertProduct() {

        String nameString = mProductName.getText().toString().trim();
        if(nameString.isEmpty()){
            Toast.makeText(this, getString(R.string.name_cannot_empty), Toast.LENGTH_SHORT).show();
            return false;
        }
        String priceString = mProductPrice.getText().toString().trim();
        if(priceString.isEmpty()){
            Toast.makeText(this, getString(R.string.price_cannot_empty), Toast.LENGTH_SHORT).show();
            return false;
        }
        String countString = mProductCount.getText().toString().trim();
        if(countString.isEmpty()){
            Toast.makeText(this, getString(R.string.count_cannot_empty), Toast.LENGTH_SHORT).show();
            return false;
        }
        String supplierString = mProductSupplier.getText().toString().trim();
        if(supplierString.isEmpty()){
            Toast.makeText(this, getString(R.string.supplier_cannot_empty), Toast.LENGTH_SHORT).show();
            return false;
        }
        String supplierEmailString = mProductSupplierEmail.getText().toString().trim();
        if(supplierEmailString.isEmpty()){
            Toast.makeText(this, getString(R.string.supplier_email_cannot_empty), Toast.LENGTH_SHORT).show();
            return false;
        }

        String supplierTelString = mProductSupplierTel.getText().toString().trim();
        if(supplierTelString.isEmpty()){
            Toast.makeText(this, getString(R.string.supplier_tel_cannot_empty), Toast.LENGTH_SHORT).show();
            return false;
        }

        ContentValues values = new ContentValues();
        values.put(ProductEntry.COLUMN_PROD_NAME, nameString);
        values.put(ProductEntry.COLUMN_PROD_PRICE, priceString);
        int count = 0;
        if (!TextUtils.isEmpty(countString)) {
            count = Integer.parseInt(countString);
        }
        values.put(ProductEntry.COLUMN_PROD_NUM, count);
        values.put(ProductEntry.COLUMN_PROD_SUPPLIER, supplierString);
        values.put(ProductEntry.COLUMN_PROD_SUPPLIER_EMAIL, supplierEmailString);
        values.put(ProductEntry.COLUMN_PROD_SUPPLIER_TEL, supplierTelString);
        if(mCurrentProductUri == null){
            // insert a new Product
            Uri newUri = getContentResolver().insert(ProductEntry.CONTENT_URI, values);
            if (newUri == null) {
                Toast.makeText(this, getString(R.string.editor_insert_prod_failed),
                        Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, getString(R.string.editor_insert_prod_successful),
                        Toast.LENGTH_SHORT).show();
            }
            return true;
        } else {
            // update a Product
            int rowsAffected = getContentResolver().update(mCurrentProductUri, values, null, null);
            if(rowsAffected == 0) {
                Toast.makeText(this, getString(R.string.editor_update_product_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_update_product_successful),
                        Toast.LENGTH_SHORT).show();
            }
            return true;
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                ProductEntry._ID,
                ProductEntry.COLUMN_PROD_NAME,
                ProductEntry.COLUMN_PROD_PRICE,
                ProductEntry.COLUMN_PROD_NUM,
                ProductEntry.COLUMN_PROD_SUPPLIER,
                ProductEntry.COLUMN_PROD_SUPPLIER_EMAIL,
                ProductEntry.COLUMN_PROD_SUPPLIER_TEL
        };
        return new CursorLoader(this,
                mCurrentProductUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data == null || data.getCount() < 1) {
            return;
        }
        if(data.moveToFirst()) {
            mProductName.setText(data.getString(data.getColumnIndex(ProductEntry.COLUMN_PROD_NAME)));
            mProductPrice.setText(Integer.toString(data.getInt(data.getColumnIndex(ProductEntry.COLUMN_PROD_PRICE))));
            mProductCount.setText(Integer.toString(data.getInt(data.getColumnIndex(ProductEntry.COLUMN_PROD_NUM))));
            mProductSupplier.setText(data.getString(data.getColumnIndex(ProductEntry.COLUMN_PROD_SUPPLIER)));
            mProductSupplierEmail.setText(data.getString(data.getColumnIndex(ProductEntry.COLUMN_PROD_SUPPLIER_EMAIL)));
            mProductSupplierTel.setText(data.getString(data.getColumnIndex(ProductEntry.COLUMN_PROD_SUPPLIER_TEL)));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mProductName.setText("");
        mProductPrice.setText("0");
        mProductCount.setText("0");
        mProductSupplier.setText("");
        mProductSupplierEmail.setText("");
        mProductSupplierTel.setText("");
    }
}
