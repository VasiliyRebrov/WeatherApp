package com.weather.view

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.data.model.City
import com.data.repo.CitiesManagerRepo
import com.weather.R
import com.weather.components.MyListener
import com.weather.components.RvLocalCitiesAdapter
import com.weather.components.SimpleItemTouchHelperCallback
import com.weather.databinding.FragmentCitiesManagerBinding
import com.weather.viewmodel.CitiesManagerViewModel
import com.weather.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_cities_manager.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class CitiesManagerFragment : BaseFragment(), MyListener {
    private var mItemTouchHelper: ItemTouchHelper? = null

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_cities_manager, menu)
    }

    override val viewModel: CitiesManagerViewModel by viewModels {
        ViewModelFactory("CitiesManagerViewModel", requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        isExistCities(R.id.action_citiesManagerFragment_to_addCityFragment)
        val binding: FragmentCitiesManagerBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_cities_manager, container, false)
        binding.lifecycleOwner = this
        binding.viewmodel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }


    private fun init() {
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
}