package com.example.ae_chat_sdk.acti.media

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.bumptech.glide.Glide
import com.example.ae_chat_sdk.R
import com.github.chrisbanes.photoview.PhotoView
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.extractor.ExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelector
import com.google.android.exoplayer2.ui.SimpleExoPlayerView
import com.google.android.exoplayer2.upstream.BandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PhotoActivity : AppCompatActivity() {
    lateinit var photoView: PhotoView
    lateinit var btnBack: ImageButton
    lateinit var context: Context
    lateinit var exoPlayerView: SimpleExoPlayerView
    lateinit var exoPlayer: SimpleExoPlayer

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo)

        context = applicationContext

        hideSystemUI()
        init()
        setData()
        setOnClickListener()
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        WindowInsetsControllerCompat(
            window,
            window.decorView.findViewById(android.R.id.content)
        ).let { controller ->
            controller.hide(WindowInsetsCompat.Type.navigationBars())

            // When the screen is swiped up at the bottom
            // of the application, the navigationBar shall
            // appear for some time
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    private fun init() {
        btnBack = findViewById(R.id.ibBack)

        exoPlayerView = findViewById(R.id.idExoPlayerVIew)
        photoView = findViewById(R.id.photo_view)
    }

    private fun setData() {
        val topic = intent.getStringExtra("topic")

        if (topic == "image") {
            GlobalScope.launch(Dispatchers.Main) {
                photoView.visibility = View.VISIBLE
                val url = intent.getStringExtra("mediaUrl")
                Glide.with(applicationContext).load(url).placeholder(R.drawable.image_default)
                    .error(R.drawable.image_default).into(photoView)
            }

        } else if (topic == "video") {
            exoPlayerView.visibility = View.VISIBLE
            val url = intent.getStringExtra("mediaUrl")
            try {
                // bandwidthmeter is used for
                // getting default bandwidth
                val bandwidthMeter: BandwidthMeter = DefaultBandwidthMeter()

                // track selector is used to navigate between
                // video using a default seekbar.
                val trackSelector: TrackSelector =
                    DefaultTrackSelector(AdaptiveTrackSelection.Factory(bandwidthMeter))

                // we are adding our track selector to exoplayer.
                exoPlayer =
                    ExoPlayerFactory.newSimpleInstance(this, trackSelector)

                // we are parsing a video url
                // and parsing its video uri.
                val videoURI: Uri = Uri.parse(url)

                // we are creating a variable for datasource factory
                // and setting its user agent as 'exoplayer_view'
                val dataSourceFactory: DefaultHttpDataSourceFactory =
                    DefaultHttpDataSourceFactory("Exoplayer_video")

                // we are creating a variable for extractor factory
                // and setting it to default extractor factory.
                val extractorsFactory: ExtractorsFactory = DefaultExtractorsFactory();

                // we are creating a media source with above variables
                // and passing our event handler as null,
                val mediaSourse: MediaSource =
                    ExtractorMediaSource(
                        videoURI,
                        dataSourceFactory,
                        extractorsFactory,
                        null,
                        null
                    )

                // inside our exoplayer view
                // we are setting our player
                exoPlayerView.player = exoPlayer

                // we are preparing our exoplayer
                // with media source.
                exoPlayer.prepare(mediaSourse)

                // we are setting our exoplayer
                // when it is ready.
                exoPlayer.playWhenReady = true


            } catch (e: Exception) {
                // on below line we
                // are handling exception
                e.printStackTrace()
            }


        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        exoPlayer.release()
    }

    private fun setOnClickListener() {
        btnBack.setOnClickListener(View.OnClickListener {
            exoPlayer.release()
            finish()
        })
    }
}