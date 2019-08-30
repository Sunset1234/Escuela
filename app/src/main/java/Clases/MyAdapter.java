package Clases;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.escuela.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private ArrayList<Alerta> ListaAlertas;
    private int layout;


    public MyAdapter(ArrayList<Alerta> names) {
        this.ListaAlertas = names;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.txtTitulo.setText(ListaAlertas.get(position).getTitulo());
        holder.txtDesc.setText(ListaAlertas.get(position).getDescripcion());
    }

    @Override
    public int getItemCount() {
        return ListaAlertas.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView txtTitulo;
        public TextView txtDesc;

        public ViewHolder(View itemView) {
            super(itemView);
            this.txtTitulo = itemView.findViewById(R.id.titulo);
            this.txtDesc= itemView.findViewById(R.id.descripcion);
        }

    }












    /*


    private List<String> names;
    private int layout;
    private OnItemClickListener itemClickListener;

    public MyAdapter(List<String> names,int layout, OnItemClickListener listener ){
        this.names=names;
        this.layout=layout;
        this.itemClickListener=listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(layout,viewGroup,false);
        ViewHolder vh = new ViewHolder(v);
        return vh;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.Bind(names.get(i),itemClickListener);
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textviewname;

        public ViewHolder(View itemView){
            super(itemView);
            this.textviewname=(TextView) itemView.findViewById(R.id.textViewName);
        }

        public void Bind(final String name, final OnItemClickListener listener){
            this.textviewname.setText(name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(name,getAdapterPosition());
                }
            });

        }

    }

    public interface OnItemClickListener{
        void onItemClick(String name,int position);
    }*/

}
