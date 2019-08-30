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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import Clases.Alumno;



public class MainActivity extends AppCompatActivity {

    public EditText Matricula,Contrasena;
    public TextInputLayout InpMatricula,InpContrasena;
    public Button BtnIniciar;

    private static final String TAG = "MainActivity";

    //Lista de espera
    public RequestQueue queue;

    //Url para petición
    public String UrlLogin="http://192.168.1.71:3333/login";

    //Url para obtener ID-Salón
    public String UrlIdSalon="http://192.168.1.71:3333/salonAlumno/";

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
                    Toast.makeText(getApplicationContext(),"Matricula o Contraseña incorrectos",Toast.LENGTH_LONG).show();

                }else{

                    try {
                        String aidialumno;
                        JSONObject object = new JSONObject(response);
                        aidialumno=object.getString("_id");
                        Log.d(TAG, "Alumno Id: "+aidialumno);
                        GetSalonId(aidialumno);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("he", "onErrorResponse: "+error);
            }
        }){
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


    public void GetSalonId(final String idalumno){

        final String matricula=Matricula.getText().toString();

        String urlfinal=UrlIdSalon+matricula;
        Log.d(TAG, "GetSalonId: "+urlfinal);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlfinal, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {
                    String aidi;
                    JSONObject object = new JSONObject(response);
                    aidi=object.getString("_id");
                    Intent ir = new Intent(getApplicationContext(),Principal.class);
                    Log.d(TAG, "onResponse: "+aidi);
                    ir.putExtra("id",aidi);
                    ir.putExtra("id_alumno",idalumno);
                    startActivity(ir);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("he", "onErrorResponse: "+error);
            }
        });

        queue.add(stringRequest);


    }

}
