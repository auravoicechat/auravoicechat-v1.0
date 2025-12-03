package com.aura.voicechat.ui.components

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.opensource.svgaplayer.SVGAImageView
import com.opensource.svgaplayer.SVGAParser
import com.opensource.svgaplayer.SVGAVideoEntity
import java.io.File

/**
 * SVGA Player Component
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Composable wrapper for SVGA animation player
 * Supports playing SVGA animations from assets or URLs
 */
@Composable
fun SvgaPlayer(
    assetName: String,
    modifier: Modifier = Modifier,
    loops: Int = 0, // 0 means infinite loop
    clearsAfterStop: Boolean = true,
    fillMode: FillMode = FillMode.Forward,
    onAnimationStart: () -> Unit = {},
    onAnimationEnd: () -> Unit = {},
    onAnimationRepeat: () -> Unit = {}
) {
    val context = LocalContext.current
    
    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            SVGAImageView(ctx).apply {
                this.loops = loops
                this.clearsAfterStop = clearsAfterStop
                this.fillMode = when (fillMode) {
                    FillMode.Backward -> SVGAImageView.FillMode.Backward
                    FillMode.Forward -> SVGAImageView.FillMode.Forward
                }
                
                // Load and play SVGA
                loadSvgaFromAssets(ctx, assetName, this) {
                    onAnimationStart()
                }
                
                // Set callback
                callback = object : com.opensource.svgaplayer.SVGACallback {
                    override fun onPause() {}
                    
                    override fun onFinished() {
                        onAnimationEnd()
                    }
                    
                    override fun onRepeat() {
                        onAnimationRepeat()
                    }
                    
                    override fun onStep(frame: Int, percentage: Double) {}
                }
            }
        },
        update = { view ->
            // Reload if asset name changes
            loadSvgaFromAssets(context, assetName, view) {
                onAnimationStart()
            }
        }
    )
}

/**
 * SVGA Player from URL
 */
@Composable
fun SvgaPlayerFromUrl(
    url: String,
    modifier: Modifier = Modifier,
    loops: Int = 0,
    clearsAfterStop: Boolean = true,
    fillMode: FillMode = FillMode.Forward,
    onAnimationStart: () -> Unit = {},
    onAnimationEnd: () -> Unit = {},
    onAnimationRepeat: () -> Unit = {}
) {
    val context = LocalContext.current
    
    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            SVGAImageView(ctx).apply {
                this.loops = loops
                this.clearsAfterStop = clearsAfterStop
                this.fillMode = when (fillMode) {
                    FillMode.Backward -> SVGAImageView.FillMode.Backward
                    FillMode.Forward -> SVGAImageView.FillMode.Forward
                }
                
                // Load from URL
                loadSvgaFromUrl(ctx, url, this) {
                    onAnimationStart()
                }
                
                callback = object : com.opensource.svgaplayer.SVGACallback {
                    override fun onPause() {}
                    
                    override fun onFinished() {
                        onAnimationEnd()
                    }
                    
                    override fun onRepeat() {
                        onAnimationRepeat()
                    }
                    
                    override fun onStep(frame: Int, percentage: Double) {}
                }
            }
        },
        update = { view ->
            loadSvgaFromUrl(context, url, view) {
                onAnimationStart()
            }
        }
    )
}

/**
 * Load SVGA from assets
 */
private fun loadSvgaFromAssets(
    context: Context,
    assetName: String,
    imageView: SVGAImageView,
    onStart: () -> Unit
) {
    try {
        val parser = SVGAParser(context)
        parser.decodeFromAssets(
            assetName,
            object : SVGAParser.ParseCompletion {
                override fun onComplete(videoItem: SVGAVideoEntity) {
                    imageView.setVideoItem(videoItem)
                    imageView.startAnimation()
                    onStart()
                }
                
                override fun onError() {
                    // Handle error silently or log
                }
            }
        )
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

/**
 * Load SVGA from URL
 */
private fun loadSvgaFromUrl(
    context: Context,
    url: String,
    imageView: SVGAImageView,
    onStart: () -> Unit
) {
    try {
        val parser = SVGAParser(context)
        parser.decodeFromURL(
            java.net.URL(url),
            object : SVGAParser.ParseCompletion {
                override fun onComplete(videoItem: SVGAVideoEntity) {
                    imageView.setVideoItem(videoItem)
                    imageView.startAnimation()
                    onStart()
                }
                
                override fun onError() {
                    // Handle error silently or log
                }
            }
        )
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

/**
 * SVGA Player with file
 */
@Composable
fun SvgaPlayerFromFile(
    file: File,
    modifier: Modifier = Modifier,
    loops: Int = 0,
    clearsAfterStop: Boolean = true,
    fillMode: FillMode = FillMode.Forward,
    onAnimationStart: () -> Unit = {},
    onAnimationEnd: () -> Unit = {}
) {
    val context = LocalContext.current
    
    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            SVGAImageView(ctx).apply {
                this.loops = loops
                this.clearsAfterStop = clearsAfterStop
                this.fillMode = when (fillMode) {
                    FillMode.Backward -> SVGAImageView.FillMode.Backward
                    FillMode.Forward -> SVGAImageView.FillMode.Forward
                }
                
                try {
                    val parser = SVGAParser(ctx)
                    parser.decodeFromInputStream(
                        file.inputStream(),
                        file.name,
                        object : SVGAParser.ParseCompletion {
                            override fun onComplete(videoItem: SVGAVideoEntity) {
                                setVideoItem(videoItem)
                                startAnimation()
                                onAnimationStart()
                            }
                            
                            override fun onError() {}
                        }
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                
                callback = object : com.opensource.svgaplayer.SVGACallback {
                    override fun onPause() {}
                    override fun onFinished() {
                        onAnimationEnd()
                    }
                    override fun onRepeat() {}
                    override fun onStep(frame: Int, percentage: Double) {}
                }
            }
        }
    )
}

enum class FillMode {
    Forward,  // Animation keeps last frame after finished
    Backward  // Animation clears to first frame after finished
}

/**
 * Utility composable for playing one-shot SVGA animations
 */
@Composable
fun SvgaOneShot(
    assetName: String,
    modifier: Modifier = Modifier,
    onComplete: () -> Unit = {}
) {
    SvgaPlayer(
        assetName = assetName,
        modifier = modifier,
        loops = 1,
        clearsAfterStop = true,
        fillMode = FillMode.Forward,
        onAnimationEnd = onComplete
    )
}

/**
 * Box wrapper for full-screen SVGA animations
 */
@Composable
fun SvgaFullScreenAnimation(
    assetName: String,
    onComplete: () -> Unit = {}
) {
    Box(modifier = Modifier.fillMaxSize()) {
        SvgaPlayer(
            assetName = "svga/$assetName",
            modifier = Modifier.fillMaxSize(),
            loops = 1,
            clearsAfterStop = true,
            fillMode = FillMode.Forward,
            onAnimationEnd = onComplete
        )
    }
}
