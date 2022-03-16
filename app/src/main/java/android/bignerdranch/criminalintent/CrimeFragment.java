package android.bignerdranch.criminalintent;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.Date;
import java.util.UUID;

public class CrimeFragment extends Fragment {
    // request code:
    private static final int REQUEST_DATE = 0;
    public static final int REQUEST_TIME = 1;

    // DatePickerFragment's tag:
    private static final String DIALOG_DATE = "DialogDate";
    public static final String DIALOG_TIME = "DialogTime";
    private Crime mCrime;           // a crime object reference.
    private EditText mTitleField;   // an EditText reference
    private Button mDateButton, btnCrimeTime;     // a Button reference
    private CheckBox mSolvedCheckBox;   // CheckBox reference
    // an argument to add to the bundle
    private static final String ARG_CRIME_ID = "crime_id";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID crimeId = (UUID)getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != Activity.RESULT_OK)
            return;

        if(requestCode == REQUEST_DATE) {

            Date date = (Date) data.getSerializableExtra(
                    DatePickerFragment.EXTRA_DATE);


            mCrime.setDate(date);
            mDateButton.setText(mCrime.getDate().toString());
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(
            R.layout.fragment_crime,    // layout resource id
            container,                  // the view's parent
            false);                     // view gets added in view activity's code.

        mTitleField = (EditText)v.findViewById(R.id.crime_title);
        // set the text...
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() { // set listener
            @Override
            public void beforeTextChanged(CharSequence s,
                                          int start,
                                          int count,
                                          int after) {
                // required to override this method, but we're not using it.
            }

            @Override
            public void onTextChanged(CharSequence s,
                                      int start,
                                      int before,
                                      int count) {
                mCrime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // required to override this method, but we're not using it.
            }
        });

        mDateButton = (Button)v.findViewById(R.id.crime_date);
        mDateButton.setText(mCrime.getDate().toString());
        // mDateButton.setEnabled(false); <-- delete this
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                // replace this
                // DatePickerFragment dialog = new DatePickerFragment();
                // with this
                DatePickerFragment dialog =
                      DatePickerFragment.newInstance(mCrime.getDate());
                // set the target fragment:
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);

                dialog.show(fm,DIALOG_DATE);
            }
        });

        btnCrimeTime = (Button)v.findViewById(R.id.crime_time);
        //btnCrimeTime.setText();
        btnCrimeTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                TimePickerFragment tDialog = TimePickerFragment.newInstance(mCrime.getDate());
                tDialog.setTargetFragment(CrimeFragment.this, REQUEST_TIME);
                tDialog.show(fm,DIALOG_TIME);
            }
        });

        mSolvedCheckBox = (CheckBox)v.findViewById(R.id.crime_solved);
        // update the check box...
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(
            new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(
                    CompoundButton buttonView,
                    boolean isChecked) {
                    mCrime.setSolved(isChecked); } } );
        return v;
    }

    public static CrimeFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);
        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }
}

