package com.phudt7.movie.activity;


import static com.phudt7.movie.Const.Const.*;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.phudt7.movie.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.Calendar;

public class EditActivity extends AppCompatActivity {
    EditText edt_name, edt_email;
    Button btn_done, btn_cancel;
    TextView tv_date;
    Calendar calendar;

    RadioGroup rg_sex;
    RadioButton rb_male;
    RadioButton rb_female;
    String sex;
    ImageView img_profile,img_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        init();

        this.setupFloatingLabelError();
        this.setupFloatingEmailError();

        setData();
        cancelProfile();
        pickDate();
        radioCheck();
        doneProfile();
        imageProfile();
    }

    private void imageProfile() {
        img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] options = {"Camera", "Gallery"};
                AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Camera")) {
                            Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(takePicture, Resquet_code_camera);
                        } else if (options[item].equals("Gallery")) {
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK);
                            pickPhoto.setType("image/*");
                            startActivityForResult(pickPhoto, Resquet_code_library);
                        }
                    }
                });
                builder.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Resquet_code_camera && resultCode == RESULT_OK && data != null) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            img_profile.setImageBitmap(bitmap);
        }
        if (requestCode == Resquet_code_library && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                img_profile.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void radioCheck() {
        rg_sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_male) {
                    sex = MALE;
                    Log.v(TAG, "male");
                } else {
                    sex = FEMALE;
                    Log.v(TAG, "female");
                }
            }
        });
    }

    private void pickDate() {
        calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String selectedDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
                tv_date.setText(selectedDate);
            }
        };

        img_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(EditActivity.this, dateSetListener,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void cancelProfile() {
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setData() {
        SharedPreferences sharedPreferences = getSharedPreferences(MyPreference, MODE_PRIVATE);
        String name = sharedPreferences.getString(Name, "");
        String email = sharedPreferences.getString(Email, "");
        String date = sharedPreferences.getString(DATE_PICKER, "");
        String image = sharedPreferences.getString(IMAGE, "");
        edt_name.setText(name);
        edt_email.setText(email);
        tv_date.setText(date);
        if (!image.equalsIgnoreCase("")) {
            byte[] b = Base64.decode(image, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
            img_profile.setImageBitmap(bitmap);
        }
    }

    private void doneProfile() {
        btn_done.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = getSharedPreferences(MyPreference, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(Name, edt_name.getText().toString().trim());
            editor.putString(Email, edt_email.getText().toString().trim());
            editor.putString(DATE_PICKER, tv_date.getText().toString().trim());
            editor.putString(SEX, sex);

            BitmapDrawable bitmapDrawable = (BitmapDrawable) img_profile.getDrawable();
            Bitmap bitmap = bitmapDrawable.getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] arr = baos.toByteArray();
            String encodedImage = Base64.encodeToString(arr, Base64.DEFAULT);
            editor.putString(IMAGE, encodedImage);

            editor.apply();
            Intent intent = new Intent(EditActivity.this, MainActivity.class);
            setResult(RESULT_OK, intent);
            finish();
        });
    }

    private void setupFloatingEmailError() {
        final TextInputLayout textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayout_email);

        textInputLayoutEmail.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setupFloatingLabelError() {
        final TextInputLayout textInputLayoutName = (TextInputLayout) findViewById(R.id.textInputLayout_name);
        textInputLayoutName.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence text, int start, int count, int after) {
                if (text.length() == 0) {
                    textInputLayoutName.setError("Name is required");
                    textInputLayoutName.setErrorEnabled(true);
                } else if (text.length() < 5) {
                    textInputLayoutName.setError("Name is required and length must be >= 5");
                    textInputLayoutName.setErrorEnabled(true);
                } else {
                    textInputLayoutName.setErrorEnabled(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void init() {
        btn_done = findViewById(R.id.btn_done);
        edt_name = findViewById(R.id.edt_name);
        edt_email = findViewById(R.id.edt_email);
        tv_date = findViewById(R.id.tv_date);
        img_date = findViewById(R.id.img_date);
        btn_cancel = findViewById(R.id.btn_cancel);
        rg_sex = findViewById(R.id.rg_sex);
        rb_male = findViewById(R.id.rb_male);
        rb_female = findViewById(R.id.rb_female);
        img_profile = findViewById(R.id.img_profile);
    }
}