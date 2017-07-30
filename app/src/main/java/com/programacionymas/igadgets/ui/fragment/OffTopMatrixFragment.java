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
import com.programacionymas.igadgets.io.response.TopMatrixHour;
import com.programacionymas.igadgets.io.response.TopProductsResponse;
import com.programacionymas.igadgets.ui.DatePickerFragment;
import com.programacionymas.igadgets.ui.adapter.TopMatrixAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class OffTopMatrixFragment extends Fragment implements View.OnClickListener, Callback<ArrayList<ArrayList<TopMatrixHour>>> {

    private EditText etStartDate, etEndDate;

    private RecyclerView recyclerView;
    private TopMatrixAdapter mAdapter;

    public OffTopMatrixFragment() {
        // Required empty public constructor
    }

    /*public static OffTopMatrixFragment newInstance(String param1, String param2) {
        OffTopMatrixFragment fragment = new OffTopMatrixFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }*/

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

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new TopMatrixAdapter();
        recyclerView.setAdapter(mAdapter);


        return view;
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
        }
    }

    @Override
    public void onFailure(Call<ArrayList<ArrayList<TopMatrixHour>>> call, Throwable t) {
        Toast.makeText(getContext(), R.string.retrofit_on_failure, Toast.LENGTH_SHORT).show();
    }
}
