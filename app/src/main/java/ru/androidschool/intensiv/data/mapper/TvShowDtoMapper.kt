package ru.androidschool.intensiv.data.mapper

import ru.androidschool.intensiv.BuildConfig
import ru.androidschool.intensiv.common.generateImagePath
import ru.androidschool.intensiv.data.network.dto.MovieDto
import ru.androidschool.intensiv.data.network.dto.TvShowDto
import ru.androidschool.intensiv.domain.entity.MovieEntity
import ru.androidschool.intensiv.domain.entity.TvShowEntity

class TvShowDtoMapper: BaseMapper<TvShowDto, TvShowEntity> {
    override fun mapTo(from: TvShowDto): TvShowEntity {
        return TvShowEntity(
            tvShowId = from.id ?: 0,
            title = from.name.orEmpty(),
            voteAverage = from.voteAverage ?: 0.0,
            horizontalPosterUrl = generateImagePath(from.backdropPath.orEmpty())
        )
    }
}