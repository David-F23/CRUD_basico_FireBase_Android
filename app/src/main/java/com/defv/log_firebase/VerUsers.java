package com.defv.log_firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class VerUsers extends AppCompatActivity {

    ListView listaUsers;
    private List<DtoUser> lista = new ArrayList<DtoUser>();
    ArrayAdapter<DtoUser> adaptador;
    DatabaseReference databaseReference;
    DatabaseReference databaseReference1;
    DtoUser datos = new DtoUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_users);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference1 = FirebaseDatabase.getInstance().getReference(getString(R.string.Usuario));

        listaUsers = findViewById(R.id.listaUsers);

        listarDatos();

    }

    private void listarDatos(){

        databaseReference.child("Usuario").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                lista.clear();

                for(DataSnapshot snapshot1 : snapshot.getChildren()){

                    DtoUser user = snapshot1.getValue(DtoUser.class);

                    lista.add(user);
                    adaptador = new ArrayAdapter<DtoUser>(VerUsers.this, android.R.layout.simple_list_item_1, lista);
                    listaUsers.setAdapter(adaptador);

                    listaUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {

                           Query query = databaseReference1.orderByChild("nombre").equalTo(listaUsers.getItemAtPosition(pos).toString());

                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    for(DataSnapshot dataSnapshot: snapshot.getChildren()){

                                        DtoUser user = dataSnapshot.getValue(DtoUser.class);
                                        String nombre = user.getNombre();
                                        String correo = user.getCorreo();
                                        String clave = user.getClave();
                                        String usuario = user.getUsuario();

                                        Log.i("Nombre", ""+ nombre);
                                        Log.i("Correo", ""+ correo);
                                        Log.i("Clave", ""+ clave);
                                        Log.i("Usuario", ""+ usuario);
                                        Log.i("Datos", ""+ dataSnapshot.getValue());

                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        intent.putExtra("senal", "1");
                                        intent.putExtra("nombre", nombre);
                                        intent.putExtra("correo", correo);
                                        intent.putExtra("clave", clave);
                                        intent.putExtra("usuario", usuario);
                                        startActivity(intent);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(VerUsers.this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}