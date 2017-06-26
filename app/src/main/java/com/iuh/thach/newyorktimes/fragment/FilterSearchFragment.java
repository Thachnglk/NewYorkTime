package com.iuh.thach.newyorktimes.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.iuh.thach.newyorktimes.R;

import java.util.ArrayList;


public class FilterSearchFragment extends DialogFragment implements DatePickerFragment.onDateChangeListener {
    private EditText edtDate,edtMore;
    private Spinner spinnerSort;
    private Button btnSave,btnCancel;
    private CheckBox cbArts,cbFashion,cbSports,cbMore;
    private ArrayList<String> arraySort;
    private ArrayAdapter<String> adapterSort;
    private int pos;


    public interface onFilterSelected{
        void setQueryFilterInterface(String date, String sort);

    }

    public FilterSearchFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.filter_search_fragment,container,false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setupViews(v);
        onClickDatePicker();
        onClickButtonCancel();
        return v;
    }
    private void setupViews(View v) {
        edtDate = (EditText) v.findViewById(R.id.etDatePicker);
        edtMore = (EditText) v.findViewById(R.id.edtMore);
        spinnerSort = (Spinner) v.findViewById(R.id.spinnerSort);
        btnSave = (Button) v.findViewById(R.id.btnSave);
        btnCancel = (Button) v.findViewById(R.id.btnCancel);
        cbArts = (CheckBox) v.findViewById(R.id.cbArts);
        cbFashion = (CheckBox) v.findViewById(R.id.cbFashion);
        cbSports = (CheckBox) v.findViewById(R.id.cbSports);
        cbMore = (CheckBox) v.findViewById(R.id.cbMore);
        cbMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtMore.setVisibility(View.VISIBLE);
            }
        });
//        onClickCheckBox();
        arraySort = new ArrayList<>();
        arraySort.add("Newest");
        arraySort.add("Oldest");
        adapterSort = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,arraySort);
        spinnerSort.setAdapter(adapterSort);
        spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pos = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFilterSelected onFilterSelected = (onFilterSelected) getActivity();
                onFilterSelected.setQueryFilterInterface(dates, arraySort.get(pos).toString());
                Log.i("Test",dates + " "+ arraySort.get(pos).toString());
                dismiss();
            }
        });
    }
//    private void onClickCheckBox(final CheckBox checkBox){
//
//        checkBox.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                switch (v.getId()){
//                    case R.id.cbArts:
//                        String arts = cbArts.getText().toString();
//                        break;
//                    case R.id.cbFashion:
//                        String fashion = cbFashion.getText().toString();
//                        break;
//                    case R.id.cbSports:
//                        String sports = cbSports.getText().toString();
//                        break;
//                    case R.id.cbMore:
//                        edtMore.setVisibility(View.VISIBLE);
//                        break;
//                }
//            }
//        });
//
//    }

    private void onClickDatePicker(){
        edtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                DatePickerFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.setTargetFragment(FilterSearchFragment.this, 300);
                datePickerFragment.show(fm, "datepicker");
            }
        });
    }
    private void onClickButtonCancel() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    String dates;
    @Override
    public void setDateChange(final int year, final int monthOfYear, final int dayOfMonth) {
        edtDate.setText("" + year + "/" + monthOfYear + "/" + dayOfMonth);
        dates = "" + year + (monthOfYear < 10 ? "0" : "") + monthOfYear + dayOfMonth;
    }



}
