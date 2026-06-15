package com.vrsalex.taskflow.domain.workspace.area

import com.vrsalex.taskflow.domain.common.model.OptionalField
import com.vrsalex.taskflow.domain.common.validation.worksapce.AreaName
import com.vrsalex.taskflow.domain.common.validation.Color
import com.vrsalex.taskflow.domain.sync.ISyncModel
import com.vrsalex.taskflow.domain.sync.ISyncModelCreate
import com.vrsalex.taskflow.domain.sync.ISyncModelUpdate
import com.vrsalex.taskflow.domain.sync.SyncModel
import com.vrsalex.taskflow.domain.sync.SyncModelCreate
import com.vrsalex.taskflow.domain.sync.SyncModelUpdate

data class Area(
    val name: AreaName,
    val color: Color,
    override val syncModel: SyncModel
): ISyncModel

data class AreaCreate(
    val name: AreaName,
    val color: Color,
    override val syncModelCreate: SyncModelCreate
): ISyncModelCreate

data class AreaUpdate(
    val name: OptionalField<AreaName> = OptionalField.Undefined,
    val color: OptionalField<Color> = OptionalField.Undefined,
    override val syncModelUpdate: SyncModelUpdate
): ISyncModelUpdate