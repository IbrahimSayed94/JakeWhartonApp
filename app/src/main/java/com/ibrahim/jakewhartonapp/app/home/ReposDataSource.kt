package com.ibrahim.jakewhartonapp.app.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.ibrahim.jakewhartonapp.MyApp
import com.ibrahim.jakewhartonapp.lib.network.ApiClient
import com.ibrahim.jakewhartonapp.lib.network.NetworkState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReposDataSource() : PageKeyedDataSource<Int, Repo>() {


    private val TAG = "ReposDataSource"

    private val FIRST_PAGE = 1
    private val PER_PAGE = 15


    private var networkState: MutableLiveData<NetworkState>? = null

    init {

        networkState = MutableLiveData()

    }
    private var call: Call<List<Repo>>?=null

    fun getNetworkState(): MutableLiveData<NetworkState>? {
        return networkState
    }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Repo>) {
        networkState!!.postValue(NetworkState.LOADING)

        call = ApiClient.build().getRepos(page = FIRST_PAGE,perPage = PER_PAGE)
        call?.enqueue(object : Callback<List<Repo>>
        {
            override fun onFailure(call: Call<List<Repo>>, t: Throwable) {
                val errorMessage = t.message!!
                Log.e(TAG,"intial $errorMessage")

                networkState!!.postValue(NetworkState(NetworkState.Status.FAILED, errorMessage))
                callback.onResult(MyApp.appDataBase.repoDao().getRepos().value!!,null, FIRST_PAGE + 1)


            }

            override fun onResponse(call: Call<List<Repo>>, response: Response<List<Repo>>) {

                if(response.isSuccessful)
                {
                    if (response.body() != null)
                    {
                        MyApp.appDataBase.repoDao().delete()
                        MyApp.appDataBase.repoDao().insert(response.body()!!)
                        callback.onResult(response.body()!!,null, FIRST_PAGE + 1)
                    }
                    networkState!!.postValue(NetworkState.getLoaded(response.body()))
                }
                else
                {
                    networkState!!.postValue(NetworkState(NetworkState.Status.FAILED,response.message()))

                }
            }
        })

    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Repo>) {

        networkState!!.postValue(NetworkState.LOADING)

        call = ApiClient.build().getRepos(page = params.key,perPage = PER_PAGE)
        call?.enqueue(object : Callback<List<Repo>>
        {
            override fun onFailure(call: Call<List<Repo>>, t: Throwable) {
                val errorMessage = t.message!!
                Log.e(TAG,"after $errorMessage")

                networkState!!.postValue(NetworkState(NetworkState.Status.FAILED, errorMessage))
            }

            override fun onResponse(call: Call<List<Repo>>, response: Response<List<Repo>>) {

                if(response.isSuccessful)
                {
                    if (response.body() != null)
                    {
                        callback.onResult(response.body()!!, params.key + 1)
                    }
                    networkState!!.postValue(NetworkState.getLoaded(response.body()))
                }
                else
                {
                    networkState!!.postValue(NetworkState(NetworkState.Status.FAILED,response.message()))
                }
            }
        })
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Repo>) {

        call = ApiClient.build().getRepos(page  = FIRST_PAGE,perPage = PER_PAGE)
        call?.enqueue(object : Callback<List<Repo>>
        {
            override fun onFailure(call: Call<List<Repo>>, t: Throwable) {
                val errorMessage = t.message!!
                Log.e(TAG,"before $errorMessage")

                networkState!!.postValue(NetworkState(NetworkState.Status.FAILED, errorMessage))
            }

            override fun onResponse(call: Call<List<Repo>>, response: Response<List<Repo>>) {

                if(response.isSuccessful)
                {
                    val adjacentKey = if (params.key > 1) params.key - 1 else null

                    if (response.body() != null)
                    {
                        callback.onResult(response.body()!!, adjacentKey)
                    }
                    networkState!!.postValue(NetworkState.getLoaded(response.body()))
                }

            }
        })
    }

    
}