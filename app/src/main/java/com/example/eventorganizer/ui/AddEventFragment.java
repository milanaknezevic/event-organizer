package com.example.eventorganizer.ui;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.eventorganizer.MainActivity;
import com.example.eventorganizer.R;
import com.example.eventorganizer.enums.Category;
import com.example.eventorganizer.model.entities.Event;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.imaginativeworld.whynotimagecarousel.ImageCarousel;
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddEventFragment extends Fragment {

    Spinner categorySpinner;
    View root;
    ArrayAdapter<String> spinnerAdapter;
    private List<Category> categories = new ArrayList<>();
    private TextView pickLocationEditText;
    private TextView pickDateEditText;
    private TextView pickTimeEditText;
    private Calendar calendar;
    private ActivityResultLauncher<Intent> startGallery;
    private ImageCarousel imageViewPager;
    private FloatingActionButton addPhotoButton;
    private int year, month, day, hour, minute;
    private File photoFile;
    private Button submit;
    private int numberOfImages = 0;
    EditText nameEditText;
    EditText descriptionEditText;
    String selectedCategoryName;


    private static final int REQUEST_LOCATION_SELECTION = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2; // Changed the request code to 2 for image capture
    private static final int REQUEST_IMAGE_GALLERY = 3; // Use a different request code for image selection from the gallery


    private Uri photoUri;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

        root = inflater.inflate(R.layout.fragment_add_event, container, false);
        categorySpinner = root.findViewById(R.id.category);
        spinnerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, getSpinnerDataAsNames());
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(spinnerAdapter);
        pickDateEditText = root.findViewById(R.id.pickDateEditText);
        pickTimeEditText = root.findViewById(R.id.pickTimeEditText);
        nameEditText = root.findViewById(R.id.name);
        descriptionEditText = root.findViewById(R.id.description);


       /* startGallery = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent intent = result.getData();
                if (intent != null) {
                    ClipData selectedImages = intent.getClipData();
                    Uri selectedImage = intent.getData();

                    if (selectedImages != null) {
                        for (int i = 0; i < selectedImages.getItemCount(); i++) {
                            Uri imageUri = selectedImages.getItemAt(i).getUri();
                            // Dodaj odabranu sliku u carousel
                            imageViewPager.addData(new CarouselItem(imageUri.toString()));
                        }
                    } else if (selectedImage != null) {
                        // Dodaj odabranu sliku u carousel
                        imageViewPager.addData(new CarouselItem(selectedImage.toString()));
                    }
                }
            }
        });*/


        submit = root.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields()) {
                    String name = nameEditText.getText().toString();
                    String description = descriptionEditText.getText().toString();
                    String date = pickDateEditText.getText().toString();
                    String time = pickTimeEditText.getText().toString();
                    String location = pickLocationEditText.getText().toString();
                    Category category = Category.valueOf(selectedCategoryName);
                    if(numberOfImages ==0)
                    {
                        //dodaj samo event
                        MainActivity.dbHelper.insertEvent(new Event(name,description,time,location,category));
                    }else{
                        //dodaj i slike
                    }
                    nameEditText.setText("");
                    descriptionEditText.setText("");
                    pickDateEditText.setText("");
                    pickTimeEditText.setText("");
                    pickLocationEditText.setText("");
                    categorySpinner.setSelection(0); // Postavi odabrani element na prvi u spinneru
                    //imageViewPager.removeAllData(); // Očisti slike iz Carousel-a
                    numberOfImages = 0;

                   Toast.makeText(requireContext(), getString(R.string.activityAdded), Toast.LENGTH_SHORT).show();
                    //requireActivity().finish();
                    fragmentManager.popBackStack();

                }
            }
        });
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        addPhotoButton = root.findViewById(R.id.addPhotoButton);
        imageViewPager = root.findViewById(R.id.carousel);
        addPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageSelectionOptions();
            }
        });
        pickLocationEditText = root.findViewById(R.id.pickLocationEditText);
        pickLocationEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), MapsActivity.class);
                startActivityForResult(intent, REQUEST_LOCATION_SELECTION);
            }
        });
        pickLocationEditText.setFocusable(false);

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
                 selectedCategoryName = (String) parentView.getItemAtPosition(position);

                if (selectedCategoryName.equals(Category.LEISURE.name())) {
                    addPhotoButton.setVisibility(View.VISIBLE);
                    imageViewPager.setVisibility(View.VISIBLE);
                } else {
                    addPhotoButton.setVisibility(View.GONE);
                    imageViewPager.setVisibility(View.GONE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

        return root;
    }

    private void showImageSelectionOptions() {
        CharSequence[] options = {"Use Camera", "Upload from Gallery", "Upload from Internet"};
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Select an option");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (item == 0) {

                    openCamera();
                } else if (item == 1) {

                    openGallery();
                } else if (item == 2) {

                    showInternetImageDialog();
                }
            }
        });
        builder.show();
    }


    private List<String> getSpinnerDataAsNames() {

        List<String> categoryNames = new ArrayList<>();
        categoryNames.add(Category.TRAVEL.name());
        categoryNames.add(Category.WORK.name());
        categoryNames.add(Category.LEISURE.name());
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
        pickDateEditText.setError(null);
    }

    private void updateTimeEditText() {

        pickTimeEditText.setText(hour + ":" + minute);
        pickDateEditText.setError(null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LOCATION_SELECTION && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                String selectedLocationName = data.getStringExtra("selectedLocationName");
                if (selectedLocationName != null) {

                    pickLocationEditText.setText(selectedLocationName);
                }
            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            if (photoUri != null) {
                // Dodaj sliku u carousel
                imageViewPager.addData(new CarouselItem(photoUri.toString()));
                numberOfImages++;
            }
        } else if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                ClipData selectedImages = data.getClipData();
                Uri selectedImage = data.getData();

                if (selectedImages != null) {
                    for (int i = 0; i < selectedImages.getItemCount(); i++) {
                        Uri imageUri = selectedImages.getItemAt(i).getUri();
                        // Dodaj odabranu sliku u carousel
                        imageViewPager.addData(new CarouselItem(imageUri.toString()));
                        numberOfImages++;
                    }
                } else if (selectedImage != null) {
                    // Dodaj odabranu sliku u carousel
                    imageViewPager.addData(new CarouselItem(selectedImage.toString()));
                    numberOfImages++;
                }
            }
        }
    }


    private void showInternetImageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Enter Image URL");

        final EditText input = new EditText(requireContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String imageUrl = input.getText().toString();

                imageViewPager.addData(new CarouselItem(imageUrl));
                numberOfImages++;
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void openCamera() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            dispatchTakePictureIntent();
        } else {
            requestCameraPermission.launch(Manifest.permission.CAMERA);
        }
    }

    private final ActivityResultLauncher<String> requestCameraPermission = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            isGranted -> {
                if (isGranted) {
                    dispatchTakePictureIntent();
                } else {
                    // Handle permission denied
                }
            }
    );

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoFile = createImageFile();
        photoUri = FileProvider.getUriForFile(
                requireContext(),
                "com.example.eventorganizer.fileprovider",
                photoFile
        );
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
    }

    private File createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        String imageFileName = "JPEG_" + timeStamp + "_";

        try {
            File imageFile = File.createTempFile(imageFileName, ".jpg", storageDir);
            return imageFile;
        } catch (IOException e) {
            // Handle file creation error
            return null; // ili neku drugu grešku
        }
    }


    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, REQUEST_IMAGE_GALLERY); // Use REQUEST_IMAGE_GALLERY
    }

    private boolean validateFields() {
        EditText nameEditText = root.findViewById(R.id.name);
        EditText descriptionEditText = root.findViewById(R.id.description);
        TextView pickDateEditText = root.findViewById(R.id.pickDateEditText);
        TextView pickTimeEditText = root.findViewById(R.id.pickTimeEditText);
        TextView pickLocationEditText = root.findViewById(R.id.pickLocationEditText);

        String name = nameEditText.getText().toString();
        String description = descriptionEditText.getText().toString();
        String date = pickDateEditText.getText().toString();
        String time = pickTimeEditText.getText().toString();
        String location = pickLocationEditText.getText().toString();

        if (name.isEmpty()) {
            nameEditText.setError("Name is required");
            return false;
        }

        if (description.isEmpty()) {
            descriptionEditText.setError("Description is required");
            return false;
        }

        if (date.isEmpty()) {
            pickDateEditText.setError("Date is required");
            return false;
        }

        if (time.isEmpty()) {
            pickTimeEditText.setError("Time is required");
            return false;
        }

        if (location.isEmpty()) {
            pickLocationEditText.setError("Location is required");
            return false;
        }


        return true;
    }


}