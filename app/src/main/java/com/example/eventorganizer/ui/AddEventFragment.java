package com.example.eventorganizer.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.eventorganizer.MainActivity;
import com.example.eventorganizer.R;
import com.example.eventorganizer.model.entities.Category;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class AddEventFragment extends Fragment {

    Spinner categorySpinner;
    View root;
    ArrayAdapter<String> spinnerAdapter;
    private Map<String, Integer> categoryMap = new HashMap<>();
    EditText editText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root=inflater.inflate(R.layout.fragment_add_event, container, false);
        List<Category> categories = getSpinnerData();
        for (Category category : categories) {
            categoryMap.put(category.getName(), category.getId());
        }
       categorySpinner=root.findViewById(R.id.category);
        spinnerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, getSpinnerDataAsNames());
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(spinnerAdapter);


        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedCategoryName = (String) parentView.getItemAtPosition(position);
                int selectedCategoryId = categoryMap.get(selectedCategoryName);
                System.out.println("Izabrana kategorija: " + selectedCategoryName + " sa ID-om: " + selectedCategoryId);

                Toast.makeText(requireContext(), selectedCategoryName + " " + selectedCategoryId, Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Ovo se poziva kada nema ničega selektovanog
            }
        });

        return root;
    }
    private List<String> getSpinnerDataAsNames() {
        List<Category> categories = getSpinnerData();
        List<String> categoryNames = new ArrayList<>();
        for (Category category : categories) {
            categoryNames.add(category.getName());
        }
        return categoryNames;
    }


    private List<Category> getSpinnerData() {

        List<Category> dataFromDatabase = MainActivity.dbHelper.getAllCategories();

        return dataFromDatabase;
    }
}