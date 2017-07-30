package com.programacionymas.igadgets.ui.fragment;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.programacionymas.igadgets.R;
import com.programacionymas.igadgets.io.MyApiAdapter;
import com.programacionymas.igadgets.io.response.TopProductsResponse;
import com.programacionymas.igadgets.ui.DatePickerFragment;
import com.programacionymas.igadgets.ui.adapter.TopProductAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OffTopProductsFragment extends Fragment implements View.OnClickListener, Callback<TopProductsResponse> {

    private EditText etStartDate, etEndDate, etTop;

    private RecyclerView recyclerView;
    private TopProductAdapter mAdapter;

    public OffTopProductsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_off_top_products, container, false);

        etStartDate = v.findViewById(R.id.etStartDate);
        etStartDate.setOnClickListener(this);

        etEndDate = v.findViewById(R.id.etEndDate);
        etEndDate.setOnClickListener(this);

        etTop = v.findViewById(R.id.etTop);

        Button btnGenerateReport = v.findViewById(R.id.btnGenerateReport);
        btnGenerateReport.setOnClickListener(this);

        recyclerView = v.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new TopProductAdapter();
        recyclerView.setAdapter(mAdapter);

        return v;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.etStartDate:
                showDatePickerDialog(etStartDate);
                break;

            case R.id.etEndDate:
                showDatePickerDialog(etEndDate);
                break;

            case R.id.btnGenerateReport:
                fetchAndGenerateReport();
                break;
        }
    }

    private void fetchAndGenerateReport() {
        String startDate = etStartDate.getText().toString();
        String endDate = etEndDate.getText().toString();
        final String top = etTop.getText().toString();

        if (startDate.isEmpty() || endDate.isEmpty() || top.isEmpty()) {
            Toast.makeText(getContext(), R.string.error_empty_fields, Toast.LENGTH_SHORT).show();
            return;
        }

        String[] parts = startDate.split("/");
        startDate = parts[2] + "-" + parts[1] + "-" + parts[0];

        parts = endDate.split("/");
        endDate = parts[2] + "-" + parts[1] + "-" + parts[0];

        Call<TopProductsResponse> call = MyApiAdapter.getApiService().getTopProductsData(startDate, endDate, top);
        call.enqueue(this);
    }

    private void showDatePickerDialog(final EditText etTarget) {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because january is zero
                final String selectedDate = twoDigits(day) + "/" + twoDigits(month+1) + "/" + year;
                etTarget.setText(selectedDate);
            }
        });
        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

    private String twoDigits(int n) {
        return (n<=9) ? ("0"+n) : String.valueOf(n);
    }

    @Override
    public void onResponse(Call<TopProductsResponse> call, Response<TopProductsResponse> response) {
        if (response.isSuccessful()) {
            TopProductsResponse topProducts = response.body();
            ArrayList<TopProductsResponse.TopProductData> pairs = topProducts.getPairs();

            // Toast.makeText(getContext(), "Productos obtenidos => " + pairs.size(), Toast.LENGTH_SHORT).show();
            mAdapter.setDataSet(pairs);
        }
    }

    @Override
    public void onFailure(Call<TopProductsResponse> call, Throwable t) {
        Toast.makeText(getContext(), R.string.retrofit_on_failure, Toast.LENGTH_SHORT).show();
    }
}
