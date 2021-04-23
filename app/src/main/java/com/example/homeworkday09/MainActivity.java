package com.example.homeworkday09;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.PopupMenu;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.example.homeworkday09.databinding.ActivityMainBinding;
import com.jaiselrahman.filepicker.activity.FilePickerActivity;
import com.jaiselrahman.filepicker.config.Configurations;
import com.jaiselrahman.filepicker.model.MediaFile;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    List<String> listTypes = new ArrayList<>();
    String[] tags = {"Family", "Game", "Android", "VTC", "Friend"};
    boolean[] chooseTags = new boolean[tags.length];
    String[] weeks = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
    boolean[] chooseWeeks = new boolean[weeks.length];
    String[] tunes = {"Nexus Tune", "Winphone Tune", "Peep tune", "Nokia Tune", "Etc"};
    boolean is24HView = true;
    int lastSelectedHour = 0;
    int lastSelectedMinute = 0;
    int selectedYear = 2021;
    int selectedMonth = 0;
    int selectedDayOfMonth = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        getSupportActionBar().setTitle(R.string.create_note);

        listTypes.add(getString(R.string.type_work));
        listTypes.add(getString(R.string.type_learn));
        listTypes.add(getString(R.string.type_go_out));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listTypes);
        binding.spinnerType.setAdapter(arrayAdapter);


        binding.tvTags.setOnClickListener(v -> {

            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle(R.string.choose_tags)
                    .setMultiChoiceItems(tags, chooseTags, (dialog, which, isChecked) -> {
                        if (isChecked == true)
                            chooseTags[which] = true;
                    })
                    .setPositiveButton(R.string.ok, (dialog, which) -> {
                        LinkedHashSet<String> set = new LinkedHashSet<>();
                        for (int i = 0; i < tags.length; i++) {
                            if (chooseTags[i] == true)
                                set.add(tags[i]);
                        }
                        int len = set.toString().length();
                        binding.tvGetTags.setText(set.toString().substring(1, len - 1));
                    })
                    .setNegativeButton(R.string.cancel, null)
                    .create();
            alertDialog.show();
        });
        binding.tvWeeks.setOnClickListener(v -> {
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle(R.string.choose_weeks)
                    .setMultiChoiceItems(weeks, chooseWeeks, (dialog, which, isChecked) -> {
                        if (isChecked == true)
                            chooseWeeks[which] = true;
                    })
                    .setPositiveButton(R.string.ok, (dialog, which) -> {
                        LinkedHashSet<String> set = new LinkedHashSet<>();
                        for (int i = 0; i < weeks.length; i++) {
                            if (chooseWeeks[i] == true)
                                set.add(weeks[i]);
                        }
                        int len = set.toString().length();
                        binding.tvGetWeeks.setText(set.toString().substring(1, len - 1));
                    })
                    .setNegativeButton(R.string.cancel, null)
                    .create();
            alertDialog.show();
        });
        binding.btnTune.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(this, v);
            MenuInflater menuInflater = popupMenu.getMenuInflater();
            menuInflater.inflate(R.menu.popup_menu, popupMenu.getMenu());
            popupMenu.show();
            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.pmFromFile:
                        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED)
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.CAMERA}, 2);
                        else {
                            filePicker();
                        }
                        break;
                    case R.id.pmFromDefaults:
                        AlertDialog alertDialog = new AlertDialog.Builder(this)
                                .setTitle("")
                                .setSingleChoiceItems(tunes, 0, null)
                                .setPositiveButton(R.string.ok, null)
                                .setNegativeButton(R.string.cancel, null)
                                .create();
                        alertDialog.show();
                        break;
                }
                return true;
            });
        });

        binding.tvTime.setOnClickListener(v -> {

            TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {

                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    binding.tvTime.setText(hourOfDay + ":" + minute);
                    lastSelectedHour = hourOfDay;
                    lastSelectedMinute = minute;
                }
            };

            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    timeSetListener, lastSelectedHour, lastSelectedMinute, is24HView);
            timePickerDialog.show();
        });
        binding.tvDate.setOnClickListener(v -> {
            DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {

                    binding.tvDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                }
            };

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    dateSetListener, selectedYear, selectedMonth, selectedDayOfMonth);

            datePickerDialog.show();
        });

    }

    private void filePicker() {
        Intent intent = new Intent(MainActivity.this, FilePickerActivity.class);
        intent.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder()
                .setCheckPermission(true)
                .setShowFiles(true)
                .setShowImages(true)
                .enableImageCapture(true)
                .setMaxSelection(1)
                .setSkipZeroSizeFiles(true)
                .build());
        startActivityForResult(intent, 10);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            ArrayList<MediaFile> mediaFiles = data.getParcelableArrayListExtra(
                    FilePickerActivity.MEDIA_FILES
            );
            if (data.toString().equals("Intent { (has extras) }"))
                return;
            else {
                String path = mediaFiles.get(0).getPath();
                switch (requestCode) {
                    case 10:
                        binding.tvGetPath.setText(path.substring(path.lastIndexOf("/") + 1));
                }
            }
        } else
            binding.tvGetPath.setText("");

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 2:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, getBaseContext().getResources().getString(R.string.connect_success),
                            Toast.LENGTH_SHORT).show();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}