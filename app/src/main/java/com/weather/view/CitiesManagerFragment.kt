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
    override val model:CitiesManagerViewModel by viewModels {
        ViewModelFactory(this::class.java.simpleName, requireActivity().application)
    }
    override val localCitiesObserver: (Result<List<City>>) -> Unit = {
        if (it is Result.Success && it.data.isEmpty())
            findNavController().navigate(R.id.action_citiesManagerFragment_to_addCityFragment)
    }
    private var mItemTouchHelper: ItemTouchHelper? = null

    override fun inflate(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_cities_manager, container, false)
        with(binding as FragmentCitiesManagerBinding) {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = model
        }
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_cities_manager, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                findNavController().navigateUp()
                true
            }
            R.id.action_add_city -> {
                findNavController().navigate(R.id.action_citiesManagerFragment_to_addCityFragment)
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
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
        with(rv_local_cities) {
            val adapter = RvLocalCitiesAdapter(this@CitiesManagerFragment)
            this.adapter = adapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            val callback: ItemTouchHelper.Callback = SimpleItemTouchHelperCallback(adapter)
            mItemTouchHelper = ItemTouchHelper(callback)
            mItemTouchHelper!!.attachToRecyclerView(rv_local_cities)
        }
    }

    // эти 2 метода нужны, чтобы активировать перетаскивание/свайп нажатием на view-компоненты
    //адаптер их не вызывает. пока что ненужные
    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder?) {
        mItemTouchHelper!!.startDrag(viewHolder!!)
    }

    override fun onStartSwipe(viewHolder: RecyclerView.ViewHolder?) {
        mItemTouchHelper!!.startSwipe(viewHolder!!)
    }

    //а эти нужные. тут конкретный юзкейс
    override fun deleteCity(city: City) {
        model.deleteCity(city)
    }

    override fun reorderCities(reorderedCities: List<City>) {
        model.reorderCities(reorderedCities)
    }

    override fun onItemClick(position: Int) {
        sharedModel.focusedCityPos = position
        findNavController().popBackStack()
    }
}