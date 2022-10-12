package com.codingwithjadrey.motiquotes.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.codingwithjadrey.motiquotes.MotiQuoteApplication
import com.codingwithjadrey.motiquotes.data.Motive
import com.codingwithjadrey.motiquotes.databinding.FragmentMotiveDetailBinding
import com.codingwithjadrey.motiquotes.model.QuotesViewModel
import com.codingwithjadrey.motiquotes.model.QuotesViewModelFactory

class MotiveDetailFragment: Fragment() {

    private val quotesViewModel: QuotesViewModel by activityViewModels {
        QuotesViewModelFactory((activity?.application as MotiQuoteApplication).quoteDatabase.motiveDao())
    }

    private var _binding: FragmentMotiveDetailBinding? = null
    private val binding get() = _binding!!
    lateinit var motive: Motive
    private val navigationArgs: MotiveDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMotiveDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val quoteId = navigationArgs.itemId
        quotesViewModel.getQuote(quoteId).observe(this.viewLifecycleOwner) { quoteItem ->
            motive = quoteItem
            binding.apply {
                detailMotiveDate.text = motive.createdOn
                detailMotiveItem.text = motive.motiveQuote
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}