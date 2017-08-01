package com.programacionymas.igadgets.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.programacionymas.igadgets.R;
import com.programacionymas.igadgets.io.response.TopProductData;

import java.util.ArrayList;

public class TopProductAdapter extends RecyclerView.Adapter<TopProductAdapter.ViewHolder> {

    private ArrayList<TopProductData> mDataSet;

    // Obtener referencias de los componentes visuales
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // en este ejemplo cada elemento consta solo de un título
        public TextView tvProduct, tvQuantity, tvPercent;

        public ViewHolder(View v) {
            super(v);
            tvProduct = v.findViewById(R.id.tvProduct);
            tvQuantity = v.findViewById(R.id.tvQuantity);
            tvPercent = v.findViewById(R.id.tvPercent);
        }
    }

    public TopProductAdapter() {
        mDataSet = new ArrayList<>();
    }

    public void setDataSet(ArrayList<TopProductData> topProductsDataSet) {
        this.mDataSet = topProductsDataSet;
        notifyDataSetChanged();
    }

    // El layout manager invoca este método para renderizar
    @Override
    public TopProductAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_top_product_data, parent, false);

        return new ViewHolder(v);
    }

    // Este método reemplaza el contenido de cada view,
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        TopProductData productData = mDataSet.get(position);

        holder.tvProduct.setText(productData.getProduct());
        holder.tvQuantity.setText("Recibió " + productData.getQuantity() + " clics.");
        holder.tvPercent.setText("Representa el " + productData.getPercent() + " %");
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

}
