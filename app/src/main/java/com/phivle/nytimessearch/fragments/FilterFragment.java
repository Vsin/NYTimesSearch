package com.phivle.nytimessearch.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Spinner;

import com.phivle.nytimessearch.R;

import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class FilterFragment extends DialogFragment implements View.OnClickListener {

    private CheckBox mFashionStyleCheckbox;
    private CheckBox mSportsCheckbox;
    private DatePicker mBeginDateDatePicker;
    private CheckBox mArtsCheckbox;
    private Spinner mSortOrderSpinner;
    private Button mDoneButton;
    private SharedPreferences mFilters;
    private String mBeginDate;
    private String mSortOrder;
    private Set<String> mNewsDesk;

    private final static SimpleDateFormat DATEFORMAT = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());

    public FilterFragment() {
        // Required empty public constructor
    }

    public static FilterFragment newInstance() {
        FilterFragment fragment = new FilterFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFilters = getActivity().getSharedPreferences("filters", Context.MODE_PRIVATE);
        mBeginDate = mFilters.getString("begin_date", null);
        try {
            mSortOrder = mFilters.getString("sort_order", null).toLowerCase();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        mNewsDesk = mFilters.getStringSet("news_desk", null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_filter, container);
        setupViews(view);
        mDoneButton.setOnClickListener(this);
        return view;
    }

    private void setupViews(View view) {
        mBeginDateDatePicker = view.findViewById(R.id.beginDatePicker);
        setupDatePicker();

        mSortOrderSpinner = view.findViewById(R.id.sortOrderSpinner);
        setupSpinner();

        mArtsCheckbox = view.findViewById(R.id.checkbox_arts);
        mFashionStyleCheckbox = view.findViewById(R.id.checkbox_fashion_style);
        mSportsCheckbox = view.findViewById(R.id.checkox_sports);

        if (mNewsDesk.contains(getString(R.string.arts))) {
            mArtsCheckbox.setChecked(true);
        }

        if (mNewsDesk.contains(getString(R.string.sports))) {
            mSportsCheckbox.setChecked(true);
        }

        if (mNewsDesk.contains(getString(R.string.fashion_and_style))) {
            mFashionStyleCheckbox.setChecked(true);
        }

        mDoneButton = view.findViewById(R.id.filter_done);
    }

    private void setupSpinner() {
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) mSortOrderSpinner.getAdapter();
        int selectionPosition = adapter.getPosition(StringUtils.capitalize(mSortOrder));
        mSortOrderSpinner.setSelection(selectionPosition);
    }

    private void setupDatePicker() {
        Calendar cal = Calendar.getInstance();
        Date date = null;
        try {
            date = DATEFORMAT.parse(mBeginDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        mBeginDateDatePicker.updateDate(year, month, day);
    }

    @Override
    public void onClick(View view) {
        SharedPreferences.Editor editor = mFilters.edit();
        editor.clear();
        int day = mBeginDateDatePicker.getDayOfMonth();
        int month = mBeginDateDatePicker.getMonth();
        int year = mBeginDateDatePicker.getYear();
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        String beginDate = DATEFORMAT.format(calendar.getTime());
        Set<String> newsDesk = new HashSet<>();
        editor.putString("begin_date", beginDate);
        editor.putString("sort_order", mSortOrderSpinner.getSelectedItem().toString());
        if (mArtsCheckbox.isChecked()) {
            newsDesk.add(mArtsCheckbox.getText().toString());
        }

        if (mSportsCheckbox.isChecked()) {
            newsDesk.add(mSportsCheckbox.getText().toString());
        }

        if (mFashionStyleCheckbox.isChecked()) {
            newsDesk.add(mFashionStyleCheckbox.getText().toString());
        }

        editor.putStringSet("news_desk", newsDesk);

        editor.apply();
        dismiss();
    }
}
