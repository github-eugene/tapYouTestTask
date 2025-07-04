package com.eugene.testtaskfortechspire.ui.fragments.graph

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton.OnCheckedChangeListener
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.drawToBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.eugene.testtaskfortechspire.App
import com.eugene.testtaskfortechspire.R
import com.eugene.testtaskfortechspire.databinding.FragmentGraphBinding
import com.eugene.testtaskfortechspire.ui.utils.string
import com.eugene.testtaskfortechspire.ui.utils.toast
import kotlinx.coroutines.launch
import javax.inject.Inject

class GraphFragment : Fragment() {

    private var _binding: FragmentGraphBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<GraphViewModel>{ viewModelFactory }

    private var pointsAdapter = PointsAdapter()
    private var checkedChangeListener: OnCheckedChangeListener? = null
    private lateinit var storagePermissionLauncher: ActivityResultLauncher<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity().application as App).appComponent.inject(this)

        storagePermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                saveGraph()
            } else {
                toast(string(R.string.no_storage_permission_gained))
            }
        }
    }

    private fun saveGraph() = with(binding) {
        viewModel.saveGraph(gvPointsGraph.drawToBitmap())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGraphBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val points = GraphFragmentArgs.fromBundle(requireArguments()).points.toList()
        viewModel.setPoints(points)
        initObservers()
        initView()
    }

    private fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.stateFlow.collect { state ->
                    initState(state)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.eventFlow.collect { event ->
                    when (event) {
                        GraphUiEvent.FileSaved-> toast(string(R.string.file_save_success))
                        GraphUiEvent.FileSaveError-> toast(string(R.string.file_save_failed))
                    }
                }
            }
        }
    }

    private fun initState(state: GraphUiState) {
        createTable(state)
        createGraph(state)
    }

    private fun createGraph(state: GraphUiState) = with(binding) {
        val pointsList = state.points
        if (pointsList.isEmpty()) return@with
        gvPointsGraph.setPoints(pointsList, isSmoothLine = state.smoothLine)
    }

    private fun createTable(state: GraphUiState) = with(binding) {
        if(state.points.isEmpty()) return@with
        pointsAdapter.setItems(state.points)
    }

    private fun initView() {
        initRecyclerView()
        initSaveGraphAction()
        initCheckBox()
    }

    private fun initSaveGraphAction() = with(binding) {
        btnSaveGraph.setOnClickListener {
            checkPermissionAndSaveGraph()
        }
    }

    private fun checkPermissionAndSaveGraph() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            saveGraph()
        } else {
            val permission = Manifest.permission.WRITE_EXTERNAL_STORAGE
            if (ContextCompat.checkSelfPermission(
                    requireContext(), permission
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                saveGraph()
            } else {
                storagePermissionLauncher.launch(permission)
            }
        }
    }

    private fun initCheckBox() = with(binding) {
        checkedChangeListener = OnCheckedChangeListener { p0, value ->
            viewModel.setSmoothness(value)
        }
        cbGraphSmooth.setOnCheckedChangeListener(checkedChangeListener)
    }

    private fun initRecyclerView() = with(binding) {
        rvPointsTable.adapter = pointsAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        checkedChangeListener = null

        _binding = null
    }

}