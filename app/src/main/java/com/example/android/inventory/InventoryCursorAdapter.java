package com.example.android.inventory;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.inventory.data.InventoryContract.ProductEntry;
import com.example.android.inventory.utils.ToastUtil;

public class InventoryCursorAdapter extends CursorAdapter{
    public InventoryCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        final TextView textViewName = (TextView) view.findViewById(R.id.name);
        final TextView textViewSupplier = (TextView) view.findViewById(R.id.supplier);

        final long id = cursor.getInt(cursor.getColumnIndex(ProductEntry._ID));
        final int currentNum = cursor.getInt(cursor.getColumnIndex(ProductEntry.COLUMN_PROD_NUM));

        textViewName.setText(cursor.getString(cursor.getColumnIndex(ProductEntry.COLUMN_PROD_NAME)));
        textViewSupplier.setText(currentNum+"");

        Button btnBuy = (Button) view.findViewById(R.id.buy);



        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentNum = Integer.parseInt(textViewSupplier.getText().toString().trim());
                if (currentNum > 0){
                    currentNum --;
                    Uri currentItemUri = ContentUris.withAppendedId(ProductEntry.CONTENT_URI, id);
                    ContentValues values = new ContentValues();
                    values.put(ProductEntry.COLUMN_PROD_NUM, currentNum);
                    int rowsAffected = context.getContentResolver().update(currentItemUri, values, null, null);
                    if(rowsAffected == 0) {
                        ToastUtil.show(context, context.getString(R.string.some_error_with_buy));
                    } else {
                        ToastUtil.show(context, context.getString(R.string.has_buied_one_product));
                        textViewSupplier.setText(currentNum + "");
                    }
                } else {
                    ToastUtil.show(context, context.getString(R.string.buy_must_not_zero));
                }

            }
        });
    }


}
