package com.ibrahim.jakewhartonapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import com.ibrahim.jakewhartonapp.app.home.Repo
import com.ibrahim.jakewhartonapp.app.home.RepoFactory
import com.ibrahim.jakewhartonapp.app.home.RepoViewModel
import com.ibrahim.jakewhartonapp.app.home.ReposDataSource
import com.ibrahim.jakewhartonapp.lib.network.NetworkState
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RepoViewModelTest {


    @Mock
    private lateinit var reposDataSource: ReposDataSource
    @Mock
    private lateinit var repoFactory: RepoFactory



    private var itemPagedReviewList: LiveData<PagedList<Repo>>? = null
    private var pagingNetworkState: LiveData<NetworkState>? = null


    private var liveDataSource: LiveData<PageKeyedDataSource<Int, Repo>>? = null

    private lateinit var repoViewModel: RepoViewModel

    @Before
    fun setupRepoViewModel()
    {
        MockitoAnnotations.initMocks(this)
        repoViewModel = RepoViewModel()
        liveDataSource = repoFactory.getDataSource()
    }

    @Test
    fun loadRepos()
    {
        pagingNetworkState = Transformations.switchMap(
                repoFactory.getMutableLiveData()
        ) { dataSource: ReposDataSource -> dataSource.getNetworkState() }

        val pagedListConfig = PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(20)
                .setPageSize(20).build()
        itemPagedReviewList = LivePagedListBuilder(repoFactory, pagedListConfig)
                .build()
    }


}