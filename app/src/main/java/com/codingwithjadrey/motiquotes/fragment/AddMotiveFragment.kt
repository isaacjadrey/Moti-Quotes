package com.codingwithjadrey.motiquotes.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.codingwithjadrey.motiquotes.MotiQuoteApplication
import com.codingwithjadrey.motiquotes.R
import com.codingwithjadrey.motiquotes.data.Motive
import com.codingwithjadrey.motiquotes.databinding.FragmentAddMotiveBinding
import com.codingwithjadrey.motiquotes.model.QuotesViewModel
import com.codingwithjadrey.motiquotes.model.QuotesViewModelFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class AddMotiveFragment : Fragment() {
    private val quotesViewModel: QuotesViewModel by activityViewModels {
        QuotesViewModelFactory((activity?.application as MotiQuoteApplication).quoteDatabase.motiveDao())
    }

    private var _binding: FragmentAddMotiveBinding? = null
    private val binding get() = _binding!!
    lateinit var motive: Motive
    private val navigationArgs: AddMotiveFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddMotiveBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val quoteId = navigationArgs.itemId
        if (quoteId > 0) {
            quotesViewModel.getQuote(quoteId).observe(this.viewLifecycleOwner) { quoteItem ->
                motive = quoteItem
                binding.apply {
                    currentDateTv.setText(motive.createdOn, TextView.BufferType.SPANNABLE)
                    motivationQuote.setText(motive.motiveQuote, TextView.BufferType.SPANNABLE)
                    saveQuoteBtn.setOnClickListener { updateQuote() }
                }
            }
        } else {
            val currentDate = LocalDateTime.now()
            val format = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
            val formattedDate = currentDate.format(format)
            binding.currentDateTv.text = formattedDate

            binding.saveQuoteBtn.setOnClickListener { validateEntry() }
        }
    }

    private fun validateEntry() {
        if (binding.motivationQuote.text.toString().isEmpty()) {
            Toast.makeText(requireContext(), "Please provide a quote", Toast.LENGTH_SHORT).show()
        } else saveNewQuote()
    }

    private fun saveNewQuote() {
        quotesViewModel.addQuote(
            binding.currentDateTv.text.toString(),
            binding.motivationQuote.text.toString()
        )
        val quoteListFragment =
            AddMotiveFragmentDirections.actionAddMotiveFragmentToMotiveListFragment()
        findNavController().navigate(quoteListFragment)
    }

    private fun updateQuote() {
        quotesViewModel.updateQuoteItem(
            this.navigationArgs.itemId,
            this.binding.currentDateTv.text.toString(),
            this.binding.motivationQuote.text.toString()
        )
        findNavController().navigate(R.id.action_addMotiveFragment_to_motiveListFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}