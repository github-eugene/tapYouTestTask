package com.eugene.testtaskfortechspire.ui.fragments.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.eugene.testtaskfortechspire.App
import com.eugene.testtaskfortechspire.R
import com.eugene.testtaskfortechspire.databinding.FragmentMainBinding
import com.eugene.testtaskfortechspire.model.PointUiModel
import com.eugene.testtaskfortechspire.ui.utils.string
import com.eugene.testtaskfortechspire.ui.utils.toast
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<MainViewModel>{ viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity().application as App).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        initView()
    }

    private fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.stateFlow.collect { state ->
                    when (state) {
                        is MainUiState.Loading -> initLoading(isShowLoading = true)
                        is MainUiState.None -> initLoading(isShowLoading = false)
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.eventFlow.collect { event ->
                    when (event) {
                        is MainUiEvent.NavigateToGraph -> navigateToGraph(event.points)
                        is MainUiEvent.NoInternetError -> toast(string(R.string.main_err_no_connection))
                        is MainUiEvent.ServerError -> toast(string(R.string.main_err_server_error))
                        is MainUiEvent.NotValidCountError -> toast(string(R.string.main_err_wrong_count))
                    }
                }
            }
        }
    }

    private fun navigateToGraph(points: List<PointUiModel>) {
        findNavController().navigate(
            MainFragmentDirections.actionMainFragmentToGraphFragment(points.toTypedArray())
        )
    }

    private fun initView() {
        binding.btnGo.setOnClickListener { checkNumberTextAndLoadPoints() }
    }

    private fun checkNumberTextAndLoadPoints() {
        val inputNumber = binding.etPointsInput.text.toString()
        if (inputNumber.isBlank()) {
            toast(string(R.string.main_no_any_symbol))
            return
        }
        viewModel.getPoints(inputNumber.toInt())
    }

    private fun initLoading(isShowLoading: Boolean) = with(binding) {
        btnGo.isInvisible = isShowLoading
        btnGo.isEnabled = !isShowLoading
        etPointsInput.isEnabled = !isShowLoading
        pbLoadingProgress.isVisible = isShowLoading
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}