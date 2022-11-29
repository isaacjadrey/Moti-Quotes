package com.codingwithjadrey.motiquotes.ui.detailqoute

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.codingwithjadrey.motiquotes.MotiQuoteApplication
import com.codingwithjadrey.motiquotes.R
import com.codingwithjadrey.motiquotes.data.entity.Motive
import com.codingwithjadrey.motiquotes.databinding.FragmentMotiveDetailBinding
import com.codingwithjadrey.motiquotes.ui.viewmodel.QuotesViewModel
import com.codingwithjadrey.motiquotes.ui.viewmodel.QuotesViewModelFactory
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
                detailSource.text = motive.quoteSource
                detailMotiveItem.text = motive.motiveQuote

                detailDeleteBtn.setOnClickListener { showConfirmationDialog() }
                detailEditBtn.setOnClickListener { editQuote() }
            }
        }
    }

    /**
     * shows confirmation before user deletes the current item
     */
    private fun showConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.delete)
            .setMessage(R.string.delete_question)
            .setCancelable(false)
            .setNegativeButton(R.string.no) { _, _ -> }
            .setPositiveButton(R.string.yes) { _, _ -> deleteQuote() }
            .show()
    }

    /**
     * Deletes the current  quote item and navigates to the quote list
      */
    private fun deleteQuote() {
        quotesViewModel.deleteQuote(motive)
        findNavController().navigate(R.id.action_motiveDetailFragment_to_motiveListFragment)
    }

    /**
     * directs user to add quote fragment with the details of the current quote and
     * sets the add quote fragment to edit mode for the selected quote item
     */
    private fun editQuote() {
        val editDirections =
            MotiveDetailFragmentDirections.actionMotiveDetailFragmentToAddMotiveFragment(
                getString(R.string.edit_quote), motive.id
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

    /**
     * function call to share the quote when a user clicks on the share icon
     */
    private fun shareQuote() {
        val shareIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, motive.motiveQuote)
            type = "text/plain"
        }
        startActivity(Intent.createChooser(shareIntent, "Share Quote"))
    }

    /**
     * this is called when the fragment is destroyed
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}