package com.codingwithjadrey.motiquotes.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.codingwithjadrey.motiquotes.MotiQuoteApplication
import com.codingwithjadrey.motiquotes.adapter.QuoteAdapter
import com.codingwithjadrey.motiquotes.databinding.FragmentMotiveListBinding
import com.codingwithjadrey.motiquotes.model.QuotesViewModel
import com.codingwithjadrey.motiquotes.model.QuotesViewModelFactory


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
                MotiveListFragmentDirections.actionMotiveListFragmentToAddMotiveFragment("Add Moti Quote")
            findNavController().navigate(addMotiveQuote)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}