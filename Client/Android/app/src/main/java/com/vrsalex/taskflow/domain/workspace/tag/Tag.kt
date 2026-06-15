package com.vrsalex.taskflow.domain.workspace.tag

import com.vrsalex.taskflow.domain.common.model.OptionalField
import com.vrsalex.taskflow.domain.common.validation.Color
import com.vrsalex.taskflow.domain.common.validation.worksapce.TagName
import com.vrsalex.taskflow.domain.sync.ISyncModel
import com.vrsalex.taskflow.domain.sync.ISyncModelCreate
import com.vrsalex.taskflow.domain.sync.ISyncModelUpdate
import com.vrsalex.taskflow.domain.sync.SyncModel
import com.vrsalex.taskflow.domain.sync.SyncModelCreate
import com.vrsalex.taskflow.domain.sync.SyncModelUpdate

data class Tag(
    val name: TagName,
    val color: Color,
    override val syncModel: SyncModel
): ISyncModel

data class TagCreate(
    val name: TagName,
    val color: Color,
    override val syncModelCreate: SyncModelCreate
): ISyncModelCreate

data class TagUpdate(
    val name: OptionalField<TagName> = OptionalField.Undefined,
    val color: OptionalField<Color> = OptionalField.Undefined,
    override val syncModelUpdate: SyncModelUpdate
): ISyncModelUpdate