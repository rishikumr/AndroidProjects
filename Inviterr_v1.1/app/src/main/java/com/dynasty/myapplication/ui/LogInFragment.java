package com.dynasty.myapplication.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telephony.TelephonyManager;
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
import android.widget.TextView;
import android.widget.Toast;


import com.dynasty.myapplication.utils.Constants;
import com.dynasty.myapplication.R;
import com.dynasty.myapplication.database.ConnectToServer;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LogInFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LogInFragment extends Fragment implements View.OnClickListener , AdapterView.OnItemSelectedListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static final String TAG = Constants.LOG_TAG;

    TextView register_button , forgetPassword_button;
    Button login_button_login;
    Toast toastMessage;
    ImageView back_button_log_in;
    EditText enter_phone_number_login;
    EditText enter_password_login;
    Handler mHandler;
    ProgressBar progressBar;
    Spinner spinnerCountryCode;
    String currISDcode;
    NavController navController;


    public final static int SUCCESSFUL_SERVER_RESPONSE = 10;
    public final static int SERVER_ERROR = 11;
    public final static int LOGGING_PASS = 12;
    public final static int LOGGING_FAIL = 13;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LogInFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment login_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LogInFragment newInstance(String param1, String param2) {
        LogInFragment fragment = new LogInFragment();
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
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

        register_button = requireActivity().findViewById(R.id.register_button_login);
        login_button_login = requireActivity().findViewById(R.id.login_button_login);
        back_button_log_in = requireActivity().findViewById(R.id.back_button_log_in);
        enter_phone_number_login = requireActivity().findViewById(R.id.enter_phone_number_login);
        enter_password_login = requireActivity().findViewById(R.id.enter_password_login);
        progressBar = requireActivity().findViewById(R.id.progressBar_login);
        spinnerCountryCode = requireActivity().findViewById(R.id.countryCodeSpinner_login);
        forgetPassword_button = requireActivity().findViewById(R.id.forgetPassword_button);

        login_button_login.setOnClickListener(this);
        register_button.setOnClickListener(this);
        back_button_log_in.setOnClickListener(this);
        enter_phone_number_login.setOnClickListener(this);
        forgetPassword_button.setOnClickListener(this);

        prepareCountryCodePicker();
        mHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message result){
                progressBar.setVisibility(View.INVISIBLE);
                if(result.what == SUCCESSFUL_SERVER_RESPONSE){
                    onSuccessfulServerResponse((Message) result.obj);
                }else if(result.what == SERVER_ERROR){
                    onServerError((Message) result.obj);
                }else {
                    showToast("Sorry. Unknown Error");
                    Log.i(TAG, "Sorry. Unknown Error 1") ;
                }
            }
        };
    }

    private void prepareCountryCodePicker() {
        spinnerCountryCode.setOnItemSelectedListener(this);
        String[] countriesCode = getResources().getStringArray(R.array.countryCode_Code);
        String[] countriesName = getResources().getStringArray(R.array.countryCode_Names);
        List<String> categories = new ArrayList<String>();
        for(int i = 0 ; i < countriesCode.length ; i++){
            categories.add(countriesName[i] +"  "+ countriesCode[i]);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(requireActivity(), android.R.layout.simple_spinner_item, categories);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinnerCountryCode.setAdapter(dataAdapter);
        spinnerCountryCode.setSelection(0);

    }

    private void onServerError(Message errorCause) {
        showToast("errorCause");
        Log.i(TAG, "Server Error. Please try again!\n"+ errorCause);
    }

    private void onSuccessfulServerResponse(Message mMessage) {
        switch (mMessage.what) {
            case LOGGING_PASS :
                onSuccessLogIn((String)mMessage.obj);
                break;

            case LOGGING_FAIL :
                showToast("Login Failed: "+mMessage.obj );
                Log.i(TAG, "Login Failed : "+ mMessage.obj);
                break;

            default:
                showToast("Sorry. Unknown Error" );
                Log.i(TAG, "Sorry. Unknown Error 2") ;

        }
    }

    private void onSuccessLogIn(String mToken) {
        //UserInformation mUser = new UserInformation( enter_phone_number_login.getText().toString(),mToken, enter_password_login.getText().toString());
        SharedPreferences pref = requireActivity().getApplicationContext().getSharedPreferences(Constants.MyPref, 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("mUserToken", mToken);
        editor.apply();
        navController.navigate(R.id.action_login_fragment_to_homeScreenTabHost);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_button_login:
                Log.i(TAG, "Pressed  LogIn button ");
                //showToast("Please wait. Logging you In." );
                progressBar.setVisibility(View.VISIBLE);
                ConnectToServer mServer = new ConnectToServer(mHandler);
                mServer.doLogIn(currISDcode+enter_phone_number_login.getText().toString(), enter_password_login.getText().toString(), requireActivity());
                break;
            case R.id.forgetPassword_button :
            case R.id.register_button_login:
                Log.i(TAG, "Pressed  register button ");
                navController.navigate(R.id.action_login_fragment_to_requestOTPFragment);
                break;

            case R.id.back_button_log_in:
                Log.i(TAG, "Pressed  login back button ");
                navController.navigateUp();
                break;

        }
    }



    private void showToast (String messgae) {
        if(toastMessage != null)    toastMessage.cancel();
        toastMessage = Toast.makeText(requireActivity(), messgae, Toast.LENGTH_SHORT);
        toastMessage.show();
    }

    public String getCountryCode(){
        TelephonyManager manager = (TelephonyManager) requireActivity().getSystemService(Context.TELEPHONY_SERVICE);
        return manager.getSimCountryIso().toUpperCase();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        currISDcode = getResources().getStringArray(R.array.countryCode_Code)[position];
        currISDcode = currISDcode.substring(1);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }





}