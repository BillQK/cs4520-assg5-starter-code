package com.cs4520.assignment5.features.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.cs4520.assignment5.R
import com.cs4520.assignment5.databinding.FragmentLoginBinding


/*
This class extends Fragment, making it a component that can be added to an activity to encapsulate
 its own UI and behavior.
 */
class LoginFragment : Fragment() {

    // Declare the view variables at the class level but do not initialize them yet
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button

    private lateinit var binding: FragmentLoginBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the views now that the onCreateView has completed and the view hierarchy is accessible
        usernameEditText = binding.usernameEditText
        passwordEditText = binding.passwordEditText
        loginButton = binding.loginButton

        // Set the click listener for the login button
        loginButton.setOnClickListener {
            // Retrieve user input
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Perform login check
            if (username == "admin" && password == "admin") {
                // Clear input fields
                usernameEditText.text.clear()
                passwordEditText.text.clear()

                // Navigate to the next fragment
                findNavController().navigate(R.id.action_loginFragment_to_productListFragment)
            } else {
                // Show an error message
                Toast.makeText(activity, "Incorrect username or password", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}
