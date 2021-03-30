package com.miguel_lm.appjsondata.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.miguel_lm.appjsondata.R;
import com.miguel_lm.appjsondata.modelo.JsonLab;
import com.miguel_lm.appjsondata.modelo.Post;
import com.miguel_lm.appjsondata.modelo.User;
import com.miguel_lm.appjsondata.ui.ListenerPost;
import com.miguel_lm.appjsondata.ui.MainActivity;
import com.miguel_lm.appjsondata.ui.adaptador.AdapterPosts;

import java.util.ArrayList;
import java.util.List;

public class Fragment_List extends Fragment {

    RecyclerView recyclerViewPosts;
    AdapterPosts adapterPosts;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View vista = inflater.inflate(R.layout.fragment__list, container, false);
        recyclerViewPosts = vista.findViewById(R.id.RecyclerViewJson);
        recyclerViewPosts.setLayoutManager(new LinearLayoutManager(getContext()));
        return vista;
    }

    public void mostrarTodosPosts() {
        JsonLab jsonLab = JsonLab.get(getContext());
        List<Post> listPosts = jsonLab.getPosts();
        adapterPosts = new AdapterPosts(getContext(), listPosts, (MainActivity)getActivity());
        recyclerViewPosts.setAdapter(adapterPosts);
    }

    public void setListaPosts(List<Post> listaPosts) {
        adapterPosts.actualizarListado(listaPosts);
    }
}