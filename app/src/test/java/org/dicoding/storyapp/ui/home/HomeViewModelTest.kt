package org.dicoding.storyapp.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.dicoding.storyapp.adapter.StoryAdapter
import org.dicoding.storyapp.model.response.ListStoryItem
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var homeRepository: HomeRepository

    private lateinit var homeViewModel: HomeViewModel
    private var token = "token"

    @Before
    fun setup(){
        homeViewModel = HomeViewModel(homeRepository)
    }

    @Test
    fun `when success to load and there is story`() = runTest {
        val dummyList = DataDummy.dummyListStory()
        val data: PagingData<ListStoryItem> = ListPagingSource.snapshot(dummyList)
        val expectedList = MutableLiveData<PagingData<ListStoryItem>>()
        expectedList.value = data

        Mockito.`when`(homeRepository.requestListStory(token)).thenReturn(expectedList)
        val actualList: PagingData<ListStoryItem> =
            homeViewModel.requestListStory(token).getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualList)

        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyList.size, differ.snapshot().size)
        Assert.assertEquals(dummyList[0], differ.snapshot()[0])

    }

    @Test
    fun `when there is no story`() = runTest {
        val data: PagingData<ListStoryItem> = PagingData.from(emptyList())
        val expectedList = MutableLiveData<PagingData<ListStoryItem>>()
        expectedList.value = data
        Mockito.`when`(homeRepository.requestListStory(Mockito.anyString())).thenReturn(expectedList)
        val actualList: PagingData<ListStoryItem> =
            homeViewModel.requestListStory(token)
                .getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualList)

        Assert.assertEquals(0, differ.snapshot().size)
    }
}

class ListPagingSource : PagingSource<Int, LiveData<List<ListStoryItem>>>() {
    companion object {
        fun snapshot(items: List<ListStoryItem>): PagingData<ListStoryItem> {
            return PagingData.from(items)
        }
    }
    override fun getRefreshKey(state: PagingState<Int, LiveData<List<ListStoryItem>>>): Int {
        return 0
    }
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<ListStoryItem>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}

