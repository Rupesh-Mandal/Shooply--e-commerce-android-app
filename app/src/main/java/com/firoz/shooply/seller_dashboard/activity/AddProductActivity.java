package com.firoz.shooply.seller_dashboard.activity;

import static com.firoz.shooply.util.Constant.addProduct;
import static com.firoz.shooply.util.Constant.getAllCategory;
import static com.firoz.shooply.util.Constant.getSubCategory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
import com.firoz.shooply.auth.model.AuthUser;
import com.firoz.shooply.seller_dashboard.helper.SellerHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class AddProductActivity extends AppCompatActivity {
    private final int storegePermision = 13;
    private final int storegePermisionForCamera = 35;
    private final int cameraPermisionFor = 73;

    ImageView product_photo;

    TextInputEditText productName, productDescription, productRate, productDeliverCharge,mrp;

    Spinner category;
    Button upload_product;
    public static boolean isCamera = false;
    public static String currentPhotoPath;

    JSONArray storeCategoryList;

    ProgressDialog progressDialog;

    AutoCompleteTextView subCategory;

    SellerHelper sellerHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        sellerHelper=new SellerHelper(this);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");

        initView();
    }


    private void initView() {
        product_photo = findViewById(R.id.product_photo);
        productName = findViewById(R.id.productName);
        productDescription = findViewById(R.id.productDescription);
        productRate = findViewById(R.id.productRate);
        productDeliverCharge = findViewById(R.id.productDeliverCharge);
        upload_product = findViewById(R.id.upload_product);
        category = findViewById(R.id.category);
        subCategory = findViewById(R.id.subCategory);
        mrp = findViewById(R.id.mrp);
        loadCategoty();


        upload_product.setOnClickListener(view -> {
            if (isValid()) {

                try {
                    JSONObject productObject=new JSONObject();

                    productObject.put("storeName",productName.getText().toString().trim());
                    productObject.put("productName",productName.getText().toString().trim());
                    productObject.put("productDescription",productDescription.getText().toString().trim());
                    productObject.put("productRate",productRate.getText().toString());
                    productObject.put("mrp",mrp.getText().toString());

                    double r=Double.parseDouble(productRate.getText().toString().trim());
                    double m=Double.parseDouble(mrp.getText().toString().trim());
                    double d=m-r;

                    double discount=(d/m)*100;

                    productObject.put("discount",String.valueOf(discount));

                    productObject.put("storecategory",category.getSelectedItem().toString().trim());
                    productObject.put("sub_category",subCategory.getText().toString().trim());
                    productObject.put("storeCategoryId",storeCategoryList.getJSONObject(category.getSelectedItemPosition()).getString("storeCategoryId"));
                    sellerHelper.uploadImage(((BitmapDrawable) product_photo.getDrawable()).getBitmap(),productObject);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        product_photo.setOnClickListener(view -> {
            selectImage();
        });

        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    loadSubCategory(storeCategoryList.getJSONObject(i).getString("category"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void loadSubCategory(String storecategory) {
        String Url=getSubCategory;
        RequestQueue queue = Volley.newRequestQueue(this);
        progressDialog.show();
        Log.e("abcd",storecategory);
        StringRequest sr = new StringRequest(Request.Method.POST, Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("abcd",response);
                        progressDialog.dismiss();
                        ArrayList<String> stringArrayList=new ArrayList<>();
                        try {
                            JSONArray subcategoryArray=new JSONArray(response);
                            for (int i=0;i<subcategoryArray.length();i++){
                                JSONObject jsonObject=subcategoryArray.getJSONObject(i);
                                stringArrayList.add(jsonObject.getString("name"));
                            }
                            subCategory.setThreshold(1);
                            subCategory.setAdapter(new ArrayAdapter<String>(AddProductActivity.this, android.R.layout.select_dialog_item, stringArrayList));
                            subCategory.setTextColor(Color.BLACK);


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("abcd",e.getMessage());
                            Toast.makeText(AddProductActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("abcd",error.getMessage());
                        progressDialog.dismiss();
                        Toast.makeText(AddProductActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                })
        {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("storecategory",storecategory);
                return params;
            }
        };
        queue.add(sr);

    }

    private void loadCategoty() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String Url=getAllCategory;

        progressDialog.show();

        StringRequest sr = new StringRequest(Request.Method.GET, Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("abcd",response);
                        progressDialog.dismiss();
                        ArrayList<String> stringArrayList=new ArrayList<>();
                        try {
                            storeCategoryList=new JSONArray(response);
                            for (int i=0;i<storeCategoryList.length();i++){
                                JSONObject jsonObject=storeCategoryList.getJSONObject(i);
                                stringArrayList.add(jsonObject.getString("category"));
                            }
                            category.setAdapter(new ArrayAdapter<String>(AddProductActivity.this, android.R.layout.simple_spinner_item,stringArrayList));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("abcd",error.getMessage());
                        progressDialog.dismiss();
                        Toast.makeText(AddProductActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
        queue.add(sr);
    }

    private void selectImage() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, R.style.bottom_shee_dailog_theam);
        View v = LayoutInflater.from(this).
                inflate(R.layout.select_image_bottomsheet, (ConstraintLayout) findViewById(R.id.bottom_sheet_layout));
        bottomSheetDialog.setContentView(v);

        ImageView gallerBtn = v.findViewById(R.id.gallerBtn);
        ImageView cameraBtn = v.findViewById(R.id.cameraBtn);

        gallerBtn.setOnClickListener(view -> {
            if (checkPermissionForGallery()) {
                bottomSheetDialog.dismiss();
                pickFromGallery();
            }
        });

        cameraBtn.setOnClickListener(view -> {
            if (checkPermissionForCamara()) {
                bottomSheetDialog.dismiss();
                pickFromCamera();
            }
        });

        bottomSheetDialog.show();
    }


    public void pickFromCamera() {
        isCamera = true;
        String fileName = "photo";
        File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try {
            File imageFile = File.createTempFile(fileName, ".jpg", storageDirectory);
            currentPhotoPath = imageFile.getAbsolutePath();
            Uri imageUri = FileProvider.getUriForFile(AddProductActivity.this, "com.softkali.sellerkf.fileprovider", imageFile);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, 110);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void pickFromGallery() {
        isCamera = false;
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, 1);

    }

    private boolean checkPermissionForCamara() {
        int resultStoreg = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int resultCamera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        if (resultStoreg == PackageManager.PERMISSION_GRANTED) {
            if (resultCamera == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, cameraPermisionFor);
                return false;
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, storegePermisionForCamera);
            return false;
        }

    }


    private boolean checkPermissionForGallery() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, storegePermision);
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case storegePermision:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickFromGallery();
                } else {
                    checkPermissionForGallery();
                }
                break;

            case storegePermisionForCamera:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkPermissionForCamara();
                }
                break;


            case cameraPermisionFor:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkPermissionForCamara();
                    pickFromCamera();
                } else {
                    checkPermissionForCamara();
                }
                break;


            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri imageUri = data.getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                                    getContentResolver(), imageUri);
                            product_photo.setImageBitmap(bitmap);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }
        if (isCamera == true) {
            if (resultCode == RESULT_OK)
            {
                Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
                product_photo.setImageBitmap(bitmap);
            }


        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    boolean isValid() {

        if (product_photo.getDrawable() == null) {
            Toast.makeText(this, "Please Select Product Photo", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            if (productName.getText().toString().trim().isEmpty()) {
                productName.setError("Please enter product Name");
                productName.requestFocus();
                return false;
            } else {
                if (productRate.getText().toString().trim().isEmpty()) {
                    productRate.setError("Please enter product Rate");
                    productRate.requestFocus();
                    return false;
                } else {
                    if (mrp.getText().toString().trim().isEmpty()) {
                        mrp.setError("Please enter mrp");
                        mrp.requestFocus();
                        return false;
                    } else {
                        return true;
                    }
                }
            }
        }
    }

}