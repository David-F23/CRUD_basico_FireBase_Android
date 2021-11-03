package com.defv.log_firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    DatabaseReference databaseReference1;
    private EditText et_nombre, et_correo, et_clave, et_usuario;
    private Button btnsave, btnUpdate, btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_nombre = findViewById(R.id.nombre);
        et_correo = findViewById(R.id.correo);
        et_clave = findViewById(R.id.clave);
        et_usuario = findViewById(R.id.usuario);
        btnsave = findViewById(R.id.btnsave);
        btnUpdate = findViewById(R.id.btnupdate);
        btnDelete = findViewById(R.id.btndelete);

        String senal = "";
        String nombre = "";
        String correo = "";
        String clave = "";
        String usuario = "";

        try {
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();

            if(bundle != null){
                senal = bundle.getString("senal");
                nombre = bundle.getString("nombre");
                correo = bundle.getString("correo");
                clave = bundle.getString("clave");
                usuario = bundle.getString("usuario");

                if(senal.equals("1")){
                    et_nombre.setText(nombre);
                    et_correo.setText(correo);
                    et_clave.setText(clave);
                    et_usuario.setText(usuario);
                }
            }

        }catch(Exception e){
            Log.e("cass", e.getMessage());
            e.getMessage();
        }


        databaseReference1 = FirebaseDatabase.getInstance().getReference(getString(R.string.Usuario));
        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("Usuario").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot snapshot1 : snapshot.getChildren()){

                    DtoUser user = snapshot1.getValue(DtoUser.class);
                    String nombre = user.getNombre();
                    String correo = user.getCorreo();
                    String clave = user.getClave();
                    String usuario = user.getUsuario();

                    /*Log.i("Nombre", ""+ nombre);
                    Log.i("Correo", ""+ correo);
                    Log.i("Clave", ""+ clave);
                    Log.i("Usuario", ""+ usuario);
                    Log.i("Datos", ""+ snapshot1.getValue());*/
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nombre = et_nombre.getText().toString();
                String correo = et_correo.getText().toString();
                String clave = et_clave.getText().toString();
                String usuario = et_usuario.getText().toString();

                saveUser(nombre, correo, clave, usuario);
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nombre = et_nombre.getText().toString();
                String correo = et_correo.getText().toString();
                String clave = et_clave.getText().toString();
                String usuario = et_usuario.getText().toString();

                updateUser(nombre, correo, clave, usuario);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nombre = et_nombre.getText().toString();
                String correo = et_correo.getText().toString();
                String clave = et_clave.getText().toString();
                String usuario = et_usuario.getText().toString();

                deleteUser(nombre, correo, clave, usuario);
            }
        });
    }

    private void saveUser(String nombre, String correo, String clave, String usuario) {
        Map<String, Object> datosUser = new HashMap<>();
        datosUser.put("nombre", nombre);
        datosUser.put("correo", correo);
        datosUser.put("clave", clave);
        datosUser.put("usuario", usuario);

        databaseReference.child("Usuario").push().setValue(datosUser);
    }

    public void verUsu(View view) {

        Intent intent = new Intent(this, VerUsers.class);
        startActivity(intent);
    }

    private void updateUser(String nombre, String correo, String clave, String usuario){

        Query query = databaseReference1.orderByChild("nombre").equalTo(nombre);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    String llave = dataSnapshot.getKey();

                    databaseReference1.child(llave).child("correo").setValue(correo);
                    databaseReference1.child(llave).child("clave").setValue(clave);
                    databaseReference1.child(llave).child("usuario").setValue(usuario);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void deleteUser(String nombre, String correo, String clave, String usuario){

        Query query = databaseReference1.orderByChild("nombre").equalTo(nombre);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    String llave = dataSnapshot.getKey();

                    databaseReference1.child(llave).removeValue();
                    /*databaseReference1.child(llave).child("correo").setValue(correo);
                    databaseReference1.child(llave).child("clave").setValue(clave);
                    databaseReference1.child(llave).child("usuario").setValue(usuario);*/
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}