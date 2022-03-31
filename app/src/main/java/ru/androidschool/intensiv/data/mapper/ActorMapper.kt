package ru.androidschool.intensiv.data.mapper

import ru.androidschool.intensiv.BuildConfig
import ru.androidschool.intensiv.common.generateImagePath
import ru.androidschool.intensiv.data.network.dto.Cast
import ru.androidschool.intensiv.domain.entity.ActorInfoEntity

class ActorMapper: BaseMapper<Cast, ActorInfoEntity> {
    override fun mapTo(from: Cast): ActorInfoEntity {
        return ActorInfoEntity(
            imageUrl = generateImagePath(from.profilePath.orEmpty()),
            fullName = from.originalName.orEmpty()
        )
    }
}