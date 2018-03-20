package com.example.asus.onlinecanteen.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.asus.onlinecanteen.R;
import com.example.asus.onlinecanteen.adapter.AdminMenuAdapter;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MainActivityAdmin extends AppCompatActivity {

    int[] images = {R.drawable.logo, R.drawable.logo, R.drawable.logo};

    String[] name = {"User Top Up", "Store Withdrawal","Log Out"};

    String[] description = {"Untuk menambahkan saldo user", "Untuk membuat request store untuk menarik uang", "Log out sebagai admin"};

    String[] TAG = {"TOP_UP","WITHDRAW", "LOGOUT"};

    ListView lView;
    ListAdapter lAdapter;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);

        lView = (ListView) findViewById(R.id.androidList);

        lAdapter = new AdminMenuAdapter(MainActivityAdmin.this, name, description, images, TAG);

        lView.setAdapter(lAdapter);

        // Fetch the service account key JSON file contents
        FileInputStream serviceAccount = null;
        try {
            serviceAccount = new FileInputStream("path/to/serviceAccount.json");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent;
                switch (TAG[i]){
                    case "TOP_UP":
                        intent = new Intent(MainActivityAdmin.this, AdminTopUpActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case "WITHDRAW":
                        intent = new Intent(MainActivityAdmin.this, AdminStoreWithdrawal.class);
                        startActivity(intent);
                        finish();
                        break;
                    case "LOGOUT":
                        logout();

                    default: break;
                }

            }
        });

    }

    //User Logout
    public void logout(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.signOut_confirmation)
                .setCancelable(false)
                .setPositiveButton(R.string.signOut_confirm, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        firebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(MainActivityAdmin.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton(R.string.signOut_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.setTitle(R.string.signOut_title);
        alert.show();
    }
}
