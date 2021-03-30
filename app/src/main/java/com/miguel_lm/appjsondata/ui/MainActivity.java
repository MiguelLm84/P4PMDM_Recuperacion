package com.miguel_lm.appjsondata.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.miguel_lm.appjsondata.R;
import com.miguel_lm.appjsondata.modelo.JsonLab;
import com.miguel_lm.appjsondata.modelo.Post;
import com.miguel_lm.appjsondata.modelo.User;
import com.miguel_lm.appjsondata.ui.fragments.Fragment_List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ListenerPost {

    private long tiempoParaSalir = 0;
    private static final int REQUEST_NUEVO_POST = 1234;
    private Post postAmodificar;
    public static final String LOG_TAG = "log_json";
    TextView tv_titulo, tv_cuerpo;
    Spinner spinnerAutor;
    RequestQueue queue;
    private Fragment_List fragLista;
    Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        spinnerAutor = findViewById(R.id.spinnerAutor);
        tv_titulo = findViewById(R.id.ed_tituloPost);
        tv_cuerpo = findViewById(R.id.ed_cuerpoPost);

        queue = Volley.newRequestQueue(this);
        conectividad();

        fragLista = new Fragment_List();
        getSupportFragmentManager().beginTransaction().add(R.id.ContenedorFragments, fragLista).commit();

        recuperacionDeDatosUser();
    }

    private void conectividad() {

        Log.d(LOG_TAG, "Comprobando conexión");
        ConnectivityManager connMgr = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        boolean connected = (networkInfo != null && networkInfo.isConnected());

        if (!connected) {
            Toast.makeText(MainActivity.this, "Sin conexión", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_NUEVO_POST && resultCode == RESULT_OK) {
            fragLista.mostrarTodosPosts();
        }
    }

    private void recuperacionDeDatosUser() {

        String url_users = "https://jsonplaceholder.typicode.com/users?_end=5";

        JsonArrayRequest requestUser = new JsonArrayRequest(Request.Method.GET, url_users, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {
                    try {
                        parsearUsuario(response.getJSONObject(i));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                recuperacionDeDatosPost();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Error, no se ha podido conectar a la url solicitada", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(requestUser);
    }

    private void recuperacionDeDatosPost() {

        String url_posts = "https://jsonplaceholder.typicode.com/posts?_end=50";

        JsonArrayRequest requestPost = new JsonArrayRequest(Request.Method.GET, url_posts, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {
                    try {
                        parsearPost(response.getJSONObject(i));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                fragLista.mostrarTodosPosts();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Error, no se ha podido conectar a la url solicitada", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(requestPost);
    }

    public void buttonFabAdd(View view) {

        crearOmodificarPosts(null, Activity_Add_Post.ActivityPostModo.crear);
    }

    private void crearOmodificarPosts(final Post postAmodificar, Activity_Add_Post.ActivityPostModo modo) {

        Intent intentNuevaTarea = new Intent(this, Activity_Add_Post.class);
        intentNuevaTarea.putExtra(Activity_Add_Post.PARAM_POST_EDITAR, postAmodificar);
        intentNuevaTarea.putExtra(Activity_Add_Post.PARAM_MODO, modo.ordinal());
        startActivityForResult(intentNuevaTarea, REQUEST_NUEVO_POST);
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }

    private void accionEscogerYModificar() {

        JsonLab jsonLab = JsonLab.get(this);
        List<Post> listaPosts = jsonLab.getPosts();

        AlertDialog.Builder builderDialogEscogerTareas = new AlertDialog.Builder(this);
        builderDialogEscogerTareas.setIcon(R.drawable.editar);
        builderDialogEscogerTareas.setTitle("Modificar elementos");

        final String[] arrayTareasAMostrar = new String[listaPosts.size()];
        for (int i = 0; i < listaPosts.size(); i++) {
            User autor = jsonLab.searchUserById(listaPosts.get(i).getUserId());
            arrayTareasAMostrar[i] = "\n· TITULO: " + listaPosts.get(i).getTitulo() + "\n\n· AUTOR:  " +autor.getName() + "\n";
        }
        builderDialogEscogerTareas.setSingleChoiceItems(arrayTareasAMostrar, -1, (dialog, posicionElementoSeleccionado) -> postAmodificar = listaPosts.get(posicionElementoSeleccionado));
        builderDialogEscogerTareas.setPositiveButton("Modificar", (dialog, i) -> {

            if (postAmodificar == null) {
                Toast.makeText(getApplicationContext(), "Debe escoger un elemento", Toast.LENGTH_SHORT).show();
            } else {
                crearOmodificarPosts(postAmodificar, Activity_Add_Post.ActivityPostModo.editar);
            }
        });
        builderDialogEscogerTareas.setNegativeButton("Cancelar", null);
        builderDialogEscogerTareas.create().show();
    }

    private void eliminarDatosPosts() {

        JsonLab jsonLab = JsonLab.get(MainActivity.this);
        List<Post> listaPosts = jsonLab.getPosts();

        AlertDialog.Builder builderEliminar = new AlertDialog.Builder(this);
        builderEliminar.setIcon(R.drawable.eliminar__2_);
        builderEliminar.setTitle("Eliminar elementos");

        final ArrayList<String> listaPostsAeliminar = new ArrayList<>();
        String[] arrayTareas = new String[listaPosts.size()];
        final boolean[] postsSeleccionados = new boolean[listaPosts.size()];
        for (int i = 0; i < listaPosts.size(); i++) {
            User autor = jsonLab.searchUserById(listaPosts.get(i).getUserId());
            arrayTareas[i] = "\n· TITULO: " + listaPosts.get(i).getTitulo() + "\n\n· AUTOR:  " + autor.getName()+"\n";
        }
        builderEliminar.setMultiChoiceItems(arrayTareas, postsSeleccionados, (dialog, indiceSeleccionado, isChecked) -> {
            postsSeleccionados[indiceSeleccionado] = isChecked;
            User autor = jsonLab.searchUserById(listaPosts.get(indiceSeleccionado).getUserId());
            String postsParaEliminar = "\n· TITULO: " + listaPosts.get(indiceSeleccionado).getTitulo() + "\n\n· AUTOR:  " + autor.getName() + "\n";
            listaPostsAeliminar.add(postsParaEliminar);
        });

        builderEliminar.setPositiveButton("Borrar", (dialog, which) -> {

            AlertDialog.Builder builderEliminar_Confirmar = new AlertDialog.Builder(this);
            builderEliminar_Confirmar.setIcon(R.drawable.eliminar__2_);
            builderEliminar_Confirmar.setTitle("¿Eliminar los elementos?");
            String postsPorBorrar = null;

            if(listaPostsAeliminar.isEmpty()){
                Toast.makeText(getApplicationContext(), "Debe escoger una elemento", Toast.LENGTH_SHORT).show();
                eliminarDatosPosts();
                return;
            }

            for (int i = 0; i < listaPostsAeliminar.size(); i++) {
                postsPorBorrar = listaPostsAeliminar.get(i);
            }
            builderEliminar_Confirmar.setMessage(postsPorBorrar);
            builderEliminar_Confirmar.setNegativeButton("Cancelar", null);
            builderEliminar_Confirmar.setPositiveButton("Borrar", (dialogInterface, which1) -> {

                for (int i = listaPosts.size() - 1; i >= 0; i--) {
                    if (postsSeleccionados[i]) {
                        JsonLab.get(this).deletePosts(listaPosts.get(i));
                        listaPosts.remove(i);
                    }
                }
                fragLista.setListaPosts(listaPosts);
                Toast.makeText(getApplicationContext(), "Elementos eliminados correctamente.", Toast.LENGTH_SHORT).show();
            });
            builderEliminar_Confirmar.create().show();
            dialog.dismiss();
        });

        builderEliminar.setNegativeButton("Cancelar", null);
        builderEliminar.create().show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_principal, menu);
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String texto) {

                JsonLab jsonLab = JsonLab.get(MainActivity.this);
                List<Post> listadoPostsEncontrados = jsonLab.searchPosts(texto);
                fragLista.setListaPosts(listadoPostsEncontrados);

                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.accionAnhadirPost) {
            buttonFabAdd(null);
        } else if (item.getItemId() == R.id.accionModificarPost) {
            accionEscogerYModificar();
        } else if (item.getItemId() == R.id.accionEliminarPost) {
            eliminarDatosPosts();
        } else if (item.getItemId() == R.id.accionRefrescar) {
            botonReset();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        long tiempo = System.currentTimeMillis();
        if (tiempo - tiempoParaSalir > 3000) {
            tiempoParaSalir = tiempo;
            Toast.makeText(this, "Presione de nuevo 'Atrás' si desea salir", Toast.LENGTH_SHORT).show();
        } else {
            super.onBackPressed();
            overridePendingTransition(R.anim.left_in, R.anim.left_out);
        }
    }

    private void parsearUsuario(JSONObject response) {

        JsonLab jsonLab = JsonLab.get(MainActivity.this);

        try {
            int id = response.getInt("id");
            String nom = response.getString("name");
            String username = response.getString("username");
            String email = response.getString("email");
            String phone = response.getString("phone");

            JSONObject jsonObj = response.getJSONObject("company");
            String company = jsonObj.getString("name");

            User user = new User(id, nom, username, email, phone, company);
            jsonLab.insertUsers(user);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parsearPost(JSONObject response) {

        JsonLab jsonLab = JsonLab.get(MainActivity.this);
        Post postDescargado = Post.parsearPost(response, true);
        jsonLab.insertPosts(postDescargado);
    }

    @Override
    public void seleccionarUser(User user) {

        Intent intent = new Intent(this, Activity_Info_Autor.class);
        intent.putExtra(Activity_Info_Autor.PARAM_USER, user);
        startActivity(intent);
    }

    @Override
    public void modificarPosts(Post post) {

        crearOmodificarPosts(post, Activity_Add_Post.ActivityPostModo.editar);
    }

    @Override
    public void eliminarPosts(Post post) {

        crearOmodificarPosts(post, Activity_Add_Post.ActivityPostModo.eliminar);
    }

    public void botonReset() {

        snackbar = Snackbar.make(findViewById(R.id.ContenedorFragments), R.string.mensaje, Snackbar.LENGTH_SHORT);

        snackbar.setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setVisible(true);
            }
        });
        snackbar.addCallback(new Snackbar.Callback() {

            @Override
            public void onShown(Snackbar sb) {
                super.onShown(sb);
            }

            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                super.onDismissed(transientBottomBar, event);

                if (event != DISMISS_EVENT_ACTION) {

                    deleteDatabase();
                    conectividad();
                    recuperacionDeDatosUser();
                }
            }
        });
        snackbar.show();
    }


    public void deleteDatabase(){

        JsonLab jsonLab = JsonLab.get(MainActivity.this);
        jsonLab.deleteAllPosts();
        jsonLab.deleteAllUsers();
    }
}
