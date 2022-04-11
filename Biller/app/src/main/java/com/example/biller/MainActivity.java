package com.example.biller;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner s = (Spinner) findViewById(R.id.spinner);
        Button add_to_cart = (Button)findViewById(R.id.button);
        Button clear_cart = (Button) findViewById(R.id.button2);
        Button view_cart = (Button) findViewById(R.id.button3);
        EditText amount = (EditText) findViewById(R.id.editTextNumberDecimal2);

        add_to_cart.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view) {
                double cur_price = 0;
                try {
                    cur_price = Double.parseDouble(amount.getText().toString());
                }
                catch(Exception e) {
                    cur_price = 0;
                }

                double qty = Double.parseDouble(s.getSelectedItem().toString());
                double qty_amount = cur_price*qty;
                String text = cur_price+"   X   "+qty+"   =   "+round_off(qty_amount)+"\n";
                write_data(read_data("cart_data")+text , "cart_data");
                double tot_amount;
                if (read_data("total_amount") == "") tot_amount = 0.0;
                else tot_amount = Double.parseDouble(read_data("total_amount"));

                tot_amount += qty_amount;

                write_data(round_off(tot_amount)+"","total_amount");
                show_message("Added to cart !");
                show_box(read_data("cart_data")+"\n\n"+"Total : "+read_data("total_amount"));

            }
        });
        clear_cart.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                write_data("","cart_data");
                write_data("","total_amount");
                show_message("Data cleared !");
            }
        });

        view_cart.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                show_box(read_data("cart_data")+"\n\n"+"Total : "+read_data("total_amount"));
            }
        });
        String qty[] = {"1","2","3","4","5"};
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item, qty);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(aa);
    }

    public String read_data(String filename)
    {
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String s1 = sh.getString(filename, "");
        return s1;
    }
    public void write_data(String data,String filename)
    {
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putString(filename, data);
        myEdit.commit();
    }
    public void show_message(String s) {
        // Error due to file writing and other operations
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
    }

    public void show_box(String s) {
        // Error due to file writing and other operations
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(s);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    public double round_off(Double n){
        BigDecimal bd = new BigDecimal(n).setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}