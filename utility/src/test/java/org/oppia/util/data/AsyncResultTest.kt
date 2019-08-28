package org.oppia.util.data

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.lang.IllegalStateException
import java.lang.UnsupportedOperationException
import kotlin.test.assertFailsWith

/** Tests for [AsyncResult]. */
@RunWith(JUnit4::class)
class AsyncResultTest {

  /* Pending tests. */

  @Test
  fun testPendingAsyncResult_isPending() {
    val result = AsyncResult.pending<String>()

    assertThat(result.isPending()).isTrue()
  }

  @Test
  fun testPendingAsyncResult_isNotSuccess() {
    val result = AsyncResult.pending<String>()

    assertThat(result.isSuccess()).isFalse()
  }

  @Test
  fun testPendingAsyncResult_isNotFailure() {
    val result = AsyncResult.pending<String>()

    assertThat(result.isFailure()).isFalse()
  }

  @Test
  fun testPendingAsyncResult_isNotCompleted() {
    val result = AsyncResult.pending<String>()

    assertThat(result.isCompleted()).isFalse()
  }

  @Test
  fun testPendingAsyncResult_getOrDefault_returnsDefault() {
    val result = AsyncResult.pending<String>()

    assertThat(result.getOrDefault("default")).isEqualTo("default")
  }

  @Test
  fun testPendingAsyncResult_getOrThrow_throwsIllegalStateExceptionDueToIncompletion() {
    val result = AsyncResult.pending<String>()

    assertFailsWith<IllegalStateException> { result.getOrThrow() }
  }

  @Test
  fun testPendingAsyncResult_getErrorOrNull_returnsNull() {
    val result = AsyncResult.pending<String>()

    assertThat(result.getErrorOrNull()).isNull()
  }

  @Test
  fun testPendingAsyncResult_transformed_isStillPending() {
    val original = AsyncResult.pending<String>()

    val transformed = original.transform { 0 }

    assertThat(transformed.isPending()).isTrue()
  }

  @Test
  @ExperimentalCoroutinesApi
  fun testPendingAsyncResult_transformedAsync_isStillPending() = runBlockingTest {
    val original = AsyncResult.pending<String>()

    val transformed = original.transformAsync { 0 }

    assertThat(transformed.isPending()).isTrue()
  }

  /* Success tests. */

  @Test
  fun testSucceededAsyncResult_isNotPending() {
    val result = AsyncResult.success("value")

    assertThat(result.isPending()).isFalse()
  }

  @Test
  fun testSucceededAsyncResult_isSuccess() {
    val result = AsyncResult.success("value")

    assertThat(result.isSuccess()).isTrue()
  }

  @Test
  fun testSucceededAsyncResult_isNotFailure() {
    val result = AsyncResult.success("value")

    assertThat(result.isFailure()).isFalse()
  }

  @Test
  fun testSucceededAsyncResult_isCompleted() {
    val result = AsyncResult.success("value")

    assertThat(result.isCompleted()).isTrue()
  }

  @Test
  fun testSucceededAsyncResult_getOrDefault_returnsValue() {
    val result = AsyncResult.success("value")

    assertThat(result.getOrDefault("default")).isEqualTo("value")
  }

  @Test
  fun testSucceededAsyncResult_getOrThrow_returnsValue() {
    val result = AsyncResult.success("value")

    assertThat(result.getOrThrow()).isEqualTo("value")
  }

  @Test
  fun testSucceededAsyncResult_getErrorOrNull_returnsNull() {
    val result = AsyncResult.success("value")

    assertThat(result.getErrorOrNull()).isNull()
  }

  @Test
  fun testSucceededAsyncResult_transformed_hasTransformedValue() {
    val original = AsyncResult.success("value")

    val transformed = original.transform { 0 }

    assertThat(transformed.getOrThrow()).isEqualTo(0)
  }

  @Test
  @ExperimentalCoroutinesApi
  fun testSucceededAsyncResult_transformedAsync_hasTransformedValue() = runBlockingTest {
    val original = AsyncResult.success("value")

    val transformed = original.transformAsync { 0 }

    assertThat(transformed.getOrThrow()).isEqualTo(0)
  }

  /* Failure tests. */

  @Test
  fun testFailedAsyncResult_isNotPending() {
    val result = AsyncResult.failed<String>(UnsupportedOperationException())

    assertThat(result.isPending()).isFalse()
  }

  @Test
  fun testFailedAsyncResult_isNotSuccess() {
    val result = AsyncResult.failed<String>(UnsupportedOperationException())

    assertThat(result.isSuccess()).isFalse()
  }

  @Test
  fun testFailedAsyncResult_isFailure() {
    val result = AsyncResult.failed<String>(UnsupportedOperationException())

    assertThat(result.isFailure()).isTrue()
  }

  @Test
  fun testFailedAsyncResult_isCompleted() {
    val result = AsyncResult.failed<String>(UnsupportedOperationException())

    assertThat(result.isCompleted()).isTrue()
  }

  @Test
  fun testFailedAsyncResult_getOrDefault_returnsDefault() {
    val result = AsyncResult.failed<String>(UnsupportedOperationException())

    assertThat(result.getOrDefault("default")).isEqualTo("default")
  }

  @Test
  fun testFailedAsyncResult_getOrThrow_throwsFailureException() {
    val result = AsyncResult.failed<String>(UnsupportedOperationException())

    assertFailsWith<UnsupportedOperationException> { result.getOrThrow() }
  }

  @Test
  fun testFailedAsyncResult_getErrorOrNull_returnsFailureException() {
    val result = AsyncResult.failed<String>(UnsupportedOperationException())

    assertThat(result.getErrorOrNull()).isInstanceOf(UnsupportedOperationException::class.java)
  }

  @Test
  fun testFailedAsyncResult_transformed_throwsChainedFailureException_withCorrectRootCause() {
    val result = AsyncResult.failed<String>(UnsupportedOperationException())

    val transformed = result.transform { 0 }

    assertThat(transformed.getErrorOrNull()).isInstanceOf(AsyncResult.ChainedFailureException::class.java)
    assertThat(transformed.getErrorOrNull()).hasCauseThat().isInstanceOf(UnsupportedOperationException::class.java)
  }

  @Test
  @ExperimentalCoroutinesApi
  fun testFailedAsyncResult_transformedAsync_throwsChainedFailureException_withCorrectRootCause() = runBlockingTest {
    val result = AsyncResult.failed<String>(UnsupportedOperationException())

    val transformed = result.transformAsync { 0 }

    assertThat(transformed.getErrorOrNull()).isInstanceOf(AsyncResult.ChainedFailureException::class.java)
    assertThat(transformed.getErrorOrNull()).hasCauseThat().isInstanceOf(UnsupportedOperationException::class.java)
  }
}