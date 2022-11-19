package com.example.quotes.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quotes.R
import com.example.domain.quotes.Quote
import com.example.quotes.databinding.FragmentQuotesBinding
import com.example.quotes.ui.adapters.QuotesAdapter
import com.example.quotes.ui.viewmodels.MainViewModel
import com.example.quotes.ui.viewmodels.QuotesUiAction
import com.example.quotes.ui.viewmodels.QuotesUiState
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_LONG
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class QuotesFragment : Fragment() {

    private var _binding: FragmentQuotesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModel()
    private lateinit var quotesAdapter: QuotesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) : View {

        _binding = FragmentQuotesBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.randomQuoteFab.setOnClickListener {
            viewModel.getRandomQuote()
        }

        binding.addQuoteFab.setOnClickListener {
            showNewQuoteDialog()
        }

        with(binding.quotesRecyclerView) {
            adapter = QuotesAdapter(arrayListOf()) {
                val bundle = bundleOf("quote" to it)
                findNavController().navigate(R.id.action_Quotes_to_QuoteDetails, bundle)
            }.also {
                quotesAdapter = it
            }
            addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState
                    .collect { uiState ->
                        when(uiState) {
                            is QuotesUiState.Success -> {
                                quotesAdapter.updateQuotes(uiState.quotes)
                            }
                            is QuotesUiState.Error -> {
                                Snackbar.make(view, uiState.message, LENGTH_LONG).show()
                            }
                        }
                    }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiActions
                    .collect { uiAction ->
                        when(uiAction) {
                            is QuotesUiAction.ShowRandomQuote -> {
                                AlertDialog.Builder(requireContext())
                                    .setTitle(getString(R.string.random_quote_title))
                                    .setMessage("${uiAction.quote?.text ?: ""}\n by ${uiAction.quote?.author ?: ""}")
                                    .setPositiveButton("Thanks!", null)
                                    .show()
                            }
                            else -> {}
                        }
                    }
            }
        }

    }

    private fun showNewQuoteDialog() {
        val newQuoteCustomView = layoutInflater.inflate(R.layout.quote_dialog_custom_layout, null)
        val newQuoteTextEditText: EditText = newQuoteCustomView.findViewById(R.id.quote_dialog_input_text)
        val newQuoteAuthorEditText: EditText = newQuoteCustomView.findViewById(R.id.quote_dialog_input_author)

        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.new_quote_dialog_title))
            .setView(newQuoteCustomView)
            .setPositiveButton(getString(R.string.save_button_text)) { dialogInterface, _ ->
                val quote = Quote(
                    id = null,
                    author = newQuoteAuthorEditText.text.toString(),
                    text = newQuoteTextEditText.text.toString()
                )
                viewModel.addQuote(quote)
                dialogInterface.dismiss()
            }
            .setNegativeButton(getString(R.string.cancel_button_text)) { dialogInterface, _ -> dialogInterface.dismiss() }
            .show()
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshQuotes()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}