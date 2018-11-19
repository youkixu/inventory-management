package com.yx.itracker;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.opencsv.CSVWriter;
import com.yx.itracker.Class.Order;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class OptionsFragment extends Fragment {

    private OnOptionsInteractionListener mListener;

    public OptionsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnOptionsInteractionListener) {
            mListener = (OnOptionsInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement onOptionsInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnOptionsInteractionListener {
        List<Order> getOrderList();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_options, container, false);

        Button btnExportToCsv = view.findViewById(R.id.btnExportToCsv);

        btnExportToCsv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String baseDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
                String fileName = "iTrackerData.csv";
                String filePath = baseDir + File.separator + fileName;
                File f = new File(filePath);
                CSVWriter writer;
                try {
                    if(f.exists() && !f.isDirectory()){
                        FileWriter mFileWriter = new FileWriter(filePath , true);
                        writer = new CSVWriter(mFileWriter);
                        String[] title = {"Item Name", "ID", "Type", "Quantity", "Price"};
                        writer.writeNext(title);
                        showAlertBox("Export Success!");
                    }
                    else {
                        writer = new CSVWriter(new FileWriter(filePath));
                    }
                    List<Order> data = mListener.getOrderList();
                    writer.writeNext(convertToStringArray(data));
                    writer.close();
                } catch ( Exception e ) {
                    showAlertBox("There was an error exporting.");
                    Log.d("aa", "ERPRR SAVING CSV: " + e.getMessage());
                }
            }
        });


        return view;
    }
    public void showAlertBox(String msg) {
        DialogFragment dialog = new AlertDialogFragment();
        Bundle b = new Bundle();
        b.putString("title", "Options");
        b.putString("msg", msg);
        dialog.setArguments(b);
        dialog.show(getActivity().getSupportFragmentManager(), "fragmentAlertDialog");
    }

    public String[] convertToStringArray(List<Order> originalData) {
        ArrayList<String> result = new ArrayList<>();
        for(Order order : originalData) {
            result.add(order.getItem().getName() + "," + order.getItem().getId() +"," + order.getType() + "," + order.getQuantity() + "," + order.getPrice());
        }
        return result.toArray(new String[result.size()]);
    }

}
