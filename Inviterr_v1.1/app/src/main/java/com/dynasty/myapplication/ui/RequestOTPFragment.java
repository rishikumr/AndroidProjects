package com.dynasty.myapplication.ui;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.dynasty.myapplication.utils.Constants;
import com.dynasty.myapplication.R;
import com.dynasty.myapplication.database.ConnectToServer;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RequestOTPFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RequestOTPFragment extends Fragment implements  View.OnClickListener, AdapterView.OnItemSelectedListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static final String TAG = Constants.LOG_TAG;

    EditText enter_phone_number;
    Button receiveOTP_signUp;
    ImageView back_button_otp_request;
    Handler mHandler;
    String currIPAddress;
    Toast toastMessage;
    String currPhoneNumber;
    ProgressBar progressBar;
    Spinner spinnerCountryCode;
    String currISDCode;
    NavController navController;
    public final static int SUCCESSFUL_SERVER_RESPONSE = 10;
    public final static int SERVER_ERROR = 11;
    public final static int SIGNING_PASS = 14;
    public final static int SIGNING_FAIL = 15;
    public final static int SEND_OTP_PASS = 16;
    public final static int SEND_OTP_FAIL = 17;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RequestOTPFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RequestOTPFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RequestOTPFragment newInstance(String param1, String param2) {
        RequestOTPFragment fragment = new RequestOTPFragment();
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
        return inflater.inflate(R.layout.fragment_request_o_t_p, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        enter_phone_number = requireActivity().findViewById(R.id.edit_phone_number_after_otp);
        receiveOTP_signUp = requireActivity().findViewById(R.id.receiveOTP_signUp);
        progressBar = requireActivity().findViewById(R.id.progressBar_cyclic_signup_otp);
        back_button_otp_request = requireActivity().findViewById(R.id.back_button_otp_request);
        receiveOTP_signUp.setOnClickListener(this);
        back_button_otp_request.setOnClickListener(this);
        currIPAddress = getLocalIpAddress();

        prepareCountryCodePicker();
        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message result) {
                progressBar.setVisibility(View.INVISIBLE);
                if (result.what == SUCCESSFUL_SERVER_RESPONSE) {
                    onSuccessfulServerResponse((Message) result.obj);
                } else if (result.what == SERVER_ERROR) {
                    onServerError(result);
                } else {
                    showToast("Sorry. Unknown Error");
                    Log.i(TAG, "Sorry. Unknown Error 1");
                }
            }
        };

        Log.i(TAG, "currIPAddress =" + currIPAddress);
    }

    private void prepareCountryCodePicker() {
        spinnerCountryCode = requireActivity().findViewById(R.id.countryCodeSpinner_signin);
        spinnerCountryCode.setOnItemSelectedListener(this);
        // Creating adapter for spinner
        String[] countriesCode = getResources().getStringArray(R.array.countryCode_Code);
        String[] countriesName = getResources().getStringArray(R.array.countryCode_Names);
        List<String> categories = new ArrayList<String>();
        for (int i = 0; i < countriesCode.length; i++) {
            categories.add(countriesName[i] + "  " + countriesCode[i]);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(requireActivity(), android.R.layout.simple_spinner_item, categories);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinnerCountryCode.setAdapter(dataAdapter);
        spinnerCountryCode.setSelection(0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.receiveOTP_signUp:
                Log.i(TAG, "Pressed  sign_up_button ");
                progressBar.setVisibility(View.VISIBLE);
                currPhoneNumber = enter_phone_number.getText().toString();
                ConnectToServer mServer = new ConnectToServer(mHandler);
                mServer.doGetOTP(currISDCode + enter_phone_number.getText().toString(), currIPAddress, requireActivity());
                break;

            case R.id.back_button_otp_request:
                Log.i(TAG, "Pressed sign up back button ");
                navController.navigateUp();
                break;
        }
    }





    private void onServerError(Message errorCause) {
        showToast("Server Error : " + errorCause);
        Log.i(TAG, "Server Error. Please try again!" + errorCause);
    }

    private void onSuccessfulServerResponse(Message mMessage) {
        switch (mMessage.what) {
            case SEND_OTP_PASS:
                Log.i(TAG, "Send OTP pass : " + mMessage.obj);
                onSendOTPSuccess((String) mMessage.obj);
                break;
            case SEND_OTP_FAIL:
                showToast("OTP sent error : " + mMessage.obj);
                Log.i(TAG, "Send OTP fail : " + mMessage.obj);
                break;

            default:
                showToast("Sorry. Unknown Error");
                Log.i(TAG, "Sorry. Unknown Error 2");

        }
    }

    private void onSendOTPSuccess(String mMessage) {
        showToast("OTP " + mMessage);
        final Bundle mBundle = new Bundle();
        mBundle.putString("mMobileNumber", currPhoneNumber);
        mBundle.putString("mISDCode", currISDCode);
        navController.navigate(R.id.action_requestOTPFragment_to_signUpFragment , mBundle);
    }

    private void showToast(String messgae) {
        if (toastMessage != null) toastMessage.cancel();
        toastMessage = Toast.makeText(requireActivity(), messgae, Toast.LENGTH_SHORT);
        toastMessage.show();
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        currISDCode = getResources().getStringArray(R.array.countryCode_Code)[position];
        currISDCode = currISDCode.substring(1);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public String getLocalIpAddress() {
        WifiManager wm = (WifiManager) requireActivity().getSystemService(Context.WIFI_SERVICE);
        String curIP= Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());

        if(curIP.equals("0.0.0.0")){
            return getMobileIPAddress().substring(0,25);
        }
        return curIP;

    }
    public static String getMobileIPAddress() {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        return  addr.getHostAddress();
                    }
                }
            }
        } catch (Exception ex) { } // for now eat exceptions
        return "";
    }


}