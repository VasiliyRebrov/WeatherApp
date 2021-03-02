package com.weather.view

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.data.common.Result
import com.data.model.City
import com.weather.R
import com.weather.common.components.LocalCitiesRVAdapterListener
import com.weather.common.adapters.RvLocalCitiesAdapter
import com.weather.common.components.SimpleItemTouchHelperCallback
import com.weather.databinding.FragmentCitiesManagerBinding
import com.weather.viewmodel.CitiesManagerViewModel
import com.weather.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_cities_manager.*

class CitiesManagerFragment : BaseFragment(), LocalCitiesRVAdapterListener {

    private var _binding: FragmentCitiesManagerBinding? = null
    private val binding get() = _binding!!

    override val navResId: Int = R.id.action_citiesManagerFragment_to_addCityFragment
    private var mItemTouchHelper: ItemTouchHelper? = null

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_cities_manager, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                findNavController().navigateUp()
                true
            }
            R.id.action_settings -> {
                findNavController().navigate(R.id.action_citiesManagerFragment_to_settingsFragment)
                true
            }
            R.id.action_add_city -> {
                findNavController().navigate(R.id.action_citiesManagerFragment_to_addCityFragment)
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override val viewModel: CitiesManagerViewModel by viewModels {
        ViewModelFactory("CitiesManagerViewModel", requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        sharedViewModel.localCitiesLiveData.observe(viewLifecycleOwner) {
            if (it is Result.Success && it.data.isEmpty())
                findNavController().navigate(R.id.action_citiesManagerFragment_to_addCityFragment)
        }
//        checkExistCitiesList(R.id.action_citiesManagerFragment_to_addCityFragment)
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_cities_manager, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewmodel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
    }

    private fun initComponents() {
        initBar()
        initRecycler()
    }

    private fun initBar() {
        with(requireActivity() as AppCompatActivity) {
            setSupportActionBar(tb_cities_manager)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun initRecycler() {
        val adapter = RvLocalCitiesAdapter(this)
        rv_local_cities.adapter = adapter
        rv_local_cities.layoutManager = LinearLayoutManager(requireContext())
        rv_local_cities.setHasFixedSize(true)
        val callback: ItemTouchHelper.Callback = SimpleItemTouchHelperCallback(adapter)
        mItemTouchHelper = ItemTouchHelper(callback)
        mItemTouchHelper!!.attachToRecyclerView(rv_local_cities)
    }

    // эти 2 метода нужны, чтобы вызвать перетаскивание/свайп кнопками
    //адаптер их не вызывает. пока что ненужные
    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder?) {
        mItemTouchHelper!!.startDrag(viewHolder!!)
    }

    override fun onStartSwipe(viewHolder: RecyclerView.ViewHolder?) {
        mItemTouchHelper!!.startSwipe(viewHolder!!)
    }

    //а эти нужные. тут конкретный юзкейс
    override fun deleteCity(city: City) {
        viewModel.deleteCity(city)
    }

    override fun reorderLocalCities(reorderedCities: List<City>) {
        viewModel.reorderLocalCities(reorderedCities)
    }

    override fun onClick(position: Int) {
        sharedViewModel.focusedCityPos = position
        findNavController().popBackStack()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}