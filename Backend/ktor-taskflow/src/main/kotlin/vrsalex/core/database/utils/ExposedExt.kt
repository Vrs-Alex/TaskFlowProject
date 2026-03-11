package vrsalex.core.database.utils

import kotlinx.coroutines.flow.any
import kotlinx.coroutines.flow.singleOrNull
import org.jetbrains.exposed.v1.core.Op
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabase
import org.jetbrains.exposed.v1.r2dbc.selectAll
import org.jetbrains.exposed.v1.r2dbc.transactions.suspendTransaction

suspend fun <T : Table> T.findOne(
    where: () -> Op<Boolean>
): ResultRow? =
    selectAll().where(where).singleOrNull()



suspend fun <T : Table> T.exists(
    where: () -> Op<Boolean>
): Boolean =
    selectAll().where(where).limit(1).any { true }
