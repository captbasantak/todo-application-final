package com.example.finaltodoapplicaction;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.core.content.ContextCompat;

import com.example.finaltodoapplicaction.Model.ToDoModel;
import com.example.finaltodoapplicaction.Utils.DatabaseHandler;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Calendar;
import java.util.Objects;

public class AddNewTask extends BottomSheetDialogFragment {
    public static final String TAG = "ActionButtonDialog";

    private EditText newTaskText, newTaskDate;
    private Button newTaskSaveButton;
    private DatabaseHandler db;
    private DatePickerDialog datePicker;

    public static AddNewTask newInstance()
    {
        return new AddNewTask();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.new_task, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }

    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState ){
        super.onViewCreated(view, savedInstanceState);
        newTaskText = view.findViewById(R.id.newTaskText);
        newTaskDate = view.findViewById(R.id.newDate);
        newTaskDate.setOnTouchListener((view1, motionEvent)->
        {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                final Calendar calendar = Calendar.getInstance();
                int mYear = calendar.get(Calendar.YEAR);
                int mMonth = calendar.get(Calendar.MONTH);
                int mDay = calendar.get(Calendar.DAY_OF_MONTH);

                datePicker = new DatePickerDialog(getActivity(),
                        (viewOne, year, monthOfYear, dayOfMonth) -> {
                            newTaskDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            datePicker.dismiss();
                        }, mYear, mMonth, mDay);
                datePicker.getDatePicker();
                datePicker.show();
            }
            return true;
        });
        newTaskSaveButton= getView().findViewById(R.id.newTaskButton);

        db = new DatabaseHandler(getActivity());
        db.openDatabase();
        boolean isUpdate = false;
        final Bundle bundle = getArguments();
        if(bundle !=null){
            isUpdate = true;
            String task = bundle.getString("task");
            newTaskText.setText(task);
            String date = bundle.getString("date");
            newTaskDate.setText(date);
            if(task.length()>0)
                newTaskSaveButton.setTextColor(ContextCompat.getColor(getContext(), R.color.add));
        }
        newTaskText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }


            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(toString().equals("")){
                    newTaskSaveButton.setEnabled(false);
                    newTaskSaveButton.setTextColor(Color.BLACK);
                }
                else {
                    newTaskSaveButton.setEnabled(true);
                    newTaskSaveButton.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                    newTaskSaveButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.add));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        boolean finalIsUpdate = isUpdate;
        newTaskSaveButton.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            String text = newTaskText.getText().toString();
                            String date = newTaskDate.getText().toString();
                            ToDoModel task = null;
                            if (finalIsUpdate) {
                                db.updateTask(bundle.getInt("id"), text);
                                db.updateDate(bundle.getInt("id"), date);
                            } else {
                                task = new ToDoModel();
                                task.setTasks(text);
                                task.setStatus(0);
                                task.setDateTask(date);
                                 db.insertTask(task);
                            }
                            dismiss();
                        }

                    });
    }
    @Override
    public void onDismiss(DialogInterface dialog){
        Activity activity = getActivity();
        if(activity instanceof DialogClosedListner){
            ((DialogClosedListner)activity).handleDialogClosed(dialog);
        }
    }
}
