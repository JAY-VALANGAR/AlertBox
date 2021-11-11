package com.example.alertbox;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // UI widgets button and
        Button bOpenAlertDialog = findViewById(R.id.openAlertDialogButton);
        final TextView tvSelectedItemsPreview = findViewById(R.id.selectedItemPreview);

        // initialise the list items for the alert dialog
        final String[] listItems = new String[]{"1", "2", "3", "4","5", "6", "7", "8","9", "10", "11", "12","13", "14", "15", "16","17", "18", "19", "20"};
        final boolean[] checkedItems = new boolean[listItems.length];

        // copy the items from the main list to the selected item list
        // for the preview if the item is checked then only the item
        // should be displayed for the user
        final List<String> selectedItems = Arrays.asList(listItems);

        // handle the Open Alert Dialog button
        bOpenAlertDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // initially set the null for the text preview
                tvSelectedItemsPreview.setText(null);

                // initialise the alert dialog builder
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                // set the title for the alert dialog
                builder.setTitle("Choose Items");

                // set the icon for the alert dialog
//                builder.setIcon(R.drawable.image_logo);

                // now this is the function which sets the alert dialog for multiple item selection ready
                builder.setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        checkedItems[which] = isChecked;

                        saveArray(checkedItems,getApplicationContext());

                        String currentItem = selectedItems.get(which);
                    }
                });

                // alert dialog shouldn't be cancellable
                builder.setCancelable(false);

                // handle the positive button of the dialog
                builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i = 0; i < checkedItems.length; i++) {
                            if (checkedItems[i]) {
//                                tvSelectedItemsPreview.setText(tvSelectedItemsPreview.getText() + selectedItems.get(i) + ", ");
                            }
                            tvSelectedItemsPreview.setText(i+") "+tvSelectedItemsPreview.getText()+" "+checkedItems[i]);

                        }

                        getArray(getApplicationContext());

                    }
                });

                // handle the negative button of the alert dialog
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                // handle the neutral button of the dialog to clear
                // the selected items boolean checkedItem
                builder.setNeutralButton("CLEAR ALL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i = 0; i < checkedItems.length; i++) {
                            checkedItems[i] = false;
                        }
                    }
                });

                // create the builder
                builder.create();

                // create the alert dialog with the
                // alert dialog builder instance
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }


    public static void saveArray(boolean[] sArray, Context context)
    {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor mEdit1 = sp.edit();
        /* sKey is an array */
        mEdit1.putInt("Status_size", sArray.length);

        for(int i=0;i<sArray.length;i++)
        {
            mEdit1.remove("Status_" + i);
            mEdit1.putString("Status_" + i, ""+sArray[i]);
        }

        mEdit1.commit();
    }

    ArrayList<String> sKey = new ArrayList<>();
    public void getArray(Context mContext)
    {
        SharedPreferences mSharedPreference1 =   PreferenceManager.getDefaultSharedPreferences(mContext);
        sKey.clear();
        int size = mSharedPreference1.getInt("Status_size", 0);

        for(int i=0;i<size;i++)
        {
            sKey.add(mSharedPreference1.getString("Status_" + i, null));
        }

        for(int i=0;i<sKey.size();i++)
        {
            Log.i("values_array", i+") "+sKey.get(i));
        }

    }


}