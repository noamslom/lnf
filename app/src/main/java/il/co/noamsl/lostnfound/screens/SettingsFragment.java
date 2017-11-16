package il.co.noamsl.lostnfound.screens;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import il.co.noamsl.lostnfound.R;
import il.co.noamsl.lostnfound.ServiceLocator;
import il.co.noamsl.lostnfound.repository.User.User;
import il.co.noamsl.lostnfound.webService.eitan.Users;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SettingsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class SettingsFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    private EditText etName;
    private EditText etEmail;
    private EditText etPhone;
    private EditText etAddress;
    private User loggedInUser;
    private Button btnSubmit;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etName = (EditText) getView().findViewById(R.id.setting_et_name);
        etEmail = (EditText) getView().findViewById(R.id.setting_et_email);
        etPhone = (EditText) getView().findViewById(R.id.settings_et_phone);
        etAddress = (EditText) getView().findViewById(R.id.setting_et_address);
        btnSubmit = (Button) getView().findViewById(R.id.settings_btn_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitChanges(v);
            }
        });

        restoreSettings();
    }

    private void restoreSettings() {
        if(loggedInUser.getAddress()!=null){
            etAddress.setText(loggedInUser.getAddress());
        }
        if(loggedInUser.getEmail()!=null){
            etEmail.setText(loggedInUser.getEmail());
        }
        if(loggedInUser.getName()!=null){
            etName.setText(loggedInUser.getName());
        }
        if(loggedInUser.getPhoneNumber()!=null){
            etPhone.setText(loggedInUser.getPhoneNumber());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loggedInUser = ServiceLocator.getExternalRepository().getLoggedInUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    public void submitChanges(View view) {
        //// TODO: 17/11/2017
        String email = getEmailFromEditText();

        if(!checkEmail(email)){
            return;
        }
        String name = etName.getText()+"";
        String phone = etPhone.getText()+"";
        String address= etAddress.getText()+"";
        Integer userId = loggedInUser.getUserid();
        ServiceLocator.getExternalRepository().updateUser(new User(new Users(name, email, phone, address, userId)));
        Toast.makeText(getContext(), "Submit Successful!", Toast.LENGTH_SHORT).show();
        getActivity().onBackPressed();
    }

    @NonNull
    private String getEmailFromEditText() {
        return etEmail.getText().toString().toLowerCase();
    }

    private boolean checkEmail(String email) {
        // Reset errors.
        etEmail.setError(null);

        // Store values at the time of the login attempt.

        boolean cancel = false;
        View focusView = null;

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            etEmail.setError(getString(R.string.error_field_required));
            focusView = etEmail;
            cancel = true;

        } else if (!isEmailValid(email)) {
            etEmail.setError(getString(R.string.error_invalid_email));
            focusView = etEmail;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        }
        return !cancel;
    }

    private boolean isEmailValid(String email) {
        return Users.validateEmail(email);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

