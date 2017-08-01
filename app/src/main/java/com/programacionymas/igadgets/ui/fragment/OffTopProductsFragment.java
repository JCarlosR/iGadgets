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
import com.programacionymas.igadgets.common.Global;
import com.programacionymas.igadgets.common.InternetConnection;
import com.programacionymas.igadgets.io.MyApiAdapter;
import com.programacionymas.igadgets.io.response.TopProductData;
import com.programacionymas.igadgets.io.response.TopProductsResponse;
import com.programacionymas.igadgets.ui.DatePickerFragment;
import com.programacionymas.igadgets.ui.adapter.TopProductAdapter;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OffTopProductsFragment extends Fragment implements View.OnClickListener, Callback<TopProductsResponse> {

    private EditText etStartDate, etEndDate, etTop;

    private RecyclerView recyclerView;
    private TopProductAdapter mAdapter;

    // track last request params
    private String startDate, endDate, top;

    private boolean internetAvailable;

    public OffTopProductsFragment() {
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

        setupRecyclerView(v);

        // Initialize Realm
        Realm.init(getContext());

        internetAvailable = InternetConnection.checkConnection(getContext());
        if (! internetAvailable) {
            final String startDate = Global.getStringFromGlobalPreferences(getContext(), "offProducts_startDate");
            final String endDate = Global.getStringFromGlobalPreferences(getContext(), "offProducts_endDate");
            final String top = Global.getStringFromGlobalPreferences(getContext(), "offProducts_top");

            etStartDate.setText(startDate);
            etEndDate.setText(endDate);
            etTop.setText(top);

            etStartDate.setEnabled(false);
            etEndDate.setEnabled(false);
            etTop.setEnabled(false);
        }

        return v;
    }

    private void setupRecyclerView(final View v) {
        recyclerView = v.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new TopProductAdapter();
        recyclerView.setAdapter(mAdapter);
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
                if (internetAvailable)
                    fetchAndGenerateReport();
                else {
                    readAndGenerateOfflineReport();
                }
                break;
        }
    }

    private void readAndGenerateOfflineReport() {
        // Get a Realm instance for this thread
        Realm realm = Realm.getDefaultInstance();

        // Query Realm for all objects of this class
        final RealmResults<TopProductData> results
                = realm.where(TopProductData.class).findAll();

        List<TopProductData> unmanagedObjects = realm.copyFromRealm(results);
        ArrayList<TopProductData> dataSet = new ArrayList<>(unmanagedObjects);
        mAdapter.setDataSet(dataSet);
    }

    private void fetchAndGenerateReport() {
        startDate = etStartDate.getText().toString();
        endDate = etEndDate.getText().toString();
        top = etTop.getText().toString();

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

    private void offlineSave(ArrayList<TopProductData> pairs) {
        Global.saveStringGlobalPreference(getContext(), "offProducts_startDate", startDate);
        Global.saveStringGlobalPreference(getContext(), "offProducts_endDate", endDate);
        Global.saveStringGlobalPreference(getContext(), "offProducts_top", top);

        // Get a Realm instance for this thread
        Realm realm = Realm.getDefaultInstance();

        // Persist your data in a transaction
        realm.beginTransaction();
        realm.copyToRealm(pairs); // Persist un-managed objects
        realm.commitTransaction();
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
            ArrayList<TopProductData> pairs = topProducts.getPairs();

            // Toast.makeText(getContext(), "Productos obtenidos => " + pairs.size(), Toast.LENGTH_SHORT).show();
            mAdapter.setDataSet(pairs);

            // store last request in offline mode
            offlineSave(pairs);
        }
    }

    @Override
    public void onFailure(Call<TopProductsResponse> call, Throwable t) {
        Toast.makeText(getContext(), R.string.retrofit_on_failure, Toast.LENGTH_SHORT).show();
    }
}
