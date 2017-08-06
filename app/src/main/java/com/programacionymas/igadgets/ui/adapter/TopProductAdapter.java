package com.programacionymas.igadgets.ui.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.programacionymas.igadgets.R;
import com.programacionymas.igadgets.common.Global;
import com.programacionymas.igadgets.io.response.TopProductData;

import java.util.ArrayList;

public class TopProductAdapter extends RecyclerView.Adapter<TopProductAdapter.ViewHolder> {

    private ArrayList<TopProductData> mDataSet;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvProduct, tvQuantity, tvPercent;
        public LinearLayout linearLayout;
        Context context;

        public ViewHolder(View v) {
            super(v);
            context = v.getContext();
            tvProduct = v.findViewById(R.id.tvProduct);
            tvQuantity = v.findViewById(R.id.tvQuantity);
            tvPercent = v.findViewById(R.id.tvPercent);
            linearLayout = v.findViewById(R.id.linearLayout);
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
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final TopProductData productData = mDataSet.get(position);

        holder.tvProduct.setText(productData.getProduct());
        holder.tvQuantity.setText("Recibió " + productData.getQuantity() + " clics.");
        holder.tvPercent.setText("Representa el " + productData.getPercent() + " %");
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String message = "De los "+productData.getQuantity()+" clics, "+productData.getMobile()+" se han realizado desde dispositivos móviles, y "+productData.getDesktop()+" desde dispositivos de escritorio.";
                Global.showMessageDialog(holder.context, "Información de la fuente de visitas", message);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

}
