package org.oppia.util.parser

import android.graphics.drawable.PictureDrawable
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

/**
 * Listener which updates the {@link ImageView} to be software rendered, because {@link
 * com.caverock.androidsvg.SVG SVG}/{@link android.graphics.Picture Picture} can't render on a
 * hardware backed {@link android.graphics.Canvas Canvas}.
 */
class SvgSoftwareLayerSetter : RequestListener<PictureDrawable> {
  override fun onLoadFailed(
    e: GlideException?,
    model: Any?,
    target: Target<PictureDrawable?>?,
    isFirstResource: Boolean
  ): Boolean {
    return false
  }

  override fun onResourceReady(
    resource: PictureDrawable?,
    model: Any?,
    target: Target<PictureDrawable?>?,
    dataSource: DataSource?,
    isFirstResource: Boolean
  ): Boolean {
    return false
  }
}
