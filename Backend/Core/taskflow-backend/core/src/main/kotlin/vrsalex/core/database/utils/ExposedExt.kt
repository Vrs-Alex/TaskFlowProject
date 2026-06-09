package vrsalex.core.database.utils

import kotlinx.coroutines.flow.singleOrNull
import org.jetbrains.exposed.v1.core.*
import org.jetbrains.exposed.v1.r2dbc.selectAll

suspend fun <T : Table> T.findOne(
    where: () -> Op<Boolean>
): ResultRow? =
    selectAll().where(where).singleOrNull()

suspend fun Join.findOne(
    where: () -> Op<Boolean>
): ResultRow? =
    selectAll().where(where).singleOrNull()

suspend fun ColumnSet.findOne(
    where: () -> Op<Boolean>
): ResultRow? =
    selectAll().where(where).singleOrNull()

suspend fun <T : Table> T.exists(
    where: () -> Op<Boolean>
): Boolean = this.selectAll().where(where).count() > 0

suspend fun ColumnSet.exists(
    where: () -> Op<Boolean>
): Boolean = this.selectAll().where(where).count() > 0