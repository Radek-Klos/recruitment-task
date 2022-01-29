package com.peye.characters.ui.common

import androidx.annotation.UiThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import timber.log.Timber
import java.util.*
import kotlin.coroutines.CoroutineContext

private val managedViewModelsJobs = WeakHashMap<ViewModel, WeakHashMap<String, Job>>()

/**
 * `launch` builder extension that ensures only a single instance of a Job is running at the time.
 * It does so by binding given jobs to the calling methods' names (private for every calling VM).
 * Once a repeated call is found, a duplicate is chosen depending on a given strategy, and canceled.
 */
@UiThread
fun ViewModel.singleLaunch(
    context: CoroutineContext = Dispatchers.Default,
    strategy: SingleLaunchStrategy = SingleLaunchStrategy.IGNORE,
    block: suspend CoroutineScope.() -> Unit
) {
    val callingMethodName = getCallingMethodName()

    when (strategy) {
        SingleLaunchStrategy.REPLACE -> {
            val oldJob = removeJob(this, callingMethodName)
            oldJob?.cancel()
        }
        SingleLaunchStrategy.IGNORE -> {
            val oldJob = getJob(this, callingMethodName)
            if (oldJob != null && oldJob.isActive) {
                Timber.i("Old job for $callingMethodName method is active, skipping new one")
                return
            }
        }
    }

    val job = viewModelScope.launch(context, block = block)

    putJob(this, callingMethodName, job)
}

private fun Any.getCallingMethodName(): String =
    Exception().stackTrace.first { it.className == this.javaClass.name }.methodName

/**
 * Single-Launch a block with a fallback option block in case of a launched block's failure.
 * @see singleLaunch
 */
@UiThread
fun ViewModel.safeSingleLaunch(
    context: CoroutineContext = Dispatchers.Default,
    onError: (Throwable) -> Unit,
    strategy: SingleLaunchStrategy = SingleLaunchStrategy.IGNORE,
    block: suspend CoroutineScope.() -> Unit
) {
    singleLaunch(context, strategy = strategy) {
        safeRun(onError, block)
    }
}

@Suppress("TooGenericExceptionCaught", "ThrowsCount")
private suspend fun CoroutineScope.safeRun(onError: (Throwable) -> Unit, block: suspend CoroutineScope.() -> Unit) {
    try {
        val result = block.invoke(this)
        if (isActive.not()) {
            throw CancellationException("Job cancelled while running suspended method")
        }
        return result
    } catch (throwable: Throwable) {
        // handle the case when the exception is thrown even if the job is cancelled
        // (i.e. in a blocking block)
        if (isActive.not()) {
            if (throwable is CancellationException) throw throwable
            else throw CancellationException("Exception thrown when cancelled", throwable)
        }

        onError(throwable)
    }
}

private fun removeJob(viewModel: ViewModel, methodName: String) =
    managedViewModelsJobs[viewModel]?.remove(methodName)

private fun getJob(viewModel: ViewModel, methodName: String) =
    managedViewModelsJobs[viewModel]?.get(methodName)

private fun putJob(viewModel: ViewModel, methodName: String, job: Job) {
    val jobsForViewModel = managedViewModelsJobs[viewModel] ?: WeakHashMap()
    jobsForViewModel[methodName] = job

    if (managedViewModelsJobs[viewModel] == null) {
        managedViewModelsJobs[viewModel] = jobsForViewModel
    }
}

enum class SingleLaunchStrategy {
    REPLACE,
    IGNORE
}
