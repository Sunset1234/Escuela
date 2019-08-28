package com.example.escuela;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;
import Clases.Alumno;



public class MainActivity extends AppCompatActivity {

    public EditText Matricula,Contrasena;
    public TextInputLayout InpMatricula,InpContrasena;
    public Button BtnIniciar;

    //Lista de espera
    public RequestQueue queue;

    //Url para petición
    public String UrlLogin="http://192.168.4.109:3333/login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Matricula=findViewById(R.id.Matricula);
        Contrasena=findViewById(R.id.Contrasena);

        InpMatricula=findViewById(R.id.InMatricula);
        InpContrasena=findViewById(R.id.InContrasena);

        BtnIniciar=findViewById(R.id.IniciarSesion);

        //Inicializar Request
        queue = Volley.newRequestQueue(this);

        //Click Inicar Sesión
        BtnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Login();
            }
        });

    }

    private void Login(){

        final String matricula=Matricula.getText().toString();
        final String contrasena=Contrasena.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlLogin, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Alumno alumno = new Alumno();

                Gson gson = new Gson();
                alumno=gson.fromJson(response,Alumno.class);

                if(response.contains("error")){
                    Toast.makeText(getApplicationContext(),"Matricula o Contraseña no encontrados",Toast.LENGTH_LONG).show();

                }else{
                    Intent ir = new Intent(getApplicationContext(),Principal.class);
                    startActivity(ir);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("he", "onErrorResponse: "+error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("matricula", matricula);
                params.put("curp", contrasena);
                return params;
            }

        };

        queue.add(stringRequest);
    }
}
