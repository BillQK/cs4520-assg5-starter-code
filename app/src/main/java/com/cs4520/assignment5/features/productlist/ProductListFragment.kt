package com.cs4520.assignment5.features.productlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.cs4520.assignment5.core.database.AppDatabase
import com.cs4520.assignment5.databinding.FragmentProductlistBinding
import com.cs4520.assignment5.core.model.Product
import com.cs4520.assignment5.common.Result

/*
This class extends Fragment, making it a component that can be added to an activity to encapsulate
its own UI and behavior.
 */
class ProductListFragment : Fragment() {
    private val viewModel: ProductListViewModel by viewModels {
        val productDAO = AppDatabase.getDatabase(requireContext()).productDao()
        val productRepo = ProductListRepo(productDAO, requireContext())
        ProductListViewModelFactory(productRepo)
    }

    private lateinit var binding: FragmentProductlistBinding
    private lateinit var productAdapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductlistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the adapter with onLoadMore callback
        productAdapter = ProductAdapter {
            viewModel.fetchNextPage()
        }

        binding.productRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = productAdapter
        }

        setUpObserver()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadData()
    }

    private fun setUpObserver() {
        viewModel.productList.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Success -> {
                    handleSuccess(result.data)
                }
                is Result.Error -> {
                    handleError(result.exception)
                }
                is Result.Empty -> {
                    handleEmpty()
                }
            }
        }
    }

    private fun handleSuccess(products: List<Product>) {
        binding.progressBar.visibility = View.GONE
        binding.textViewNoProduct.visibility = View.GONE

        productAdapter.addProducts(products)
    }

    private fun handleError(exception: Exception) {
        binding.progressBar.visibility = View.GONE
        if (productAdapter.itemCount == 0) {
            binding.textviewApiError.visibility = View.VISIBLE
        }
        Toast.makeText(context, "Error: ${exception.message}", Toast.LENGTH_LONG).show()
    }

    private fun handleEmpty() {
        binding.progressBar.visibility = View.GONE
        if (productAdapter.itemCount == 0) {
            binding.textViewNoProduct.visibility = View.VISIBLE
        }
        Toast.makeText(context, "No product available", Toast.LENGTH_LONG).show()
    }


}
