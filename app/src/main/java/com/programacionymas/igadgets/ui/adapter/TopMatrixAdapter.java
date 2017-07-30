package com.programacionymas.igadgets.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.programacionymas.igadgets.R;
import com.programacionymas.igadgets.io.response.TopMatrixHour;
import com.programacionymas.igadgets.io.response.TopProductsResponse;

import java.util.ArrayList;
import java.util.Locale;

public class TopMatrixAdapter extends RecyclerView.Adapter<TopMatrixAdapter.ViewHolder> {

    private ArrayList<ArrayList<TopMatrixHour>> mDataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvDayOfWeek, tvTotal, tvHours;

        public ViewHolder(View v) {
            super(v);
            tvDayOfWeek = v.findViewById(R.id.tvDayOfWeek);
            tvTotal = v.findViewById(R.id.tvTotal);
            tvHours = v.findViewById(R.id.tvHours);
        }
    }

    public TopMatrixAdapter() {
        mDataSet = new ArrayList<>();
    }

    public void setDataSet(ArrayList<ArrayList<TopMatrixHour>> topProductsDataSet) {
        this.mDataSet = topProductsDataSet;
        notifyDataSetChanged();
    }

    // El layout manager invoca este método para renderizar
    @Override
    public TopMatrixAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_top_day_data, parent, false);

        return new ViewHolder(v);
    }

    // Este método reemplaza el contenido de cada view,
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        ArrayList<TopMatrixHour> dayHoursData = mDataSet.get(position);
        String hoursText = "";
        int totalClicks = 0;
        for (int i=0; i<dayHoursData.size(); ++i) {
            final int q = dayHoursData.get(i).getQuantity();
            String p = dayHoursData.get(i).getPercentage();
            if (p.length() > 5) {
                p = p.substring(0, 5);
            }
            hoursText += (i + " a " + (i+1) + ": " + q + " clics (" + p + " %)\n");
            totalClicks += q;
        }

        holder.tvDayOfWeek.setText(getDayOfWeekTitle(position));
        holder.tvHours.setText(hoursText);

        final String totalClicksText = "En total: " + totalClicks + " clics.";
        holder.tvTotal.setText(totalClicksText);
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    private String getDayOfWeekTitle(int i) {
        String prefixTitle = "Día ";
        switch (i) {
            case 0: return prefixTitle + "Domingo";
            case 1: return prefixTitle + "Lunes";
            case 2: return prefixTitle + "Martes";
            case 3: return prefixTitle + "Miércoles";
            case 4: return prefixTitle + "Jueves";
            case 5: return prefixTitle + "Viernes";
            case 6: return prefixTitle + "Sábado";
            default: return prefixTitle + "desconocido";
        }
    }

}
