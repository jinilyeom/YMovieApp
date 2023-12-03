package com.ymovie.app.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.ymovie.app.R
import com.ymovie.app.databinding.ActivityMovieDetailBinding
import com.ymovie.app.ui.UiConstants.MOVIE_ID
import com.ymovie.app.ui.UiConstants.MOVIE_NAME

class MovieDetailActivity : AppCompatActivity() {
    private val binding: ActivityMovieDetailBinding by lazy {
        ActivityMovieDetailBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        var movieId = -1
        var movieName = ""

        intent.extras?.let {
            movieId = it.getInt(MOVIE_ID)
            movieName = it.getString(MOVIE_NAME) ?: ""
        }

        if (savedInstanceState == null) {
            replaceFragment(MovieDetailFragment.newInstance(movieId, movieName))
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_view, fragment)
            .commit()
    }
}