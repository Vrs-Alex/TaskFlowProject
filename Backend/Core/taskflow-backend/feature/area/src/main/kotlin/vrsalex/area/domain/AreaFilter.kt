package vrsalex.area.domain

import vrsalex.core.value_object.Color

data class AreaFilter(
    val name: String? = null,
    val nameExact: Boolean = false,
    val color: Color? = null
  )
