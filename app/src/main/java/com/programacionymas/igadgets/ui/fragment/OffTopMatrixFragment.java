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
import com.programacionymas.igadgets.io.response.TopMatrixDay;
import com.programacionymas.igadgets.io.response.TopMatrixHour;
import com.programacionymas.igadgets.io.response.TopProductsResponse;
import com.programacionymas.igadgets.ui.DatePickerFragment;
import com.programacionymas.igadgets.ui.adapter.TopMatrixAdapter;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class OffTopMatrixFragment extends Fragment implements View.OnClickListener, Callback<ArrayList<ArrayList<TopMatrixHour>>> {

    private EditText etStartDate, etEndDate;

    private RecyclerView recyclerView;
    private TopMatrixAdapter mAdapter;

    // track last request params
    private String startDate, endDate, top;

    private boolean internetAvailable;

    public OffTopMatrixFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_off_top_matrix, container, false);

        etStartDate = view.findViewById(R.id.etStartDate);
        etStartDate.setOnClickListener(this);

        etEndDate = view.findViewById(R.id.etEndDate);
        etEndDate.setOnClickListener(this);


        Button btnGenerateReport = view.findViewById(R.id.btnGenerateReport);
        btnGenerateReport.setOnClickListener(this);

        setupRecyclerView(view);

        // Initialize Realm
        Realm.init(getContext());

        internetAvailable = InternetConnection.checkConnection(getContext());
        if (! internetAvailable) {
            final String startDate = Global.getStringFromGlobalPreferences(getContext(), "offMatrix_startDate");
            final String endDate = Global.getStringFromGlobalPreferences(getContext(), "offMatrix_endDate");

            etStartDate.setText(startDate);
            etEndDate.setText(endDate);

            etStartDate.setEnabled(false);
            etEndDate.setEnabled(false);
        }

        return view;
    }

    private void setupRecyclerView(final View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new TopMatrixAdapter();
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
        Realm realm = Realm.getDefaultInstance();

        // Query Realm for all objects of this class
        final RealmResults<TopMatrixDay> results
                = realm.where(TopMatrixDay.class).findAll();

        // From TopMatrixDay to TopMatrixHours (ArrayList with 2 dimensions)
        ArrayList<ArrayList<TopMatrixHour>> matrixDataSet = new ArrayList<>();
        for (TopMatrixDay matrixDay : results) {
            List<TopMatrixHour> listMatrixDay = realm.copyFromRealm(matrixDay.getHours());
            matrixDataSet.add(new ArrayList<>(listMatrixDay));
        }
        mAdapter.setDataSet(matrixDataSet);
    }

    private void fetchAndGenerateReport() {
        startDate = etStartDate.getText().toString();
        endDate = etEndDate.getText().toString();

        if (startDate.isEmpty() || endDate.isEmpty()) {
            Toast.makeText(getContext(), R.string.error_empty_fields, Toast.LENGTH_SHORT).show();
            return;
        }

        String[] parts = startDate.split("/");
        startDate = parts[2] + "-" + parts[1] + "-" + parts[0];

        parts = endDate.split("/");
        endDate = parts[2] + "-" + parts[1] + "-" + parts[0];

        Call<ArrayList<ArrayList<TopMatrixHour>>> call = MyApiAdapter.getApiService().getTopProductsData(startDate, endDate);
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
    public void onResponse(Call<ArrayList<ArrayList<TopMatrixHour>>> call, Response<ArrayList<ArrayList<TopMatrixHour>>> response) {
        if (response.isSuccessful()) {
            ArrayList<ArrayList<TopMatrixHour>> topMatrixResponse = response.body();

            // Toast.makeText(getContext(), "Productos obtenidos => " + pairs.size(), Toast.LENGTH_SHORT).show();
            mAdapter.setDataSet(topMatrixResponse);
            offlineSave(topMatrixResponse);
        }
    }

    @Override
    public void onFailure(Call<ArrayList<ArrayList<TopMatrixHour>>> call, Throwable t) {
        Toast.makeText(getContext(), R.string.retrofit_on_failure, Toast.LENGTH_SHORT).show();
    }

    private void offlineSave(ArrayList<ArrayList<TopMatrixHour>> matrix) {
        Global.saveStringGlobalPreference(getContext(), "offMatrix_startDate", startDate);
        Global.saveStringGlobalPreference(getContext(), "offMatrix_endDate", endDate);

        // Get a Realm instance for this thread
        Realm realm = Realm.getDefaultInstance();

        // Persist your data in a transaction
        realm.beginTransaction();
        realm.delete(TopMatrixDay.class);
        for (ArrayList<TopMatrixHour> matrixHours : matrix) {
            TopMatrixDay matrixDay = new TopMatrixDay();
            matrixDay.setHours(new RealmList<>(matrixHours.toArray(new TopMatrixHour[matrixHours.size()])));
            realm.copyToRealm(matrixDay);
        }
        realm.commitTransaction();
    }
}
