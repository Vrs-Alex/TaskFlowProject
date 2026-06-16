package com.vrsalex.taskflow.domain.workspace.area

import com.vrsalex.taskflow.domain.common.model.OptionalField
import com.vrsalex.taskflow.domain.common.validation.worksapce.AreaName
import com.vrsalex.taskflow.domain.common.validation.Color
import com.vrsalex.taskflow.domain.sync.model.ISyncModel
import com.vrsalex.taskflow.domain.sync.model.ISyncModelCreate
import com.vrsalex.taskflow.domain.sync.model.ISyncModelUpdate
import com.vrsalex.taskflow.domain.sync.model.SyncModel
import com.vrsalex.taskflow.domain.sync.model.SyncModelCreate
import com.vrsalex.taskflow.domain.sync.model.SyncModelUpdate

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