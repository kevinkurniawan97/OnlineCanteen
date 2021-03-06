package com.example.asus.onlinecanteen.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.example.asus.onlinecanteen.model.Store;
import com.example.asus.onlinecanteen.model.Transaction;
import com.example.asus.onlinecanteen.utils.AccountUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;

import java.util.ArrayList;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by Rickhen Hermawan on 15/03/2018.
 */

public class QrActivity extends Activity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    private String result, value;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    ArrayList<Transaction> transactionHistory;
    int pos;
    String id;
    private Store store;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);


        Intent intent = getIntent();
        if(intent.hasExtra("Location"))
        {
            value = intent.getStringExtra("Location");
            if(value.equals("order"))
            {
                pos = intent.getIntExtra("Position", 0);
                transactionHistory =(ArrayList<Transaction>) intent.getSerializableExtra("Transaction");
            }

        }

    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        Log.v("TAG", rawResult.getText()); // Prints scan results
        Log.v("TAG", rawResult.getBarcodeFormat().toString());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Scan Result");
        builder.setMessage("SCAN SUCCESS");
        result = rawResult.getText();
        //AlertDialog alert1 = builder.create();
        //alert1.show();
        id = rawResult.getText();
        DatabaseReference databaseStore = FirebaseDatabase.getInstance().getReference("store").child(id);
        databaseStore.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                store = dataSnapshot.getValue(Store.class);
                Log.d("QrActivity", "Store is " + store.getStoreId());
                store.setStoreId(id);
                AccountUtil.setCurrentAccount(store);
                Intent intent = new Intent(QrActivity.this, UserStoreProductActivity.class);
                intent.putExtra(UserStoreProductActivity.CURRENT_STORE_KEY, store);
                startActivity(intent);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
//        if (value.equals("order")) {
//            Intent i = new Intent();
//            i.putExtra("result", result);
//            i.putExtra("Transaction", transactionHistory);
//            i.putExtra("Position", pos);
//            setResult(RESULT_OK, i);
//            finish();
//
//
//            //mScannerView.resumeCameraPreview(this);
//        }

    }
}
