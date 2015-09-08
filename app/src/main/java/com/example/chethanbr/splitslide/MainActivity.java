package com.example.chethanbr.splitslide;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements UpdateSlideListener {

    EditText etTotalAmount, etCredit, etQR, etRecharge;
    SplitbarView splitView = null;
    int getCredit;
    Button imageUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("Activity", "Inside onCreate Method");
        setContentView(R.layout.activity_main);

        init();
        updateViews();

    }

    private void updateViews() {
        etTotalAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0 && etTotalAmount.getText().toString().length() != 0) {


                    int baseAmount = Integer.parseInt(etTotalAmount.getText().toString());
                    if (baseAmount > 0) {
                        splitView.updateBar(0, 0, 0, baseAmount);

                    }

                    etCredit.setText(s);
                    getCredit = Integer.parseInt(etCredit.getText().toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etQR.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() != 0 && s.length() >= 0 && etRecharge.getText().toString().length() != 0) {
                    int getRecharge = Integer.parseInt(etRecharge.getText().toString());
                    int updateCredit =
                            getCredit - getRecharge - Integer.parseInt(s.toString());

                    etCredit.setText(String.valueOf(updateCredit));

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etRecharge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {

                    int updateCredit = getCredit - Integer.parseInt(s.toString());

                    etCredit.setText(String.valueOf(updateCredit));

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        imageUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etCredit.getText().toString().length() != 0 && etRecharge.getText().toString().length() != 0 && etQR.getText().toString().length() != 0 && etTotalAmount.getText().toString().length() != 0) {
                    int creditValue = Integer.parseInt(etCredit.getText().toString());
                    int rechargeValue = Integer.parseInt(etRecharge.getText().toString());
                    int qrValue = Integer.parseInt(etQR.getText().toString());
                    int totalAmount = Integer.parseInt(etTotalAmount.getText().toString());
                    splitView.updateBar(qrValue, rechargeValue, creditValue, totalAmount);
                } else {
                    Toast.makeText(getApplicationContext(), "Please fill the data...", Toast.LENGTH_LONG).show();
                }
            }
        });

    }


    private void init() {
        etTotalAmount = (EditText) findViewById(R.id.totalamountet);
        etCredit = (EditText) findViewById(R.id.creditet);
        etQR = (EditText) findViewById(R.id.qret);
        etRecharge = (EditText) findViewById(R.id.rechargeet);
        imageUpdate = (Button) findViewById(R.id.imageupdatebtn);
        splitView = (SplitbarView) findViewById(R.id.splitbar);
        splitView.setUpdateSlideListener(this);
        splitView.setETtoUpdate(etCredit,etRecharge,etQR);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_settings) {
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void getData(double qrAmount, double rechargeAmount, double creditAmount, double baseAmount, int selectedThumb) {
        Log.i("Activity", "Inside getData Method in Activity");
        Log.i("CHAKRI", "qramount:" + qrAmount + "rechargeamount: " + rechargeAmount + "creditamount:" + creditAmount + "Selectwed thumb:" + selectedThumb);
    /*   if (selectedThumb == 2) {
            etRecharge.setText(String.valueOf((int) rechargeAmount));
            etQR.setText(String.valueOf((int) qrAmount));
       }  else if (selectedThumb == 1) {
            etCredit.setText(String.valueOf((int) creditAmount));
            etRecharge.setText(String.valueOf((int) rechargeAmount));
       }*/


        splitView.updateBar((int) qrAmount, (int) rechargeAmount, (int) creditAmount, (int) baseAmount);
    }
}
