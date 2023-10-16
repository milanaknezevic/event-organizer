package com.example.eventorganizer.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.eventorganizer.R;
import com.example.eventorganizer.enums.Category;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddEventFragment extends Fragment {

    Spinner categorySpinner;
    View root;
    ArrayAdapter<String> spinnerAdapter;
    private List<Category> categories = new ArrayList<>();
    EditText editText;
    private EditText pickDateEditText;
    private EditText pickTimeEditText;
    private Calendar calendar;
    private int year, month, day, hour, minute;
    ImageView imageView;


    private static final int REQUEST_IMAGE_CAPTURE = 1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_add_event, container, false);
        categorySpinner = root.findViewById(R.id.category);
        spinnerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, getSpinnerDataAsNames());
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(spinnerAdapter);
        pickDateEditText = root.findViewById(R.id.pickDateEditText);
        pickTimeEditText = root.findViewById(R.id.pickTimeEditText);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        pickDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        pickTimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedCategoryName = (String) parentView.getItemAtPosition(position);
                Toast.makeText(requireContext(), selectedCategoryName, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Ovo se poziva kada nema ničega selektovanog
            }
        });

        return root;
    }

    private List<String> getSpinnerDataAsNames() {

        List<String> categoryNames = new ArrayList<>();
        categoryNames.add(Category.LEISURE.name());
        categoryNames.add(Category.WORK.name());
        categoryNames.add(Category.TRAVEL.name());
        List<String> c = new ArrayList<>();
        return categoryNames;
    }


    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), R.style.DatePickerTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                year = selectedYear;
                month = selectedMonth;
                day = selectedDay;
                updateDateEditText();
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(), R.style.TimePickerTheme, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                hour = selectedHour;
                minute = selectedMinute;
                updateTimeEditText();
            }
        }, hour, minute, true);
        timePickerDialog.show();
    }

    private void updateDateEditText() {
        pickDateEditText.setText(year + "-" + (month + 1) + "-" + day);
    }

    private void updateTimeEditText() {
        pickTimeEditText.setText(hour + ":" + minute);
    }


}