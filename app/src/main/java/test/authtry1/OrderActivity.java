package test.authtry1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends AppCompatActivity implements View.OnClickListener {

    DatabaseReference databaseOrders;

    TextView textOrder;
    List<ItemInOrder> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        databaseOrders = FirebaseDatabase.getInstance().getReference("Orders");
        textOrder = findViewById(R.id.textView3);

        textOrder.setOnClickListener(this);
        itemList = new ArrayList<>();

    }


    public void createOrders(){


        for(int i =0;i<5;i++){
            itemList.add(new ItemInOrder("10000"+i,5-i));
        }
        String generatedOrderId = databaseOrders.push().getKey();
        Order temp = new Order(generatedOrderId,"100001","10001",250,true,itemList,125472);
        databaseOrders.child(generatedOrderId).setValue(temp);
        Toast.makeText(this,"Order Updated",Toast.LENGTH_SHORT).show();


    }




    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.textView3:
//                createOrders();
                break;

        }
    }
}
