package vrsalex.notify.web

import vrsalex.core.database.transaction.TransactionManager
import vrsalex.notify.domain.UserDeviceRepository
import vrsalex.notify.domain.model.UserDevice

class NotifyService(
    private val userDeviceRepository: UserDeviceRepository,
    private val transactionManager: TransactionManager
) {

    suspend fun registerDevice(data: UserDevice) {
        transactionManager.dbTransaction {
            userDeviceRepository.add(data)
        }
    }

}