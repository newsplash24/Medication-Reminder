package com.example.www.medicationReminder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.www.medicationReminder.data.MedicationContract;
import com.example.www.medicationReminder.data.MedicationContract.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentAdd.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentAdd#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentShow extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    Button saveButton, setPDateBtn, setEDateBtn;
    EditText mNameInput, mTypeInput, mDosageInput;
    TextView mPDateLabel, mEDateLabel;
    TextView out;
    String mDateFormat = "dd/MM/yyyy";
    String pDate = "", eDate = "";
    TableLayout tableLayout;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FragmentShow() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Screen1Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentAdd newInstance(String param1, String param2) {
        FragmentAdd fragment = new FragmentAdd();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_show, container, false);


        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/helveticaneueltcom_thex.ttf");
        Typeface tfc = Typeface.createFromAsset(getActivity().getAssets(), "fonts/HelveticaNeueLT_Arabic_55_Roman.ttf");

        TextView hName = (TextView) root.findViewById(R.id.hName);
        TextView hDosage = (TextView) root.findViewById(R.id.hDosage);
        TextView hPDate = (TextView) root.findViewById(R.id.hPDate);
        TextView hEDate = (TextView) root.findViewById(R.id.hEDate);
        tableLayout=(TableLayout)root.findViewById(R.id.tableLayout);

        hName.setTypeface(tf); hPDate.setTypeface(tf);  hEDate.setTypeface(tf); hDosage.setTypeface(tf);

        Cursor cursor = getActivity().getContentResolver().query(MedicationEntry.CONTENT_URI,
                null, null, null, null);




            while (cursor.moveToNext()) {
                View tableRow = inflater.inflate(R.layout.table_show_item, null, false);
                TextView item_name = (TextView) tableRow.findViewById(R.id.item_name);
                TextView item_dosage = (TextView) tableRow.findViewById(R.id.item_dosage);
                TextView item_PDate = (TextView) tableRow.findViewById(R.id.item_PDate);
                TextView item_EDate = (TextView) tableRow.findViewById(R.id.item_EDate);

                item_name.setText(cursor.getString(cursor.getColumnIndex(MedicationEntry.COLUMN_TYPE)));
                item_dosage.setText(cursor.getString(cursor.getColumnIndex(MedicationEntry.COLUMN_DOSAGE)));
                item_PDate.setText(cursor.getString(cursor.getColumnIndex(MedicationEntry.COLUMN_PRODUCTION_DATE)));
                item_EDate.setText(cursor.getString(cursor.getColumnIndex(MedicationEntry.COLUMN_EXPIRY_DATE)));

                tableLayout.addView(tableRow);
            }





        return root;
    }

    public void setDate(final String type){
        {
            final View dialogView = View.inflate(getActivity(), R.layout.date_picker, null);
            final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();

            dialogView.findViewById(R.id.date_time_set).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.date_picker);
//                    TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.time_picker);

                    Calendar calendar = new GregorianCalendar(datePicker.getYear(),
                            datePicker.getMonth(),
                            datePicker.getDayOfMonth());

                    long time = calendar.getTimeInMillis();
                    String date = getDate(time, "dd/MM/yyyy");

                    Toast.makeText(getActivity(), ""+time, Toast.LENGTH_SHORT).show();
                    Toast.makeText(getActivity(), ""+getDate(time, "dd/MM/yyyy"), Toast.LENGTH_SHORT).show();

                    String toParse = getDate(time, "dd/MM/yyyy");
                    Toast.makeText(getActivity(), "--"+getDateMillis(toParse), Toast.LENGTH_SHORT).show();
                    if(type.equals("pDate")) {
                        mPDateLabel.setText(mPDateLabel.getText().toString() +
                                        System.getProperty("line.separator") +
                                        time
//                                getDate(time, mDateFormat)
                        );

                        pDate = date;
                    }
                    else if (type.equals("eDate")) {
                        mEDateLabel.setText(mEDateLabel.getText().toString() +
                                        System.getProperty("line.separator") +
                                        time
//                                getDate(time, mDateFormat)
                        );
                        if(System.currentTimeMillis() > time)
                            Toast.makeText(getActivity(), "Warning! this medication is expired!", Toast.LENGTH_SHORT).show();
                        eDate = date;
                    }
                    alertDialog.dismiss();
                }});
            alertDialog.setView(dialogView);
            alertDialog.show();

        }
    }

    public String getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public long getDateMillis(String toParse){

        SimpleDateFormat formatter = new SimpleDateFormat(mDateFormat);
        Date date = null; // You will need try/catch around this
        try {
            date = formatter.parse(toParse);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long millis = date.getTime();
        return millis;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
