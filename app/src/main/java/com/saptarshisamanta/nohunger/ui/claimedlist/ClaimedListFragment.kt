package com.saptarshisamanta.nohunger.ui.claimedlist

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.saptarshisamanta.nohunger.R
import com.saptarshisamanta.nohunger.adapters.ClaimedListAdapter
import com.saptarshisamanta.nohunger.api.Resource
import com.saptarshisamanta.nohunger.di.TokenSharedPreferences
import com.saptarshisamanta.nohunger.util.Constants.Companion.KEY_TOKEN
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_claimedlist.*
import javax.inject.Inject

@AndroidEntryPoint
class ClaimedListFragment : Fragment(R.layout.fragment_claimedlist) {
    private val viewModel by viewModels<ClaimedListFragmentViewModel>()
    private val claimedAdapter = ClaimedListAdapter()

    @TokenSharedPreferences
    @Inject
    lateinit var tokenSharedPreferences: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val t = tokenSharedPreferences.getString(KEY_TOKEN, "token")!!
        recyclerview.apply {
            adapter = claimedAdapter
            layoutManager = LinearLayoutManager(context)
            hasFixedSize()
        }
        if (t == "token") {
            textView.isVisible = true
        } else {
            viewModel.claimedFoodCall(t)
        }
        viewModel.result.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {
                    claimedAdapter.submitList(it.data!!)
                }
                is Resource.Error -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {
                    Toast.makeText(context, "loading", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}