package com.example.shokacshoes;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class resister extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resister);

        final EditText mailAddress = findViewById(R.id.MailAddress);
        final EditText customerID = findViewById(R.id.CustomerID);
        final EditText passWord = findViewById(R.id.PassWord);
        final EditText passWordRe = findViewById(R.id.PassWordRe);
        final RadioGroup sex = findViewById(R.id.Sex);
        final EditText birthDay = findViewById(R.id.Birthday);
        final EditText height = findViewById(R.id.Height);
        final EditText weight = findViewById(R.id.Weight);
        final EditText shoesSize = findViewById(R.id.ShoesSize);
        final RadioGroup Q1 = findViewById(R.id.Q1);
        final RadioGroup Q2 = findViewById(R.id.Q2);
        final RadioGroup Q3 = findViewById(R.id.Q3);
        final RadioGroup Q4 = findViewById(R.id.Q4);
        final RadioGroup Q5 = findViewById(R.id.Q5);
        final RadioGroup Q6 = findViewById(R.id.Q6);
        final RadioGroup Q7 = findViewById(R.id.Q7);
        final RadioGroup Q8 = findViewById(R.id.Q8);
        final EditText Q9 = findViewById(R.id.Q9);
        final EditText Q10 = findViewById(R.id.Q10);
        final Button loginButton = findViewById(R.id.Resister);
        final Button agreeButton = findViewById(R.id.agree);


        agreeButton.setOnClickListener(v -> {
            loginButton.setEnabled(true);

        });

        //EditTextにリスナーをつける
        birthDay.setOnClickListener(v -> {
            //Calendarインスタンスを取得
            final Calendar date = Calendar.getInstance();

            //DatePickerDialogインスタンスを取得
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    resister.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                            birthDay.setText(String.format("%d / %02d / %02d", year, month+1, dayOfMonth));
                        }

                    },
                    date.get(Calendar.YEAR),
                    date.get(Calendar.MONTH),
                    date.get(Calendar.DATE)
            );

            //MaxDateの設定
            Calendar maxCal = Calendar.getInstance();

            datePickerDialog.getDatePicker().setMaxDate(maxCal.getTimeInMillis());

            //dialogを表示
            datePickerDialog.show();

        });






        String url = "http://192.168.20.63:80/resister";


        loginButton.setOnClickListener(v -> {
            String tempPass1 = passWord.getText().toString();
            String tempPass2 = passWordRe.getText().toString();
            if(!tempPass1.equals(tempPass2)) {
                Toast.makeText(getApplicationContext() , "パスワードが違います", Toast.LENGTH_LONG).show();
            }else if(mailAddress.getText().toString().equals("")){
                Toast.makeText(getApplicationContext() , "メールアドレスを入力してください", Toast.LENGTH_LONG).show();
            }else if(customerID.getText().toString().equals("")){
                Toast.makeText(getApplicationContext() , "顧客IDを入力してください", Toast.LENGTH_LONG).show();
            }else if(passWord.getText().toString().equals("")){
                Toast.makeText(getApplicationContext() , "パスワードを入力してください", Toast.LENGTH_LONG).show();
            }else if(birthDay.getText().toString().equals("")){
                Toast.makeText(getApplicationContext() , "生年月日を入力してください", Toast.LENGTH_LONG).show();
            }else if(birthDay.getText().toString().equals("")){
                Toast.makeText(getApplicationContext() , "身長を入力してください", Toast.LENGTH_LONG).show();
            }else if(weight.getText().toString().equals("")){
                Toast.makeText(getApplicationContext() , "体重を入力してください", Toast.LENGTH_LONG).show();
            }else if(shoesSize.getText().toString().equals("")){
                Toast.makeText(getApplicationContext() , "靴のサイズを入力してください", Toast.LENGTH_LONG).show();
            }


            else{
                JSONObject sendToServerProfile = new JSONObject();
                try {
                    sendToServerProfile.put("mailaddress", mailAddress.getText().toString());
                    sendToServerProfile.put("customerID", customerID.getText().toString());
                    sendToServerProfile.put("passWord", passWord.getText().toString());
                    sendToServerProfile.put("birthDay", birthDay.getText().toString());
                    sendToServerProfile.put("height", height.getText().toString());
                    sendToServerProfile.put("weight", weight.getText().toString());
                    sendToServerProfile.put("shoesSize", shoesSize.getText().toString());
                    sendToServerProfile.put("Q9", Q9.getText().toString());
                    sendToServerProfile.put("Q10", Q10.getText().toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                int checkedId;

                try {
                    checkedId = sex.getCheckedRadioButtonId();
                    if (checkedId == R.id.Man) {
                        sendToServerProfile.put("sex", "1");
                    } else if (checkedId == R.id.Woman) {
                        sendToServerProfile.put("sex", "0");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    checkedId = Q1.getCheckedRadioButtonId();
                    if (checkedId == R.id.Q1_1) {
                        sendToServerProfile.put("Q1", "1");
                    } else if (checkedId == R.id.Q1_2) {
                        sendToServerProfile.put("Q1", "2");
                    } else if (checkedId == R.id.Q1_3) {
                        sendToServerProfile.put("Q1", "3");
                    } else if (checkedId == R.id.Q1_4) {
                        sendToServerProfile.put("Q1", "4");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    checkedId = Q2.getCheckedRadioButtonId();
                    if (checkedId == R.id.Q2_1) {
                        sendToServerProfile.put("Q2", "1");
                    } else if (checkedId == R.id.Q2_2) {
                        sendToServerProfile.put("Q2", "2");
                    } else if (checkedId == R.id.Q2_3) {
                        sendToServerProfile.put("Q2", "3");
                    } else if (checkedId == R.id.Q2_4) {
                        sendToServerProfile.put("Q2", "4");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    checkedId = Q3.getCheckedRadioButtonId();
                    if (checkedId == R.id.Q3_1) {
                        sendToServerProfile.put("Q3", "1");
                    } else if (checkedId == R.id.Q3_2) {
                        sendToServerProfile.put("Q3", "2");
                    } else if (checkedId == R.id.Q3_3) {
                        sendToServerProfile.put("Q3", "3");
                    } else if (checkedId == R.id.Q3_4) {
                        sendToServerProfile.put("Q3", "4");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    checkedId = Q4.getCheckedRadioButtonId();
                    if (checkedId == R.id.Q4_1) {
                        sendToServerProfile.put("Q4", "1");
                    } else if (checkedId == R.id.Q4_2) {
                        sendToServerProfile.put("Q4", "2");
                    } else if (checkedId == R.id.Q4_3) {
                        sendToServerProfile.put("Q4", "3");
                    } else if (checkedId == R.id.Q4_4) {
                        sendToServerProfile.put("Q4", "4");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    checkedId = Q5.getCheckedRadioButtonId();
                    if (checkedId == R.id.Q5_1) {
                        sendToServerProfile.put("Q5", "1");
                    } else if (checkedId == R.id.Q5_2) {
                        sendToServerProfile.put("Q5", "2");
                    } else if (checkedId == R.id.Q5_3) {
                        sendToServerProfile.put("Q5", "3");
                    } else if (checkedId == R.id.Q5_4) {
                        sendToServerProfile.put("Q5", "4");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    checkedId = Q6.getCheckedRadioButtonId();
                    if (checkedId == R.id.Q6_1) {
                        sendToServerProfile.put("Q6", "1");
                    } else if (checkedId == R.id.Q6_2) {
                        sendToServerProfile.put("Q6", "2");
                    } else if (checkedId == R.id.Q6_3) {
                        sendToServerProfile.put("Q6", "3");
                    } else if (checkedId == R.id.Q6_4) {
                        sendToServerProfile.put("Q6", "4");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    checkedId = Q7.getCheckedRadioButtonId();
                    if (checkedId == R.id.Q7_1) {
                        sendToServerProfile.put("Q7", "1");
                    } else if (checkedId == R.id.Q7_2) {
                        sendToServerProfile.put("Q7", "2");
                    } else if (checkedId == R.id.Q7_3) {
                        sendToServerProfile.put("Q7", "3");
                    } else if (checkedId == R.id.Q7_4) {
                        sendToServerProfile.put("Q7", "4");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    checkedId = Q8.getCheckedRadioButtonId();
                    if (checkedId == R.id.Q8_1) {
                        sendToServerProfile.put("Q8", "1");
                    } else if (checkedId == R.id.Q8_2) {
                        sendToServerProfile.put("Q8", "2");
                    } else if (checkedId == R.id.Q8_3) {
                        sendToServerProfile.put("Q8", "3");
                    } else if (checkedId == R.id.Q8_4) {
                        sendToServerProfile.put("Q8", "4");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                postData(url, sendToServerProfile);
            }

        });








    }


    public void postData(String url, JSONObject data){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        JsonObjectRequest jsonobj = new JsonObjectRequest(Request.Method.POST, url,data,
                response -> {


                    try {

                        String result = response.getString("result");
                        if(result.equals("OK")) {
                            Toast.makeText(getApplicationContext(), "認証完了", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "認証失敗", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {

                        Toast.makeText(getApplicationContext(),"認証失敗", Toast.LENGTH_LONG).show();

                        e.printStackTrace();
                    }

                },
                error -> {
                }
        ){
            //here I want to post data to sever
        };
        requestQueue.add(jsonobj);

    }
}