package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(
            this, MainViewModel.MainViewModelFactory(requireNotNull(activity?.application))
        ).get(MainViewModel::class.java)
    }
    lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        binding.asteroidRecycler.adapter =
            AsteroidAdapter(AsteroidAdapter.onClickListener { asteroid ->
                viewModel.NavigateHelper(asteroid)
            })

        viewModel.NavigateToDetail.observe(viewLifecycleOwner, Observer { asteroid ->
            asteroid?.let {
                this.findNavController().navigate(MainFragmentDirections.actionShowDetail(asteroid))
                viewModel.onNavigateToDetailComplete()
            }

        })

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        //inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }
}
