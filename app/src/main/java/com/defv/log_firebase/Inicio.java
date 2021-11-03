package com.defv.log_firebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Inicio extends AppCompatActivity {

    DatabaseReference databaseReference;
    private EditText et_correo, et_clave;
    private Button btnIniciar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        et_clave = findViewById(R.id.clave_log);
        et_correo = findViewById(R.id.correo_log);
        btnIniciar = findViewById(R.id.btniniciarSesion);

        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String correo = et_correo.getText().toString();
                String clave = et_clave.getText().toString();

                verificar(correo, clave);
            }
        });
    }

    private void verificar(String correoE, String contrasena){

        databaseReference.child("Usuario").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot snapshot1 : snapshot.getChildren()){

                    DtoUser user = snapshot1.getValue(DtoUser.class);
                    String nombre = user.getNombre();
                    String correo = user.getCorreo();
                    String clave = user.getClave();
                    String usuario = user.getUsuario();

                    if(correoE.equals(user.getCorreo()) && contrasena.equals(user.getClave())){

                        Intent intent = new Intent(Inicio.this, MainActivity.class);
                        startActivity(intent);

                    }else{
                        Toast.makeText(getApplicationContext(), "No se encontró", Toast.LENGTH_SHORT).show();
                    }


                    Log.i("Nombre", ""+ nombre);
                    Log.i("Correo", ""+ correo);
                    Log.i("Clave", ""+ clave);
                    Log.i("Usuario", ""+ usuario);
                    Log.i("Datos", ""+ snapshot1.getValue());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "No se pudo conectar", Toast.LENGTH_SHORT).show();
            }
        });
    }
}