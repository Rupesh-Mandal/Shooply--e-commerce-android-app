package com.firoz.shooply.user_dashboard.activity;

import static com.firoz.shooply.util.Constant.oderProduct;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firoz.shooply.R;
import com.firoz.shooply.checkout.adapter.PaymentListAdapter;
import com.firoz.shooply.model.CartModel;
import com.firoz.shooply.user_dashboard.UserDashboardActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class CheckOutActivity extends AppCompatActivity {

    TextView total;
    RecyclerView payment_recyclerView;
    Button cash_on_delivery;
    List<CartModel> cartModelList = new ArrayList<>();
    ProgressDialog progressDialog;
    SharedPreferences sharedpreferences;
    String userId,userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
//        sharedpreferences = getContext().getSharedPreferences("MyPREFERENCES", getContext().MODE_PRIVATE);
//        try {
//            JSONObject jsonObject = new JSONObject(sharedpreferences.getString("authUser", ""));
//            userId = jsonObject.getString("userId");
//            userName = jsonObject.getString("name");
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        total = view.findViewById(R.id.total);
//        cash_on_delivery = view.findViewById(R.id.cash_on_delivery);
//        payment_recyclerView = view.findViewById(R.id.payment_recyclerView);
//        payment_recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
//        progressDialog=new ProgressDialog(getContext());
//        progressDialog.setMessage("Please Wait");
//        total.setText(String.valueOf(totalAmount));
//
//        getTasks();
//        cash_on_delivery.setOnClickListener(v -> {
//            progressDialog.show();
//            String url=oderProduct;
//
//            JSONArray jsonArray=new JSONArray();
//
//            for (int i=0;i<taskForOrder.size();i++){
//                Task task=taskForOrder.get(i);
//
//                JSONObject jsonObject=new JSONObject();
//                try {
//                    jsonObject.put("productId",task.getProductId());
//                    jsonObject.put("storeId",task.getStoreId());
//                    jsonObject.put("storeName",task.getStoreName());
//                    jsonObject.put("storeEmail",task.getStoreEmail());
//                    jsonObject.put("productName",task.getProductName());
//                    jsonObject.put("productDescription",task.getProductDescription());
//                    jsonObject.put("productQuantity",String.valueOf(task.getCount()));
//                    jsonObject.put("productRate",task.getProductRate());
//
//                    double q=task.getCount();
//                    double r= Double.parseDouble(task.getProductRate());
//                    double total=q*r;
//
//                    jsonObject.put("productTotalRate",String.valueOf(total));
//                    jsonObject.put("productDeliverAddress",address.getProductDeliverAddress());
//                    jsonObject.put("productImageLink",task.getProductImageLink());
//                    jsonObject.put("productCategory",task.getProductCategory());
//                    jsonObject.put("userPhoneNumber",address.getUserPhoneNumber());
//                    jsonObject.put("userName","");
//                    jsonObject.put("userId",userId);
//
//                    jsonArray.put(jsonObject);
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    progressDialog.dismiss();
//                    Log.e("abcd",e.toString());
//                    Toast.makeText(getContext(), "something went worng error= "+e.toString(), Toast.LENGTH_SHORT).show();
//
//                }
//            }
//            Log.e("abcd",jsonArray.toString());
//            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
//
//            final String mRequestBody = jsonArray.toString();
//            Log.e("abcd",mRequestBody);
//
//            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//                @Override
//                public void onResponse(String response) {
//                    progressDialog.dismiss();
//                    Log.e("abcd",response);
//                    try {
//                        JSONObject responseObject=new JSONObject(response);
//                        boolean status =responseObject.getBoolean("status");
//                        if (status){
//                            Toast.makeText(getContext(), responseObject.getString("messag"), Toast.LENGTH_SHORT).show();
//                            deleteAllTask(getContext());
//
//                        }else {
//                            Toast.makeText(getContext(), responseObject.getString("messag"), Toast.LENGTH_SHORT).show();
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                        Toast.makeText(getContext(), "something went worng", Toast.LENGTH_SHORT).show();
//
//                    }
//
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    progressDialog.dismiss();
//                    Log.e("abcd",error.toString());
//                    Toast.makeText(getContext(), "something went worng error= "+error.toString(), Toast.LENGTH_SHORT).show();
//
//                }
//            }) {
//                @Override
//                public String getBodyContentType() {
//                    return "application/json; charset=utf-8";
//                }
//
//                @Override
//                public byte[] getBody() throws AuthFailureError {
//                    try {
//                        return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
//                    } catch (UnsupportedEncodingException uee) {
//                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
//                        return null;
//                    }
//                }
//
//            };
//
//            requestQueue.add(stringRequest);
//
//
//
//        });
    }

//    private static void deleteAllTask(Context context) {
//        progressDialog.show();
//        class DeleteTask extends AsyncTask<Void, Void, Void> {
//
//            @Override
//            protected Void doInBackground(Void... voids) {
//                DatabaseClient.getInstance(context).getAppDatabase()
//                        .taskDao()
//                        .deleteAll();
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(Void aVoid) {
//                super.onPostExecute(aVoid);
//                progressDialog.dismiss();
//                Intent intent=new Intent(context, UserDashboardActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                context.startActivity(intent);
//            }
//        }
//
//        DeleteTask dt = new DeleteTask();
//        dt.execute();
//
//    }
//
//    private void getTasks() {
//        progressDialog.show();
//        class GetTasks extends AsyncTask<Void, Void, List<Task>> {
//
//            @Override
//            protected List<Task> doInBackground(Void... voids) {
//                List<Task> taskList = DatabaseClient
//                        .getInstance(getContext())
//                        .getAppDatabase()
//                        .taskDao()
//                        .getAll();
//                return taskList;
//            }
//
//            @Override
//            protected void onPostExecute(List<Task> tasks) {
//                super.onPostExecute(tasks);
//                progressDialog.dismiss();
//                totalAmount = 0;
//                totalItem=0;
//                taskForOrder=tasks;
//                for (int i = 0; i < tasks.size(); i++) {
//                    Task task = tasks.get(i);
//                    double rate = Double.parseDouble(task.getProductRate());
//                    double quntity = task.getCount();
//
//                    double amount = rate * quntity;
//                    totalAmount = totalAmount + amount;
//                    totalItem=totalItem+quntity;
//                    Log.e("abcd", String.valueOf(tasks.get(i)));
//
//                }
//                taskList = tasks;
//                PaymentListAdapter paymentListAdapter = new PaymentListAdapter(getContext(), taskList);
//                payment_recyclerView.setAdapter(paymentListAdapter);
//                paymentListAdapter.notifyDataSetChanged();
//
//
//                total.setText(String.valueOf(totalAmount));
//
//
//            }
//        }
//
//        GetTasks gt = new GetTasks();
//        gt.execute();
//    }
}