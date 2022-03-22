package com.example.attendancecapturingapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class Register extends AppCompatActivity {
    MaterialButton regbtn;
    TextInputEditText name, phone, mailid, password;
    AutoCompleteTextView department, role;
    ArrayAdapter adapter1, adapter2;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore fstore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Hooks
        firebaseAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        password = findViewById(R.id.password1);
        name = findViewById(R.id.name);
        phone = findViewById(R.id.mobilenumber);
        mailid = findViewById(R.id.emailid);
        department = findViewById(R.id.department);
        role = findViewById(R.id.role);
        regbtn = findViewById(R.id.registerBtn1);
        adapter2 = ArrayAdapter.createFromResource(this, R.array.departmentnames, android.R.layout.simple_list_item_1);
        adapter1 = ArrayAdapter.createFromResource(this, R.array.rolenames, android.R.layout.simple_list_item_1);
        department.setAdapter(adapter1);
        role.setAdapter(adapter2);
        regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mobile = phone.getText().toString();

                String emailPattern1 = "/^[\\W.+\\-]+@drngpit+\\.ac+\\.in$/";
                String name1 = name.getText().toString();
                String pass = password.getText().toString();
                String mail = mailid.getText().toString();
                String department1 = department.getText().toString();
                String role1 = role.getText().toString();


                if (name1.isEmpty() || pass.isEmpty() || mail.isEmpty() || department1.isEmpty() || role1.isEmpty() || mobile.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Fields should not be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (name1.length() < 3 || name1.length() > 20) {
                    Toast.makeText(getApplicationContext(), "Length should be minimum 3 and maximum 20 characters", Toast.LENGTH_SHORT).show();
                    return;
                }
                if ((Patterns.EMAIL_ADDRESS.matcher(mail).matches() || (mail.matches(emailPattern1)))) {
                    mailid.setError(null);
                } else {
                    Toast.makeText(getApplicationContext(), "Invalid Email Id", Toast.LENGTH_SHORT).show();
                    mailid.setError("Invalid email id");
                    return;
                }
                if(!mobile.matches("[0-9]{10}")){
                    phone.setError("Invalid Mobile Number");
                    return;
                }
                if (!pass.matches("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$")) {
                    Toast.makeText(getApplicationContext(), "Password is weak", Toast.LENGTH_SHORT).show();
                    return;
                }

                //if all details are correct and acceptable
                firebaseAuth.createUserWithEmailAndPassword(mail,pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(getApplicationContext(), "User Added Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), VideoCapture.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Registration Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                String uid = firebaseAuth.getUid();

                HashMap<String,String> hm = new HashMap<>();
                hm.put("name",name1);
                hm.put("department",department1);
                hm.put("role",role1);
                hm.put("phone",mobile);
                hm.put("email",mail);

                DocumentReference documentReference = fstore.collection("users").document(uid);

                documentReference.set(hm).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Registration Failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}