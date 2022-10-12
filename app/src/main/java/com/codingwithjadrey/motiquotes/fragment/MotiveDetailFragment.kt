package com.codingwithjadrey.motiquotes.fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.codingwithjadrey.motiquotes.MotiQuoteApplication
import com.codingwithjadrey.motiquotes.R
import com.codingwithjadrey.motiquotes.data.Motive
import com.codingwithjadrey.motiquotes.databinding.FragmentMotiveDetailBinding
import com.codingwithjadrey.motiquotes.model.QuotesViewModel
import com.codingwithjadrey.motiquotes.model.QuotesViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MotiveDetailFragment : Fragment() {

    private val quotesViewModel: QuotesViewModel by activityViewModels {
        QuotesViewModelFactory((activity?.application as MotiQuoteApplication).quoteDatabase.motiveDao())
    }

    private var _binding: FragmentMotiveDetailBinding? = null
    private val binding get() = _binding!!
    lateinit var motive: Motive
    private val navigationArgs: MotiveDetailFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

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

                detailDeleteBtn.setOnClickListener { showConfirmationDialog() }
                detailEditBtn.setOnClickListener { editQuote() }
            }
        }
    }

    private fun showConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.delete)
            .setMessage(R.string.delete_question)
            .setCancelable(false)
            .setNegativeButton(R.string.no) {_,_ -> }
            .setPositiveButton(R.string.yes) {_,_ -> deleteQuote() }
            .show()
    }

    private fun deleteQuote() {
        quotesViewModel.deleteQuote(motive)
        findNavController().navigate(R.id.action_motiveDetailFragment_to_motiveListFragment)
    }

    private fun editQuote() {
        val editDirections = MotiveDetailFragmentDirections.actionMotiveDetailFragmentToAddMotiveFragment(
            "Edit Quote", motive.id
        )
        findNavController().navigate(editDirections)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.layout_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_share_quote -> {
                shareQuote()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun shareQuote() {
        val intent = Intent(Intent.ACTION_SEND)
            .setType("text/plain")
            .putExtra(Intent.EXTRA_SUBJECT, "New Quote")
            .putExtra(Intent.EXTRA_TEXT, motive.motiveQuote)
        if (activity?.packageManager?.resolveActivity(intent, 0) != null) {
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}