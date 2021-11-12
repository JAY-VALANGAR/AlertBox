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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    String mapKey = "mapKey";

    // initialise the list items for the alert dialog
//    final String[] listItems = new String[]{"Anemia", "Appetite", "Arthritis", "AutoImmune", "Beh health", "Blood Pressure", "Brain", "Chronic Kidney Disease", "Circulation", "Diabetes", "Dialysis", "Eyes", "Fever", "GI", "Heart", "Heart Failure", "Immune Suppression", "Infection", "Kidney", "Lipid", "Liver", "Memory", "Migraine", "Nerves", "OTHER", "Pain", "Prostate", "Skin", "Stomach", "Transplant", "Ulcers", "Weight"};

    final String[] listItems = new String[]{"Heart Failure",
            "Immune Suppression",
            "Infection",
            "Kidney",
            "Lipid",
            "Liver",
            "Memory",
            "Anemia",
            "Fever",
            "Appetite",
            "Arthritis",
            "AutoImmune",
            "Beh health",
            "Blood Pressure",
            "Brain",
            "GI",
            "Heart",
            "Migraine",
            "Weight",
            "OTHER",
            "Pain",
            "Prostate",
            "Skin",
            "Stomach",
            "Transplant",
            "Ulcers",
            "Chronic Kidney Disease",
            "Circulation",
            "Diabetes",
            "Dialysis",
            "Eyes",
            "Nerves"};

    final boolean[] checkedItems = new boolean[listItems.length];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // UI widgets button and
        Button bOpenAlertDialog = findViewById(R.id.openAlertDialogButton);
        final TextView tvSelectedItemsPreview = findViewById(R.id.selectedItemPreview);



        // copy the items from the main list to the selected item list
        // for the preview if the item is checked then only the item
        // should be displayed for the user
        final List<String> selectedItems = Arrays.asList(listItems);

        // handle the Open Alert Dialog button
        bOpenAlertDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Map<String, Object> stringObjectMap = loadMap();

                    //----------------------------------------------------------------
                    Log.i("display_map", ""+stringObjectMap.size());
                    SharedPreferences pSharedPref = getApplicationContext().getSharedPreferences("MyVariables",
                            Context.MODE_PRIVATE);
                    Set keys = stringObjectMap.keySet();
                    for (Iterator i = keys.iterator(); i.hasNext(); ) {
                        String key = (String) i.next();
                        String jsonString = pSharedPref.getString(mapKey, (new JSONObject()).toString());
                        JSONObject jsonObject = new JSONObject(jsonString);
                        String value = (String) stringObjectMap.get(jsonObject.get(key));

                        int index = getArrayIndex(listItems, value);
                        checkedItems[index] = true;
                        
                        Log.i("display_map", i+") "+value);
                    }

                    //----------------------------------------------------------------
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // initially set the null for the text preview
//                tvSelectedItemsPreview.setText(null);

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
                        Map<String, Object> inputMap = new HashMap<>();
                        for (int i = 0; i < checkedItems.length; i++) {
                            if (checkedItems[i]) {
                                tvSelectedItemsPreview.setText(tvSelectedItemsPreview.getText() + selectedItems.get(i) + ", ");
                                inputMap.put(listItems[i], listItems[i]);
                            }
//                            tvSelectedItemsPreview.setText(i+") "+tvSelectedItemsPreview.getText()+" "+checkedItems[i]);
                        }
                        saveMap(inputMap);


                        saveArray(checkedItems,getApplicationContext());

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



//==================================================================================================
private void saveMap(Map<String, Object> inputMap) {
    SharedPreferences pSharedPref = getApplicationContext().getSharedPreferences("MyVariables",
            Context.MODE_PRIVATE);
    if (pSharedPref != null) {
        JSONObject jsonObject = new JSONObject(inputMap);
        String jsonString = jsonObject.toString();
        SharedPreferences.Editor editor = pSharedPref.edit();
        editor.remove(mapKey).apply();
        editor.putString(mapKey, jsonString);
        editor.commit();
    }
}
    private Map<String, Object> loadMap() throws JSONException {
        Map<String, Object> outputMap = new HashMap<>();
        SharedPreferences pSharedPref = getApplicationContext().getSharedPreferences("MyVariables",
                Context.MODE_PRIVATE);
        try {
            if (pSharedPref != null) {
                String jsonString = pSharedPref.getString(mapKey, (new JSONObject()).toString());
                JSONObject jsonObject = new JSONObject(jsonString);
                Iterator<String> keysItr = jsonObject.keys();
                while (keysItr.hasNext()) {
                    String key = keysItr.next();
                    outputMap.put(key, jsonObject.get(key));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return outputMap;
    }
//==================================================================================================


    //----------------------------------------------------------------------------------------------
    public int getArrayIndex(String[] arr,String value) {

        int k=0;
        for(int i=0;i<arr.length;i++){

            if(arr[i].equalsIgnoreCase(value)){
                k=i;
                break;
            }
        }
        return k;
    }
    //----------------------------------------------------------------------------------------------

}