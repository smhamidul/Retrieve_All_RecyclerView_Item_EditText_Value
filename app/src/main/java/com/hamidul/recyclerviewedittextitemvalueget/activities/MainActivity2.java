package com.hamidul.recyclerviewedittextitemvalueget.activities;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hamidul.recyclerviewedittextitemvalueget.R;
import com.hamidul.recyclerviewedittextitemvalueget.model.Order;

import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {

    public static ArrayList<Order> arrayList;
    ArrayList<Order> orders;
    RecyclerView recyclerView;
    MyAdapter myAdapter;
    LinearLayout totalAmountLayout, discountAmountLayout, netAmountLayout;
    TextView tvTotalAmount, tvDiscountAmount, tvNetAmount;
    double sum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_main2);
        recyclerView = findViewById(R.id.recyclerViw);
        totalAmountLayout = findViewById(R.id.totalAmountLayout);
        discountAmountLayout = findViewById(R.id.discountAmountLayout);
        netAmountLayout = findViewById(R.id.netAmountLayout);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        tvDiscountAmount = findViewById(R.id.tvDiscountAmount);
        tvNetAmount = findViewById(R.id.tvNetAmount);

        orders = new ArrayList<>();
        for (Order item : arrayList){
            if (!item.getQuantity().isEmpty()){
                orders.add(item);
            }
        }

        if (orders.size()>1){
            getSupportActionBar().setTitle("Your Order ( "+orders.size()+" SKU's )");
        }
        else {
            getSupportActionBar().setTitle("Your Order ( "+orders.size()+" SKU )");
        }

        sum = 0;
        for (int i=0; i<orders.size(); i++){
            int quantity = Integer.parseInt(orders.get(i).getQuantity());
            double tp = orders.get(i).getTp();
            double product = quantity*tp;
            sum = sum + product;
        }

        tvTotalAmount.setText(String.format("%.2f",sum));

        totalAmountLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                discountAmountLayout.setVisibility(View.GONE);
                netAmountLayout.setVisibility(View.GONE);

                View view = LayoutInflater.from(MainActivity2.this).inflate(R.layout.price_dialog,null);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity2.this);
                builder.setView(view);

                Button DP = view.findViewById(R.id.DP);
                Button TP = view.findViewById(R.id.TP);

                final AlertDialog dialog = builder.create();

                DP.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sum = 0;
                        for (int i=0; i<orders.size(); i++){
                            int quantity = Integer.parseInt(orders.get(i).getQuantity());
                            int mrp = orders.get(i).getPrice();
                            int product = quantity*mrp;
                            sum = sum + product;
                        }
                        double totalTp = sum/1.15;
                        double totalDp = totalTp/1.05;
                        tvTotalAmount.setText(String.format("%.2f",totalDp));

                        dialog.cancel();
                    }
                });

                TP.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sum = 0;
                        for (int i=0; i<orders.size(); i++){
                            int quantity = Integer.parseInt(orders.get(i).getQuantity());
                            int mrp = orders.get(i).getPrice();
                            int product = quantity*mrp;
                            sum = sum + product;
                        }
                        double totalTp = sum/1.15;
                        tvTotalAmount.setText(String.format("%.2f",totalTp));

                        dialog.cancel();
                    }
                });

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                return false;
            }
        });

        myAdapter = new MyAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        /**DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,linearLayoutManager.getOrientation());*/
        recyclerView.setLayoutManager(linearLayoutManager);
        /**recyclerView.addItemDecoration(dividerItemDecoration);*/
        recyclerView.setAdapter(myAdapter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.myViewHolder>{

        @NonNull
        @Override
        public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = getLayoutInflater();
            return new myViewHolder(layoutInflater.inflate(R.layout.item_order,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull myViewHolder holder, int position) {

            holder.productName.setText(orders.get(position).getName());
            holder.multiply.setText( orders.get(position).getQuantity()+" * "+String.format("%.0f",orders.get(position).getTp()) );
            holder.tvUnit.setText(String.format("%.0f",Double.parseDouble(orders.get(position).getQuantity())*orders.get(position).getTp()));

        }

        @Override
        public int getItemCount() {
            return orders.size();
        }

        public class myViewHolder extends RecyclerView.ViewHolder{
            TextView productName,tvUnit,multiply;
            public myViewHolder(@NonNull View itemView) {
                super(itemView);
                productName = itemView.findViewById(R.id.productName);
                tvUnit = itemView.findViewById(R.id.tvUnit);
                multiply = itemView.findViewById(R.id.multiply);

            }
        }

    }

}