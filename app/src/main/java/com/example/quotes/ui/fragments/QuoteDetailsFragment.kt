package com.example.quotes.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.quotes.R
import com.example.quotes.data.quotes.Quote
import com.example.quotes.databinding.FragmentQuoteDetailsBinding
import com.example.quotes.ui.viewmodels.MainViewModel
import com.example.quotes.ui.viewmodels.QuotesUiAction
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_LONG
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class QuoteDetailsFragment : Fragment() {

    private var _binding: FragmentQuoteDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ) : View {

        _binding = FragmentQuoteDetailsBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val quote = arguments?.getSerializable("quote") as? Quote

        with(binding) {
            quoteDetailsText.text = quote?.text ?: ""
            quoteDetailsAuthor.text = quote?.author ?: ""
            deleteQuoteButton.setOnClickListener {
                quote?.let {
                    it.id?.let { id ->
                        viewModel.deleteQuote(id)
                    }
                }
            }
            editQuoteButton.setOnClickListener {
                quote?.let {
                    showEditQuoteDialog(it)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiActions
                    .collect { uiAction ->
                        when(uiAction) {
                            is QuotesUiAction.CloseQuoteDetails -> {
                                Snackbar.make(view, uiAction.message, LENGTH_LONG).show()
                                findNavController().navigate(R.id.action_QuoteDetails_to_Quotes)
                            }
                            else -> {}
                        }
                    }
            }
        }
    }

    private fun showEditQuoteDialog(quote: Quote) {
        val editQuoteCustomView = layoutInflater.inflate(R.layout.quote_dialog_custom_layout, null)
        val editQuoteTextEditText: EditText = editQuoteCustomView.findViewById(R.id.quote_dialog_input_text)
        val editQuoteAuthorEditText: EditText = editQuoteCustomView.findViewById(R.id.quote_dialog_input_author)

        editQuoteAuthorEditText.setText(quote.author)
        editQuoteTextEditText.setText(quote.text)

        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.edit_quote_dialog_title))
            .setView(editQuoteCustomView)
            .setPositiveButton(getString(R.string.save_button_text)) { dialogInterface, _ ->
                val newQuote = quote.copy(
                    author = editQuoteAuthorEditText.text.toString(),
                    text = editQuoteTextEditText.text.toString()
                )
                viewModel.updateQuote(newQuote)
                dialogInterface.dismiss()
            }
            .setNegativeButton(getString(R.string.cancel_button_text)) { dialogInterface, _ -> dialogInterface.dismiss() }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}