package com.example.findit.presentation.screen.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.findit.domain.model.LocationModel
import com.example.findit.domain.model.PostDomain
import com.example.findit.domain.model.PostType
import com.example.findit.domain.resource.Resource
import com.example.findit.domain.usecase.post.GetPostsUseCase
import com.example.findit.domain.usecase.user.GetUserNameUseCase
import com.example.findit.domain.usecase.post.SearchPostsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.anyString
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class HomeScreenViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var getPostsUseCase: GetPostsUseCase

    @Mock
    private lateinit var getUserNameUseCase: GetUserNameUseCase

    @Mock
    private lateinit var searchPostsUseCase: SearchPostsUseCase

    private lateinit var viewModel: HomeScreenViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        MockitoAnnotations.openMocks(this)
        viewModel = HomeScreenViewModel(getPostsUseCase, getUserNameUseCase, searchPostsUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadUserName should update userName in state`() = runTest {
        val expectedName = "John Doe"
        `when`(getUserNameUseCase.invoke(anyString())).thenReturn(expectedName)

        viewModel.onEvent(HomeScreenEvent.ClearError)
        advanceUntilIdle()

        Assert.assertEquals(expectedName, viewModel.state.value.userName)
    }

    @Test
    fun `loadPosts should update state with posts on success`() = runTest {
        val postList = listOf(dummyPostDto)
        `when`(getPostsUseCase.invoke()).thenReturn(flow {
            emit(Resource.Success(postList))
        })

        viewModel.onEvent(HomeScreenEvent.OnSearchQueryChanged("")) // triggers loadPosts
        advanceUntilIdle()

        Assert.assertEquals(postList.size, viewModel.state.value.posts.size)
        Assert.assertEquals(false, viewModel.state.value.isLoading)
    }

    companion object {
        val dummyPostDto = PostDomain(
            postId = "post123",
            imageUrl = "https://example.com/image.jpg",
            description = "This is a test post",
            userId = "user123",
            timestamp = System.currentTimeMillis(),
            location = LocationModel(37.7749, -122.4194),
            userProfilePicture = "https://example.com/profile.jpg",
            postType = PostType.LOST,
            userFullName = "John Doe"
        )
    }
}
