package com.saptarshisamanta.nohunger.ui.food

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.saptarshisamanta.nohunger.R
import com.saptarshisamanta.nohunger.adapters.FoodClickListener
import com.saptarshisamanta.nohunger.adapters.FoodItemAdapter
import com.saptarshisamanta.nohunger.adapters.LoadStateAdapter
import com.saptarshisamanta.nohunger.adapters.RetryListener
import com.saptarshisamanta.nohunger.api.Resource
import com.saptarshisamanta.nohunger.di.NewSharedPreferences
import com.saptarshisamanta.nohunger.di.TokenSharedPreferences
import com.saptarshisamanta.nohunger.ui.RegisterActivity
import com.saptarshisamanta.nohunger.util.Constants
import com.saptarshisamanta.nohunger.util.Constants.Companion.KEY_AC_FIRST_TIME
import com.saptarshisamanta.nohunger.util.Constants.Companion.KEY_AC_TYPE
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_food.*
import javax.inject.Inject

@AndroidEntryPoint
class FoodFragment : Fragment(R.layout.fragment_food) {
    private val viewModel by viewModels<FoodFragmentViewModel>()

    @NewSharedPreferences
    @Inject
    lateinit var sharedPreferences: SharedPreferences

    @TokenSharedPreferences
    @Inject
    lateinit var tokenSharedPreferences: SharedPreferences

    //    private var adapter:FoodItemAdapter? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = FoodItemAdapter(FoodClickListener { cardView, id ->
            val t = tokenSharedPreferences.getString(Constants.KEY_TOKEN, "token")!!
            if (t == "token") {
                showRegDialog()
            } else {
                val parameter = HashMap<String, String>()
                parameter["food_id"] = id
                viewModel.claimCall(t, parameter)
//                view.button.text = "Claimed"
//                view.button.isEnabled = false
            }
        })
        recyclerview.apply {
            this.adapter = adapter.withLoadStateHeaderAndFooter(
                header = LoadStateAdapter(RetryListener { adapter.retry() } ),
                footer = LoadStateAdapter(RetryListener { adapter.retry() } )
            )
            layoutManager = LinearLayoutManager(context)
            hasFixedSize()
        }
        viewModel.currentFoods.observe(viewLifecycleOwner, {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        })
        viewModel.result.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {
                    Toast.makeText(context, "Item added to claimed list", Toast.LENGTH_SHORT).show()
                    adapter.refresh()
                }
                is Resource.Error -> {
                    if (it.data?.error == "auth_error") {
                        showRegDialog()
                    } else {
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    }
                }
                is Resource.Loading -> {
                    Toast.makeText(context, "Loading", Toast.LENGTH_SHORT).show()
                }
            }
        })
        adapter.addLoadStateListener { combinedLoadStates ->
            progress_bar.isVisible = combinedLoadStates.source.refresh is LoadState.Loading
            recyclerview.isVisible = combinedLoadStates.source.refresh is LoadState.NotLoading
            button_retry.isVisible = combinedLoadStates.source.refresh is LoadState.Error
            text_view_error.isVisible = combinedLoadStates.source.refresh is LoadState.Error

            if (combinedLoadStates.source.refresh is LoadState.NotLoading &&
                    combinedLoadStates.append.endOfPaginationReached &&
                    adapter.itemCount <1){
                recyclerview.isVisible = false
                text_view_empty.isVisible = true
            }else{
                text_view_empty.isVisible = false
            }
        }
        button_retry.setOnClickListener {
            adapter.retry()
        }
        setHasOptionsMenu(true)

    }

    private fun showRegDialog() {
        context?.let {
            MaterialAlertDialogBuilder(it)
                .setTitle("Register")
                .setMessage("You must be log in")
                .setNegativeButton("No thanks") { dialogInterface, i ->
                    dialogInterface.dismiss()
                }
                .setPositiveButton("LogIn") { dialogInterface, i ->
                    sharedPreferences.edit()
                        .putBoolean(KEY_AC_FIRST_TIME, true)
                        .remove(KEY_AC_TYPE)
                        .apply()
                    val intent = Intent(context, RegisterActivity::class.java)
                    startActivity(intent)
                }
                .show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.top_app_bar, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.queryHint = "Search by city"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    Log.e("xyz", "search")
                    recyclerview.scrollToPosition(0)
                    viewModel.searchFoods(query)
                    searchView.clearFocus()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }
}