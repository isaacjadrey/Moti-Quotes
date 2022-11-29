package com.codingwithjadrey.motiquotes.ui.quotelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.codingwithjadrey.motiquotes.MotiQuoteApplication
import com.codingwithjadrey.motiquotes.R
import com.codingwithjadrey.motiquotes.ui.detailqoute.QuoteAdapter
import com.codingwithjadrey.motiquotes.databinding.FragmentMotiveListBinding
import com.codingwithjadrey.motiquotes.ui.viewmodel.QuotesViewModel
import com.codingwithjadrey.motiquotes.ui.viewmodel.QuotesViewModelFactory


class MotiveListFragment : Fragment() {
    private val quotesViewModel: QuotesViewModel by activityViewModels {
        QuotesViewModelFactory((activity?.application as MotiQuoteApplication).quoteDatabase.motiveDao())
    }

    private var _binding: FragmentMotiveListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMotiveListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = QuoteAdapter {
            val detailQuote =
                MotiveListFragmentDirections.actionMotiveListFragmentToMotiveDetailFragment(it.id)
            findNavController().navigate(detailQuote)
        }
        binding.motiveRecycler.layoutManager = LinearLayoutManager(context)
        binding.motiveRecycler.adapter = adapter
        quotesViewModel.getAllMotiveItems.observe(this.viewLifecycleOwner) { quoteItem ->
            quoteItem.let { adapter.submitList(it) }
        }

        binding.addMotiveBtn.setOnClickListener {
            val addMotiveQuote =
                MotiveListFragmentDirections.actionMotiveListFragmentToAddMotiveFragment(getString(R.string.add_moti_quote))
            findNavController().navigate(addMotiveQuote)
        }
    }

    /**
     * this is called when the fragment gets destroyed
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}