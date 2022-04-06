package ru.androidschool.intensiv.data.mapper

import ru.androidschool.intensiv.data.database.entity.ActorDbEntity
import ru.androidschool.intensiv.domain.entity.ActorInfoEntity

class DbActorsMapper: BaseMapper<List<ActorInfoEntity>, List<ActorDbEntity>> {
    override fun mapTo(from: List<ActorInfoEntity>): List<ActorDbEntity> {
        return from.map {
            ActorDbEntity(
                actorId = it.id,
                imageUrl = it.imageUrl,
                fullName = it.fullName
            )
        }
    }
}