package com.example.escuela;

import android.content.Intent;
import android.icu.util.Output;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Clases.Alerta;
import Clases.MyAdapter;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;


public class Principal extends AppCompatActivity {

    private OkHttpClient client;

    WebSocket websocket;

    public String aidi;
    public String aidialumno;

    private static final String TAG = "Principal";

    //Lista de espera
    public RequestQueue queue;

    //Url para petición
    public String UrlAlertas="http://192.168.1.71:3333/alertas/ver/alumno2/";

    private List<String> names;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        client = new OkHttpClient();

        //Inicializar Request
        queue = Volley.newRequestQueue(this);

        //Recibiendo datos de MainActivity
        aidi=getIntent().getStringExtra("id");
        aidialumno=getIntent().getStringExtra("id_alumno");

        //Recycler
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayout.VERTICAL,false));
        VerAlertas();
        start();
    }

    private List<String> getNames(){
        return new ArrayList<String>() {{
         add("Jaime");
         add("Emmanuel");
        }};
    }

    private void start() {
        Request request = new Request.Builder().url("ws://192.168.1.71:3333/adonis-ws").build();
        EchoWebSocketListener listener = new EchoWebSocketListener();
        WebSocket ws = client.newWebSocket(request, listener);

        client.dispatcher().executorService().shutdown();
    }


    public void emit(JSONObject data){
        try {
            JSONObject text = new JSONObject();
            JSONObject topics = new JSONObject();
            topics.put("topic", "alerta:"+aidi);
            topics.put("event", "message");
            topics.put("data", new JSONObject(data.toString()));
            text.put("t", 7);
            text.put("d", topics);
            Log.v("socket", "Try to send data " + text.toString());

            websocket.send(text.toString());

        } catch (JSONException e) {
            Log.e("socket", "Try to send data with wrong JSON format, data: " + e);
        }
    }

    private final class EchoWebSocketListener extends WebSocketListener {
        private static final int NORMAL_CLOSURE_STATUS = 1000;

        @Override
        public void onOpen(WebSocket webSocket, Response response) {

            websocket = webSocket;
            try {
                JSONObject text = new JSONObject();
                JSONObject topics = new JSONObject();
                topics.put("topic", "alerta:"+aidi);
                topics.put("event", "message");
                text.put("t", 1);
                text.put("d", topics);
                websocket.send(text.toString());
            } catch (JSONException e) {
                Log.e("socket", "Try to send data with wrong JSON format, data: " + e);
            }
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {

            try {
                JSONObject object = new JSONObject(text);
                int type = object.optInt("t");
                Log.v("socket: 61", "JSON object " + object);
                Log.v("onMessage", ""+type);

                switch (type) {
                    case 7: {
                        JSONObject d = object.optJSONObject("d");
                        Log.v("socket: 7", "");
                        VerAlertas();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            webSocket.close(NORMAL_CLOSURE_STATUS, null);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        }
    }


    public void VerAlertas(){

        String finall=UrlAlertas+aidialumno;

        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET, finall, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //Arreglo de comentarios
                final ArrayList<Alerta> ListData;
                ListData = new ArrayList<Alerta>();
                ListData.clear();

                try {

                    String titulo;
                    JSONArray object = new JSONArray(response);

                    Log.d(TAG, "onResponse: "+object);

                    for (int i = 0; i < object.length(); i++) {

                        JSONObject item = object.getJSONObject(i);

                        String name = item.getString("Titulo");
                        String desc = item.getString("Descripcion");
                        ListData.add(new Alerta(name,desc));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mAdapter = new MyAdapter(ListData);
                recyclerView.setAdapter(mAdapter);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("he", "onErrorResponse: "+error);
            }
        });

        queue.add(stringRequest);

    }

}


/*
package com.example.aplicaciond;

        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.TextView;

        import androidx.appcompat.app.AppCompatActivity;

        import org.json.JSONException;
        import org.json.JSONObject;

        import java.util.ArrayList;
        import java.util.List;
        import java.util.concurrent.TimeUnit;

        import okhttp3.OkHttpClient;
        import okhttp3.Request;
        import okhttp3.Response;
        import okhttp3.WebSocket;
        import okhttp3.WebSocketListener;
        import okio.ByteString;


public class Main2Activity extends AppCompatActivity {
    TextView tex;
    WebSocket websocket;
    private Button iniciar;
    private TextView output;
    private OkHttpClient client;
    private List<String> lista = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main2);
        tex = (TextView) findViewById(R.id.respuesta2);
        client = new OkHttpClient();
        start();
    }
    private void start(){
        Request request = new Request.Builder().url("ws://192.168.137.116:3333/adonis-ws").build();
        EchoWebSocketListener listener = new EchoWebSocketListener();
        WebSocket ws = client.newWebSocket(request, listener);

        client.dispatcher().executorService().shutdown();
    }
    //runnable se utiliza para poder entrar en los hilos
    private void output(final String txt) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tex.setText(tex.getText().toString() + "\n\n" + txt);
            }
        });
    }

    public void emit(View view){
        try {
            JSONObject text = new JSONObject();
            JSONObject topics = new JSONObject();
            topics.put("topic", "android");
            topics.put("event", "message");
            topics.put("data", new JSONObject("{mensaje:hola}"));
            text.put("t", 7);
            text.put("d", topics);
            Log.v("socket 69", "Try to send data " + text.toString());

            websocket.send(text.toString());
        } catch (JSONException e) {
            Log.e("socket", "Try to send data with wrong JSON format, data: " + e);
        }
    }

    private final class EchoWebSocketListener extends WebSocketListener {
        private static final int NORMAL_CLOSURE_STATUS = 1000;
        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            websocket = webSocket;
            try {
                JSONObject text = new JSONObject();
                JSONObject topics = new JSONObject();
                topics.put("topic", "android");
                topics.put("event", "Message");
                text.put("t", 1);
                text.put("d", topics);
                Log.v("socket 89", "otra cosa " + text.toString());

                webSocket.send(text.toString());
            } catch (JSONException e) {
                Log.e("socket 93", "Try to send data with wrong JSON format, data: " + e);
            }

            output(websocket.toString());


        }
        @Override
        public void onMessage(WebSocket webSocket, String text) {
            try {
                JSONObject object = new JSONObject(text);
                int type = object.optInt("t");
                Log.v("socket: 61", "JSON object " + object);
                Log.v("onMessage", ""+type);

                switch (type) {
                    case 0: {
                        Log.v("socket: 0","");
                    }
                    case 1: {
                        Log.v("socket: 1","");
                    }
                    case 2: {
                        Log.v("socket: 2","");
                    }
                    case 3: {
                        Log.v("socket: 3","");
                    }
                    case 4: {
                        Log.v("socket: 4","");
                    }
                    case 5: {
                        Log.v("socket: 5","");
                    }
                    case 6: {
                        Log.v("socket: 6","");
                    }
                    case 7: {
                        JSONObject d = object.optJSONObject("d");
                        output(d.get("data").toString());
                        Log.v("socket: 7", "");
                    }
                    case 8: {
                        Log.v("socket: 8","");
                    }
                    case 9: {
                        Log.v("socket: 9","");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            webSocket.close(NORMAL_CLOSURE_STATUS, null);

        }    @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            output("Error : " + t.getMessage());
        }
    }
}
*/