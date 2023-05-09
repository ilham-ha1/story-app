package org.dicoding.storyapp.ui.map

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.dicoding.storyapp.R
import org.dicoding.storyapp.databinding.ActivityMapsBinding
import org.dicoding.storyapp.factory.ViewModelFactory
import org.dicoding.storyapp.model.preference.UserPreference

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var mapsViewModel: MapsViewModel
    private lateinit var token:String
    private val boundsBuilder = LatLngBounds.Builder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapsInitializer.initialize(this, MapsInitializer.Renderer.LATEST) {}

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Story Map"

        mapsViewModel = ViewModelProvider(
            this,
            ViewModelFactory(this, UserPreference.getInstance(dataStore))
        )[MapsViewModel::class.java]


        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        lifecycleScope.launch {
            token = UserPreference.getInstance(dataStore).getToken().first()
        }

        getList()
        setMapStyle()
        getMyLocation()
    }

    private fun getList() {
        mapsViewModel.reqListMap(token)
        mapsViewModel.getList().observe(this) { list ->
            Log.d("MAP", "SHOW MARKER SUCCESS")
            val latLngList = mutableListOf<LatLng>()
            for (item in list.listIterator()) {
                val lat = item.lat
                val lon = item.lon
                val latLng = LatLng(lat, lon)
                val markerOptions = MarkerOptions()
                    .position(latLng)
                    .title(item.name)

                mMap.addMarker(markerOptions)
                boundsBuilder.include(latLng)
                latLngList.add(latLng)
            }

            if (latLngList.isNotEmpty()) {
                val firstLatLng = latLngList[0]
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstLatLng, 12f))
            }
        }
    }

    private val requestPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()){
        if (it){
            getMyLocation()
        }
    }
    private fun getMyLocation(){
        if (ContextCompat.checkSelfPermission(this.applicationContext, ACCESS_FINE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED){
            mMap.isMyLocationEnabled = true
        } else {
            requestPermission.launch(ACCESS_FINE_LOCATION)
        }
    }

    private fun setMapStyle() {
        try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", exception)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.maps, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.normal_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                true
            }
            R.id.satellite_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
                true
            }
            R.id.terrain_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
                true
            }
            R.id.hybrid_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


}
