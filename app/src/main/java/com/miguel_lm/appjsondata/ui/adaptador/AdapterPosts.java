package com.miguel_lm.appjsondata.ui.adaptador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.miguel_lm.appjsondata.R;
import com.miguel_lm.appjsondata.modelo.Post;
import com.miguel_lm.appjsondata.ui.ListenerPost;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AdapterPosts extends RecyclerView.Adapter<ViewHolderPost> implements Filterable {

    private final ListenerPost listenerJson;
    private List<Post> listaPosts;
    private List<Post> listaPostsAll;
    Context context;

    public AdapterPosts(Context context, List<Post> listaPosts, ListenerPost listenerJson) {

        this.context = context;
        this.listaPosts = listaPosts;
        this.listenerJson = listenerJson;
        this.listaPostsAll = new ArrayList<>(listaPosts);
    }

    public void actualizarListado(List<Post> listaPosts) {
        this.listaPosts = listaPosts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolderPost onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_texto_json, parent, false);
        return new ViewHolderPost(v, listenerJson);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderPost holder, int position) {
        holder.mostrarTexto(listaPosts.get(position), context);
    }

    @Override
    public int getItemCount() {
        return listaPosts.size();
    }

    @Override
    public Filter getFilter() {
        return listaPostsFiltrada;
    }

    private final Filter listaPostsFiltrada = new Filter() {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            List<Post> listaFiltradaDePosts = new ArrayList<>();
            if(charSequence == null || charSequence.length() == 0){
                listaFiltradaDePosts.addAll(listaPostsAll);
            } else {
                String filterPatter = charSequence.toString().toLowerCase().trim();
                for(Post post1 : listaPostsAll){
                    if(post1.getTitulo().toLowerCase().contains(filterPatter)){
                        listaFiltradaDePosts.add(post1);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = listaFiltradaDePosts;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            listaPosts.clear();
            listaPosts.addAll((Collection<? extends Post>) results.values);
            notifyDataSetChanged();
        }
    };
}
